package com.example.myapplication.api;

import com.example.myapplication.dto.offerDTO.MinimalOfferDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OfferApi {

    @GET("top5")
    Call<List<MinimalOfferDTO>> getTop5Offers(
            @Query("id") Integer id
    );

    @GET("rest")
    Call<List<MinimalOfferDTO>> getAllOffers(
            @Query("id") Integer id
    );

    @GET("filter")
    Call<List<MinimalOfferDTO>> getOfferList(
            @Query("isProduct") Boolean isProduct,
            @Query("isService") Boolean isService,
            @Query("name") String name,
            @Query("category") String categoryName,
            @Query("lowestPrice") Integer lowestPrice,
            @Query("isAvailable") String isAvailable,
            @Query("eventTypes") List<Integer> eventTypes,
            @Query("id") Integer id
    );
}
