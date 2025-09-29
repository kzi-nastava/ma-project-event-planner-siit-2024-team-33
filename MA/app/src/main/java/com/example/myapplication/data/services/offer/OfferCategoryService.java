package com.example.myapplication.data.services.offer;

import com.example.myapplication.data.api.offer.OfferCategoryApi;
import com.example.myapplication.data.models.dto.OfferCategoryDTO.HandleSuggestionDTO;
import com.example.myapplication.data.models.dto.OfferCategoryDTO.MinimalOfferCategoryDTO;
import com.example.myapplication.data.models.dto.OfferCategoryDTO.PostOfferCategoryDTO;
import com.example.myapplication.data.models.dto.OfferCategoryDTO.PutOfferCategoryDTO;
import com.example.myapplication.data.services.ApiClient;
import com.example.myapplication.utils.Settings;

import java.util.List;

import retrofit2.Call;

public class OfferCategoryService {
    private static final String BASE_URL = Settings.BASE_URL + "/";
    private final OfferCategoryApi offerCategoryApi;

    public OfferCategoryService() {
        offerCategoryApi = ApiClient.getRetrofit(BASE_URL).create(OfferCategoryApi.class);
    }

    public Call<List<MinimalOfferCategoryDTO>> getAvailableCategories() {
        return offerCategoryApi.getActiveCategories();
    }

    public Call<List<MinimalOfferCategoryDTO>> getPendingCategories() {
        return offerCategoryApi.getPendingCategories();
    }

    public Call<List<MinimalOfferCategoryDTO>> getCategories(Boolean isAccepted, Boolean isEnabled) {
        return offerCategoryApi.getCategories(isAccepted, isEnabled);
    }

    public Call<MinimalOfferCategoryDTO> createCategory(PostOfferCategoryDTO data){
        return offerCategoryApi.createCategory(data);
    }

    public Call<Void> editCategory(Integer id, PutOfferCategoryDTO data){
        return offerCategoryApi.editCategory(id, data);
    }

    public Call<Void> deleteCategory(Integer id){
        return offerCategoryApi.deleteCategory(id);
    }

    public Call<Void> handlePendingCategory(Integer id, HandleSuggestionDTO data){
        return offerCategoryApi.handleSuggestion(id, data);
    }
}
