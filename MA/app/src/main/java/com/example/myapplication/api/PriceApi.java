package com.example.myapplication.api;

import com.example.myapplication.dto.budgetDTO.GetBudgetDTO;
import com.example.myapplication.dto.budgetDTO.MinimalBudgetItemDTO;
import com.example.myapplication.dto.budgetDTO.PostBudgetItemDTO;
import com.example.myapplication.dto.budgetDTO.PutBudgetItemDTO;
import com.example.myapplication.dto.pricesDTO.PriceItemDTO;
import com.example.myapplication.dto.pricesDTO.PutPriceDTO;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PriceApi {
    @GET("/api/offers/mine/prices/export")
    Call<ResponseBody> getPdfExport();

    @GET("/api/offers/mine/prices")
    Call<List<PriceItemDTO>> getLoggedUsersPrices();

    @PUT("/api/offers/mine/prices/{offerId}")
    Call<List<PriceItemDTO>> editPrice(@Path("offerId") Integer offerId, @Body PutPriceDTO data);
}
