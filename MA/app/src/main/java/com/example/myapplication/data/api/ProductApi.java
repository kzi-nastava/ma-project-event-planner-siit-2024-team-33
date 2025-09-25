package com.example.myapplication.data.api;

import com.example.myapplication.data.dto.productDTO.GetProductDTO;
import com.example.myapplication.data.dto.productDTO.MinimalProductDTO;
import com.example.myapplication.data.dto.productDTO.PostProductReservationDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ProductApi {
    @GET("api/products/{id}")
    Call<GetProductDTO> getDetails(@Path("id") Integer productId);

    @POST("api/products/{id}/reservations")
    Call<MinimalProductDTO> buyProduct(@Path("id") Integer productId, @Body PostProductReservationDTO data);

    @DELETE("api/products/{id}/reservations/{eventId}")
    Call<Void> cancelProductReservation(
            @Path("id") Integer productId,
            @Path("eventId") Integer eventId
    );
}
