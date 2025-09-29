package com.example.myapplication.data.api.offer;

import com.example.myapplication.data.models.dto.PageResponse;
import com.example.myapplication.data.models.dto.productDTO.GetProductDTO;
import com.example.myapplication.data.models.dto.productDTO.MinimalProductDTO;
import com.example.myapplication.data.models.dto.productDTO.PostProductReservationDTO;
import com.example.myapplication.data.models.dto.CreateProductDTO;
import com.example.myapplication.data.models.dto.CreatedProductDTO;
import com.example.myapplication.data.models.dto.ProviderProductDTO;
import com.example.myapplication.data.models.dto.UpdateProductDTO;
import com.example.myapplication.data.models.dto.UpdatedProductDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @GET("api/products/me")
    Call<PageResponse<ProviderProductDTO>> getProviderProducts(
            @Query("page") int page,
            @Query("size") int size,
            @Query("query") String query,
            @Query("offerCategoryId") List<Integer> offerCategoryId,
            @Query("eventTypeId") List<Integer> eventTypeId,
            @Query("maxPrice") Double maxPrice,
            @Query("availability") List<String> availability
    );

    @POST("api/products")
    Call<CreatedProductDTO> createProduct(@Body CreateProductDTO product);

    @PUT("api/products/{id}")
    Call<UpdatedProductDTO> updateProduct(@Path("id") int id, @Body UpdateProductDTO product);

    @DELETE("api/products/{id}")
    Call<Void> deleteProduct(@Path("id") int id);
}
