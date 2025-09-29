package com.example.myapplication.ui.viewmodel.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.data.dto.eventDTO.MinimalEventDTO;
import com.example.myapplication.data.services.event.EventService;

import java.time.LocalDate;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleViewModel extends ViewModel {

    private final EventService eventService = new EventService();
    private final MutableLiveData<List<MinimalEventDTO>> eventsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<List<MinimalEventDTO>> getEventsLiveData() {
        return eventsLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadEventsForDate(LocalDate date) {
        isLoading.setValue(true);
        eventService.getUserEventsByDate(date).enqueue(new Callback<List<MinimalEventDTO>>() {
            @Override
            public void onResponse(Call<List<MinimalEventDTO>> call, Response<List<MinimalEventDTO>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    eventsLiveData.setValue(response.body());
                } else {
                    error.setValue("Failed to load events: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<MinimalEventDTO>> call, Throwable t) {
                isLoading.setValue(false);
                error.setValue("Error: " + t.getMessage());
            }
        });
    }
}
