package com.example.myapplication.data.api;

import com.example.myapplication.data.dto.eventTypeDTO.MinimalEventTypeDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface EventTypesApi {
    @GET("/api/eventTypes/active")
    Call<List<MinimalEventTypeDTO>> getActiveTypes();

}
