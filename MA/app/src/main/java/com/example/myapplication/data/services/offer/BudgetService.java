package com.example.myapplication.data.services.offer;

import com.example.myapplication.data.api.offer.BudgetApi;
import com.example.myapplication.data.models.dto.budgetDTO.GetBudgetDTO;
import com.example.myapplication.data.models.dto.budgetDTO.MinimalBudgetItemDTO;
import com.example.myapplication.data.models.dto.budgetDTO.PostBudgetItemDTO;
import com.example.myapplication.data.models.dto.budgetDTO.PutBudgetItemDTO;
import com.example.myapplication.data.services.ApiClient;
import com.example.myapplication.utils.Settings;

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
