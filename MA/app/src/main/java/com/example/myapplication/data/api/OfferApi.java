package com.example.myapplication.data.api;

import com.example.myapplication.data.dto.PageResponse;
import com.example.myapplication.data.dto.offerDTO.MinimalOfferDTO;
import com.example.myapplication.data.models.Availability;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OfferApi {

    @GET("top5")
    Call<List<MinimalOfferDTO>> getTop5Offers(
    );

    @GET("rest")
    Call<List<MinimalOfferDTO>> getAllOffers(
    );

    @GET("filter")
    Call<PageResponse<MinimalOfferDTO>> getOfferList(
            @Query("isProduct") Boolean isProduct,
            @Query("isService") Boolean isService,
            @Query("name") String name,
            @Query("category") String categoryName,
            @Query("lowestPrice") Integer lowestPrice,
            @Query("isAvailable") Availability isAvailable,
            @Query("eventTypes") List<Integer> eventTypes,
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("mine")
    Call<List<MinimalOfferDTO>> getLoggedUsersOffers();
}
