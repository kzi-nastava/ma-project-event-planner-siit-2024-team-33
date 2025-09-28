package com.example.myapplication.data.services.event;

import com.example.myapplication.data.api.event.EventTypesApi;
import com.example.myapplication.data.dto.eventTypeDTO.MinimalEventTypeDTO;
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
}
