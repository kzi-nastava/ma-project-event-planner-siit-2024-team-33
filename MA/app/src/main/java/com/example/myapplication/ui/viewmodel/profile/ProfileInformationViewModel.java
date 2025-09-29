package com.example.myapplication.ui.viewmodel.profile;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.data.models.dto.userDTO.GetUserDTO;
import com.example.myapplication.data.models.dto.userDTO.UpdateUser;
import com.example.myapplication.data.models.dto.userDTO.UpdatedUser;
import com.example.myapplication.data.models.AuthentifiedUser;
import com.example.myapplication.data.services.authentication.AuthenticationService;
import com.example.myapplication.data.services.user.UsersService;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileInformationViewModel extends ViewModel {

    private final UsersService userService;
    private final MutableLiveData<GetUserDTO> userLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> successLiveData = new MutableLiveData<>();

    private final MutableLiveData<Boolean> _isDeleted = new MutableLiveData<>(false);
    public LiveData<Boolean> isDeleted = _isDeleted;

    public LiveData<String> getSuccessLiveData() {
        return successLiveData;
    }

    public ProfileInformationViewModel() {
        userService = new UsersService();
    }

    public LiveData<GetUserDTO> getUserLiveData() {
        return userLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    //getting currently logged in user
    public void loadCurrentUser() {
        userService.getCurrentUser().enqueue(new Callback<GetUserDTO>() {
            @Override
            public void onResponse(Call<GetUserDTO> call, Response<GetUserDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userLiveData.postValue(response.body());
                } else {
                    errorLiveData.postValue("Failed to load user");
                }
            }

            @Override
            public void onFailure(Call<GetUserDTO> call, Throwable t) {
                errorLiveData.postValue(t.getMessage());
            }
        });
    }

    //updating user
    public void updateUser(UpdateUser updateUser) {
        userService.updateUser(updateUser).enqueue(new Callback<UpdatedUser>() {
            @Override
            public void onResponse(Call<UpdatedUser> call, Response<UpdatedUser> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fetchUpdatedUserFromServer();
                } else {
                    errorLiveData.postValue("Failed to update profile");
                }
            }

            @Override
            public void onFailure(Call<UpdatedUser> call, Throwable t) {
                errorLiveData.postValue(t.getMessage());
            }
        });
    }
    //saving newly updated user
    private void fetchUpdatedUserFromServer() {
        UsersService service = new UsersService();
        service.getCurrentUser().enqueue(new Callback<GetUserDTO>() {
            @Override
            public void onResponse(Call<GetUserDTO> call, Response<GetUserDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetUserDTO updatedDTO = response.body();
                    userLiveData.postValue(updatedDTO);

                    //saving to share preferences
                    AuthentifiedUser updatedUser = new AuthentifiedUser(updatedDTO);
                    AuthenticationService.saveUserToStorage(updatedUser);

                    successLiveData.postValue("Profile updated successfully");
                } else {
                    errorLiveData.postValue("Failed to fetch updated user");
                }
            }

            @Override
            public void onFailure(Call<GetUserDTO> call, Throwable t) {
                errorLiveData.postValue("Failed to fetch updated user: " + t.getMessage());
            }
        });
    }

    public boolean isValidName(String name) {
        return name != null && name.matches("^[a-zA-Z]{1,50}$");
    }

    public boolean isValidSurname(String surname) {
        return surname != null && surname.matches("^[a-zA-Z]{1,50}$");
    }

    public boolean isValidResidency(String residency) {
        return residency != null
                && residency.length() <= 150
                && residency.matches("^[A-Za-z][A-Za-z\\-' ]*[A-Za-z], [A-Za-z][A-Za-z\\-' ]*[A-Za-z]$");
    }

    public boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^\\+?[0-9\\s()-]{7,15}$");
    }

    public boolean isValidDescription(String desc) {
        return desc != null && desc.matches("^.{20,}$");
    }

    public boolean isValidProviderName(String providerName) {
        return providerName != null && providerName.matches("^[a-zA-Z0-9]{0,20}$");
    }

    public boolean isValidProfilePicture(String base64) {
        if (base64 == null) return true;
        if (!base64.startsWith("data:image/")) return false;

        String mime = base64.split(";")[0].replace("data:", "");
        List<String> allowed = Arrays.asList("image/jpeg", "image/png", "image/webp", "image/gif");
        if (!allowed.contains(mime)) return false;

        String b64data = base64.split(",")[1];
        int size = (b64data.length() * 3) / 4;
        return size <= 2 * 1024 * 1024;
    }

    public boolean isValidProviderPictures(List<String> pictures) {
        if (pictures == null) return true;
        if (pictures.size() > 8) return false;

        for (String base64 : pictures) {
            if (!isValidProfilePicture(base64)) return false;
        }
        return true;
    }

    //checking validation based on user
    public boolean validateFieldsByRole(String role,
                                        String name,
                                        String surname,
                                        String residency,
                                        String phone,
                                        String providerName,
                                        String description,
                                        String profilePicture,
                                        List<String> providerPictures) {
        switch (role) {
            case "PROVIDER_ROLE":
                return isValidName(name)
                        && isValidSurname(surname)
                        && isValidResidency(residency)
                        && isValidPhone(phone)
                        && isValidProviderName(providerName)
                        && isValidDescription(description)
                        && isValidProfilePicture(profilePicture)
                        && isValidProviderPictures(providerPictures);

            case "ORGANIZER_ROLE":
                return isValidName(name)
                        && isValidSurname(surname)
                        && isValidResidency(residency)
                        && isValidPhone(phone)
                        && isValidProfilePicture(profilePicture);

            case "AUSER_ROLE":
                return isValidName(name)
                        && isValidSurname(surname)
                        && isValidProfilePicture(profilePicture);

            case "ADMIN_ROLE":
                return false;

            default:
                return false;
        }
    }

    public void terminateUser(Context context) {
        userService.terminateUser().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    AuthenticationService authenticationService = new AuthenticationService(context);
                    authenticationService.logout();

                    userLiveData.postValue(null);
                    _isDeleted.postValue(true);
                } else {
                    errorLiveData.postValue("Failed to delete profile");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorLiveData.postValue("Error: " + t.getMessage());
            }
        });
    }
}
