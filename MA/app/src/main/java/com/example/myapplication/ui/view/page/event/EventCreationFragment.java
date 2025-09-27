package com.example.myapplication.ui.view.page.event;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.dto.eventTypeDTO.MinimalEventTypeDTO;
import com.example.myapplication.ui.view.page.EventsPage;
import com.example.myapplication.ui.view.page.authentication.RegistrationCompletedFragment;
import com.example.myapplication.ui.viewmodel.EventCreationViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EventCreationFragment extends Fragment {

    class SimpleTextWatcher implements TextWatcher {
        private final Consumer<CharSequence> callback;

        public SimpleTextWatcher(Consumer<CharSequence> callback) {
            this.callback = callback;
        }

        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
            callback.accept(s);
        }
        @Override public void afterTextChanged(Editable s) {}
    }


    //mandatory data
    private TextInputEditText inputEventName, inputDescription, inputCapacity, inputLocation;
    private TextInputLayout layoutName, layoutDescription, layoutCapacity, layoutLocation;
    private LinearLayout privateFormSection;
    private DatePicker datePickerStart, datePickerEnd;
    private TimePicker timePickerStart, timePickerEnd;
    private TextView dateStartError, dateEndError;
    private Spinner spinnerEventType;
    private RadioGroup radioGroupPrivacy;



    //event activities fields
    private TextInputEditText inputNameEa, inputDescriptionEa, inputLocationEa;
    private DatePicker datePickerStartEa, datePickerEndEa;
    private TimePicker timePickerStartEa, timePickerEndEa;
    private Button btnAddActivity;
    private LinearLayout activitiesContainer;



    private Button btnConfirm;

    //event creation viewModel
    private EventCreationViewModel viewModel;
    private MinimalEventTypeDTO selectedEventType;
    private List<String> activities = new ArrayList<>();

    public static EventCreationFragment newInstance() {
        return new EventCreationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_creation2, container, false);

        //mandatory data
        inputEventName = view.findViewById(R.id.inputName);
        inputDescription = view.findViewById(R.id.inputDescription);
        inputCapacity = view.findViewById(R.id.inputCapacity);
        inputLocation = view.findViewById(R.id.inputLocation);
        layoutName = view.findViewById(R.id.layoutName);
        layoutDescription = view.findViewById(R.id.layoutDescription);
        layoutCapacity = view.findViewById(R.id.layoutCapacity);
        layoutLocation = view.findViewById(R.id.layoutLocation);
        spinnerEventType = view.findViewById(R.id.spinnerEventType);
        datePickerStart = view.findViewById(R.id.datePickerStart);
        timePickerStart = view.findViewById(R.id.timePickerStart);
        datePickerEnd = view.findViewById(R.id.datePickerEnd);
        timePickerEnd = view.findViewById(R.id.timePickerEnd);
        dateStartError = view.findViewById(R.id.dateStartError);
        dateEndError = view.findViewById(R.id.dateEndError);
        radioGroupPrivacy = view.findViewById(R.id.radioGroupPrivacy);
        privateFormSection = view.findViewById(R.id.privateFormSection);

        //event activities
        activitiesContainer = view.findViewById(R.id.activitiesContainer);
        btnAddActivity = view.findViewById(R.id.btnAddActivity);
        inputNameEa = view.findViewById(R.id.inputNameEa);
        inputDescriptionEa = view.findViewById(R.id.inputDescriptionEa);
        inputLocationEa = view.findViewById(R.id.inputLocationEa);
        datePickerStartEa = view.findViewById(R.id.datePickerStartEa);
        timePickerStartEa = view.findViewById(R.id.timePickerStartEa);
        datePickerEndEa = view.findViewById(R.id.datePickerEndEa);
        timePickerEndEa = view.findViewById(R.id.timePickerEndEa);

        btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setEnabled(false);

        btnAddActivity.setOnClickListener(v -> {
            String name = inputNameEa.getText() != null ? inputNameEa.getText().toString().trim() : "";
            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter activity name", Toast.LENGTH_SHORT).show();
                return;
            }
            activities.add(name);

            View row = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_event_activity, activitiesContainer, false);
            TextView tvName = row.findViewById(R.id.tvActivityName);
            tvName.setText(name);
            Button btnRemove = row.findViewById(R.id.btnRemoveActivity);
            btnRemove.setOnClickListener(x -> {
                activitiesContainer.removeView(row);
                activities.remove(name);
            });
            activitiesContainer.addView(row);

            //clearing fields
            inputNameEa.setText("");
            inputDescriptionEa.setText("");
            inputLocationEa.setText("");
        });

        //toggling privacy
        radioGroupPrivacy.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioPrivate) {
                privateFormSection.setVisibility(View.VISIBLE);
            } else {
                privateFormSection.setVisibility(View.GONE);
            }
        });

        viewModel = new ViewModelProvider(this).get(EventCreationViewModel.class);

        //setting up event types dropdown
        viewModel.getEventTypes().observe(getViewLifecycleOwner(), this::setupSpinner);
        viewModel.fetchEventTypes();


        //validating fields
        viewModel.getErrors().observe(getViewLifecycleOwner(), errors -> {
            layoutName.setError(errors.get("name"));
            layoutDescription.setError(errors.get("description"));
            layoutCapacity.setError(errors.get("capacity"));
            layoutLocation.setError(errors.get("location"));
            dateStartError.setVisibility(errors.get("startDate") != null ? View.VISIBLE : View.GONE);
            dateEndError.setVisibility(errors.get("endDate") != null ? View.VISIBLE : View.GONE);

            Calendar start = Calendar.getInstance();
            start.set(datePickerStart.getYear(), datePickerStart.getMonth(), datePickerStart.getDayOfMonth(),
                    timePickerStart.getHour(), timePickerStart.getMinute());

            Calendar end = Calendar.getInstance();
            end.set(datePickerEnd.getYear(), datePickerEnd.getMonth(), datePickerEnd.getDayOfMonth(),
                    timePickerEnd.getHour(), timePickerEnd.getMinute());

            btnConfirm.setEnabled(viewModel.canConfirm(
                    inputEventName.getText() != null ? inputEventName.getText().toString() : null,
                    inputDescription.getText() != null ? inputDescription.getText().toString() : null,
                    inputCapacity.getText() != null ? inputCapacity.getText().toString() : null,
                    inputLocation.getText() != null ? inputLocation.getText().toString() : null,
                    start,
                    end,
                    selectedEventType,
                    radioGroupPrivacy.getCheckedRadioButtonId()
            ));
        });
        //observing watchers for inputs
        inputEventName.addTextChangedListener(new SimpleTextWatcher(s ->
                viewModel.validateName(s.toString())));
        inputDescription.addTextChangedListener(new SimpleTextWatcher(s ->
                viewModel.validateDescription(s.toString())));
        inputCapacity.addTextChangedListener(new SimpleTextWatcher(s ->
                viewModel.validateCapacity(s.toString())));
        inputLocation.addTextChangedListener(new SimpleTextWatcher(s ->
                viewModel.validateLocation(s.toString())));
        //validation of dates
        datePickerStart.setOnDateChangedListener((viewDate, year, monthOfYear, dayOfMonth) -> {
            Calendar picked = Calendar.getInstance();
            picked.set(year, monthOfYear, dayOfMonth,
                    timePickerStart.getHour(), timePickerStart.getMinute());
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.set(datePickerEnd.getYear(), datePickerEnd.getMonth(), datePickerEnd.getDayOfMonth(),
                    timePickerEnd.getHour(), timePickerEnd.getMinute());
            viewModel.validateDates(picked, endCalendar);
        });
        timePickerStart.setOnTimeChangedListener((viewTime, hourOfDay, minute) -> {
            Calendar picked = Calendar.getInstance();
            picked.set(datePickerStart.getYear(), datePickerStart.getMonth(),
                    datePickerStart.getDayOfMonth(), hourOfDay, minute);
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.set(datePickerEnd.getYear(), datePickerEnd.getMonth(), datePickerEnd.getDayOfMonth(),
                    timePickerEnd.getHour(), timePickerEnd.getMinute());
            viewModel.validateDates(picked, endCalendar);
        });
        datePickerEnd.setOnDateChangedListener((viewDate, year, monthOfYear, dayOfMonth) -> {
            Calendar picked = Calendar.getInstance();
            picked.set(year, monthOfYear, dayOfMonth,
                    timePickerEnd.getHour(), timePickerEnd.getMinute());
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.set(datePickerStart.getYear(), datePickerStart.getMonth(), datePickerStart.getDayOfMonth(),
                    timePickerStart.getHour(), timePickerStart.getMinute());
            viewModel.validateDates(startCalendar, picked);
        });
        timePickerEnd.setOnTimeChangedListener((viewTime, hourOfDay, minute) -> {
            Calendar picked = Calendar.getInstance();
            picked.set(datePickerEnd.getYear(), datePickerEnd.getMonth(),
                    datePickerEnd.getDayOfMonth(), hourOfDay, minute);
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.set(datePickerStart.getYear(), datePickerStart.getMonth(), datePickerStart.getDayOfMonth(),
                    timePickerStart.getHour(), timePickerStart.getMinute());
            viewModel.validateDates(startCalendar, picked);
        });


        //on confirmation of event creation
        btnConfirm.setOnClickListener(v -> {
            String name = inputEventName.getText() != null ? inputEventName.getText().toString().trim() : "";
            String description = inputDescription.getText() != null ? inputDescription.getText().toString().trim() : "";
            String capacityStr = inputCapacity.getText() != null ? inputCapacity.getText().toString().trim() : "0";
            String location = inputLocation.getText() != null ? inputLocation.getText().toString().trim() : "";

            int capacity = 0;
            try { capacity = Integer.parseInt(capacityStr); } catch (NumberFormatException ignored) {}

            Calendar start = Calendar.getInstance();
            start.set(datePickerStart.getYear(), datePickerStart.getMonth(), datePickerStart.getDayOfMonth(),
                    timePickerStart.getHour(), timePickerStart.getMinute());

            Calendar end = Calendar.getInstance();
            end.set(datePickerEnd.getYear(), datePickerEnd.getMonth(), datePickerEnd.getDayOfMonth(),
                    timePickerEnd.getHour(), timePickerEnd.getMinute());

            boolean isPrivate = radioGroupPrivacy.getCheckedRadioButtonId() == R.id.radioPrivate;

            viewModel.createEvent(name, description, capacity, location,
                    start, end, selectedEventType, isPrivate, activities, null);
        });
        //checking if creation was successful
        viewModel.getCreationSuccess().observe(getViewLifecycleOwner(), success -> {
            if (success != null && success) {
                Toast.makeText(requireContext(), "Event created successfully!", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, new EventsPage())
                        .commit();
            }
        });
        viewModel.getCreationError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }


    //adapter for event types dropdown
    private void setupSpinner(List<MinimalEventTypeDTO> types) {
        if (types == null || types.isEmpty()) return;

        //custom adapter that shows name
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                getNames(types)
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEventType.setAdapter(adapter);

        spinnerEventType.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                selectedEventType = types.get(position); // store full object
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                selectedEventType = null;
            }
        });
    }
    private List<String> getNames(List<MinimalEventTypeDTO> types) {
        List<String> names = new ArrayList<>();
        for (MinimalEventTypeDTO t : types) {
            names.add(t.getName());
        }
        return names;
    }

    public MinimalEventTypeDTO getSelectedEventType() {
        return selectedEventType;
    }
}