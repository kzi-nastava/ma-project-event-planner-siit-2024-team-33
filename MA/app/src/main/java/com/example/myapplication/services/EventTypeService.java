package com.example.myapplication.services;

import com.example.myapplication.api.EventTypesApi;
import com.example.myapplication.dto.eventTypeDTO.MinimalEventTypeDTO;
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
