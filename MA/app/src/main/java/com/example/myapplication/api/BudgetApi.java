package com.example.myapplication.api;

import com.example.myapplication.dto.PageResponse;
import com.example.myapplication.dto.budgetDTO.GetBudgetDTO;
import com.example.myapplication.dto.budgetDTO.MinimalBudgetItemDTO;
import com.example.myapplication.dto.budgetDTO.PostBudgetItemDTO;
import com.example.myapplication.dto.budgetDTO.PutBudgetItemDTO;
import com.example.myapplication.dto.eventDTO.MinimalEventDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
