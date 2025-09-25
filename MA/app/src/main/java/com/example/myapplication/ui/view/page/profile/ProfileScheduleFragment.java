package com.example.myapplication.ui.view.page.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.R;

public class ProfileScheduleFragment extends Fragment {

    private Button buttonInfo, buttonFavo, buttonSchedule;

    public ProfileScheduleFragment() {}

    public static ProfileScheduleFragment newInstance() {
        return new ProfileScheduleFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_schedule, container, false);

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