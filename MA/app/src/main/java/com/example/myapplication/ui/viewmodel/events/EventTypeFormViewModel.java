package com.example.myapplication.ui.viewmodel.events;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.data.models.dto.CreateEventTypeDTO;
import com.example.myapplication.data.models.dto.CreatedEventTypeDTO;
import com.example.myapplication.data.models.dto.GetEventTypeDTO;
import com.example.myapplication.data.models.dto.UpdateEventTypeDTO;
import com.example.myapplication.data.models.dto.UpdatedEventTypeDTO;
import com.example.myapplication.data.services.offer.OfferCategoryService;
import com.example.myapplication.data.services.event.EventTypeService;
import com.example.myapplication.data.models.dto.OfferCategoryDTO.MinimalOfferCategoryDTO;

import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventTypeFormViewModel extends ViewModel {

    private final EventTypeService eventTypeService = new EventTypeService();
    private final OfferCategoryService offerCategoryService = new OfferCategoryService();

    private final MutableLiveData<List<MinimalOfferCategoryDTO>> categories = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();

    private final MutableLiveData<String> nameError = new MutableLiveData<>();
    private final MutableLiveData<String> descriptionError = new MutableLiveData<>();

    public LiveData<List<MinimalOfferCategoryDTO>> getCategories() {
        return categories;
    }
    public LiveData<Boolean> getLoading() {
        return loading;
    }
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }
    public LiveData<String> getNameError() {
        return nameError;
    }
    public LiveData<String> getDescriptionError() {
        return descriptionError;
    }

    public void loadCategories(@Nullable GetEventTypeDTO eventType, Set<Integer> selectedIds) {
        loading.setValue(true);
        offerCategoryService.getAvailableCategories().enqueue(new Callback<List<MinimalOfferCategoryDTO>>() {
            @Override
            public void onResponse(Call<List<MinimalOfferCategoryDTO>> call, Response<List<MinimalOfferCategoryDTO>> response) {
                loading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<MinimalOfferCategoryDTO> list = response.body();
                    categories.setValue(list);

                    if (eventType != null && selectedIds != null) {
                        for (MinimalOfferCategoryDTO cat : list) {
                            if (eventType.getRecommendedCategories().contains(cat.id)) {
                                selectedIds.add(cat.id);
                            }
                        }
                    }
                } else {
                    errorMessage.setValue("Failed to load categories");
                }
            }

            @Override
            public void onFailure(Call<List<MinimalOfferCategoryDTO>> call, Throwable t) {
                loading.setValue(false);
                errorMessage.setValue("Error: " + t.getMessage());
            }
        });
    }

    private boolean validateFields(String name, String description, Set<Integer> selectedCategories, boolean isEdit) {
        boolean valid = true;

        if (!isEdit) {
            if (name.isEmpty() || name.length() < 5 || name.length() > 24) {
                nameError.setValue("Name must be 5-24 characters");
                valid = false;
            } else {
                nameError.setValue(null);
            }
        }

        if (description.isEmpty() || description.length() < 5 || description.length() > 80) {
            descriptionError.setValue("Description must be 5-80 characters");
            valid = false;
        } else {
            descriptionError.setValue(null);
        }

        return valid;
    }

    public void confirmEventType(@Nullable GetEventTypeDTO eventType,
                                 String name,
                                 String description,
                                 Set<Integer> selectedCategories) {

        boolean isEdit = eventType != null;

        if (!validateFields(name, description, selectedCategories, isEdit)) {
            return;
        }

        if (!isEdit) {
            CreateEventTypeDTO dto = new CreateEventTypeDTO();
            dto.setName(name);
            dto.setDescription(description);
            dto.setRecommendedCategoriesIds(selectedCategories);

            eventTypeService.createEventType(dto).enqueue(new Callback<CreatedEventTypeDTO>() {
                @Override
                public void onResponse(Call<CreatedEventTypeDTO> call, Response<CreatedEventTypeDTO> response) {
                    if (response.isSuccessful()) {
                        successMessage.setValue("Event type created successfully");
                    } else {
                        errorMessage.setValue("Failed to create event type");
                    }
                }

                @Override
                public void onFailure(Call<CreatedEventTypeDTO> call, Throwable t) {
                    errorMessage.setValue("Error: " + t.getMessage());
                }
            });
        } else {
            UpdateEventTypeDTO dto = new UpdateEventTypeDTO();
            dto.setDescription(description);
            dto.setRecommendedCategoriesIds(selectedCategories);

            eventTypeService.updateEventType(eventType.getId(), dto).enqueue(new Callback<UpdatedEventTypeDTO>() {
                @Override
                public void onResponse(Call<UpdatedEventTypeDTO> call, Response<UpdatedEventTypeDTO> response) {
                    if (response.isSuccessful()) {
                        successMessage.setValue("Event type updated successfully");
                    } else {
                        errorMessage.setValue("Failed to update event type");
                    }
                }

                @Override
                public void onFailure(Call<UpdatedEventTypeDTO> call, Throwable t) {
                    errorMessage.setValue("Error: " + t.getMessage());
                }
            });
        }
    }
}
