package com.example.myapplication.data.services;

import com.example.myapplication.data.api.FavoriteApi;
import com.example.myapplication.utils.Settings;

import retrofit2.Call;

public class FavoritesService {
    private static final String BASE_URL = Settings.BASE_URL + "/";
    private final FavoriteApi favoritesApi;

    public FavoritesService() {
        favoritesApi = ApiClient.getRetrofit(BASE_URL).create(FavoriteApi.class);
    }

    public Call<Boolean> isOfferFavorite(Integer offerId){
        return favoritesApi.isOfferFavorite(offerId);
    }

    public Call<Void> addOfferToFavorites(Integer offerId){
        return favoritesApi.addOfferToFavorites(offerId);
    }

    public Call<Void> removeOfferFromFavorites(Integer offerId){
        return favoritesApi.removeOfferFromFavorites(offerId);
    }
}
