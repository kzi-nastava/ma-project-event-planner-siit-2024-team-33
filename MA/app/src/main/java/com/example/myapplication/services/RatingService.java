package com.example.myapplication.services;

import com.example.myapplication.api.RatingApi;
import com.example.myapplication.dto.PageResponse;
import com.example.myapplication.dto.ratingDTO.GetRatingDTO;
import com.example.myapplication.dto.ratingDTO.PostRatingDTO;
import com.example.myapplication.models.Rating;
import com.example.myapplication.utils.Settings;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RatingService {
    private static final String BASE_URL = Settings.BASE_URL + "/api/";
    private final RatingApi ratingApi;

    public RatingService() {
        ratingApi = ApiClient.getRetrofit(BASE_URL).create(RatingApi.class);
    }

    public Call<GetRatingDTO> submitRating(PostRatingDTO postRatingDTO, int offerId) {
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

    public Call<PageResponse<GetRatingDTO>> getAllRatings(int page, int size) {
        return ratingApi.getAllRatings(page,size);
    }

    public Call<Rating> getRatingById(int ratingId) {
        return ratingApi.getRatingById(ratingId);
    }
}
