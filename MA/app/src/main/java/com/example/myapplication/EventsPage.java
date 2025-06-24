package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.myapplication.dto.eventDTO.FilterEventDTO;
import com.example.myapplication.dto.eventDTO.MinimalEventDTO;
import com.example.myapplication.dto.eventDTO.MinimalEventTypeDTO;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsPage extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private EventService eventService;
    private List<MinimalEventDTO> events;

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

        eventService = new EventService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_page, container, false);

        Button filterButton = view.findViewById(R.id.open_filters);
        filterButton.setOnClickListener(v -> showFilterDialog());

        loadAllEvents(view);
        return view;
    }

    private void loadAllEvents(View view) {
        eventService.getAllEvents().enqueue(new Callback<List<MinimalEventDTO>>() {
            @Override
            public void onResponse(Call<List<MinimalEventDTO>> call, Response<List<MinimalEventDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    events = response.body();
                    displayAllEvents(view);
                } else {
                    Log.e("EventsPage", "Failed to fetch events. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<MinimalEventDTO>> call, Throwable t) {
                Log.e("EventsPage", "Error fetching events: " + t.getMessage(), t);
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

        FilterEventDTO filter = new FilterEventDTO();
        filter.name = etName.getText().toString().trim();
        filter.location = etLocation.getText().toString().trim();

        String attendeesText = etAttendees.getText().toString().trim();
        try {
            filter.numOfAttendees = attendeesText.isEmpty() ? 0 : Integer.parseInt(attendeesText);
        } catch (NumberFormatException e) {
            Log.e("EventsPage", "Invalid number format for attendees");
        }

        filter.firstPossibleDate = getFormattedDate(datePickerFirst);
        filter.lastPossibleDate = getFormattedDate(datePickerLast);
        filter.eventTypes = getSelectedEventTypeIds(spinnerCategory);

        getFilteredEvents(filter, 2);
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

    private void getFilteredEvents(FilterEventDTO filter, Integer id) {
        eventService.getFilteredEvents(filter, id).enqueue(new Callback<List<MinimalEventDTO>>() {
            @Override
            public void onResponse(Call<List<MinimalEventDTO>> call, Response<List<MinimalEventDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    events = response.body();
                    View rootView = getView();
                    if (rootView != null) {
                        displayAllEvents(rootView);
                    }
                } else {
                    Log.e("EventsPage", "Filtering failed. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<MinimalEventDTO>> call, Throwable t) {
                Log.e("EventsPage", "Error fetching filtered events: " + t.getMessage(), t);
            }
        });
    }
}
