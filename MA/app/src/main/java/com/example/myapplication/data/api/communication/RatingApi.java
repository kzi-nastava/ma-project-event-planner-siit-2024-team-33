package com.example.myapplication.data.api.communication;

import com.example.myapplication.data.models.dto.PageResponse;
import com.example.myapplication.data.models.dto.ratingDTO.EventRatingDTO;
import com.example.myapplication.data.models.dto.ratingDTO.GetRatingDTO;
import com.example.myapplication.data.models.dto.ratingDTO.PostRatingDTO;
import com.example.myapplication.data.models.Rating;

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

    @POST("ratings/events")
    Call<EventRatingDTO> submitEventRating(
            @Body EventRatingDTO dto,
            @Query("eventId") int eventId
    );

    @GET("ratings/events/{eventId}")
    Call<List<EventRatingDTO>> getEventRatingsByEvent(
            @Path("eventId") int eventId
    );

    @GET("ratings/events")
    Call<PageResponse<EventRatingDTO>> getAllEventRatings(
            @Query("page") int page,
            @Query("size") int size
    );

    @PUT("ratings/events/approve/{ratingId}")
    Call<Void> approveEventRating(
            @Path("ratingId") int ratingId
    );

    @DELETE("ratings/events/{ratingId}")
    Call<Void> deleteEventRating(
            @Path("ratingId") int ratingId
    );

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

    @GET("ratings")
    Call<PageResponse<GetRatingDTO>> getAllRatings(
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("ratings/{ratingId}")
    Call<Rating> getRatingById(
            @Path("ratingId") int ratingId
    );

    @GET("ratings/provider/{providerId}")
    Call<List<GetRatingDTO>> getRatingsByProvider(
            @Path("providerId") int providerId
    );
}
