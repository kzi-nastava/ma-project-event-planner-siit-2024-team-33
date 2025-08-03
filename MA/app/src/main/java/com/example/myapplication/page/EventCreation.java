package com.example.myapplication.page;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.component.MultipleStringInput;
import com.example.myapplication.dto.eventDTO.CreateEventActivityDTO;
import com.example.myapplication.dto.eventDTO.CreateEventDTO;
import com.example.myapplication.dto.eventDTO.CreatedEventDTO;
import com.example.myapplication.services.EventService;

import java.time.LocalDateTime;
import java.util.HashSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventCreation extends Fragment {
    private final EventService eventService = new EventService();


    public EventCreation() {
        // Required empty public constructor
    }

    public static EventCreation newInstance() {
        EventCreation fragment = new EventCreation();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    MultipleStringInput emails;
    Button createButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_creation, container, false);

        emails = MultipleStringInput.newInstance();

        getChildFragmentManager().beginTransaction()
                .replace(R.id.emailContainer, emails)
                .commit();

        createButton = view.findViewById(R.id.buttonSubmit);
        createButton.setOnClickListener(v -> onCreateClicked());

        return view;
    }

    public void onCreateClicked(){
        CreateEventActivityDTO activityDTO = new CreateEventActivityDTO();
        activityDTO.name = "Becej aktiviti";
        activityDTO.location = "Becej, Srbija";
        activityDTO.startingTime = LocalDateTime.of(2025, 9, 2, 0, 0).toString();
        activityDTO.endingTime = LocalDateTime.of(2025, 9, 12, 0, 0).toString();
        activityDTO.description = "Kul aktiviti verujte mi";

        CreateEventDTO data = new CreateEventDTO();
        data.name = "New event example";
        data.description = "Super cool new event really great guys!!!";
        data.numOfAttendees = 100;
        data.isPrivate = true;
        data.place = "Paris, France";
        data.dateOfEvent = LocalDateTime.of(2025, 9, 1, 0, 0).toString();
        data.endOfEvent = LocalDateTime.of(2025, 9, 20, 0, 0).toString();
        data.eventTypeId = 3;
        data.latitude = 45.60562196077926;
        data.longitude = 20.04272108348792;
        data.eventActivities.add(activityDTO);
        data.privateInvitations = new HashSet<>(emails.getItemList());

        eventService.createEvent(data).enqueue(new Callback<CreatedEventDTO>() {
            @Override
            public void onResponse(Call<CreatedEventDTO> call, Response<CreatedEventDTO> response) {
                if(response.isSuccessful() && response.body() != null)
                    Toast.makeText(getContext(), "SUCCESS!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), "ERROR!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CreatedEventDTO> call, Throwable t) {
                Toast.makeText(getContext(), "ERROR!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}