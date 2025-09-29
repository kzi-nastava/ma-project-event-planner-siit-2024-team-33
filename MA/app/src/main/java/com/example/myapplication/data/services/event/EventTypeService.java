package com.example.myapplication.data.services.event;

import com.example.myapplication.data.api.event.EventTypesApi;
import com.example.myapplication.data.models.dto.PageResponse;
import com.example.myapplication.data.models.dto.eventTypeDTO.MinimalEventTypeDTO;
import com.example.myapplication.data.models.dto.CreateEventTypeDTO;
import com.example.myapplication.data.models.dto.CreatedEventTypeDTO;
import com.example.myapplication.data.models.dto.GetEventTypeDTO;
import com.example.myapplication.data.models.dto.UpdateEventTypeDTO;
import com.example.myapplication.data.models.dto.UpdatedEventTypeDTO;
import com.example.myapplication.data.services.ApiClient;
import com.example.myapplication.utils.Settings;

import java.util.List;

import retrofit2.Call;

public class EventTypeService {
    private static final String BASE_URL = Settings.BASE_URL + "/";
    private final EventTypesApi eventTypesApi;

    public EventTypeService() {
        eventTypesApi = ApiClient.getRetrofit(BASE_URL).create(EventTypesApi.class);
    }

    public Call<List<MinimalEventTypeDTO>> getEventTypes() {
        return eventTypesApi.getActiveTypes();
    }

    public Call<PageResponse<GetEventTypeDTO>> getEventTypes(int page, int size) {
        return eventTypesApi.getEventTypes(page, size);
    }

    public Call<CreatedEventTypeDTO> createEventType(CreateEventTypeDTO dto) {
        return eventTypesApi.createEventType(dto);
    }

    public Call<UpdatedEventTypeDTO> updateEventType(Integer id, UpdateEventTypeDTO dto) {
        return eventTypesApi.updateEventType(id, dto);
    }

    public Call<UpdatedEventTypeDTO> activateEventType(Integer id) {
        return eventTypesApi.activateEventType(id);
    }

    public Call<UpdatedEventTypeDTO> deactivateEventType(Integer id) {
        return eventTypesApi.deactivateEventType(id);
    }

    public Call<Boolean> checkIfExists(String name) {
        return eventTypesApi.checkIfExists(name);
    }
}
