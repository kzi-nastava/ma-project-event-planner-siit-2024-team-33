package com.example.myapplication.ui.view.dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;

import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;
import com.example.myapplication.data.dto.eventDTO.MinimalEventDTO;
import com.example.myapplication.data.dto.serviceReservationDTO.CreatedServiceReservationDTO;
import com.example.myapplication.data.dto.serviceReservationDTO.PostServiceReservationDTO;
import com.example.myapplication.data.services.event.EventService;
import com.example.myapplication.data.services.ServiceReservationService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceBookingDialog extends DialogFragment {

    private static int serviceId;
    private static int minLength;
    private static int maxLength;

    private Spinner eventSpinner;
    private EditText dateInput, timeFromInput, timeToInput;
    private TextView durationNote;
    private Button bookButton;

    private final ServiceReservationService reservationService = new ServiceReservationService();
    private final EventService eventService = new EventService();
    private List<MinimalEventDTO> events = new ArrayList<>();
    private MinimalEventDTO selectedEvent;

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            DisplayMetrics metrics = new DisplayMetrics();
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

            int width = (int) (metrics.widthPixels * 0.8);
            window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    public static ServiceBookingDialog newInstance(int serveID, int MINLEN, int MAXLEN) {
        ServiceBookingDialog dialog = new ServiceBookingDialog();
        Bundle args = new Bundle();
        serviceId = serveID;
        minLength = MINLEN;
        maxLength = MAXLEN;
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.service_booking, null);

        eventSpinner = view.findViewById(R.id.spinner_events);
        dateInput = view.findViewById(R.id.input_date);
        timeFromInput = view.findViewById(R.id.input_time_from);
        timeToInput = view.findViewById(R.id.input_time_to);
        durationNote = view.findViewById(R.id.note_duration);
        bookButton = view.findViewById(R.id.button_book_service);

        dateInput.setOnClickListener(v -> showDatePicker(dateInput));
        timeFromInput.setOnClickListener(v -> showTimePicker(timeFromInput));
        timeToInput.setOnClickListener(v -> showTimePicker(timeToInput));
        bookButton.setOnClickListener(v -> onBookService());

        fetchEvents();

        builder.setView(view);
        return builder.create();
    }

    private void onBookService() {
        if (selectedEvent == null) {
            Toast.makeText(requireContext(), "Please select an event", Toast.LENGTH_SHORT).show();
            return;
        }

        String date = dateInput.getText().toString().trim();
        String timeFrom = timeFromInput.getText().toString().trim();
        String timeTo = timeToInput.getText().toString().trim();

        if (date.isEmpty() || timeFrom.isEmpty() || timeTo.isEmpty()) {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        long startTime = parseTimeToMillis(timeFrom);
        long endTime = parseTimeToMillis(timeTo);

        if (startTime == -1 || endTime == -1 || endTime <= startTime) {
            Toast.makeText(requireContext(), "Invalid time range", Toast.LENGTH_SHORT).show();
            return;
        }

        long duration = (endTime - startTime) / 60000;

        if (duration < minLength || duration > maxLength) {
            durationNote.setText("Duration must be between " + minLength + " and " + maxLength + " mins.");
            durationNote.setVisibility(View.VISIBLE);
            return;
        } else {
            durationNote.setVisibility(View.GONE);
        }

        PostServiceReservationDTO dto = new PostServiceReservationDTO(
                selectedEvent.getId(), date, timeFrom, timeTo
        );
        reservationService.reserveService(serviceId, dto).enqueue(new Callback<CreatedServiceReservationDTO>() {
            @Override
            public void onResponse(Call<CreatedServiceReservationDTO> call, Response<CreatedServiceReservationDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Service booked", Toast.LENGTH_LONG).show();
                    dismiss();
                } else {
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        String message;
                        try {
                            JSONObject json = new JSONObject(errorBody);
                            message = json.optString("message", errorBody);
                        } catch (Exception parseEx) {
                            message = errorBody;
                        }
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(requireContext(), "Booking failed (could not parse server message)", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreatedServiceReservationDTO> call, Throwable t) {
                Toast.makeText(requireContext(), "Request error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void fetchEvents() {
        eventService.getEventsForOrganizerUpdated()
                .enqueue(new Callback<List<MinimalEventDTO>>() {
                    @Override
                    public void onResponse(Call<List<MinimalEventDTO>> call, Response<List<MinimalEventDTO>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            events.clear();
                            events.addAll(response.body());
                            populateEventSpinner();
                        } else {
                            Toast.makeText(getContext(), "No events found for this service.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<MinimalEventDTO>> call, Throwable t) {
                        Toast.makeText(getContext(), "Failed to load events: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void populateEventSpinner() {
        ArrayAdapter<MinimalEventDTO> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, events);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSpinner.setAdapter(adapter);

        eventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedEvent = events.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedEvent = null;
            }
        });
    }

    private void showDatePicker(EditText target) {
        final Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            target.setText(String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker(EditText target) {
        final Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(requireContext(), (view, hourOfDay, minute) -> {
            target.setText(String.format(Locale.US, "%02d:%02d", hourOfDay, minute));
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }

    private long parseTimeToMillis(String time) {
        try {
            String[] parts = time.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, hours);
            cal.set(Calendar.MINUTE, minutes);
            cal.set(Calendar.SECOND, 0);
            return cal.getTimeInMillis();
        } catch (Exception e) {
            return -1;
        }
    }
}
