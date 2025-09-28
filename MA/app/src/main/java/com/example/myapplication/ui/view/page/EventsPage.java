package com.example.myapplication.ui.view.page;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.data.dto.PageResponse;
import com.example.myapplication.data.dto.eventDTO.FilterEventDTO;
import com.example.myapplication.data.dto.eventDTO.MinimalEventDTO;
import com.example.myapplication.data.dto.eventTypeDTO.MinimalEventTypeDTO;
import com.example.myapplication.data.services.authentication.AuthenticationService;
import com.example.myapplication.data.services.event.EventService;
import com.example.myapplication.data.services.event.EventTypeService;
import com.example.myapplication.ui.view.page.events.EventDetailsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsPage extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<MinimalEventTypeDTO> availableEventTypes = new ArrayList<>();

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

    public static EventsPage newInstance() {
        EventsPage fragment = new EventsPage();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
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

        eventService = new EventService();
        filter = new FilterEventDTO("", "", 0, "", "", Collections.emptyList());
        loadAllEvents(view);
    }


    private void loadAllEvents(View view) {
        Call<PageResponse<MinimalEventDTO>> call = eventService.getEventList(filter, currentPage, pageSize);

        call.enqueue(new Callback<PageResponse<MinimalEventDTO>>() {
            @Override
            public void onResponse(Call<PageResponse<MinimalEventDTO>> call, Response<PageResponse<MinimalEventDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PageResponse<MinimalEventDTO> page = response.body();
                    List<MinimalEventDTO> newEvents = page.getContent();

                    if (newEvents != null && !newEvents.isEmpty()) {
                        events = newEvents;
                        displayAllEvents(view);
                    } else {
                        events = Collections.emptyList();
                        displayAllEvents(view);
                        Toast.makeText(requireContext(), "No events found for this filter.", Toast.LENGTH_SHORT).show();
                    }

                    boolean isLastPage = page.getNumber() + 1 >= page.getTotalPages();
                    updatePaginationButtons(view, isLastPage);
                } else {
                    Log.e("EventsPage", "Failed to load events: " + response.code());
                    Toast.makeText(requireContext(), "Failed to load events.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PageResponse<MinimalEventDTO>> call, Throwable t) {
                Log.e("EventsPage", "Error loading events: " + t.getMessage());
                Toast.makeText(requireContext(), "Error loading events.", Toast.LENGTH_SHORT).show();
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

                eventView.setOnClickListener(v -> {
                    EventDetailsFragment fragment = new EventDetailsFragment();

                    Bundle args = new Bundle();
                    args.putInt("eventId", event.getId());
                    fragment.setArguments(args);

                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment, fragment, "EventDetailsFragment")
                            .addToBackStack(null)
                            .commit();
                });

                parentLayout.addView(eventView);
            }
        }
    }

    private void showFilterDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_event_filter, null);
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        dialog.setContentView(dialogView);

        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);

        dialog.setOnShowListener(d -> {
            bottomNav.animate()
                    .translationY(-1500)
                    .setDuration(100)
                    .start();
        });

        dialog.setOnDismissListener(d -> {
            bottomNav.animate()
                    .translationY(0)
                    .setDuration(100)
                    .start();
        });

        Spinner spinnerCategory = dialogView.findViewById(R.id.spinner_category);
        EventTypeService eventTypeService = new EventTypeService();

        eventTypeService.getEventTypes().enqueue(new Callback<List<MinimalEventTypeDTO>>() {
            @Override
            public void onResponse(Call<List<MinimalEventTypeDTO>> call, Response<List<MinimalEventTypeDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MinimalEventTypeDTO> allTypes = response.body();
                    List<MinimalEventTypeDTO> filteredTypes = filterEventTypesByEvents(allTypes);

                    MinimalEventTypeDTO allOption = new MinimalEventTypeDTO();
                    allOption.setId(-1);
                    allOption.setName("All");
                    filteredTypes.add(0, allOption);

                    ArrayAdapter<MinimalEventTypeDTO> adapter = new ArrayAdapter<>(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            filteredTypes
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategory.setAdapter(adapter);
                } else {
                    Log.e("EventsPage", "Failed to load event types: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<MinimalEventTypeDTO>> call, Throwable t) {
                Log.e("EventsPage", "Error loading event types: " + t.getMessage());
            }
        });

        Button confirmButton = dialogView.findViewById(R.id.btn_confirm);
        confirmButton.setOnClickListener(v -> {
            handleFilterDialogConfirmation(dialogView);
            dialog.dismiss();
        });

        CheckBox checkboxFirstDate = dialogView.findViewById(R.id.checkbox_first_date);
        DatePicker datePickerFirst = dialogView.findViewById(R.id.datepicker_first);
        datePickerFirst.setVisibility(View.GONE);
        checkboxFirstDate.setOnCheckedChangeListener((buttonView, isChecked) ->
                datePickerFirst.setVisibility(isChecked ? View.VISIBLE : View.GONE)
        );

        CheckBox checkboxLastDate = dialogView.findViewById(R.id.checkbox_last_date);
        DatePicker datePickerLast = dialogView.findViewById(R.id.datepicker_last);
        datePickerLast.setVisibility(View.GONE);
        checkboxLastDate.setOnCheckedChangeListener((buttonView, isChecked) ->
                datePickerLast.setVisibility(isChecked ? View.VISIBLE : View.GONE)
        );

        dialog.show();
    }




    private void handleFilterDialogConfirmation(View dialogView) {
        EditText etName = dialogView.findViewById(R.id.et_name);
        EditText etLocation = dialogView.findViewById(R.id.et_location);
        EditText etAttendees = dialogView.findViewById(R.id.et_attendees);

        CheckBox checkboxFirstDate = dialogView.findViewById(R.id.checkbox_first_date);
        CheckBox checkboxLastDate = dialogView.findViewById(R.id.checkbox_last_date);

        DatePicker datePickerFirst = dialogView.findViewById(R.id.datepicker_first);
        DatePicker datePickerLast = dialogView.findViewById(R.id.datepicker_last);
        Spinner spinnerCategory = dialogView.findViewById(R.id.spinner_category);

        filter.name = etName.getText().toString().trim();
        filter.location = etLocation.getText().toString().trim();

        String attendeesText = etAttendees.getText().toString().trim();
        try {
            filter.numOfAttendees = attendeesText.isEmpty() ? 0 : Integer.parseInt(attendeesText);
        } catch (NumberFormatException e) {
            Log.e("EventsPage", "Invalid number format for attendees");
            filter.numOfAttendees = 0;
        }

        filter.firstPossibleDate = checkboxFirstDate.isChecked() ? getFormattedDate(datePickerFirst) : "";
        filter.lastPossibleDate = checkboxLastDate.isChecked() ? getFormattedDate(datePickerLast) : "";
        filter.eventTypes = getSelectedEventTypeIds(spinnerCategory);
        Log.e("SUPER GASCINA",filter.firstPossibleDate);
        Log.e("SUPER GASCINA",filter.lastPossibleDate);

        currentPage = 0;

        View rootView = getView();
        if (rootView != null) {
            loadAllEvents(rootView);
        }
    }


    private List<Integer> getSelectedEventTypeIds(Spinner spinner) {
        List<Integer> selectedTypeIds = new ArrayList<>();
        MinimalEventTypeDTO selectedType = (MinimalEventTypeDTO) spinner.getSelectedItem();
        if (selectedType != null && selectedType.getId() != -1) {
            selectedTypeIds.add(selectedType.getId());
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

    private List<MinimalEventTypeDTO> filterEventTypesByEvents(List<MinimalEventTypeDTO> allTypes) {
        if (events == null || events.isEmpty()) return new ArrayList<>();

        List<MinimalEventTypeDTO> filteredTypes = new ArrayList<>();
        for (MinimalEventTypeDTO type : allTypes) {
            boolean typeUsed = false;
            for (MinimalEventDTO event : events) {
                MinimalEventTypeDTO eventTypes = event.getValidEvent();
                if (eventTypes != null) {
                    if (eventTypes.getId().equals(type.getId())) {
                        typeUsed = true;
                        break;
                    }
                }
                if (typeUsed) break;
            }
            if (typeUsed) {
                filteredTypes.add(type);
            }
        }

        return filteredTypes;
    }
}
