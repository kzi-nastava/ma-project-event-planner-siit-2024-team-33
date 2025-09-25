package com.example.myapplication.data.services;

import com.example.myapplication.data.api.OfferApi;
import com.example.myapplication.data.dto.PageResponse;
import com.example.myapplication.data.dto.offerDTO.OfferFilterDTO;
import com.example.myapplication.data.dto.offerDTO.MinimalOfferDTO;
import com.example.myapplication.utils.Settings;

import java.util.List;

import retrofit2.Call;

public class OfferService {
//    private static final String BASE_URL = "http://10.0.2.2:8080/api/offers/";
    private static final String BASE_URL = Settings.BASE_URL + "/api/offers/";
    private final OfferApi offerApi;

    public OfferService() {offerApi = ApiClient.getRetrofit(BASE_URL).create(OfferApi.class);}


    public Call<List<MinimalOfferDTO>> getTop5Offers() {
        return offerApi.getTop5Offers();
    }

    public Call<List<MinimalOfferDTO>> getAllOffers() {
        return offerApi.getAllOffers();
    }

    public Call<PageResponse<MinimalOfferDTO>> getOfferList(OfferFilterDTO filter, int page, int size) {
        return offerApi.getOfferList(filter.isProduct, filter.isService, filter.name, filter.category,
                filter.lowestPrice, filter.isAvailable, filter.eventTypes,page,size);
    }

    public Call<List<MinimalOfferDTO>> GetLoggedUsersOffers(){
        return offerApi.getLoggedUsersOffers();
    }
}
