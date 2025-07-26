package com.example.myapplication.services;

import com.example.myapplication.api.OfferCategoryApi;
import com.example.myapplication.dto.OfferCategoryDTO.MinimalOfferCategoryDTO;
import com.example.myapplication.utils.Settings;

import java.util.List;

import retrofit2.Call;

public class OfferCategoryService {
    private static final String BASE_URL = Settings.BASE_URL + "/";
    private final OfferCategoryApi offerCategoryApi;

    public OfferCategoryService() {
        offerCategoryApi = ApiClient.getRetrofit(BASE_URL).create(OfferCategoryApi.class);
    }

    public Call<List<MinimalOfferCategoryDTO>> getAvailableCategories() {
        return offerCategoryApi.getActiveCategories();
    }
}
