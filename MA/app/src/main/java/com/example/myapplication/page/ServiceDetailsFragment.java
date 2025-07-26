package com.example.myapplication.page;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.component.ImageCarouselFragment;
import com.example.myapplication.dto.eventTypeDTO.MinimalEventTypeDTO;
import com.example.myapplication.dto.serviceDTO.ServiceDetailsDTO;
import com.example.myapplication.reviews.ReviewsSectionView;
import com.example.myapplication.services.AuthenticationService;
import com.example.myapplication.services.FavoritesService;
import com.example.myapplication.services.ServiceService;
import com.example.myapplication.services.UsersService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceDetailsFragment extends Fragment {
    private ReviewsSectionView reviewsSection;
    private final ServiceService serviceService = new ServiceService();
    private final UsersService usersService = new UsersService();
    private final FavoritesService favoritesService = new FavoritesService();

    private Integer serviceId;
    private ServiceDetailsDTO service;
    private Boolean isFavorite;


    TextView tvServiceName;
    TextView valueProvider;
    TextView valueCategory;
    TextView valuePrice;
    TextView valueDiscount;
    TextView valueBooking;
    TextView valueCancellation;
    TextView valueDuration;
    TextView valueAvailability;
    TextView valueReservationType;
    TextView valueDescription;
    LinearLayout containerValidTypes;

    ImageButton favoriteButton;

    public ServiceDetailsFragment() {
        // Required empty public constructor
    }

    public static ServiceDetailsFragment newInstance(Integer serviceId) {
        ServiceDetailsFragment fragment = new ServiceDetailsFragment();
        Bundle args = new Bundle();
        args.putInt("id", serviceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            serviceId = getArguments().getInt("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_details, container, false);
        tvServiceName = view.findViewById(R.id.tvServiceName);
        valueProvider = view.findViewById(R.id.valueProvider);
        valueCategory = view.findViewById(R.id.valueCategory);
        valuePrice = view.findViewById(R.id.valuePrice);
        valueDiscount = view.findViewById(R.id.valueDiscount);
        valueBooking = view.findViewById(R.id.valueBooking);
        valueCancellation = view.findViewById(R.id.valueCancellation);
        valueDuration = view.findViewById(R.id.valueDuration);
        valueAvailability = view.findViewById(R.id.valueAvailability);
        valueReservationType = view.findViewById(R.id.valueReservationType);
        valueDescription = view.findViewById(R.id.valueDescription);
        containerValidTypes = view.findViewById(R.id.containerValidTypes);
        favoriteButton = view.findViewById(R.id.btnFavorite);
        favoriteButton.setOnClickListener(v -> {toggleIsFavorite();});
        reviewsSection = view.findViewById(R.id.reviewsSection);

        loadData();

        return view;
    }

    public void loadData(){
        serviceService.getServiceDetails(serviceId).enqueue(new Callback<ServiceDetailsDTO>() {
            @Override
            public void onResponse(Call<ServiceDetailsDTO> call, Response<ServiceDetailsDTO> response) {
                if(response.isSuccessful() && response.body() != null) {
                    service = response.body();
                    updateTextBoxes();
                }
                else
                    Toast.makeText(getContext(), "Error loading Service :(", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ServiceDetailsDTO> call, Throwable t) {
                Toast.makeText(getContext(), "Error loading Service :(", Toast.LENGTH_SHORT).show();
            }
        });
        loadIsFavorite();
    }

    public void loadIsFavorite(){
        favoritesService.isOfferFavorite(serviceId).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if(response.body())
                        favoriteButton.setImageResource(android.R.drawable.btn_star_big_on);
                    else
                        favoriteButton.setImageResource(android.R.drawable.btn_star_big_off);
                    isFavorite = response.body();
                }
                else
                    Toast.makeText(getContext(), "Error loading favorite :(", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getContext(), "Error loading favorite :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void toggleIsFavorite(){
        Callback<Void> reloadCallback = new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                loadIsFavorite();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error toggling favorite :(", Toast.LENGTH_SHORT).show();
            }
        };

        if(isFavorite)
            favoritesService.removeOfferFromFavorites(serviceId).enqueue(reloadCallback);
        else
            favoritesService.addOfferToFavorites(serviceId).enqueue(reloadCallback);
    }

    public void updateTextBoxes(){
        tvServiceName.setText(service.name);
        valueProvider.setText(service.providerName);
        valueCategory.setText(service.category != null ? service.category.name : "—");
        valuePrice.setText(String.format("€%.2f", service.price));
        valueDiscount.setText(String.format("€%.2f", service.discount));
        valueBooking.setText(service.reservationInHours + "h in advance");
        valueCancellation.setText(service.cancellationInHours + "h in advance");
        valueDuration.setText(service.minLengthInMins + "–" + service.maxLengthInMins + " min");
        valueAvailability.setText(service.isAvailable ? "Available" : "Unavailable");
        valueReservationType.setText(service.isAutomatic ? "Automatic" : "Manual");
        valueDescription.setText(service.description != null ? service.description : "No description provided");

        containerValidTypes.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (MinimalEventTypeDTO event : service.validEventCategories) {
            TextView chip = new TextView(getContext());
            chip.setText(event.name);
            chip.setPadding(24, 12, 24, 12);
            chip.setTextColor(getResources().getColor(R.color.nonchalant_blue));
            chip.setBackgroundResource(R.drawable.rounded_corners); // Use drawable with rounded bg
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            lp.setMargins(8, 0, 8, 0);
            chip.setLayoutParams(lp);
            chip.setTextSize(20.0f);
            containerValidTypes.addView(chip);
        }

        ImageCarouselFragment carouselFrag = ImageCarouselFragment.newInstance(new ArrayList<>(service.picturesDataURI));

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.carouselContainer, carouselFrag)
                .commit();
        reviewsSection.setOfferId(serviceId);
    }
}