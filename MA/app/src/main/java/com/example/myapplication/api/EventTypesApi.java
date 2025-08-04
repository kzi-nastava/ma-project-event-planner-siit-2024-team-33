package com.example.myapplication.api;

import com.example.myapplication.dto.PageResponse;
import com.example.myapplication.dto.eventDTO.MinimalEventDTO;
import com.example.myapplication.dto.eventTypeDTO.MinimalEventTypeDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventTypesApi {
    @GET("/api/eventTypes/active")
    Call<List<MinimalEventTypeDTO>> getActiveTypes();

}
