package com.example.myapplication.data.api;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FavoriteApi {
    @GET("/api/favorites/offers/{id}")
    Call<Boolean> isOfferFavorite(@Path("id") Integer offerId);

    @POST("/api/favorites/offers/{id}")
    Call<Void> addOfferToFavorites(@Path("id") Integer offerId);

    @DELETE("/api/favorites/offers/{id}")
    Call<Void> removeOfferFromFavorites(@Path("id") Integer offerId);
}
