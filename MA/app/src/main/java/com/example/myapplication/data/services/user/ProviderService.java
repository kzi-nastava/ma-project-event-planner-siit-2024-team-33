package com.example.myapplication.data.services.user;

import com.example.myapplication.data.api.user.ProviderApi;
import com.example.myapplication.data.models.dto.providerDTO.ProviderDetailsDTO;
import com.example.myapplication.data.services.ApiClient;
import com.example.myapplication.utils.Settings;

import retrofit2.Call;

public class ProviderService {
    private static final String BASE_URL = Settings.BASE_URL + "/";
    private final ProviderApi providerApi;

    public ProviderService() {
        providerApi = ApiClient.getRetrofit(BASE_URL).create(ProviderApi.class);
    }

    public Call<ProviderDetailsDTO> getProviderDetails(Integer providerId){
        return providerApi.getDetails(providerId);
    }
}
