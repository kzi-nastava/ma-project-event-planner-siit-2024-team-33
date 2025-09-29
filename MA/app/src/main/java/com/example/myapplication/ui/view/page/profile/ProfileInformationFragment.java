package com.example.myapplication.ui.view.page.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.data.models.dto.userDTO.GetUserDTO;
import com.example.myapplication.data.models.dto.userDTO.UpdateUser;
import com.example.myapplication.ui.view.page.home.component.ImageCarouselFragment;
import com.example.myapplication.ui.view.page.authentication.LoginFragment;
import com.example.myapplication.ui.viewmodel.profile.ProfileInformationViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ProfileInformationFragment extends Fragment {

    private class ValidationWatcher implements TextWatcher {
        private final Runnable validator;

        ValidationWatcher(Runnable validator) {
            this.validator = validator;
        }

        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override public void afterTextChanged(Editable s) { validator.run(); }
    }

    private ProfileInformationViewModel viewModel;

    private Button buttonInfo, buttonFavo, buttonSchedule;

    private ShapeableImageView profileIcon;
    private TextInputEditText inputEmail, inputName, inputSurname, inputResidency,
            inputPhone, inputProviderName, inputDescription;
    private TextInputLayout layoutProviderName, layoutDescription, layoutResidency, layoutPhone;
    private FrameLayout providerPicturesContainer;
    private ImageCarouselFragment imageCarouselFragment;
    private Button uploadPictures;

    private Button buttonUpdate, buttonChangePassword, buttonDelete;

    private TextView tvProviderNameLabel, tvDescriptionLabel, tvResidencyLabel, tvPhoneLabel;
    private TextView tvNameDesc, tvSurnameDesc, tvProviderNameDesc, tvDescriptionDesc, tvResidencyDesc, tvPhoneDesc, tvPicturesDesc;


    public ProfileInformationFragment() {}

    public static ProfileInformationFragment newInstance() {
        return new ProfileInformationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_information, container, false);

        buttonInfo = view.findViewById(R.id.button_information);
        buttonFavo = view.findViewById(R.id.button_favorites);
        buttonSchedule = view.findViewById(R.id.button_schedule);

        profileIcon = view.findViewById(R.id.profile_icon);
        inputEmail = view.findViewById(R.id.inputEmail);
        inputName = view.findViewById(R.id.inputName);
        inputSurname = view.findViewById(R.id.inputSurname);
        inputResidency = view.findViewById(R.id.inputResidency);
        inputPhone = view.findViewById(R.id.inputPhone);
        inputProviderName = view.findViewById(R.id.inputProviderName);
        inputDescription = view.findViewById(R.id.inputDescription);

        layoutProviderName = view.findViewById(R.id.layoutProviderName);
        layoutDescription = view.findViewById(R.id.layoutDescription);
        layoutResidency = view.findViewById(R.id.layoutResidency);
        layoutPhone = view.findViewById(R.id.layoutPhone);

        providerPicturesContainer = view.findViewById(R.id.providerPicturesContainer);
        uploadPictures = view.findViewById(R.id.button_upload);

        buttonUpdate = view.findViewById(R.id.button_update);
        buttonChangePassword = view.findViewById(R.id.button_cpassword);
        buttonDelete = view.findViewById(R.id.button_delete);

        tvProviderNameLabel = view.findViewById(R.id.tvProviderNameLabel);
        tvDescriptionLabel = view.findViewById(R.id.tvDescriptionLabel);
        tvResidencyLabel = view.findViewById(R.id.tvResidencyLabel);
        tvPhoneLabel = view.findViewById(R.id.tvPhoneLabel);
        tvNameDesc = view.findViewById(R.id.tvNameDesc);
        tvSurnameDesc = view.findViewById(R.id.tvSurnameDesc);
        tvProviderNameDesc = view.findViewById(R.id.tvProviderNameDesc);
        tvDescriptionDesc = view.findViewById(R.id.tvDescriptionDesc);
        tvResidencyDesc = view.findViewById(R.id.tvResidencyDesc);
        tvPhoneDesc = view.findViewById(R.id.tvPhoneDesc);
        tvPicturesDesc = view.findViewById(R.id.tvPicturesDesc);

        viewModel = new ViewModelProvider(this).get(ProfileInformationViewModel.class);
        observeViewModel();

        //loading current user
        viewModel.loadCurrentUser();

        //opening information fragment
        buttonInfo.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, ProfileInformationFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        });
        //opening favorites fragment
        buttonFavo.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, ProfileFavoritesFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        });
        //opening schedule fragment
        buttonSchedule.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, ProfileScheduleFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        });

        //pictures upload handling
        profileIcon.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_PROFILE_IMAGE);
        });
        uploadPictures.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Provider Pictures"), PICK_PROVIDER_IMAGES);
        });

        //update button click
        buttonUpdate.setOnClickListener(v -> {
            //open dialog
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Confirm Update")
                    .setMessage("Are you sure you want to update your profile?")
                    .setCancelable(false) //user must choose explicitly
                    .setPositiveButton("Yes", (dialog, which) -> {
                        GetUserDTO currentUser = viewModel.getUserLiveData().getValue();
                        if (currentUser == null) return;
                        String role = currentUser.getRole();

                        //if any of the fileds are null
                        java.util.function.Function<TextInputEditText, String> getSafe = editText -> {
                            if (editText == null || editText.getText() == null) return "";
                            return editText.getText().toString().trim();
                        };

                        //handling pictures
                        String profileBase64 = selectedProfileUri != null
                                ? encodeImageToBase64(selectedProfileUri)
                                : (currentUser.getPicture() != null ? currentUser.getPicture() : "");
                        List<String> pictures = (tempProviderPictures == null || tempProviderPictures.isEmpty())
                                ? (currentUser.getPictures() != null ? currentUser.getPictures() : new ArrayList<>())
                                : tempProviderPictures;

                        UpdateUser updateUser = new UpdateUser();
                        updateUser.setName(getSafe.apply(inputName));
                        updateUser.setSurname(getSafe.apply(inputSurname));
                        updateUser.setResidency(getSafe.apply(inputResidency));
                        updateUser.setPhoneNumber(getSafe.apply(inputPhone));
                        updateUser.setProviderName(getSafe.apply(inputProviderName));
                        updateUser.setDescription(getSafe.apply(inputDescription));
                        updateUser.setPicture(profileBase64);
                        updateUser.setPictures(pictures);

                        viewModel.updateUser(updateUser);
                        dialog.dismiss();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });

        //on change password
        buttonChangePassword.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, ProfilePasswordChangeFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        });

        //on delete profile
        buttonDelete.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Delete Profile")
                    .setMessage("Are you sure you want to delete your profile? This action cannot be undone.")
                    .setCancelable(false) //user must choose explicitly
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dialog.dismiss();
                        viewModel.terminateUser(requireContext());
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });
        //if deletion successful
        viewModel.isDeleted.observe(getViewLifecycleOwner(), deleted -> {
            if (deleted != null && deleted) {
                Toast.makeText(getContext(), "Successfully deleted account.", Toast.LENGTH_LONG).show();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, LoginFragment.newInstance())
                        .commit();
            }
        });

        inputName.addTextChangedListener(new ValidationWatcher(() -> {
            boolean ok = viewModel.isValidName(inputName.getText().toString());
            inputName.setError(ok ? null : "Invalid name (letters only, max 50)");
            updateButtonState();
        }));

        inputSurname.addTextChangedListener(new ValidationWatcher(() -> {
            boolean ok = viewModel.isValidSurname(inputSurname.getText().toString());
            inputSurname.setError(ok ? null : "Invalid surname (letters only, max 50)");
            updateButtonState();
        }));

        inputResidency.addTextChangedListener(new ValidationWatcher(() -> {
            boolean ok = viewModel.isValidResidency(inputResidency.getText().toString());
            layoutResidency.setError(ok ? null : "Invalid residency format: City, Country");
            updateButtonState();
        }));

        inputPhone.addTextChangedListener(new ValidationWatcher(() -> {
            boolean ok = viewModel.isValidPhone(inputPhone.getText().toString());
            layoutPhone.setError(ok ? null : "Invalid phone number");
            updateButtonState();
        }));

        inputProviderName.addTextChangedListener(new ValidationWatcher(() -> {
            boolean ok = viewModel.isValidProviderName(inputProviderName.getText().toString());
            layoutProviderName.setError(ok ? null : "Alphanumeric only, max 20");
            updateButtonState();
        }));

        inputDescription.addTextChangedListener(new ValidationWatcher(() -> {
            boolean ok = viewModel.isValidDescription(inputDescription.getText().toString());
            layoutDescription.setError(ok ? null : "Must be at least 20 characters");
            updateButtonState();
        }));

        viewModel.getSuccessLiveData().observe(getViewLifecycleOwner(), message -> {
            if (message != null && !message.isEmpty()) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    //after change of any field check if everything is valid so you can update profile
    private void updateButtonState() {
        GetUserDTO currentUser = viewModel.getUserLiveData().getValue();
        if (currentUser == null) return;
        String role = currentUser.getRole();

        java.util.function.Function<TextInputEditText, String> getSafe = editText -> {
            if (editText == null || editText.getText() == null) return "";
            return editText.getText().toString().trim();
        };

        String profileBase64 = selectedProfileUri != null
                ? encodeImageToBase64(selectedProfileUri)
                : (currentUser.getPicture() != null ? currentUser.getPicture() : "");
        List<String> pictures = (tempProviderPictures == null || tempProviderPictures.isEmpty())
                ? (currentUser.getPictures() != null ? currentUser.getPictures() : new ArrayList<>())
                : tempProviderPictures;

        //checking if all fileds are valid depending on the user role
        boolean isValid = viewModel.validateFieldsByRole(
                role,
                getSafe.apply(inputName),
                getSafe.apply(inputSurname),
                getSafe.apply(inputResidency),
                getSafe.apply(inputPhone),
                getSafe.apply(inputProviderName),
                getSafe.apply(inputDescription),
                profileBase64,
                pictures
        );
        buttonUpdate.setEnabled(isValid);
    }

    //handling loading of user
    private void observeViewModel() {
        viewModel.getUserLiveData().observe(getViewLifecycleOwner(), this::populateFields);
        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
        });
    }

    //handling what fields to show to user depending on his role
    private void populateFields(GetUserDTO user) {
        if (user == null) return;

        String role = user.getRole();

        inputEmail.setText(user.getEmail());
        inputName.setText(user.getName());
        inputSurname.setText(user.getSurname());
        inputResidency.setText(user.getResidency());
        inputPhone.setText(user.getPhoneNumber());
        inputProviderName.setText(user.getProviderName());
        inputDescription.setText(user.getDescription());

        //profile picture
        if (user.getPicture() != null && !user.getPicture().isEmpty()) {
            profileIcon.setShapeAppearanceModel(
                    profileIcon.getShapeAppearanceModel()
                            .toBuilder()
                            .setAllCornerSizes(profileIcon.getWidth() / 2f)
                            .build()
            );
            Glide.with(this)
                    .load(user.getPicture())
                    .placeholder(R.drawable.proficon)
                    .error(R.drawable.proficon)
                    .centerCrop()
                    .into(profileIcon);
        }

        //make all invisible
        layoutProviderName.setVisibility(View.GONE);
        layoutDescription.setVisibility(View.GONE);
        layoutResidency.setVisibility(View.GONE);
        layoutPhone.setVisibility(View.GONE);
        tvProviderNameLabel.setVisibility(View.GONE);
        tvDescriptionLabel.setVisibility(View.GONE);
        tvResidencyLabel.setVisibility(View.GONE);
        tvPhoneLabel.setVisibility(View.GONE);
        tvNameDesc.setVisibility(View.GONE);
        tvSurnameDesc.setVisibility(View.GONE);
        tvProviderNameDesc.setVisibility(View.GONE);
        tvDescriptionDesc.setVisibility(View.GONE);
        tvResidencyDesc.setVisibility(View.GONE);
        tvPhoneDesc.setVisibility(View.GONE);
        providerPicturesContainer.setVisibility(View.GONE);
        uploadPictures.setVisibility(View.GONE);
        tvPicturesDesc.setVisibility(View.GONE);

        //based on role make fields appear
        switch (role) {
            case "PROVIDER_ROLE":
                layoutProviderName.setVisibility(View.VISIBLE);
                layoutProviderName.setEnabled(false);
                layoutDescription.setVisibility(View.VISIBLE);
                layoutResidency.setVisibility(View.VISIBLE);
                layoutPhone.setVisibility(View.VISIBLE);

                tvProviderNameLabel.setVisibility(View.VISIBLE);
                tvDescriptionLabel.setVisibility(View.VISIBLE);
                tvResidencyLabel.setVisibility(View.VISIBLE);
                tvPhoneLabel.setVisibility(View.VISIBLE);

                tvNameDesc.setVisibility(View.VISIBLE);
                tvSurnameDesc.setVisibility(View.VISIBLE);
                tvProviderNameDesc.setVisibility(View.VISIBLE);
                tvDescriptionDesc.setVisibility(View.VISIBLE);
                tvResidencyDesc.setVisibility(View.VISIBLE);
                tvPhoneDesc.setVisibility(View.VISIBLE);

                providerPicturesContainer.setVisibility(View.VISIBLE);
                uploadPictures.setVisibility(View.VISIBLE);
                tvPicturesDesc.setVisibility(View.VISIBLE);
                imageCarouselFragment = ImageCarouselFragment.newInstance(new ArrayList<>(user.getPictures()));
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.providerPicturesContainer, imageCarouselFragment)
                        .commit();
                break;

            case "ORGANIZER_ROLE":
                layoutResidency.setVisibility(View.VISIBLE);
                layoutPhone.setVisibility(View.VISIBLE);

                tvNameDesc.setVisibility(View.VISIBLE);
                tvSurnameDesc.setVisibility(View.VISIBLE);
                tvResidencyLabel.setVisibility(View.VISIBLE);
                tvPhoneLabel.setVisibility(View.VISIBLE);
                tvResidencyDesc.setVisibility(View.VISIBLE);
                tvPhoneDesc.setVisibility(View.VISIBLE);
                break;

            case "AUSER_ROLE":
                tvNameDesc.setVisibility(View.VISIBLE);
                tvSurnameDesc.setVisibility(View.VISIBLE);
                break;

            case "ADMIN_ROLE":
                inputName.setEnabled(false);
                inputSurname.setEnabled(false);
                break;
        }

        buttonUpdate.setVisibility(!role.equals("ADMIN_ROLE") ? View.VISIBLE : View.GONE);
        buttonChangePassword.setVisibility(!role.equals("ADMIN_ROLE") ? View.VISIBLE : View.GONE);
        buttonDelete.setVisibility(!role.equals("AUSER_ROLE") && !role.equals("ADMIN_ROLE") ? View.VISIBLE : View.GONE);
    }

    //handling selection of pictures
    private static final int PICK_PROFILE_IMAGE = 1001;
    private static final int PICK_PROVIDER_IMAGES = 1002;
    private Uri selectedProfileUri;
    private List<Uri> selectedProviderUris = new ArrayList<>();
    private List<String> tempProviderPictures = new ArrayList<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK && data != null) {
            if (requestCode == PICK_PROFILE_IMAGE) {
                selectedProfileUri = data.getData();

                //if not valid image don't update
                String encoded = encodeImageToBase64(selectedProfileUri);
                if (!viewModel.isValidProfilePicture(encoded)) {
                    Toast.makeText(getContext(), "Selected profile picture is invalid or too large", Toast.LENGTH_SHORT).show();
                    selectedProfileUri = null;
                    return;
                }

                profileIcon.setShapeAppearanceModel(
                        profileIcon.getShapeAppearanceModel()
                                .toBuilder()
                                .setAllCornerSizes(profileIcon.getWidth() / 2f)
                                .build()
                );
                Glide.with(this).load(selectedProfileUri).centerCrop().into(profileIcon);
            } else if (requestCode == PICK_PROVIDER_IMAGES) {
                selectedProviderUris.clear();
                List<String> encodedImages = new ArrayList<>();
                int count = 0;

                if (data.getClipData() != null) {
                    count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        String base64 = encodeImageToBase64(imageUri);

                        if (!viewModel.isValidProfilePicture(base64)) {
                            Toast.makeText(getContext(), "One or more provider pictures are invalid or too large", Toast.LENGTH_SHORT).show();
                            continue;
                        }

                        selectedProviderUris.add(imageUri);
                        encodedImages.add(base64);
                    }
                } else if (data.getData() != null) {
                    Uri imageUri = data.getData();
                    String base64 = encodeImageToBase64(imageUri);

                    if (!viewModel.isValidProfilePicture(base64)) {
                        Toast.makeText(getContext(), "Selected provider picture is invalid or too large", Toast.LENGTH_SHORT).show();
                    } else {
                        selectedProviderUris.add(imageUri);
                        encodedImages.add(base64);
                    }
                }

                //storing it later in updateUser
                tempProviderPictures = encodedImages;
                //updating picture carousel UI
                imageCarouselFragment = ImageCarouselFragment.newInstance(new ArrayList<>(encodedImages));
                getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.providerPicturesContainer, imageCarouselFragment)
                        .commit();
            }
        }
    }

    //encoding image in correct format
    private String encodeImageToBase64(Uri uri) {
        try {
            String mimeType = getContext().getContentResolver().getType(uri);
            if (mimeType == null) mimeType = "image/jpeg";

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            Bitmap.CompressFormat format = mimeType.contains("png")
                    ? Bitmap.CompressFormat.PNG
                    : Bitmap.CompressFormat.JPEG;

            bitmap.compress(format, 80, baos);
            byte[] bytes = baos.toByteArray();

            String base64Data = Base64.encodeToString(bytes, Base64.NO_WRAP);
            return "data:" + mimeType + ";base64," + base64Data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}