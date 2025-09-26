package com.example.myapplication.data.api;

import com.example.myapplication.data.dto.providerDTO.ProviderDetailsDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProviderApi {
    @GET("api/providers/{id}")
    Call<ProviderDetailsDTO> getDetails(@Path("id") Integer providerId);
}
