package com.example.myapplication.api;

import com.example.myapplication.dto.common.PaginatedResponse;
import com.example.myapplication.dto.offerDTO.MinimalOfferDTO;
import com.example.myapplication.models.Availability;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OfferApi {

    @GET("offers/top5/authentified")
    Call<List<MinimalOfferDTO>> getTop5OffersAuthorized();

    @GET("offers/top5/unauthentified")
    Call<List<MinimalOfferDTO>> getTop5OffersUnauthorized();

    @GET("offers/rest")
    Call<List<MinimalOfferDTO>> getAllOffers();

    @GET("offers/filter/authentified")
    Call<PaginatedResponse<MinimalOfferDTO>> getFilteredOffersAuthorized(
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

    @GET("offers/filter/unauthentified")
    Call<PaginatedResponse<MinimalOfferDTO>> getFilteredOffersUnauthorized(
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
}
