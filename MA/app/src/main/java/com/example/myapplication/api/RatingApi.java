package com.example.myapplication.api;

import com.example.myapplication.dto.PageResponse;
import com.example.myapplication.dto.ratingDTO.EventRatingDTO;
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
    Call<GetRatingDTO> submitRating(
            @Body PostRatingDTO postRatingDTO,
            @Query("offerId") int offerId
    );

    @GET("events/{eventId}")
    Call<List<EventRatingDTO>> getEventRatingsByEvent(@Path("eventId") int eventId);

    @POST("/api/ratings/events")
    Call<EventRatingDTO> submitEventRating(@Body EventRatingDTO dto, @Query("eventId") int eventId);
    @PUT("ratings/approve/{commentId}")
    Call<Void> approveRating(
            @Path("commentId") int commentId
    );

    @DELETE("ratings/{commentId}")
    Call<Void> deleteRating(
            @Path("commentId") int commentId
    );

    @GET("ratings/offer/{offerId}")
    Call<List<GetRatingDTO>> getRatingsByOffer(
            @Path("offerId") int offerId
    );

    @GET("/api/ratings/events")
    Call<PageResponse<EventRatingDTO>> getAllEventRatings(
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("ratings")
    Call<PageResponse<GetRatingDTO>> getAllRatings(
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("ratings/{ratingId}")
    Call<Rating> getRatingById(
            @Path("ratingId") int ratingId
    );
}
