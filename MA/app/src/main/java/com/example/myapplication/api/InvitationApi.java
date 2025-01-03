package com.example.myapplication.api;

import com.example.myapplication.dto.invitationDTO.GetInvitationDTO;
import com.example.myapplication.dto.invitationDTO.PostInvitationDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InvitationApi {

    @POST("{inviterId}")
    Call<Void> createInvitations(
            //@Path("eventID") Integer eventId,
            @Path("inviterId") Integer inviterId,
            @Body PostInvitationDTO postInvitationDTO
    );

    @GET
    Call<List<GetInvitationDTO>> getInvitationsForEvent(
            @Path("eventID") Integer eventId
    );
}
