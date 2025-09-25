package com.example.myapplication.ui.view.page.authentication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.ui.viewmodel.authentication.OrganizerSignupViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class OrganizerSignupMandatoryFragment extends Fragment {

    private OrganizerSignupViewModel viewModel;

    //step containers
    private View stepEmail, stepMandatory, stepOptional;

    //step 1
    private TextInputLayout layoutEmail;
    private TextInputEditText inputEmail;

    //step 2
    private TextInputLayout layoutName, layoutSurname, layoutPassword, layoutConfirmPassword, layoutResidency, layoutPhone;
    private TextInputEditText inputName, inputSurname, inputPassword, inputConfirmPassword, inputResidency, inputPhone;

    //step 3
    private FrameLayout profilePictureContainer;

    //buttons for navigation
    private Button buttonPrev, buttonNext;

    public OrganizerSignupMandatoryFragment() {}

    public static OrganizerSignupMandatoryFragment newInstance() {
        return new OrganizerSignupMandatoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_organizer_signup_mandatory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(OrganizerSignupViewModel.class);

        stepEmail = view.findViewById(R.id.step_email);
        stepMandatory = view.findViewById(R.id.step_mandatory);
        stepOptional = view.findViewById(R.id.step_optional);

        layoutEmail = view.findViewById(R.id.layoutEmail);
        inputEmail = view.findViewById(R.id.inputEmail);

        layoutName = view.findViewById(R.id.layoutName);
        layoutSurname = view.findViewById(R.id.layoutSurname);
        layoutPassword = view.findViewById(R.id.layoutPassword);
        layoutConfirmPassword = view.findViewById(R.id.layoutConfirmPassword);
        layoutResidency = view.findViewById(R.id.layoutResidency);
        layoutPhone = view.findViewById(R.id.layoutPhone);
        inputName = view.findViewById(R.id.inputName);
        inputSurname = view.findViewById(R.id.inputSurname);
        inputPassword = view.findViewById(R.id.inputPassword);
        inputConfirmPassword = view.findViewById(R.id.inputConfirmPassword);
        inputResidency = view.findViewById(R.id.inputResidency);
        inputPhone = view.findViewById(R.id.inputPhone);

        profilePictureContainer = view.findViewById(R.id.profilePictureContainer);

        buttonPrev = view.findViewById(R.id.button_prev);
        buttonNext = view.findViewById(R.id.button_next);

        //observing errors on form fields
        viewModel.emailError.observe(getViewLifecycleOwner(), layoutEmail::setError);
        viewModel.nameError.observe(getViewLifecycleOwner(), layoutName::setError);
        viewModel.surnameError.observe(getViewLifecycleOwner(), layoutSurname::setError);
        viewModel.passwordError.observe(getViewLifecycleOwner(), layoutPassword::setError);
        viewModel.confirmPasswordError.observe(getViewLifecycleOwner(), layoutConfirmPassword::setError);
        viewModel.residencyError.observe(getViewLifecycleOwner(), layoutResidency::setError);
        viewModel.phoneError.observe(getViewLifecycleOwner(), layoutPhone::setError);

        //opening system picker for images
        profilePictureContainer.setOnClickListener(v -> openImagePicker());

        //observing changes in step
        viewModel.getCurrentStep().observe(getViewLifecycleOwner(), step -> updateStepUI());

        //adding text watchers which will update view model
        addTextWatchers();

        //observing if signup successful move to registration completed
        viewModel.signupSuccess.observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                Toast.makeText(requireContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, new RegistrationCompletedFragment())
                        .commit();
            }
        });

        //observing if signup failed show message
        viewModel.signupError.observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show();
            }
        });

        //going to previous step
        buttonPrev.setOnClickListener(v -> viewModel.goPrevious());

        //either go to next step or submit registration form
        buttonNext.setOnClickListener(v -> {
            if (viewModel.isStepValid(viewModel.getCurrentStep().getValue() != null ? viewModel.getCurrentStep().getValue() : 1)) {
                if (viewModel.getCurrentStep().getValue() != null && viewModel.getCurrentStep().getValue() < 3) {
                    viewModel.goNext();
                } else {
                    viewModel.submitSignup();
                }
            }
        });

        //observing if the email is already taken
        viewModel.emailTaken.observe(getViewLifecycleOwner(), taken -> {
            if (taken != null && taken) {
                buttonNext.setClickable(false);
                layoutEmail.setError("Email already taken");
            } else {
                buttonNext.setClickable(true);
                layoutEmail.setError(null);
            }
        });
    }

    private void addTextWatchers() {
        TextInputEditText[] fields = {inputEmail, inputName, inputSurname, inputPassword,
                inputConfirmPassword, inputResidency, inputPhone};

        for (TextInputEditText field : fields) {
            field.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                @Override
                public void afterTextChanged(Editable editable) {
                    int id = field.getId();
                    String value = field.getText() != null ? field.getText().toString() : "";

                    if (id == R.id.inputEmail) {
                        viewModel.email.setValue(value);
                        if (viewModel.getCurrentStep().getValue() != null &&
                                viewModel.getCurrentStep().getValue() == 1) {
                            viewModel.isStepValid(1);
                        }
                    } else if (id == R.id.inputName) {
                        viewModel.name.setValue(value);
                    } else if (id == R.id.inputSurname) {
                        viewModel.surname.setValue(value);
                    } else if (id == R.id.inputPassword) {
                        viewModel.password.setValue(value);
                    } else if (id == R.id.inputConfirmPassword) {
                        viewModel.confirmPassword.setValue(value);
                    } else if (id == R.id.inputResidency) {
                        viewModel.residency.setValue(value);
                    } else if (id == R.id.inputPhone) {
                        viewModel.phone.setValue(value);
                    }

                    if (viewModel.getCurrentStep().getValue() != null &&
                            viewModel.getCurrentStep().getValue() == 2) {
                        viewModel.isStepValid(2);
                    }
                }
            });
        }
    }

    //changing between containers
    private void updateStepUI() {
        Integer step = viewModel.getCurrentStep().getValue();
        stepEmail.setVisibility(step != null && step == 1 ? View.VISIBLE : View.GONE);
        stepMandatory.setVisibility(step != null && step == 2 ? View.VISIBLE : View.GONE);
        stepOptional.setVisibility(step != null && step == 3 ? View.VISIBLE : View.GONE);

        buttonPrev.setEnabled(step != null && step > 1);
        buttonNext.setText(step != null && step == 3 ? "Submit" : "Next");
    }

    //profile image selection
    private static final int PICK_IMAGE_REQUEST = 1;

    //opening image picker from phone
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), PICK_IMAGE_REQUEST);
    }

    //after picking image
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //checking image data
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            //converting image to bitmap
            Bitmap bitmap = null;
            try {
                //depending on version use convert image
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().getContentResolver(), imageUri));
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), imageUri);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "Failed to process image", Toast.LENGTH_SHORT).show();
                return;
            }

            //getting MIME type
            String mimeType = requireContext().getContentResolver().getType(imageUri);
            viewModel.setProfilePicture(bitmap, mimeType);

            //checking if image was accepted
            if (viewModel.profilePicture.getValue() != null) {
                //showing image
                ImageView imageView = new ImageView(requireContext());
                imageView.setImageURI(imageUri);
                profilePictureContainer.removeAllViews();
                profilePictureContainer.addView(imageView);
            } else {
                //removing any image
                profilePictureContainer.removeAllViews();
                Toast.makeText(requireContext(), "Invalid profile picture (too large or unsupported type)", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
