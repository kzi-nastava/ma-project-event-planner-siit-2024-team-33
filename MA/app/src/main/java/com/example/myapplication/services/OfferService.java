package com.example.myapplication.services;

import com.example.myapplication.api.EventApi;
import com.example.myapplication.api.OfferApi;
import com.example.myapplication.dto.PageResponse;
import com.example.myapplication.dto.offerDTO.OfferFilterDTO;
import com.example.myapplication.dto.offerDTO.MinimalOfferDTO;
import com.example.myapplication.models.Availability;
import com.example.myapplication.utils.Settings;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OfferService {
//    private static final String BASE_URL = "http://10.0.2.2:8080/api/offers/";
    private static final String BASE_URL = Settings.BASE_URL + "/api/offers/";
    private final OfferApi offerApi;

    public OfferService() {offerApi = ApiClient.getRetrofit(BASE_URL).create(OfferApi.class);}


    public Call<List<MinimalOfferDTO>> getTop5Offers() {
        return offerApi.getTop5Offers();
    }

    public Call<List<MinimalOfferDTO>> GetTop5OffersUnauthentified() {
        return offerApi.GetTop5OffersUnauthentified();
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
