package com.example.myapplication.data.api.offer;

import com.example.myapplication.data.models.dto.pricesDTO.PriceItemDTO;
import com.example.myapplication.data.models.dto.pricesDTO.PutPriceDTO;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
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
