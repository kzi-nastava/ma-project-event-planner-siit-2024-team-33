package com.example.myapplication.api;

import com.example.myapplication.dto.PageResponse;
import com.example.myapplication.dto.eventDTO.MinimalEventDTO;
import com.example.myapplication.dto.serviceDTO.PostServiceDTO;
import com.example.myapplication.dto.serviceDTO.PutServiceDTO;
import com.example.myapplication.dto.serviceDTO.ServiceDetailsDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceApi {
    @GET("/api/services/{id}")
    Call<ServiceDetailsDTO> getDetails(@Path("id") Integer serviceId);

    @POST("/api/services")
    Call<ServiceDetailsDTO> createService(@Body PostServiceDTO data);

    @PUT("/api/services/{id}")
    Call<Void> editService(@Path("id") Integer serviceId, @Body PutServiceDTO data);

    @DELETE("/api/services/{id}")
    Call<Void> deleteService(@Path("id") Integer serviceId);
}
