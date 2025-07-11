package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.api.EventApi;
import com.example.myapplication.dto.PageResponse;
import com.example.myapplication.dto.eventDTO.FilterEventDTO;
import com.example.myapplication.dto.eventDTO.MinimalEventDTO;
import com.example.myapplication.dto.eventDTO.MinimalEventTypeDTO;
import com.example.myapplication.dto.offerDTO.MinimalOfferDTO;
import com.example.myapplication.services.ApiClient;
import com.example.myapplication.services.AuthenticationService;
import com.example.myapplication.services.EventService;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsPage extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FilterEventDTO filter;
    private String mParam1;
    private String mParam2;
    private EventService eventService;
    private List<MinimalEventDTO> events;
    private AuthenticationService authService;
    private int currentPage = 0;
    private final int pageSize = 5;

    public EventsPage() {
        // Required empty public constructor
    }

    public static EventsPage newInstance(String param1, String param2) {
        EventsPage fragment = new EventsPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_page, container, false);

        Button filterButton = view.findViewById(R.id.open_filters);
        filterButton.setOnClickListener(v -> showFilterDialog());

        Button btnNext = view.findViewById(R.id.btn_next);
        Button btnPrev = view.findViewById(R.id.btn_previous);

        btnNext.setOnClickListener(v -> {
            currentPage++;
            loadAllEvents(view);
        });

        btnPrev.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                loadAllEvents(view);
            }
        });

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authService = new AuthenticationService(requireContext());

        SharedPreferences prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        String token = prefs.getString("jwt", null);

        if (token != null) {
            eventService = new EventService();
            filter = new FilterEventDTO("", "", 0, "", "", Collections.emptyList());
            loadAllEvents(view);
        } else {
            Log.w("EventsPage", "JWT token is not yet available, skipping API call.");
        }
    }


    private void loadAllEvents(View view) {
        Call<PageResponse<MinimalEventDTO>> call;

        call = eventService.getFilteredEvents(this.filter, this.currentPage, this.pageSize);
        call.enqueue(new Callback<PageResponse<MinimalEventDTO>>(){
            @Override
            public void onResponse(Call<PageResponse<MinimalEventDTO>> call, Response<PageResponse<MinimalEventDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    events = response.body().getContent();
                    displayAllEvents(view);
                    PageResponse<MinimalEventDTO> page = response.body();
                    boolean isLastPage = page.getNumber() + 1 >= page.getTotalPages();
                    updatePaginationButtons(view, isLastPage);
                } else {
                    Log.e("EventsPage", "Failed to load events: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<PageResponse<MinimalEventDTO>> call, Throwable t) {
                Log.e("EventsPage", "Error loading events: " + t.getMessage());
            }
        });
    }

    private void displayAllEvents(View view) {
        LinearLayout parentLayout = view.findViewById(R.id.eventCardsPlace);
        LayoutInflater inflater = LayoutInflater.from(getContext());

        parentLayout.removeAllViews();

        if (events != null && !events.isEmpty()) {
            for (MinimalEventDTO event : events) {
                View eventView = inflater.inflate(R.layout.event_card, parentLayout, false);

                TextView itemTitle = eventView.findViewById(R.id.item_title);
                TextView itemText = eventView.findViewById(R.id.item_text);

                itemTitle.setText(event.getName());
                itemText.setText(event.getDescription());

                parentLayout.addView(eventView);
            }
        }
    }

    private void showFilterDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_event_filter, null);
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        dialog.setContentView(dialogView);

        Button confirmButton = dialogView.findViewById(R.id.btn_confirm);
        confirmButton.setOnClickListener(v -> {
            handleFilterDialogConfirmation(dialogView);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void handleFilterDialogConfirmation(View dialogView) {
        EditText etName = dialogView.findViewById(R.id.et_name);
        EditText etLocation = dialogView.findViewById(R.id.et_location);
        EditText etAttendees = dialogView.findViewById(R.id.et_attendees);
        DatePicker datePickerFirst = dialogView.findViewById(R.id.datepicker_first);
        DatePicker datePickerLast = dialogView.findViewById(R.id.datepicker_last);
        Spinner spinnerCategory = dialogView.findViewById(R.id.spinner_category);


        this.filter.name = etName.getText().toString().trim();
        this.filter.location = etLocation.getText().toString().trim();

        String attendeesText = etAttendees.getText().toString().trim();
        try {
            this.filter.numOfAttendees = attendeesText.isEmpty() ? 0 : Integer.parseInt(attendeesText);
        } catch (NumberFormatException e) {
            Log.e("EventsPage", "Invalid number format for attendees");
        }

        this.filter.firstPossibleDate = getFormattedDate(datePickerFirst);
        this.filter.lastPossibleDate = getFormattedDate(datePickerLast);
        this.filter.eventTypes = getSelectedEventTypeIds(spinnerCategory);

        View rootView = getView();
        if (rootView != null) {
            loadAllEvents(rootView);
        }
    }

    private List<Integer> getSelectedEventTypeIds(Spinner spinner) {
        List<Integer> selectedTypeIds = new ArrayList<>();
        MinimalEventTypeDTO selectedType = (MinimalEventTypeDTO) spinner.getSelectedItem();
        if (selectedType != null) {
            selectedTypeIds.add(selectedType.id);
        }
        return selectedTypeIds;
    }

    private String getFormattedDate(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        return String.format("%04d-%02d-%02d", year, month, day);
    }
    private void updatePaginationButtons(View view, boolean isLastPage) {
        Button btnNext = view.findViewById(R.id.btn_next);
        Button btnPrev = view.findViewById(R.id.btn_previous);
        TextView pageIndicator = view.findViewById(R.id.page_indicator);

        btnPrev.setEnabled(currentPage > 0);
        btnNext.setEnabled(!isLastPage);

        pageIndicator.setText("Page " + (currentPage + 1));
    }

}
