package com.example.myapplication.services;

import com.example.myapplication.api.NotificationApi;
import com.example.myapplication.api.ReportApi;
import com.example.myapplication.api.ServiceReservationApi;
import com.example.myapplication.dto.serviceReservationDTO.CreatedServiceReservationDTO;
import com.example.myapplication.dto.serviceReservationDTO.GetServiceReservationDTO;
import com.example.myapplication.dto.serviceReservationDTO.PostServiceReservationDTO;
import com.example.myapplication.utils.Settings;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class ServiceReservationService {
    private static final String BASE_URL = Settings.BASE_URL + "/api/services/";

    private final ServiceReservationApi serviceReservationApi;

    public ServiceReservationService() {serviceReservationApi =ApiClient.getRetrofit(BASE_URL).create(ServiceReservationApi.class);}

    public Call<CreatedServiceReservationDTO> reserveService(Integer serviceID, PostServiceReservationDTO postServiceReservationDTO){
        return serviceReservationApi.reserveService(serviceID,postServiceReservationDTO);
    }

    Call<GetServiceReservationDTO> getServiceReservationById( Integer serviceID, Integer reservationId){
        return serviceReservationApi.getServiceReservationById(serviceID,reservationId);
    }

    Call<CreatedServiceReservationDTO> updateServiceReservation(Integer serviceID,Integer reservationId, PostServiceReservationDTO postServiceReservationDTO){
        return serviceReservationApi.updateServiceReservation(serviceID,reservationId,postServiceReservationDTO);
    }




}
