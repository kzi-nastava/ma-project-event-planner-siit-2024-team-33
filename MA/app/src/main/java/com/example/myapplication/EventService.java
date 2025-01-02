package com.example.myapplication;

import com.example.myapplication.api.EventApi;
import com.example.myapplication.dto.FilterEventDTO;
import com.example.myapplication.dto.MinimalEventDTO;
import com.example.myapplication.dto.MinimalEventTypeDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventService {
    private static final String BASE_URL = "http://localhost:8080/api/events/";
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

    public Call<List<MinimalEventDTO>> getTop5Events(Integer id) {
        return eventApi.getTop5Events(id);
    }

    public Call<List<MinimalEventDTO>> getAllEvents(Integer id) {
        return eventApi.getAllEvents(id);
    }

    public Call<List<MinimalEventDTO>> getFilteredEvents(FilterEventDTO filter, Integer id) {
        List<Integer> ids = extractIntegers(filter.getEventTypes());
        return eventApi.getFilteredEvents(
                filter.getName(),
                filter.getLocation(),
                filter.getNumOfAttendees(),
                filter.getFirstPossibleDate(),
                filter.getLastPossibleDate(),
                ids,
                id
        );
    }

    private List<Integer> extractIntegers(List<MinimalEventTypeDTO> eventTypeDTOs) {
        List<Integer> integerList = new ArrayList<>();
        for (MinimalEventTypeDTO dto : eventTypeDTOs) {
            if (dto.id != null) {
                integerList.add(dto.id);
            }
        }
        return integerList;
    }
}
