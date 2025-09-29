package com.example.myapplication.data.api.offer;

import com.example.myapplication.data.models.dto.budgetDTO.GetBudgetDTO;
import com.example.myapplication.data.models.dto.budgetDTO.MinimalBudgetItemDTO;
import com.example.myapplication.data.models.dto.budgetDTO.PostBudgetItemDTO;
import com.example.myapplication.data.models.dto.budgetDTO.PutBudgetItemDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface BudgetApi {
    @GET("api/events/{eventId}/budget")
    Call<GetBudgetDTO> getEventBudget(@Path("eventId") Integer eventId);

    @POST("api/events/{eventId}/budget")
    Call<MinimalBudgetItemDTO> addBudgetItem(
            @Path("eventId") Integer eventId,
            @Body PostBudgetItemDTO data
    );

    @PUT("api/events/{eventId}/budget/{categoryId}")
    Call<MinimalBudgetItemDTO> editBudgetItem(
            @Path("eventId") Integer eventId,
            @Path("categoryId") Integer categoryId,
            @Body PutBudgetItemDTO data
    );

    @DELETE("api/events/{eventId}/budget/{categoryId}")
    Call<Void> deleteBudgetItem(
            @Path("eventId") Integer eventId,
            @Path("categoryId") Integer categoryId
    );
}
