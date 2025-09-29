package com.example.myapplication.data.services.offer;

import com.example.myapplication.data.api.offer.ServiceApi;
import com.example.myapplication.data.models.dto.serviceDTO.PostServiceDTO;
import com.example.myapplication.data.models.dto.serviceDTO.PutServiceDTO;
import com.example.myapplication.data.models.dto.serviceDTO.ServiceDetailsDTO;
import com.example.myapplication.data.services.ApiClient;
import com.example.myapplication.utils.Settings;

import retrofit2.Call;

public class ServiceService {
    private static final String BASE_URL = Settings.BASE_URL + "/";
    private final ServiceApi serviceApi;

    public ServiceService() {
        serviceApi = ApiClient.getRetrofit(BASE_URL).create(ServiceApi.class);
    }

    public Call<ServiceDetailsDTO> getServiceDetails(Integer serviceId){
        return serviceApi.getDetails(serviceId);
    }

    public Call<ServiceDetailsDTO> createService(PostServiceDTO data){
        return serviceApi.createService(data);
    }

    public Call<Void> editService(Integer serviceId, PutServiceDTO data){
        return serviceApi.editService(serviceId, data);
    }

    public Call<Void> deleteService(Integer serviceId){
        return serviceApi.deleteService(serviceId);
    }
}
