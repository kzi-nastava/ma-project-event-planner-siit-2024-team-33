package com.example.myapplication.api;

import com.example.myapplication.dto.invitationDTO.GetInvitationDTO;
import com.example.myapplication.dto.invitationDTO.PostInvitationDTO;
import com.example.myapplication.dto.invitationDTO.SimpleInvitation;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.*;

public interface InvitationApi {

    @POST("invitations")
    Call<Void> createInvitations(@Body PostInvitationDTO postInvitationDTO);

    @PATCH("invitations/{id}")
    Call<Void> updateInvitationStatus(@Path("id") int id, @Body Map<String, String> status);

    @GET("invitations/pending")
    Call<List<SimpleInvitation>> getMyPendingInvitations();

    @GET("{eventID}/invitations")
    Call<List<GetInvitationDTO>> getInvitationsForEvent(@Path("eventID") int eventID);

    @GET("invitations/{invitationId}")
    Call<GetInvitationDTO> getInvitationById(@Path("invitationId") int invitationId);

    @DELETE("invitations/{invitationId}")
    Call<Void> deleteInvitation(@Path("invitationId") int invitationId);
}
