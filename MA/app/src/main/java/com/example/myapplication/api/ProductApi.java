package com.example.myapplication.api;

import com.example.myapplication.dto.productDTO.GetProductDTO;
import com.example.myapplication.dto.serviceDTO.PostServiceDTO;
import com.example.myapplication.dto.serviceDTO.PutServiceDTO;
import com.example.myapplication.dto.serviceDTO.ServiceDetailsDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ProductApi {
    @GET("/{id}")
    Call<GetProductDTO> getDetails(@Path("id") Integer productId);
}
