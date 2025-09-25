package com.example.myapplication.ui.view.page.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.dto.userDTO.GetUserDTO;
import com.example.myapplication.data.dto.userDTO.UpdateUser;
import com.example.myapplication.ui.viewmodel.profile.ProfileInformationViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class ProfileFavoritesFragment extends Fragment {

    private Button buttonInfo, buttonFavo, buttonSchedule;

    public ProfileFavoritesFragment() {}

    public static ProfileFavoritesFragment newInstance() {
        return new ProfileFavoritesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_favorites, container, false);

        buttonInfo = view.findViewById(R.id.button_information);
        buttonFavo = view.findViewById(R.id.button_favorites);
        buttonSchedule = view.findViewById(R.id.button_schedule);

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

        return view;
    }
}