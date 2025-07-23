package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.dto.eventDTO.MinimalEventDTO;
import com.example.myapplication.dto.serviceReservationDTO.CreatedServiceReservationDTO;
import com.example.myapplication.dto.serviceReservationDTO.PostServiceReservationDTO;
import com.example.myapplication.services.EventService;
import com.example.myapplication.services.ServiceReservationService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceBookingDialog extends Dialog {

    private final Context context;
    private final int serviceId;
    private final int minLength;
    private final int maxLength;
    private Spinner eventSpinner;
    private EditText dateInput, timeFromInput, timeToInput;
    private TextView durationNote;
    private Button bookButton;
    private ServiceReservationService serviceReservation;
    private List<MinimalEventDTO> events = new ArrayList<>();
    private MinimalEventDTO selectedEvent;
    private final ServiceReservationService reservationService = new ServiceReservationService();
    private final EventService eventService = new EventService();


    public ServiceBookingDialog(@NonNull Context context, int maxlength, int minlength, int serviceId){
        super(context);
        this.context=context;
        this.maxLength=maxlength;
        this.minLength=minlength;
        this.serviceId=serviceId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_booking);

        eventSpinner = findViewById(R.id.spinner_events);
        dateInput = findViewById(R.id.input_date);
        timeFromInput =findViewById(R.id.input_time_from);
        timeToInput = findViewById(R.id.input_time_to);
        durationNote = findViewById(R.id.note_duration);
        bookButton = findViewById(R.id.button_book_service);


        dateInput.setOnClickListener(v -> showDatePicker(dateInput));
        timeFromInput.setOnClickListener(v -> showTimePicker(timeFromInput));
        timeToInput.setOnClickListener(v -> showTimePicker(timeToInput));

        bookButton.setOnClickListener(v -> onBookService());

        fetchEvents();
    }

    private void onBookService() {
        if (selectedEvent == null) {
            Toast.makeText(context, "Please select an event", Toast.LENGTH_SHORT).show();
            return;
        }

        String date = dateInput.getText().toString().trim();
        String timeFrom = timeFromInput.getText().toString().trim();
        String timeTo = timeToInput.getText().toString().trim();

        if (date.isEmpty() || timeFrom.isEmpty() || timeTo.isEmpty()) {
            Toast.makeText(context, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        long startTime = parseTimeToMillis(timeFrom);
        long endTime = parseTimeToMillis(timeTo);
        if (startTime == -1 || endTime == -1 || endTime <= startTime) {
            Toast.makeText(context, "Invalid time range", Toast.LENGTH_SHORT).show();
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
                selectedEvent.getId(),
                date,
                timeFrom,
                timeTo
        );

        reservationService.reserveService(serviceId, dto).enqueue(new Callback<CreatedServiceReservationDTO>() {
            @Override
            public void onResponse(Call<CreatedServiceReservationDTO> call, Response<CreatedServiceReservationDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Service booked successfully", Toast.LENGTH_LONG).show();
                    dismiss();
                } else {
                    Toast.makeText(context, "Booking failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreatedServiceReservationDTO> call, Throwable t) {
                Toast.makeText(context, "Request error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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

    private void fetchEvents() {  //Fetch events by event type that matches with the service
        eventService.getEventsByService(1).enqueue(new Callback<List<MinimalEventDTO>>() {
            @Override
            public void onResponse(Call<List<MinimalEventDTO>> call, Response<List<MinimalEventDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    events = response.body();
                    populateEventSpinner();
                }
            }

            @Override
            public void onFailure(Call<List<MinimalEventDTO>> call, Throwable t) {
                Toast.makeText(context, "Error loading events", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateEventSpinner() {
        ArrayAdapter<MinimalEventDTO> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, events);
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
        new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
            target.setText(String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimePicker(EditText target) {
        final Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(context, (view, hourOfDay, minute) -> {
            target.setText(String.format(Locale.US, "%02d:%02d", hourOfDay, minute));
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }
}
