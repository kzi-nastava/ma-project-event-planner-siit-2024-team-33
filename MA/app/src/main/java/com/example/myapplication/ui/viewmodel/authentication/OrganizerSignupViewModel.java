package com.example.myapplication.ui.viewmodel.authentication;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.data.dto.userDTO.RegisterUser;
import com.example.myapplication.data.dto.userDTO.RegisteredUser;
import com.example.myapplication.data.services.AuthenticationService;
import com.example.myapplication.data.services.UsersService;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;

public class OrganizerSignupViewModel extends AndroidViewModel {

    private final AuthenticationService authService;

    public OrganizerSignupViewModel(@NonNull Application application) {
        super(application);
        authService = new AuthenticationService(application.getApplicationContext());
    }

    private final MutableLiveData<Integer> currentStep = new MutableLiveData<>(1);
    public LiveData<Integer> getCurrentStep() { return currentStep; }

    //form fields
    public final MutableLiveData<String> email = new MutableLiveData<>();
    public final MutableLiveData<String> name = new MutableLiveData<>();
    public final MutableLiveData<String> surname = new MutableLiveData<>();
    public final MutableLiveData<String> password = new MutableLiveData<>();
    public final MutableLiveData<String> confirmPassword = new MutableLiveData<>();
    public final MutableLiveData<String> residency = new MutableLiveData<>();
    public final MutableLiveData<String> phone = new MutableLiveData<>();
    public final MutableLiveData<String> profilePicture = new MutableLiveData<>();

    //error messages
    public final MutableLiveData<String> emailError = new MutableLiveData<>();
    public final MutableLiveData<String> nameError = new MutableLiveData<>();
    public final MutableLiveData<String> surnameError = new MutableLiveData<>();
    public final MutableLiveData<String> passwordError = new MutableLiveData<>();
    public final MutableLiveData<String> confirmPasswordError = new MutableLiveData<>();
    public final MutableLiveData<String> residencyError = new MutableLiveData<>();
    public final MutableLiveData<String> phoneError = new MutableLiveData<>();

    //signup successfulness state
    public final MutableLiveData<Boolean> signupSuccess = new MutableLiveData<>();
    public final MutableLiveData<String> signupError = new MutableLiveData<>();

    //email availability state
    public final MutableLiveData<Boolean> emailTaken = new MutableLiveData<>(false);

    //debounce for email checks
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable emailCheckRunnable;


    //navigation
    public void goNext() {
        Integer step = currentStep.getValue();
        if (step != null && step < 3) currentStep.setValue(step + 1);
    }

    public void goPrevious() {
        Integer step = currentStep.getValue();
        if (step != null && step > 1) currentStep.setValue(step - 1);
    }

    //validation
    public boolean isStepValid(int step) {
        boolean valid = true;
        switch (step) {
            case 1: //email
                String e = email.getValue() != null ? email.getValue().trim() : "";
                if (e.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
                    emailError.setValue("Invalid email");
                    valid = false;
                } else {
                    //always re-checking on text change
                    emailError.setValue(null);
                    emailTaken.setValue(false); //reseting previous taken state
                    checkEmailAvailabilityDebounced(e);
                }
                break;

            case 2: //mandatory data
                String n = name.getValue() != null ? name.getValue().trim() : "";
                if (n.isEmpty() || !n.matches("^[a-zA-Z]{1,50}$")) {
                    nameError.setValue("Name required (letters only, max 50 chars)");
                    valid = false;
                } else nameError.setValue(null);

                String s = surname.getValue() != null ? surname.getValue().trim() : "";
                if (s.isEmpty() || !s.matches("^[a-zA-Z]{1,50}$")) {
                    surnameError.setValue("Surname required (letters only, max 50 chars)");
                    valid = false;
                } else surnameError.setValue(null);

                String p = password.getValue() != null ? password.getValue() : "";
                if (!p.matches("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$")) {
                    passwordError.setValue("Password must be 8-16 chars, letters & numbers");
                    valid = false;
                } else passwordError.setValue(null);

                String cp = confirmPassword.getValue() != null ? confirmPassword.getValue() : "";
                if (!cp.equals(p)) {
                    confirmPasswordError.setValue("Passwords do not match");
                    valid = false;
                } else confirmPasswordError.setValue(null);

                String r = residency.getValue() != null ? residency.getValue().trim() : "";
                String residencyRegex = "^[A-Za-z][A-Za-z\\-' ]*[A-Za-z], [A-Za-z][A-Za-z\\-' ]*[A-Za-z]$";
                if (r.isEmpty() || r.length() > 150 || !r.matches(residencyRegex)) {
                    residencyError.setValue("Residency required (City, Country)");
                    valid = false;
                } else residencyError.setValue(null);

                String ph = phone.getValue() != null ? phone.getValue().trim() : "";
                String phoneRegex = "^\\+?[0-9\\s()-]{7,15}$";
                if (!ph.matches(phoneRegex)) {
                    phoneError.setValue("Invalid phone number (+1234567890 format)");
                    valid = false;
                } else phoneError.setValue(null);

                break;

            case 3:
                // Optional step
                break;
        }
        return valid;
    }

