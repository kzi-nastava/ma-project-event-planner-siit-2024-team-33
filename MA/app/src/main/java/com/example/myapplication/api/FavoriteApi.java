package com.example.myapplication.api;

import com.example.myapplication.dto.serviceDTO.PostServiceDTO;
import com.example.myapplication.dto.serviceDTO.PutServiceDTO;
import com.example.myapplication.dto.serviceDTO.ServiceDetailsDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface FavoriteApi {
    @GET("/api/favorites/offers/{id}")
    Call<Boolean> isOfferFavorite(@Path("id") Integer offerId);

    @POST("/api/favorites/offers/{id}")
    Call<Void> addOfferToFavorites(@Path("id") Integer offerId);

    @DELETE("/api/favorites/offers/{id}")
    Call<Void> removeOfferFromFavorites(@Path("id") Integer offerId);
}
