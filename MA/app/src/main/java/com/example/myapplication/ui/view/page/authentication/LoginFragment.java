package com.example.myapplication.ui.view.page.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.data.models.AuthentifiedUser;
import com.example.myapplication.ui.view.page.navigation.MainPageActivity;
import com.example.myapplication.ui.viewmodel.authentication.LoginViewModel;

public class LoginFragment extends Fragment {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private TextView registerText;

    private LoginViewModel loginViewModel;

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailInput = view.findViewById(R.id.email_input);
        passwordInput = view.findViewById(R.id.password_input);
        loginButton = view.findViewById(R.id.login_button);
        registerText = view.findViewById(R.id.login_register_text);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Get data from intent if available (optional)
        /*Uri data = getActivity().getIntent().getData();
        if (data != null) {
            String emailFromLink = data.getQueryParameter("email");
            if (emailFromLink != null) {
                emailInput.setText(emailFromLink);
            }
        }*/

        //click listener
        loginButton.setOnClickListener(v -> performLogin());

        registerText.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, new ProfileTypeFragment())
                    .addToBackStack(null)
                    .commit();
        });

        observeViewModel();

        return view;
    }

    private void performLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Please enter email and password.", Toast.LENGTH_SHORT).show();
            return;
        }
        loginViewModel.login(email, password);
    }

    private void observeViewModel() {
        loginViewModel.getLoginSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                AuthentifiedUser user = loginViewModel.getLoggedUser().getValue();
                if (user != null) {
                    Intent intent = new Intent(getActivity(), MainPageActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        loginViewModel.getLoginError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
            }
        });

    }
}