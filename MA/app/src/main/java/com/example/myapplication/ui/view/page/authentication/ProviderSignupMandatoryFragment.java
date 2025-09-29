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
import com.example.myapplication.ui.view.page.home.component.ImageCarouselFragment;
import com.example.myapplication.ui.viewmodel.authentication.ProviderSignupViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;


public class ProviderSignupMandatoryFragment extends Fragment {

    private ProviderSignupViewModel viewModel;

    //step containers
    private View stepEmail, stepMandatory, stepOptional;

    //step 1
    private TextInputLayout layoutEmail;
    private TextInputEditText inputEmail;

    //step 2
    private TextInputLayout layoutName, layoutSurname, layoutPassword, layoutConfirmPassword, layoutResidency, layoutPhone, layoutProviderName, layoutDescription;
    private TextInputEditText inputName, inputSurname, inputPassword, inputConfirmPassword, inputResidency, inputPhone, inputProviderName, inputDescription;

    //step 3
    private FrameLayout profilePictureContainer;
    private Button uploadPictures;

    //buttons for navigation
    private Button buttonPrev, buttonNext;

    public ProviderSignupMandatoryFragment() {}

    public static ProviderSignupMandatoryFragment newInstance() {
        return new ProviderSignupMandatoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_provider_signup_mandatory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(ProviderSignupViewModel.class);

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
        layoutProviderName = view.findViewById(R.id.layoutProviderName);
        layoutDescription = view.findViewById(R.id.layoutDescription);
        inputName = view.findViewById(R.id.inputName);
        inputSurname = view.findViewById(R.id.inputSurname);
        inputPassword = view.findViewById(R.id.inputPassword);
        inputConfirmPassword = view.findViewById(R.id.inputConfirmPassword);
        inputResidency = view.findViewById(R.id.inputResidency);
        inputPhone = view.findViewById(R.id.inputPhone);
        inputProviderName = view.findViewById(R.id.inputProviderName);
        inputDescription = view.findViewById(R.id.inputDescription);

        profilePictureContainer = view.findViewById(R.id.profilePictureContainer);
        uploadPictures = view.findViewById(R.id.button_upload);

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
        viewModel.providerNameError.observe(getViewLifecycleOwner(), layoutProviderName::setError);
        viewModel.descriptionError.observe(getViewLifecycleOwner(), layoutDescription::setError);

        //opening system picker for images
        profilePictureContainer.setOnClickListener(v -> openImagePicker());
        uploadPictures.setOnClickListener(v -> openProviderPicturesPicker());

        viewModel.providerPictures.observe(getViewLifecycleOwner(), pictures -> {
            if (pictures != null && !pictures.isEmpty()) {
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.providerPicturesContainer, ImageCarouselFragment.newInstance(new ArrayList<>(pictures)))
                        .commit();
            } else {
                //clearing if empty
                getChildFragmentManager().beginTransaction()
                        .replace(R.id.providerPicturesContainer, new Fragment())
                        .commit();
            }
        });

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
                inputConfirmPassword, inputResidency, inputPhone, inputProviderName, inputDescription};

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
                    } else if (id == R.id.inputProviderName) {
                        viewModel.providerName.setValue(value);
                    } else if (id == R.id.inputPassword) {
                        viewModel.password.setValue(value);
                    } else if (id == R.id.inputConfirmPassword) {
                        viewModel.confirmPassword.setValue(value);
                    } else if (id == R.id.inputDescription) {
                        viewModel.description.setValue(value);
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

    //working with images
    private static final int PICK_IMAGE_REQUEST = 1;

    //opening image picker from phone
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), PICK_IMAGE_REQUEST);
    }

    private static final int PICK_PROVIDER_PICTURES_REQUEST = 2;

    private void openProviderPicturesPicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Provider Pictures"), PICK_PROVIDER_PICTURES_REQUEST);
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
        else if(requestCode == PICK_PROVIDER_PICTURES_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
            List<Bitmap> bitmaps = new ArrayList<>();
            List<String> mimeTypes = new ArrayList<>();

            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count && i < 8; i++) {
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    Bitmap bitmap = loadBitmapFromUri(uri);
                    String mime = requireContext().getContentResolver().getType(uri);
                    if (bitmap != null && mime != null) {
                        bitmaps.add(bitmap);
                        mimeTypes.add(mime);
                    }
                }
            } else if (data.getData() != null) {
                Uri uri = data.getData();
                Bitmap bitmap = loadBitmapFromUri(uri);
                String mime = requireContext().getContentResolver().getType(uri);
                if (bitmap != null && mime != null) {
                    bitmaps.add(bitmap);
                    mimeTypes.add(mime);
                }
            }

            //now replace all old pictures
            viewModel.setProviderPictures(bitmaps, mimeTypes);
        }
    }

    private Bitmap loadBitmapFromUri(Uri uri) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                return ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().getContentResolver(), uri));
            } else {
                return MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Failed to process image", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}