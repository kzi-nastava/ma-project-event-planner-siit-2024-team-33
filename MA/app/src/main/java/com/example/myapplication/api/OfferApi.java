package com.example.myapplication.api;

import com.example.myapplication.dto.PageResponse;
import com.example.myapplication.dto.offerDTO.MinimalOfferDTO;
import com.example.myapplication.models.Availability;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OfferApi {

    @GET("top5/authentified")
    Call<List<MinimalOfferDTO>> getTop5Offers(
    );

    @GET("top5/unauthentified")
    Call<List<MinimalOfferDTO>> GetTop5OffersUnauthentified(
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
}
