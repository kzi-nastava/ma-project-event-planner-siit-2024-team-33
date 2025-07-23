package com.example.myapplication.api;

import com.example.myapplication.dto.PageResponse;
import com.example.myapplication.dto.eventDTO.MinimalEventDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventApi {
    @GET("organizer/{id}")
    Call<List<MinimalEventDTO>> getEventsByOrganizer(@Path("id") Integer organizerId);

    @GET("top5")
    Call<List<MinimalEventDTO>> getTop5Events();

    @GET("rest")
    Call<List<MinimalEventDTO>> getAllEvents();
    @GET("service/{serviceId}")
    Call<List<MinimalEventDTO>> getEventsByService(@Path("serviceId") int serviceId);
    @GET("/paginated")
    Call<PageResponse<MinimalEventDTO>> getPaginatedEvents(@Query("page") int page, @Query("size") int size);
    @GET("filter")
    Call<PageResponse<MinimalEventDTO>> getEventList(
            @Query("name") String name,
            @Query("location") String location,
            @Query("numOfAttendees") Integer numOfAttendees,
            @Query("firstPossibleDate") String firstPossibleDate,
            @Query("lastPossibleDate") String lastPossibleDate,
            @Query("eventTypes") List<Integer> eventTypes,
            @Query("page") int page,
            @Query("size") int size
    );
}
