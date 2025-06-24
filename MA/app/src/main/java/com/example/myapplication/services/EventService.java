package com.example.myapplication.services;

import com.example.myapplication.api.EventApi;
import com.example.myapplication.dto.common.PaginatedResponse;
import com.example.myapplication.dto.eventDTO.FilterEventDTO;
import com.example.myapplication.dto.eventDTO.MinimalEventDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventService {
    private static final String BASE_URL = "http://10.0.2.2:8080/api/";
    private final EventApi eventApi;

    public EventService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        eventApi = retrofit.create(EventApi.class);
    }

    public Call<List<MinimalEventDTO>> getTop5EventsAuthorized() {
        return eventApi.getTop5EventsAuthorized();
    }

    public Call<List<MinimalEventDTO>> getTop5EventsUnauthorized() {
        return eventApi.getTop5EventsUnauthorized();
    }

    public Call<List<MinimalEventDTO>> getAllEvents() {
        return eventApi.getAllEvents();
    }

    public Call<PaginatedResponse<MinimalEventDTO>> getPaginatedEvents(int page, int size) {
        return eventApi.getPaginatedEvents(page, size);
    }

    public Call<PaginatedResponse<MinimalEventDTO>> getFilteredEventsAuthorized(FilterEventDTO filter, int page, int size) {
        return eventApi.getFilteredEventsAuthorized(
                filter.getName(),
                filter.getLocation(),
                filter.getNumOfAttendees(),
                filter.getFirstPossibleDate(),
                filter.getLastPossibleDate(),
                filter.eventTypes,
                page,
                size
        );
    }

    public Call<PaginatedResponse<MinimalEventDTO>> getFilteredEventsUnauthorized(FilterEventDTO filter, int page, int size) {
        return eventApi.getFilteredEventsUnauthorized(
                filter.getName(),
                filter.getLocation(),
                filter.getNumOfAttendees(),
                filter.getFirstPossibleDate(),
                filter.getLastPossibleDate(),
                filter.eventTypes,
                page,
                size
        );
    }

    public Call<List<MinimalEventDTO>> getEventsForOrganizer() {
        return eventApi.getEventsForOrganizer();
    }
}
