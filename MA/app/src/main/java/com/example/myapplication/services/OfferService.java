package com.example.myapplication.services;

import com.example.myapplication.api.OfferApi;
import com.example.myapplication.dto.offerDTO.MinimalOfferDTO;
import com.example.myapplication.models.Availability;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OfferService {
    private static final String BASE_URL = "http://10.0.2.2:8080/api/offers/";
//    private static final String BASE_URL = "http://185.156.155.96:8080/api/offers/";
    private final OfferApi offerApi;

    public OfferService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        offerApi = retrofit.create(OfferApi.class);
    }


    public Call<List<MinimalOfferDTO>> getTop5Offers(Integer id) {
        return offerApi.getTop5Offers(id);
    }


    public Call<List<MinimalOfferDTO>> getAllOffers(Integer id) {
        return offerApi.getAllOffers(id);
    }

    public Call<List<MinimalOfferDTO>> getOfferList(Boolean isProduct, Boolean isService,
                                                    String name, String categoryName,
                                                    Integer lowestPrice, Availability isAvailable,
                                                    List<Integer> eventTypes, Integer id) {
        return offerApi.getOfferList(isProduct, isService, name, categoryName,
                lowestPrice, isAvailable, eventTypes, id);
    }
}
