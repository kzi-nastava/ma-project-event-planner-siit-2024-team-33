package com.example.myapplication.api;

import com.example.myapplication.dto.ratingDTO.GetRatingDTO;
import com.example.myapplication.dto.ratingDTO.PostRatingDTO;
import com.example.myapplication.models.Rating;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RatingApi {

    @POST("ratings")
    Call<Rating> submitRating(
            @Body PostRatingDTO postRatingDTO,
            @Query("offerId") int offerId
    );

    @PUT("approve/{commentId}")
    Call<Void> approveRating(
            @Path("commentId") int commentId
    );

    @DELETE("{commentId}")
    Call<Void> deleteRating(
            @Path("commentId") int commentId
    );

    @GET("offer/{offerId}")
    Call<List<GetRatingDTO>> getRatingsByOffer(
            @Path("offerId") int offerId
    );

    @GET
    Call<List<GetRatingDTO>> getAllRatings();

    @GET("ratings/{ratingId}")
    Call<Rating> getRatingById(
            @Path("ratingId") int ratingId
    );
}
