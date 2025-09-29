package com.example.myapplication.data.api.offer;

import com.example.myapplication.data.models.dto.serviceReservationDTO.CreatedServiceReservationDTO;
import com.example.myapplication.data.models.dto.serviceReservationDTO.GetServiceReservationDTO;
import com.example.myapplication.data.models.dto.serviceReservationDTO.PostServiceReservationDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @DELETE("{serviceID}/reservations/{reservationId}")
    Call<Void> cancelReservation(
            @Path("serviceID") Integer serviceID,
            @Path("reservationId") Integer reservationId
    );

    @GET("{serviceID}/reservations/my-reservations")
    Call<List<GetServiceReservationDTO>> getMyReservationsForService(
            @Path("serviceID") Integer serviceID
    );

    @PUT("{serviceID}/reservations/{reservationId}")
    Call<CreatedServiceReservationDTO> updateServiceReservation(
            @Path("serviceID") Integer serviceID,
            @Path("reservationId") Integer reservationId,
            @Body PostServiceReservationDTO postServiceReservationDTO
    );
}
