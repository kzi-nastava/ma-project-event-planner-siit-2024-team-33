package com.example.myapplication.services;

import com.example.myapplication.api.EventApi;
import com.example.myapplication.dto.PageResponse;
import com.example.myapplication.dto.eventDTO.FilterEventDTO;
import com.example.myapplication.dto.eventDTO.MinimalEventDTO;
import com.example.myapplication.dto.eventDTO.MinimalEventTypeDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventService {
//    private static final String BASE_URL = "http://10.0.2.2:8080/api/events/";
private static final String BASE_URL = "http://192.168.2.8:8080/api/events/";
    private final EventApi eventApi;

    public EventService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        eventApi = retrofit.create(EventApi.class);
    }

    public Call<List<MinimalEventDTO>> getEventsByOrganizer(Integer organizerId) {
        return eventApi.getEventsByOrganizer(organizerId);
    }

    public Call<List<MinimalEventDTO>> GetTop5EventsAuthorized() {
        return eventApi.GetTop5EventsAuthorized();
    }

    public Call<List<MinimalEventDTO>> GetTop5EventsUnauthorized() {
        return eventApi.GetTop5EventsUnauthorized();
    }

    public Call<List<MinimalEventDTO>> getAllEvents() {
        return eventApi.getAllEvents();
    }

    public Call<PageResponse<MinimalEventDTO>> getFilteredEvents(FilterEventDTO filter,int page,int size) {
        return eventApi.getFilteredEvents(
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

}
