package com.example.myapplication.ui.viewmodel.events;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.data.models.dto.PageResponse;
import com.example.myapplication.data.models.dto.GetEventTypeDTO;
import com.example.myapplication.data.models.dto.UpdatedEventTypeDTO;
import com.example.myapplication.data.services.event.EventTypeService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EventTypeViewModel extends ViewModel {
    private final EventTypeService service = new EventTypeService();

    private final MutableLiveData<List<GetEventTypeDTO>> eventTypes = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private int currentPage = 0;
    private final int pageSize = 10;
    private boolean isLastPage = false;
    private boolean isLoadingPage = false;

    public LiveData<List<GetEventTypeDTO>> getEventTypes() {
        return eventTypes;
    }

    public LiveData<Boolean> getLoading() {
        return loading;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchFirstPage() {
        currentPage = 0;
        isLastPage = false;
        eventTypes.setValue(new ArrayList<>());
        loadNextPage();
    }

    public void loadNextPage() {
        if (isLoadingPage || isLastPage) return;

        isLoadingPage = true;
        loading.setValue(true);

        service.getEventTypes(currentPage, pageSize).enqueue(new Callback<PageResponse<GetEventTypeDTO>>() {
            @Override
            public void onResponse(Call<PageResponse<GetEventTypeDTO>> call, Response<PageResponse<GetEventTypeDTO>> response) {
                isLoadingPage = false;
                loading.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    PageResponse<GetEventTypeDTO> pageResponse = response.body();

                    List<GetEventTypeDTO> currentList = eventTypes.getValue();
                    if (currentList == null) currentList = new ArrayList<>();

                    currentList.addAll(pageResponse.getContent());
                    eventTypes.setValue(currentList);

                    isLastPage = pageResponse.getNumber() >= pageResponse.getTotalPages() - 1;
                    if (!isLastPage) {
                        currentPage++;
                    }
                } else {
                    errorMessage.setValue("Failed to load event types.");
                }
            }

            @Override
            public void onFailure(Call<PageResponse<GetEventTypeDTO>> call, Throwable t) {
                isLoadingPage = false;
                loading.setValue(false);
                errorMessage.setValue("Error: " + t.getMessage());
            }
        });
    }


    public void toggleActivation(GetEventTypeDTO eventType) {
        if (eventType.getId() == 1) {
            errorMessage.setValue("This event type cannot be activated/deactivated.");
            return;
        }

        loading.setValue(true);

        Call<UpdatedEventTypeDTO> call;
        if (Boolean.TRUE.equals(eventType.getIsActive())) {
            call = service.deactivateEventType(eventType.getId());
        } else {
            call = service.activateEventType(eventType.getId());
        }

        call.enqueue(new Callback<UpdatedEventTypeDTO>() {
            @Override
            public void onResponse(Call<UpdatedEventTypeDTO> call, Response<UpdatedEventTypeDTO> response) {
                loading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    //updating local list
                    List<GetEventTypeDTO> currentList = eventTypes.getValue();
                    if (currentList != null) {
                        for (int i = 0; i < currentList.size(); i++) {
                            if (currentList.get(i).getId().equals(eventType.getId())) {
                                //replacing with updated DTO
                                GetEventTypeDTO updated = new GetEventTypeDTO(response.body());
                                currentList.set(i, updated);
                                break;
                            }
                        }
                        eventTypes.setValue(currentList);
                    }
                } else {
                    errorMessage.setValue("Failed to toggle activation");
                }
            }

            @Override
            public void onFailure(Call<UpdatedEventTypeDTO> call, Throwable t) {
                loading.setValue(false);
                errorMessage.setValue("Error: " + t.getMessage());
            }
        });
    }
}