    //email availability check
    private void checkEmailAvailabilityDebounced(String email) {
        //canceling previous checks
        if (emailCheckRunnable != null) handler.removeCallbacks(emailCheckRunnable);

        emailCheckRunnable = () -> authService.checkEmailAvailability(email,
                new AuthenticationService.EmailCheckCallback() {
                    @Override
                    public void onResult(boolean exists) {
                        emailTaken.postValue(exists);
                        if (exists) {
                            emailError.postValue("Email already taken");
                        } else {
                            emailError.postValue(null);
                        }
                    }

                    @Override
                    public void onError(String message) {
                        emailError.postValue("Error checking email");
                    }
        });

        handler.postDelayed(emailCheckRunnable, 400); //waiting 400ms after user stops typing
    }


    //sending the form to server and getting the response
    public void submitSignup() {
        RegisterUser registerUser = new RegisterUser();
        registerUser.setEmail(email.getValue());
        registerUser.setPassword(password.getValue());
        registerUser.setName(name.getValue());
        registerUser.setSurname(surname.getValue());
        registerUser.setResidency(residency.getValue());
        registerUser.setPhoneNumber(phone.getValue());
        registerUser.setPicture(profilePicture.getValue()); //base64
        registerUser.setRole("ORGANIZER_ROLE");

        UsersService usersService = new UsersService();
        usersService.signup(registerUser).enqueue(new retrofit2.Callback<RegisteredUser>() {
            @Override
            public void onResponse(Call<RegisteredUser> call, retrofit2.Response<RegisteredUser> response) {
                if (response.isSuccessful() && response.body() != null) {
                    signupSuccess.postValue(true);
                } else {
                    signupError.postValue("Registration failed. Server returned error.");
                }
            }

            @Override
            public void onFailure(Call<RegisteredUser> call, Throwable t) {
                signupError.postValue("Registration failed: " + t.getMessage());
            }
        });
    }

    //setting profile picture data for form
    public void setProfilePicture(Bitmap bitmap, String mimeType) {
        if (bitmap != null && mimeType != null) {
            String base64 = convertBitmapToBase64(bitmap, mimeType);
            if (isProfilePictureValid(base64)) {
                profilePicture.setValue(base64);
            } else {
                profilePicture.setValue(null); //rejecting invalid images
                signupError.setValue("Invalid profile picture (unsupported type or too large)");
            }
        } else {
            profilePicture.setValue(null);
        }
    }

    //compressing image and converting to base64
    private String convertBitmapToBase64(Bitmap bitmap, String mimeType) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        if (mimeType.equals("image/png")) format = Bitmap.CompressFormat.PNG;
        else if (mimeType.equals("image/webp")) format = Bitmap.CompressFormat.WEBP_LOSSY;

        bitmap.compress(format, 80, outputStream);
        String base64 = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP);

        return "data:" + mimeType + ";base64," + base64;
    }

    private boolean isProfilePictureValid(String base64Image) {
        if (base64Image == null) return true; //no picture is allowed

        //allowed MIME types
        String[] allowedImageTypes = {"image/jpeg", "image/png", "image/webp", "image/gif"};
        boolean supported = false;

        //extracting MIME type
        String mimeRegex = "^data:(image/[a-zA-Z]+);base64,";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(mimeRegex);
        java.util.regex.Matcher matcher = pattern.matcher(base64Image);

        //is MIME type allowed
        if (!matcher.find()) {
            return false;
        }

        String mimeType = matcher.group(1);
        for (String allowed : allowedImageTypes) {
            if (allowed.equalsIgnoreCase(mimeType)) {
                supported = true;
                break;
            }
        }
        if (!supported) return false;

        //estimating size from base64 string
        String base64Data = base64Image.split(",")[1];
        int sizeInBytes = (base64Data.length() * 3) / 4;
        int maxSizeMB = 2;

        return sizeInBytes <= maxSizeMB * 1024 * 1024;
    }
}