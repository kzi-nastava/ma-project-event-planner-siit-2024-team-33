package com.example.myapplication.ui.viewmodel.events;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.data.dto.eventDTO.GetEventDetails;
import com.example.myapplication.data.services.event.EventService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailsViewModel extends ViewModel {

    private final EventService eventService = new EventService();

    private final MutableLiveData<GetEventDetails> eventDetails = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LiveData<GetEventDetails> getEventDetailsLiveData() {
        return eventDetails;
    }

    public LiveData<String> getErrorLiveData() {
        return error;
    }

    public void fetchEventDetails(int eventId) {
        eventService.getEventDetails(eventId).enqueue(new Callback<GetEventDetails>() {
            @Override
            public void onResponse(Call<GetEventDetails> call, Response<GetEventDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    eventDetails.postValue(response.body());
                } else {
                    error.postValue("Failed to load event details");
                }
            }

            @Override
            public void onFailure(Call<GetEventDetails> call, Throwable t) {
                error.postValue("Failed to load event details: " + t.getMessage());
            }
        });
    }
}
