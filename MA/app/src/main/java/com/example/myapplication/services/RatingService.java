package com.example.myapplication.services;

import com.example.myapplication.api.RatingApi;
import com.example.myapplication.dto.ratingDTO.GetRatingDTO;
import com.example.myapplication.dto.ratingDTO.PostRatingDTO;
import com.example.myapplication.models.Rating;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RatingService {
    private static final String BASE_URL = "http://192.168.2.8:8080/api/ratings/";
    private final RatingApi ratingApi;

    public RatingService() {
        ratingApi = ApiClient.getRetrofit(BASE_URL).create(RatingApi.class);
    }

    public Call<Rating> submitRating(PostRatingDTO postRatingDTO, int offerId) {
        return ratingApi.submitRating(postRatingDTO, offerId);
    }

    public Call<Void> approveRating(int commentId) {
        return ratingApi.approveRating(commentId);
    }

    public Call<Void> deleteRating(int commentId) {
        return ratingApi.deleteRating(commentId);
    }

    public Call<List<GetRatingDTO>> getRatingsByOffer(int offerId) {
        return ratingApi.getRatingsByOffer(offerId);
    }

    public Call<List<GetRatingDTO>> getAllRatings() {
        return ratingApi.getAllRatings();
    }

    public Call<Rating> getRatingById(int ratingId) {
        return ratingApi.getRatingById(ratingId);
    }
}
