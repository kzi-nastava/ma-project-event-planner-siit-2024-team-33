package com.example.myapplication.api;

import com.example.myapplication.dto.productDTO.GetProductDTO;
import com.example.myapplication.dto.providerDTO.ProviderDetailsDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProviderApi {
    @GET("api/providers/{id}")
    Call<ProviderDetailsDTO> getDetails(@Path("id") Integer providerId);
}
