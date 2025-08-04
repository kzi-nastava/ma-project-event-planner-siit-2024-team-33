package com.example.myapplication.services;

import com.example.myapplication.api.EventApi;
import com.example.myapplication.dto.PageResponse;
import com.example.myapplication.dto.eventDTO.CreateEventDTO;
import com.example.myapplication.dto.eventDTO.CreatedEventDTO;
import com.example.myapplication.dto.eventDTO.FilterEventDTO;
import com.example.myapplication.dto.eventDTO.GetEventDetails;
import com.example.myapplication.dto.eventDTO.MinimalEventDTO;
import com.example.myapplication.utils.Settings;

import java.util.List;

import retrofit2.Call;

public class EventService {
//    private static final String BASE_URL = "http://10.0.2.2:8080/api/events/";
    private static final String BASE_URL = Settings.BASE_URL + "/";
    private final EventApi eventApi;

    public EventService() {
        eventApi = ApiClient.getRetrofit(BASE_URL).create(EventApi.class);
    }

    public Call<List<MinimalEventDTO>> getEventsByOrganizer(Integer organizerId) {
        return eventApi.getEventsByOrganizer(organizerId);
    }
    public Call<GetEventDetails> getEventDetails(int eventId) {
        return eventApi.getEventDetails(eventId);
    }
    public Call<List<MinimalEventDTO>> getEventsByService(int serviceId) {
        return eventApi.getEventsByService(serviceId);
    }

    public Call<List<MinimalEventDTO>> GetTop5Events() {
        return eventApi.getTop5Events();
    }

    public Call<List<MinimalEventDTO>> getAllEvents() {
        return eventApi.getAllEvents();
    }

    public Call<PageResponse<MinimalEventDTO>> getEventList(FilterEventDTO filter,int page,int size) {
        return eventApi.getEventList(
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

    public Call<List<MinimalEventDTO>> getEventsForOrganizerUpdated(){
        return eventApi.getEventsForOrganizerUpdated();
    }

    public Call<CreatedEventDTO> createEvent(CreateEventDTO data){
        return eventApi.createEvent(data);
    }
}
