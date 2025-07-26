package com.example.myapplication.api;

import com.example.myapplication.dto.OfferCategoryDTO.MinimalOfferCategoryDTO;
import com.example.myapplication.dto.eventTypeDTO.MinimalEventTypeDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface OfferCategoryApi {
    @GET("/api/offerCategories/available")
    Call<List<MinimalOfferCategoryDTO>> getActiveCategories();

}
