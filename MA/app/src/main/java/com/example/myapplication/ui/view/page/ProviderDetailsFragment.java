package com.example.myapplication.ui.view.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.ui.view.component.ImageCarouselFragment;
import com.example.myapplication.data.dto.chatDTO.ChatContactDTO;
import com.example.myapplication.data.dto.providerDTO.ProviderDetailsDTO;
import com.example.myapplication.data.services.ChatWebsocketService;
import com.example.myapplication.data.services.ProviderService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProviderDetailsFragment extends Fragment {
    private final ProviderService providerService = new ProviderService();
    private final ChatWebsocketService chatWebsocketService = ChatWebsocketService.getInstance();

    private Integer providerId;
    private ProviderDetailsDTO provider;


    TextView tvTitle;
    TextView tvName;
    TextView tvSurname;
    TextView tvEmail;
    TextView tvResidency;
    TextView tvDescription;
    TextView tvCity;
    TextView tvPhone;

    Button chatBtn;


    public ProviderDetailsFragment() {
        // Required empty public constructor
    }

    public static ProviderDetailsFragment newInstance(Integer providerId) {
        ProviderDetailsFragment fragment = new ProviderDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("id", providerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            providerId = getArguments().getInt("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_provider_details, container, false);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvName = view.findViewById(R.id.tvName);
        tvSurname = view.findViewById(R.id.tvSurname);
        tvCity = view.findViewById(R.id.tvCity);
        tvPhone = view.findViewById(R.id.tvPhone);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvResidency = view.findViewById(R.id.tvResidency);
        tvDescription = view.findViewById(R.id.tvDescription);
        chatBtn = view.findViewById(R.id.btnChat);
        chatBtn.setOnClickListener(v -> {
            ChatContactDTO dto = new ChatContactDTO();
            dto.email = provider.email;
            dto.username = provider.name;
            chatWebsocketService.openChatWith(dto);
        });
        loadData();

        return view;
    }

    public void loadData(){
        providerService.getProviderDetails(providerId).enqueue(new Callback<ProviderDetailsDTO>() {
            @Override
            public void onResponse(Call<ProviderDetailsDTO> call, Response<ProviderDetailsDTO> response) {
                if(response.isSuccessful() && response.body() != null){
                    provider = response.body();
                    updateTextBoxes();
                }
                else
                    Toast.makeText(getContext(), "Error gettin provider data", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ProviderDetailsDTO> call, Throwable t) {
                Toast.makeText(getContext(), "Error gettin provider data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void updateTextBoxes(){
        tvTitle.setText(provider.providerName);
        tvName.setText(provider.name);
        tvSurname.setText(provider.surname);
        tvDescription.setText(provider.description);
        tvPhone.setText(provider.phoneNumber);
        tvCity.setText(provider.residency);
        tvResidency.setText(provider.residency);
        tvEmail.setText(provider.email);

        ImageCarouselFragment carouselFrag = ImageCarouselFragment.newInstance(new ArrayList<>(provider.picturesDataURI));

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.carouselContainer, carouselFrag)
                .commit();
    }
}