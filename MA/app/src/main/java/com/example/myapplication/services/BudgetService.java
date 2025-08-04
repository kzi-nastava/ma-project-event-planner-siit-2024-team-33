package com.example.myapplication.services;

import com.example.myapplication.api.BudgetApi;
import com.example.myapplication.api.EventApi;
import com.example.myapplication.dto.PageResponse;
import com.example.myapplication.dto.budgetDTO.GetBudgetDTO;
import com.example.myapplication.dto.budgetDTO.MinimalBudgetItemDTO;
import com.example.myapplication.dto.budgetDTO.PostBudgetItemDTO;
import com.example.myapplication.dto.budgetDTO.PutBudgetItemDTO;
import com.example.myapplication.dto.eventDTO.FilterEventDTO;
import com.example.myapplication.dto.eventDTO.MinimalEventDTO;
import com.example.myapplication.utils.Settings;

import java.util.List;

import retrofit2.Call;

public class BudgetService {
    private static final String BASE_URL = Settings.BASE_URL + "/";
    private final BudgetApi budgetApi;

    public BudgetService() {
        budgetApi = ApiClient.getRetrofit(BASE_URL).create(BudgetApi.class);
    }

    public Call<GetBudgetDTO> getEventBudget(int eventId){
        return budgetApi.getEventBudget(eventId);
    }

    public Call<MinimalBudgetItemDTO> addBudgetItem(int eventId, PostBudgetItemDTO data){
        return budgetApi.addBudgetItem(eventId, data);
    }

    public Call<MinimalBudgetItemDTO> editBudgetItem(int eventId, int categoryId, PutBudgetItemDTO data){
        return budgetApi.editBudgetItem(eventId, categoryId, data);
    }

    public Call<Void> deleteBudgetItem(int eventId, int categoryId){
        return budgetApi.deleteBudgetItem(eventId, categoryId);
    }
}
