package com.example.myapplication.data.api.event;

import com.example.myapplication.data.dto.PageResponse;
import com.example.myapplication.data.dto.eventDTO.CreateEventDTO;
import com.example.myapplication.data.dto.eventDTO.CreatedEventDTO;
import com.example.myapplication.data.dto.eventDTO.GetEventDetails;
import com.example.myapplication.data.dto.eventDTO.MinimalEventDTO;
import com.example.myapplication.data.models.JoinedEventDTO;

import java.time.LocalDate;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface EventApi {
    @GET("api/events/organizer/{id}")
    Call<List<MinimalEventDTO>> getEventsByOrganizer(@Path("id") Integer organizerId);

    @GET("api/events/organizer/updated")
    Call<List<MinimalEventDTO>> getEventsForOrganizerUpdated();

    @GET("api/events/top5")
    Call<List<MinimalEventDTO>> getTop5Events();

    @Streaming
    @GET("api/events/{eventId}/reports/details")
    Call<ResponseBody> getEventDetailsPdf(@Path("eventId") int eventId);

    @Streaming
    @GET("api/events/{eventId}/reports/statistics")
    Call<ResponseBody> getEventStatisticsPdf(@Path("eventId") int eventId);

    @GET("api/events/{id}")
    Call<GetEventDetails> getEventDetails(@Path("id") int eventId);
    @GET("api/events/rest")
    Call<List<MinimalEventDTO>> getAllEvents();
    @GET("api/events/service/{serviceId}")
    Call<List<MinimalEventDTO>> getEventsByService(@Path("serviceId") int serviceId);
    @GET("api/events/paginated")
    Call<PageResponse<MinimalEventDTO>> getPaginatedEvents(@Query("page") int page, @Query("size") int size);
    @GET("api/events/filter")
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

    @POST("api/events")
    Call<CreatedEventDTO> createEvent(@Body CreateEventDTO data);

    @POST("api/events/{eventId}/join")
    Call<JoinedEventDTO> joinEvent(@Path("eventId") int eventId, @Body Object emptyBody);

    @GET("api/events/me")
    Call<List<MinimalEventDTO>> getUserEventsByDate(
            @Query("date") LocalDate date
    );
}
