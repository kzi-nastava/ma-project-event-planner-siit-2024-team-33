package com.example.myapplication.api;

import com.example.myapplication.dto.serviceReservationDTO.CreatedServiceReservationDTO;
import com.example.myapplication.dto.serviceReservationDTO.GetServiceReservationDTO;
import com.example.myapplication.dto.serviceReservationDTO.PostServiceReservationDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceReservationApi {

    @POST("{serviceID}/reservations")
    Call<CreatedServiceReservationDTO> reserveService(
            @Path("serviceID") Integer serviceID,
            @Body PostServiceReservationDTO dto
    );

    @GET("{serviceID}/reservations/{reservationId}")
    Call<GetServiceReservationDTO> getServiceReservationById(
            @Path("serviceID") Integer serviceID,
            @Path("reservationId") Integer reservationId
    );

    @PUT("{serviceID}/reservations/{reservationId}")
    Call<CreatedServiceReservationDTO> updateServiceReservation(
            @Path("serviceID") Integer serviceID,
            @Path("reservationId") Integer reservationId,
            @Body PostServiceReservationDTO postServiceReservationDTO
    );
}
