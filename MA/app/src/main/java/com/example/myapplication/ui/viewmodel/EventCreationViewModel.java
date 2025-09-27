package com.example.myapplication.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.data.dto.eventDTO.CreateEventActivityDTO;
import com.example.myapplication.data.dto.eventDTO.CreateEventDTO;
import com.example.myapplication.data.dto.eventDTO.CreatedEventDTO;
import com.example.myapplication.data.dto.eventTypeDTO.MinimalEventTypeDTO;
import com.example.myapplication.data.services.EventService;
import com.example.myapplication.data.services.EventTypeService;

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
                            boolean isPrivate, List<String> activities, Set<String> invites) {
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
        //fake
        dto.setLongitude(22.0);
        dto.setLatitude(22.0);

        //converting activity names to DTOs
        Set<CreateEventActivityDTO> activitySet = new HashSet<>();
        for (String actName : activities) {
            CreateEventActivityDTO actDto = new CreateEventActivityDTO();
            actDto.setName(actName);
            activitySet.add(actDto);
        }
        dto.setEventActivities(activitySet);
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
}
