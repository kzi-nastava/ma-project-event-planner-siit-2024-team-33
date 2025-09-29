package com.example.myapplication.data.services.offer;

import com.example.myapplication.data.api.offer.ProductApi;
import com.example.myapplication.data.models.dto.PageResponse;
import com.example.myapplication.data.models.dto.productDTO.GetProductDTO;
import com.example.myapplication.data.models.dto.productDTO.MinimalProductDTO;
import com.example.myapplication.data.models.dto.productDTO.PostProductReservationDTO;
import com.example.myapplication.data.models.dto.CreateProductDTO;
import com.example.myapplication.data.models.dto.CreatedProductDTO;
import com.example.myapplication.data.models.dto.ProviderProductDTO;
import com.example.myapplication.data.models.dto.UpdateProductDTO;
import com.example.myapplication.data.models.dto.UpdatedProductDTO;
import com.example.myapplication.data.services.ApiClient;
import com.example.myapplication.utils.Settings;

import java.util.List;

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

    public Call<PageResponse<ProviderProductDTO>> getProviderProducts(
            int page,
            int size,
            String query,
            List<Integer> offerCategoryId,
            List<Integer> eventTypeId,
            Double maxPrice,
            List<String> availability
    ) {
        return productApi.getProviderProducts(page, size, query, offerCategoryId, eventTypeId, maxPrice, availability);
    }

    public Call<CreatedProductDTO> createProduct(CreateProductDTO dto) {
        return productApi.createProduct(dto);
    }

    public Call<UpdatedProductDTO> updateProduct(int id, UpdateProductDTO dto) {
        return productApi.updateProduct(id, dto);
    }

    public Call<Void> deleteProduct(int id) {
        return productApi.deleteProduct(id);
    }
}
