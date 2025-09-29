package com.example.myapplication.data.api.offer;

import com.example.myapplication.data.models.dto.OfferCategoryDTO.HandleSuggestionDTO;
import com.example.myapplication.data.models.dto.OfferCategoryDTO.MinimalOfferCategoryDTO;
import com.example.myapplication.data.models.dto.OfferCategoryDTO.PostOfferCategoryDTO;
import com.example.myapplication.data.models.dto.OfferCategoryDTO.PutOfferCategoryDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OfferCategoryApi {
    @GET("/api/offerCategories/available")
    Call<List<MinimalOfferCategoryDTO>> getActiveCategories();

    @GET("/api/offerCategories/pending")
    Call<List<MinimalOfferCategoryDTO>> getPendingCategories();

    @GET("/api/offerCategories")
    Call<List<MinimalOfferCategoryDTO>> getCategories(
            @Query("isAccepted") Boolean isAccepted,
            @Query("isEnabled") Boolean isEnabled
    );

    @POST("/api/offerCategories")
    Call<MinimalOfferCategoryDTO> createCategory(@Body PostOfferCategoryDTO data);

    @PUT("/api/offerCategories/{id}")
    Call<Void> editCategory(
            @Path("id") Integer id,
            @Body PutOfferCategoryDTO data
    );

    @DELETE("/api/offerCategories/{id}")
    Call<Void> deleteCategory(@Path("id") Integer id);

    @PUT("/api/offerCategories/pending/{id}")
    Call<Void> handleSuggestion(
            @Path("id") Integer id,
            @Body HandleSuggestionDTO data
            );
}
