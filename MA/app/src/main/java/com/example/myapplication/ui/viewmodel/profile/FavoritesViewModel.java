package com.example.myapplication.ui.viewmodel.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.data.models.dto.PageResponse;
import com.example.myapplication.data.models.dto.eventDTO.MinimalEventDTO;
import com.example.myapplication.data.models.dto.offerDTO.MinimalOfferDTO;
import com.example.myapplication.data.services.profile.FavoritesService;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesViewModel extends ViewModel {

    private final FavoritesService service = new FavoritesService();

    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadFavoriteEventsPage(int page, int size, Consumer<List<MinimalEventDTO>> onResult) {
        loading.setValue(true);
        service.getFavoriteEvents(page, size).enqueue(new Callback<PageResponse<MinimalEventDTO>>() {
            @Override
            public void onResponse(Call<PageResponse<MinimalEventDTO>> call, Response<PageResponse<MinimalEventDTO>> response) {
                loading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    onResult.accept(response.body().getContent());
                } else {
                    error.setValue("Failed to load favorite events");
                }
            }

            @Override
            public void onFailure(Call<PageResponse<MinimalEventDTO>> call, Throwable t) {
                loading.setValue(false);
                error.setValue("Error: " + t.getMessage());
            }
        });
    }

    public void loadFavoriteOffersPage(int page, int size, Consumer<List<MinimalOfferDTO>> onResult) {
        loading.setValue(true);
        service.getFavoriteOffers(page, size).enqueue(new Callback<PageResponse<MinimalOfferDTO>>() {
            @Override
            public void onResponse(Call<PageResponse<MinimalOfferDTO>> call, Response<PageResponse<MinimalOfferDTO>> response) {
                loading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    onResult.accept(response.body().getContent());
                } else {
                    error.setValue("Failed to load favorite offers");
                }
            }

            @Override
            public void onFailure(Call<PageResponse<MinimalOfferDTO>> call, Throwable t) {
                loading.setValue(false);
                error.setValue("Error: " + t.getMessage());
            }
        });
    }
}
