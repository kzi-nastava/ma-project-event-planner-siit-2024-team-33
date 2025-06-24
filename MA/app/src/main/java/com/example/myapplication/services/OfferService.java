package com.example.myapplication.services;

import com.example.myapplication.api.OfferApi;
import com.example.myapplication.dto.common.PaginatedResponse;
import com.example.myapplication.dto.offerDTO.MinimalOfferDTO;
import com.example.myapplication.models.Availability;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OfferService {
    private static final String BASE_URL = "http://10.0.2.2:8080/api/";
//    private static final String BASE_URL = "http://185.156.155.96:8080/api/";

    private final OfferApi offerApi;

    public OfferService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        offerApi = retrofit.create(OfferApi.class);
    }

    // --- Top 5 ---

    public Call<List<MinimalOfferDTO>> getTop5OffersAuthorized() {
        return offerApi.getTop5OffersAuthorized();
    }

    public Call<List<MinimalOfferDTO>> getTop5OffersUnauthorized() {
        return offerApi.getTop5OffersUnauthorized();
    }

    public Call<List<MinimalOfferDTO>> getAllOffers() {
        return offerApi.getAllOffers();
    }

    public Call<PaginatedResponse<MinimalOfferDTO>> getFilteredOffersAuthorized(
            Boolean isProduct, Boolean isService, String name, String categoryName,
            Integer lowestPrice, Availability isAvailable, List<Integer> eventTypes,
            int page, int size
    ) {
        return offerApi.getFilteredOffersAuthorized(isProduct, isService, name, categoryName,
                lowestPrice, isAvailable, eventTypes, page, size);
    }

    public Call<PaginatedResponse<MinimalOfferDTO>> getFilteredOffersUnauthorized(
            Boolean isProduct, Boolean isService, String name, String categoryName,
            Integer lowestPrice, Availability isAvailable, List<Integer> eventTypes,
            int page, int size
    ) {
        return offerApi.getFilteredOffersUnauthorized(isProduct, isService, name, categoryName,
                lowestPrice, isAvailable, eventTypes, page, size);
    }
}
