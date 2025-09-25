package com.example.myapplication.data.api;

import com.example.myapplication.data.dto.serviceDTO.PostServiceDTO;
import com.example.myapplication.data.dto.serviceDTO.PutServiceDTO;
import com.example.myapplication.data.dto.serviceDTO.ServiceDetailsDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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
