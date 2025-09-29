package com.example.myapplication.ui.view.page.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.ui.viewmodel.profile.ProfilePasswordChangeViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ProfilePasswordChangeFragment extends Fragment {

    private class ValidationWatcher implements TextWatcher {
        private final Runnable validator;

        ValidationWatcher(Runnable validator) {
            this.validator = validator;
        }

        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override public void afterTextChanged(Editable s) { validator.run(); }
    }

    private ProfilePasswordChangeViewModel viewModel;

    private Button buttonInfo, buttonFavo, buttonSchedule;

    private TextInputLayout layoutPassword, layoutConfirmPassword;
    private TextInputEditText inputOldPassword, inputPassword, inputConfirmPassword;

    private Button buttonBack, buttonConfirm;

    public ProfilePasswordChangeFragment() {}

    public static ProfilePasswordChangeFragment newInstance() {
        return new ProfilePasswordChangeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_password_change, container, false);

        viewModel = new ViewModelProvider(this).get(ProfilePasswordChangeViewModel.class);
        viewModel.init(requireContext());

        buttonInfo = view.findViewById(R.id.button_information);
        buttonFavo = view.findViewById(R.id.button_favorites);
        buttonSchedule = view.findViewById(R.id.button_schedule);

        inputOldPassword = view.findViewById(R.id.inputOldPassword);
        layoutPassword = view.findViewById(R.id.layoutPassword);
        layoutConfirmPassword = view.findViewById(R.id.layoutConfirmPassword);
        inputPassword = view.findViewById(R.id.inputPassword);
        inputConfirmPassword = view.findViewById(R.id.inputConfirmPassword);

        buttonBack = view.findViewById(R.id.go_back);
        buttonConfirm = view.findViewById(R.id.confirmPass);

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

        //going back to information page
        buttonBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, ProfileInformationFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        });

        //listeners for change of password fields
        inputPassword.addTextChangedListener(new ValidationWatcher(() -> {
            viewModel.validatePasswords(
                    inputPassword.getText() != null ? inputPassword.getText().toString() : "",
                    inputConfirmPassword.getText() != null ? inputConfirmPassword.getText().toString() : ""
            );
        }));
        inputConfirmPassword.addTextChangedListener(new ValidationWatcher(() -> {
            viewModel.validatePasswords(
                    inputPassword.getText() != null ? inputPassword.getText().toString() : "",
                    inputConfirmPassword.getText() != null ? inputConfirmPassword.getText().toString() : ""
            );
        }));
        viewModel.passwordError.observe(getViewLifecycleOwner(), error -> {
            layoutPassword.setError(error);
        });
        viewModel.confirmPasswordError.observe(getViewLifecycleOwner(), error -> {
            layoutConfirmPassword.setError(error);
        });
        viewModel.isFormValid.observe(getViewLifecycleOwner(), valid -> {
            buttonConfirm.setEnabled(valid != null && valid);
        });


        //confirming password change
        buttonConfirm.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Confirm Password Change")
                    .setMessage("Are you sure you want to change your password?")
                    .setCancelable(false) //user must choose explicitly
                    .setPositiveButton("Yes", (dialog, which) -> {

                        String oldPass = inputOldPassword.getText() != null ? inputOldPassword.getText().toString() : "";
                        String newPass = inputPassword.getText() != null ? inputPassword.getText().toString() : "";

                        viewModel.changePassword(oldPass, newPass);

                        dialog.dismiss();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });

        //after successful update
        viewModel.user.observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                Toast.makeText(requireContext(), "Password changed successfully!", Toast.LENGTH_LONG).show();

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, ProfileInformationFragment.newInstance())
                        .commit();
            }
        });
        //after unsuccessful update
        viewModel.error.observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }
}