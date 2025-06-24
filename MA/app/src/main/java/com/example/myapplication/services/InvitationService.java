package com.example.myapplication.services;

import com.example.myapplication.api.InvitationApi;
import com.example.myapplication.dto.invitationDTO.GetInvitationDTO;
import com.example.myapplication.dto.invitationDTO.PostInvitationDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InvitationService {
    private static final String BASE_URL = "http://10.0.2.2:8080/api/invitations/";
    private final InvitationApi invitationApi;

    public InvitationService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        invitationApi = retrofit.create(InvitationApi.class);
    }

    public Call<Void> createInvitations(PostInvitationDTO postInvitationDTO) {
        return invitationApi.createInvitations(postInvitationDTO);
    }

    public Call<List<GetInvitationDTO>> getInvitationsForEvent(Integer eventId) {
        return invitationApi.getInvitationsForEvent(eventId);
    }
}
