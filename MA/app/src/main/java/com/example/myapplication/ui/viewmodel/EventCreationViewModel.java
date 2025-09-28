package com.example.myapplication.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.data.dto.eventDTO.CreateEventActivityDTO;
import com.example.myapplication.data.dto.eventDTO.CreateEventDTO;
import com.example.myapplication.data.dto.eventDTO.CreatedEventDTO;
import com.example.myapplication.data.dto.eventTypeDTO.MinimalEventTypeDTO;
import com.example.myapplication.data.services.event.EventService;
import com.example.myapplication.data.services.event.EventTypeService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventCreationViewModel extends ViewModel {
    private final EventTypeService eventTypeService = new EventTypeService();
    private final EventService eventService = new EventService();
    private final MutableLiveData<List<MinimalEventTypeDTO>> eventTypes = new MutableLiveData<>();
    private final MutableLiveData<MinimalEventTypeDTO> selectedEventType = new MutableLiveData<>();
    private final MutableLiveData<String> activityValidationError = new MutableLiveData<>();
    public LiveData<String> getActivityValidationError() {
        return activityValidationError;
    }


    public LiveData<List<MinimalEventTypeDTO>> getEventTypes() {
        return eventTypes;
    }

    public LiveData<MinimalEventTypeDTO> getSelectedEventType() {
        return selectedEventType;
    }

    public void setSelectedEventType(MinimalEventTypeDTO type) {
        selectedEventType.setValue(type);
    }

    public void fetchEventTypes() {
        eventTypeService.getEventTypes().enqueue(new Callback<List<MinimalEventTypeDTO>>() {
            @Override
            public void onResponse(Call<List<MinimalEventTypeDTO>> call, Response<List<MinimalEventTypeDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    eventTypes.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<MinimalEventTypeDTO>> call, Throwable t) {
                eventTypes.postValue(null);
            }
        });
    }


    //creating event
    private final MutableLiveData<Boolean> creationSuccess = new MutableLiveData<>();
    private final MutableLiveData<String> creationError = new MutableLiveData<>();

    public LiveData<Boolean> getCreationSuccess() {
        return creationSuccess;
    }

    public LiveData<String> getCreationError() {
        return creationError;
    }

    public void createEvent(String name, String description, int capacity, String location,
                            Calendar start, Calendar end, MinimalEventTypeDTO type,
                            boolean isPrivate, List<CreateEventActivityDTO> activities, List<String> invites, double longitude, double latitude) {
        CreateEventDTO dto = new CreateEventDTO();
        dto.setName(name);
        dto.setDescription(description);
        dto.setNumOfAttendees(capacity);
        dto.setPlace(location);
        dto.setDateOfEvent(calendarToIso(start));
        dto.setEndOfEvent(calendarToIso(end));
        dto.setEventTypeId(type.getId());
        dto.setIsPrivate(isPrivate);
        dto.setPrivateInvitations(invites);
        dto.setLongitude(longitude);
        dto.setLatitude(latitude);
        dto.setEventActivities(new HashSet<>(activities));
        eventService.createEvent(dto).enqueue(new Callback<CreatedEventDTO>() {
            @Override
            public void onResponse(Call<CreatedEventDTO> call, Response<CreatedEventDTO> response) {
                if (response.isSuccessful()) {
                    creationSuccess.postValue(true);
                } else {
                    creationError.postValue("Failed: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CreatedEventDTO> call, Throwable t) {
                creationError.postValue("Error: " + t.getMessage());
            }
        });
    }
    private String calendarToIso(Calendar cal) {
        return String.format("%04d-%02d-%02dT%02d:%02d:00",
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE));
    }





    //activities
    private final MutableLiveData<List<String>> activities = new MutableLiveData<>(new ArrayList<>());



    //validation
    private final MutableLiveData<Map<String, String>> errors = new MutableLiveData<>(new HashMap<>());

    public LiveData<Map<String, String>> getErrors() {
        return errors;
    }

    public void validateName(String name) {
        updateError("name",
                (name == null || name.length() < 5 || name.length() > 50)
                        ? "Name must be 5–50 characters" : null);
    }

    public void validateDescription(String desc) {
        updateError("description",
                (desc == null || desc.length() < 5 || desc.length() > 250)
                        ? "Description must be 5–250 characters" : null);
    }

    public void validateCapacity(String capacityStr) {
        try {
            int cap = Integer.parseInt(capacityStr);
            updateError("capacity", cap < 0 ? "Capacity must be ≥ 0" : null);
        } catch (Exception e) {
            updateError("capacity", "Enter a valid number");
        }
    }

    public void validateLocation(String location) {
        Pattern regex = Pattern.compile("^[\\p{L}\\-' ]+,\\s[\\p{L}\\-' ]+$");
        updateError("location",
                (location == null || !regex.matcher(location).matches())
                        ? "Location must be in format: City, Country" : null);
    }

    public void validateDates(Calendar start, Calendar end) {
        Calendar now = Calendar.getInstance();
        if (start.before(now)) {
            updateError("startDate", "Start must be in the future");
        } else {
            updateError("startDate", null);
        }

        if (end.before(start) || end.equals(start)) {
            updateError("endDate", "End must be after start");
        } else {
            updateError("endDate", null);
        }
    }

    private void updateError(String field, String message) {
        Map<String, String> current = errors.getValue();
        if (current == null) current = new HashMap<>();
        if (message == null) {
            current.remove(field);
        } else {
            current.put(field, message);
        }
        errors.postValue(new HashMap<>(current));
    }

    public boolean canConfirm(String name, String desc, String capacity, String location,
                              Calendar start, Calendar end, MinimalEventTypeDTO type,
                              int privacyCheckedId) {
        //must have no errors
        if (errors.getValue() != null && !errors.getValue().isEmpty()) {
            return false;
        }

        //check mandatory fields
        if (name == null || name.trim().isEmpty()) return false;
        if (desc == null || desc.trim().isEmpty()) return false;
        if (capacity == null || capacity.trim().isEmpty()) return false;
        if (location == null || location.trim().isEmpty()) return false;
        if (type == null) return false;
        if (privacyCheckedId == -1) return false;

        //also dates should be valid (not null)
        if (start == null || end == null) return false;

        return true;
    }


    private LocalDateTime eventStartTime;
    private LocalDateTime eventEndTime;

    public void setEventBounds(LocalDateTime start, LocalDateTime end) {
        this.eventStartTime = start;
        this.eventEndTime = end;
    }
    public void addActivity(String name, String description, String location,
                            Calendar start, Calendar end,
                            List<CreateEventActivityDTO> activities) {

        if (name == null || name.trim().length() < 5 || name.trim().length() > 50) {
            activityValidationError.postValue("Name must be 5–50 characters.");
            return;
        }

        if (description == null || description.trim().length() < 5 || description.trim().length() > 80) {
            activityValidationError.postValue("Description must be 5–80 characters.");
            return;
        }

        if (location == null || location.trim().length() < 2 || location.trim().length() > 50) {
            activityValidationError.postValue("Location must be 2–50 characters.");
            return;
        }

        LocalDateTime startTime = calendarToLocalDateTime(start);
        LocalDateTime endTime = calendarToLocalDateTime(end);

        if (startTime.isBefore(LocalDateTime.now())) {
            activityValidationError.postValue("Activity must start in the future.");
            return;
        }

        if (!endTime.isAfter(startTime)) {
            activityValidationError.postValue("End time must be after start time.");
            return;
        }

        if (eventStartTime != null && eventEndTime != null) {
            if (startTime.isBefore(eventStartTime) || endTime.isAfter(eventEndTime)) {
                activityValidationError.postValue("Activity must be within the event timeframe.");
                return;
            }
        }

        //passed validation
        activityValidationError.postValue(null);

        CreateEventActivityDTO dto = new CreateEventActivityDTO();
        dto.setName(name.trim());
        dto.setDescription(description.trim());
        dto.setLocation(location.trim());
        dto.setStartingTime(startTime.toString());
        dto.setEndingTime(endTime.toString());

        activities.add(dto);
    }
    public LocalDateTime calendarToLocalDateTime(Calendar cal) {
        return LocalDateTime.of(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE)
        );
    }
}
