package com.example.myapplication.api;

import com.example.myapplication.dto.PageResponse;
import com.example.myapplication.dto.eventDTO.MinimalEventDTO;
import com.example.myapplication.dto.serviceDTO.ServiceDetailsDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceApi {
    @GET("/{id}")
    Call<ServiceDetailsDTO> getDetails(@Path("id") Integer serviceId);
}
