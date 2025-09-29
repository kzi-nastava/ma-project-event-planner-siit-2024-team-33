package com.example.myapplication.ui.viewmodel.events;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.data.models.dto.eventDTO.GetEventDetails;
import com.example.myapplication.data.models.dto.eventDTO.MinimalEventDTO;
import com.example.myapplication.data.models.AuthentifiedUser;
import com.example.myapplication.data.models.dto.JoinedEventDTO;
import com.example.myapplication.data.services.profile.FavoritesService;
import com.example.myapplication.data.services.authentication.AuthenticationService;
import com.example.myapplication.data.services.event.EventService;

import java.io.File;
import java.io.FileOutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailsViewModel extends AndroidViewModel {
    private final EventService eventService = new EventService();
    private final FavoritesService favoritesService = new FavoritesService();
    private final AuthenticationService authService;

    private final MutableLiveData<GetEventDetails> eventDetails = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isFavorite = new MutableLiveData<>();

    public LiveData<GetEventDetails> getEventDetailsLiveData() {
        return eventDetails;
    }

    public LiveData<String> getErrorLiveData() {
        return error;
    }

    public LiveData<Boolean> getIsFavoriteLiveData() {
        return isFavorite;
    }

    public EventDetailsViewModel(Application application) {
        super(application);
        authService = new AuthenticationService(application.getApplicationContext());
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


    //favorites
    public void checkIfFavorite(int eventId) {
        favoritesService.isEventFavorite(eventId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    isFavorite.postValue(response.body());
                } else {
                    isFavorite.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                isFavorite.postValue(false);
            }
        });
    }

    public void addToFavorites(int eventId) {
        favoritesService.addEventToFavorites(eventId).enqueue(new Callback<MinimalEventDTO>() {
            @Override
            public void onResponse(Call<MinimalEventDTO> call, Response<MinimalEventDTO> response) {
                if (response.isSuccessful()) {
                    isFavorite.postValue(true);
                } else {
                    error.postValue("Failed to add to favorites");
                }
            }

            @Override
            public void onFailure(Call<MinimalEventDTO> call, Throwable t) {
                error.postValue("Failed to add to favorites: " + t.getMessage());
            }
        });
    }

    public void removeFromFavorites(int eventId) {
        favoritesService.removeEventFromFavorites(eventId).enqueue(new Callback<MinimalEventDTO>() {
            @Override
            public void onResponse(Call<MinimalEventDTO> call, Response<MinimalEventDTO> response) {
                if (response.isSuccessful()) {
                    isFavorite.postValue(false);
                } else {
                    error.postValue("Failed to remove from favorites");
                }
            }

            @Override
            public void onFailure(Call<MinimalEventDTO> call, Throwable t) {
                error.postValue("Failed to remove from favorites: " + t.getMessage());
            }
        });
    }


    //checking user roles for event details
    public boolean isOrganizerOfEvent(GetEventDetails eventDetails) {
        if (eventDetails == null || eventDetails.getMinimalOrganizer() == null) return false;
        String organizerEmail = eventDetails.getMinimalOrganizer().getEmail();
        return organizerEmail != null &&
                AuthenticationService.getLoggedInUser() != null &&
                organizerEmail.equals(AuthenticationService.getLoggedInUser().getEmail());
    }

    public boolean isAdmin() {
        AuthentifiedUser user = authService.getLoggedInUser();
        return user != null && user.getRole() != null && "ADMIN_ROLE".equalsIgnoreCase(user.getRole().getName());
    }


    //joining event
    private final MutableLiveData<JoinedEventDTO> joinResult = new MutableLiveData<>();
    private final MutableLiveData<String> joinError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isJoining = new MutableLiveData<>(false);

    public LiveData<JoinedEventDTO> getJoinResult() {
        return joinResult;
    }

    public LiveData<String> getJoinError() {
        return joinError;
    }

    public LiveData<Boolean> getIsJoining() {
        return isJoining;
    }

    public void joinEvent(int eventId) {
        isJoining.setValue(true);

        eventService.joinEvent(eventId).enqueue(new Callback<JoinedEventDTO>() {
            @Override
            public void onResponse(Call<JoinedEventDTO> call, Response<JoinedEventDTO> response) {
                isJoining.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    joinResult.setValue(response.body());
                } else {
                    joinError.setValue("Failed to join event: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<JoinedEventDTO> call, Throwable t) {
                isJoining.setValue(false);
                joinError.setValue("Error joining event: " + t.getMessage());
            }
        });
    }

    //pdf downloading
    public void downloadEventPdf(int eventId, String type, PdfDownloadCallback callback) {
        Call<ResponseBody> call;
        if ("details".equals(type)) {
            call = eventService.getEventDetailsPdf(eventId);
        } else if ("statistics".equals(type)) {
            call = eventService.getEventStatisticsPdf(eventId);
        } else {
            callback.onError("Unknown PDF type: " + type);
            return;
        }

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        File pdfFile = new File(getApplication().getCacheDir(),
                                "event-" + eventId + "-" + type + ".pdf");

                        FileOutputStream fos = new FileOutputStream(pdfFile);
                        fos.write(response.body().bytes());
                        fos.close();

                        callback.onSuccess(pdfFile);

                    } catch (Exception e) {
                        callback.onError(e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    callback.onError("Failed to download PDF");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface PdfDownloadCallback {
        void onSuccess(File pdfFile);
        void onError(String message);
    }


}
