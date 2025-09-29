package com.example.myapplication.data.api;

import com.example.myapplication.data.dto.PageResponse;
import com.example.myapplication.data.dto.eventDTO.MinimalEventDTO;
import com.example.myapplication.data.dto.offerDTO.MinimalOfferDTO;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FavoriteApi {
    @GET("/api/favorites/offers/{id}")
    Call<Boolean> isOfferFavorite(@Path("id") Integer offerId);

    @POST("/api/favorites/offers/{id}")
    Call<Void> addOfferToFavorites(@Path("id") Integer offerId);

    @DELETE("/api/favorites/offers/{id}")
    Call<Void> removeOfferFromFavorites(@Path("id") Integer offerId);

    @GET("/api/favorites/events/{id}/exists")
    Call<Boolean> isEventFavorite(@Path("id") Integer eventId);

    @POST("/api/favorites/events/{id}")
    Call<MinimalEventDTO> addEventToFavorites(@Path("id") Integer eventId);

    @DELETE("/api/favorites/events/{id}")
    Call<MinimalEventDTO> removeEventFromFavorites(@Path("id") Integer eventId);

    @GET("/api/favorites/events")
    Call<PageResponse<MinimalEventDTO>> getFavoriteEvents(
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("/api/favorites/offers")
    Call<PageResponse<MinimalOfferDTO>> getFavoriteOffers(
            @Query("page") int page,
            @Query("size") int size
    );
}
