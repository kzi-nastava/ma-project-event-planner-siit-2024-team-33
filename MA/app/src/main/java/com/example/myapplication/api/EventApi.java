package com.example.myapplication.api;

import com.example.myapplication.dto.common.PaginatedResponse;
import com.example.myapplication.dto.eventDTO.MinimalEventDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import retrofit2.http.QueryMap;
import retrofit2.http.Path;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

import retrofit2.http.Headers;
import retrofit2.http.Multipart;

import retrofit2.http.PartMap;
import retrofit2.http.Part;

import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;

import retrofit2.http.Header;

import retrofit2.http.Url;

import retrofit2.http.QueryName;
import retrofit2.http.Tag;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import retrofit2.http.GET;
import retrofit2.http.Path;

import retrofit2.http.Query;
import retrofit2.http.GET;

import com.example.myapplication.dto.eventDTO.MinimalEventDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Body;
import retrofit2.http.Path;
import retrofit2.http.DELETE;

import java.util.List;
import retrofit2.http.Query;

public interface EventApi {

    @GET("events/top5/authentified")
    Call<List<MinimalEventDTO>> getTop5EventsAuthorized();

    @GET("events/top5/unauthentified")
    Call<List<MinimalEventDTO>> getTop5EventsUnauthorized();

    @GET("events/rest")
    Call<List<MinimalEventDTO>> getAllEvents();

    @GET("events/paginated")
    Call<PaginatedResponse<MinimalEventDTO>> getPaginatedEvents(
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("events/filter/authentified")
    Call<PaginatedResponse<MinimalEventDTO>> getFilteredEventsAuthorized(
            @Query("name") String name,
            @Query("location") String location,
            @Query("numOfAttendees") Integer numOfAttendees,
            @Query("firstPossibleDate") String firstDate,
            @Query("lastPossibleDate") String lastDate,
            @Query("eventTypes") List<Integer> eventTypes,
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("events/filter/unauthentified")
    Call<PaginatedResponse<MinimalEventDTO>> getFilteredEventsUnauthorized(
            @Query("name") String name,
            @Query("location") String location,
            @Query("numOfAttendees") Integer numOfAttendees,
            @Query("firstPossibleDate") String firstDate,
            @Query("lastPossibleDate") String lastDate,
            @Query("eventTypes") List<Integer> eventTypes,
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("events/organizer")
    Call<List<MinimalEventDTO>> getEventsForOrganizer();

//    @POST("events")
//    Call<CreatedEventDTO> createEvent(@Body CreateEventDTO createEventDTO);
//
//    @PUT("events/{id}")
//    Call<Void> updateEvent(@Path("id") int id, @Body UpdateEventDTO updateEventDTO);

    @DELETE("events/{id}")
    Call<Void> deleteEvent(@Path("id") int id);
}
