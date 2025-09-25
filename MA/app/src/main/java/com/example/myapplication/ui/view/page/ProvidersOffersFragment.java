package com.example.myapplication.ui.view.page;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.dto.offerDTO.MinimalOfferDTO;
import com.example.myapplication.data.models.OfferType;
import com.example.myapplication.data.services.OfferService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProvidersOffersFragment extends Fragment {

    private final OfferService offerService = new OfferService();

    Button pricesButton;
    LinearLayout offerContainer;

    List<MinimalOfferDTO> offers;

    public ProvidersOffersFragment() {
        // Required empty public constructor
    }

    public static ProvidersOffersFragment newInstance() {
        ProvidersOffersFragment fragment = new ProvidersOffersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_providers_offers, container, false);

        pricesButton = view.findViewById(R.id.btn_pricing_page);
        offerContainer = view.findViewById(R.id.offers_container);

        offerService.GetLoggedUsersOffers().enqueue(new Callback<List<MinimalOfferDTO>>() {
            @Override
            public void onResponse(Call<List<MinimalOfferDTO>> call, Response<List<MinimalOfferDTO>> response) {
                if(response.isSuccessful() && response.body() != null){
                    offers = response.body();
                    ShowOffers();
                }
                else
                    Toast.makeText(getContext(), "Error fetching offers", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<MinimalOfferDTO>> call, Throwable t) {
                Toast.makeText(getContext(), "Error fetching offers", Toast.LENGTH_SHORT).show();
            }
        });

        pricesButton.setOnClickListener(v -> {
            Fragment f = ProvidersPrices.newInstance();
            FragmentTransaction transaction = requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();

            transaction.replace(R.id.nav_host_fragment, f);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }

    void ShowOffers(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (MinimalOfferDTO offer : offers) {
            View offerView = inflater.inflate(R.layout.offering_card, offerContainer, false);


            TextView itemTitle = offerView.findViewById(R.id.offering_title);
            TextView itemText = offerView.findViewById(R.id.offering_description);
            Button infoButton = offerView.findViewById(R.id.offering_button);

            if (itemTitle != null && itemText != null) {
                itemTitle.setText(offer.getName());
                itemText.setText(offer.getDescription());
                infoButton.setOnClickListener(v -> {
                    Fragment f = null;
                    if(offer.getType() == OfferType.SERVICE)
                        f = ServiceDetailsFragment.newInstance(offer.offerId);
                    else
                        f = ProductDetailsFragment.newInstance(offer.offerId);
                    FragmentTransaction transaction = requireActivity()
                            .getSupportFragmentManager()
                            .beginTransaction();

                    transaction.replace(R.id.nav_host_fragment, f);
                    transaction.addToBackStack(null);
                    transaction.commit();
                });
            }

            offerContainer.addView(offerView);
        }
    }
}