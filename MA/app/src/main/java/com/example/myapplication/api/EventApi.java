package com.example.myapplication.api;

import com.example.myapplication.dto.MinimalEventDTO;
import com.example.myapplication.dto.FilterEventDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventApi {
    @GET("organizer/{id}")
    Call<List<MinimalEventDTO>> getEventsByOrganizer(@Path("id") Integer organizerId);

    @GET("path/to/top5")
    Call<List<MinimalEventDTO>> getTop5Events(@Query("id") Integer id);

    @GET("rest")
    Call<List<MinimalEventDTO>> getAllEvents(@Query("id") Integer id);

    @GET("filter")
    Call<List<MinimalEventDTO>> getFilteredEvents(
            @Query("name") String name,
            @Query("location") String location,
            @Query("numOfAttendees") Integer numOfAttendees,
            @Query("firstPossibleDate") String firstPossibleDate,
            @Query("lastPossibleDate") String lastPossibleDate,
            @Query("eventTypes") List<Integer> eventTypes,
            @Query("id") Integer id
    );
}
