package com.example.myapplication.data.api.event;

import com.example.myapplication.data.dto.PageResponse;
import com.example.myapplication.data.dto.eventTypeDTO.MinimalEventTypeDTO;
import com.example.myapplication.data.models.CreatedEventTypeDTO;
import com.example.myapplication.data.models.GetEventTypeDTO;
import com.example.myapplication.data.models.UpdateEventTypeDTO;
import com.example.myapplication.data.models.UpdatedEventTypeDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventTypesApi {
    @GET("/api/eventTypes/active")
    Call<List<MinimalEventTypeDTO>> getActiveTypes();

    @GET("/api/eventTypes")
    Call<PageResponse<GetEventTypeDTO>> getEventTypes(
            @Query("page") int page,
            @Query("size") int size
    );

    @POST("/api/eventTypes")
    Call<CreatedEventTypeDTO> createEventType(@Body com.example.myapplication.data.models.CreateEventTypeDTO dto);

    @PUT("/api/eventTypes/{id}")
    Call<UpdatedEventTypeDTO> updateEventType(@Path("id") Integer id, @Body UpdateEventTypeDTO dto);

    @PUT("/api/eventTypes/{id}/activation")
    Call<UpdatedEventTypeDTO> activateEventType(@Path("id") Integer id);

    @PUT("/api/eventTypes/{id}/deactivation")
    Call<UpdatedEventTypeDTO> deactivateEventType(@Path("id") Integer id);

    @GET("/api/eventTypes/exists")
    Call<Boolean> checkIfExists(@Query("name") String name);
}
