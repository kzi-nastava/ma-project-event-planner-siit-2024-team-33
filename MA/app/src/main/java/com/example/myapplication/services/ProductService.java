package com.example.myapplication.services;

import com.example.myapplication.api.ProductApi;
import com.example.myapplication.dto.productDTO.GetProductDTO;
import com.example.myapplication.dto.productDTO.MinimalProductDTO;
import com.example.myapplication.dto.productDTO.PostProductReservationDTO;
import com.example.myapplication.utils.Settings;

import retrofit2.Call;

public class ProductService {
    private static final String BASE_URL = Settings.BASE_URL + "/";
    private final ProductApi productApi;

    public ProductService() {
        productApi = ApiClient.getRetrofit(BASE_URL).create(ProductApi.class);
    }

    public Call<GetProductDTO> getProductDetails(Integer productId){
        return productApi.getDetails(productId);
    }

    public Call<MinimalProductDTO> buyProduct(Integer productId, Integer eventId){
        PostProductReservationDTO data = new PostProductReservationDTO();
        data.eventId = eventId;
        return productApi.buyProduct(productId, data);
    }

    public Call<Void> cancelProductReservation(Integer productId, Integer eventId) {
        return  productApi.cancelProductReservation(productId, eventId);
    }
}
