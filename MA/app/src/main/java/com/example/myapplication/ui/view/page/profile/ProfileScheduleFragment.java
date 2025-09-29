package com.example.myapplication.ui.view.page.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.ui.view.page.event.EventDetailsFragment;
import com.example.myapplication.ui.viewmodel.profile.ScheduleViewModel;

import java.time.LocalDate;

public class ProfileScheduleFragment extends Fragment {

    private Button buttonInfo, buttonFavo, buttonSchedule;

    ScheduleViewModel viewModel;
    RecyclerView recycler;
    ProgressBar progressBar;

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

        viewModel = new ViewModelProvider(this).get(ScheduleViewModel.class);
        FavoriteEventsAdapter adapter = new FavoriteEventsAdapter(event -> {
            Bundle args = new Bundle();
            args.putInt("eventId", event.getId());
            EventDetailsFragment fragment = EventDetailsFragment.newInstance();
            fragment.setArguments(args);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        recycler = view.findViewById(R.id.favorites_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setAdapter(adapter);

        progressBar = view.findViewById(R.id.progressBarFavorites);

        DatePicker datePicker = view.findViewById(R.id.datePickerStart);
        datePicker.setOnDateChangedListener((view1, year, monthOfYear, dayOfMonth) -> {
            LocalDate selectedDate = LocalDate.of(year, monthOfYear + 1, dayOfMonth);
            viewModel.loadEventsForDate(selectedDate);
        });

        viewModel.getEventsLiveData().observe(getViewLifecycleOwner(), events -> {
            adapter.clearItems();
            adapter.addItems(events);
        });

        viewModel.getIsLoading().observe(getViewLifecycleOwner(), loading -> {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        });

        viewModel.getError().observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}