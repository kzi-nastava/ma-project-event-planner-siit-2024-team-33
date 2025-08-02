package com.example.myapplication.services;

import com.example.myapplication.api.BudgetApi;
import com.example.myapplication.api.PriceApi;
import com.example.myapplication.dto.budgetDTO.GetBudgetDTO;
import com.example.myapplication.dto.budgetDTO.MinimalBudgetItemDTO;
import com.example.myapplication.dto.budgetDTO.PostBudgetItemDTO;
import com.example.myapplication.dto.budgetDTO.PutBudgetItemDTO;
import com.example.myapplication.dto.pricesDTO.PriceItemDTO;
import com.example.myapplication.dto.pricesDTO.PutPriceDTO;
import com.example.myapplication.utils.Settings;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;

public class PriceService {
    private static final String BASE_URL = Settings.BASE_URL + "/";
    private final PriceApi priceApi;

    public PriceService() {
        priceApi = ApiClient.getRetrofit(BASE_URL).create(PriceApi.class);
    }

    public Call<List<PriceItemDTO>> getLoggedUsersPrices(){
        return priceApi.getLoggedUsersPrices();
    }

    public Call<List<PriceItemDTO>> editPrice(Integer offerId, @Body PutPriceDTO data){
        return priceApi.editPrice(offerId, data);
    }

    public Call<ResponseBody> getPdfData(){
        return priceApi.getPdfExport();
    }
}
