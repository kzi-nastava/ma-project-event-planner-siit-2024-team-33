package com.example.myapplication.ui.view.page.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;


public class ProfileTypeFragment extends Fragment {

    private RadioGroup profileTypeGroup;
    private Button nextButton, previousButton;
    private TextView errorText;


    public ProfileTypeFragment() {
    }

    public static ProfileTypeFragment newInstance() {
        return new ProfileTypeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_type, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileTypeGroup = view.findViewById(R.id.profile_type_group);
        nextButton = view.findViewById(R.id.button_next);
        previousButton = view.findViewById(R.id.button_previous);
        errorText = view.findViewById(R.id.error_message);

        profileTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            nextButton.setEnabled(true);
            errorText.setVisibility(View.GONE);
        });

        nextButton.setOnClickListener(v -> {
            int selectedProfileId = profileTypeGroup.getCheckedRadioButtonId();

            if (selectedProfileId == -1) {
                //no option selected
                errorText.setVisibility(View.VISIBLE);
            } else {
                Fragment nextFragment;
                if (selectedProfileId == R.id.radio_event_planner) {
                    nextFragment = OrganizerSignupMandatoryFragment.newInstance();
                } else {
                    nextFragment = ProviderSignupMandatoryFragment.newInstance();
                }

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, nextFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        previousButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, LoginFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        });
    }
}