package com.example.myapplication.ui.view.page.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.myapplication.R;
import com.example.myapplication.data.models.OfferType;
import com.example.myapplication.ui.view.page.offers.ProductDetailsFragment;
import com.example.myapplication.ui.view.page.services.ServiceDetailsFragment;
import com.example.myapplication.ui.view.page.event.EventDetailsFragment;
import com.example.myapplication.ui.viewmodel.profile.FavoritesViewModel;

public class ProfileFavoritesFragment extends Fragment {

    private Button buttonInfo, buttonFavo, buttonSchedule;

    private RecyclerView favoritesRecycler;
    private FavoriteEventsAdapter eventsAdapter;
    private FavoriteOffersAdapter offersAdapter;
    private Button buttonEvents, buttonOffers;
    private ProgressBar progressBar;

    private boolean showingEvents = true;
    private int currentPage = 0;
    private boolean isLoadingPage = false;
    private final int pageSize = 10;


    private FavoritesViewModel viewModel;

    public ProfileFavoritesFragment() {}

    public static ProfileFavoritesFragment newInstance() {
        return new ProfileFavoritesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_favorites, container, false);

        buttonInfo = view.findViewById(R.id.button_information);
        buttonFavo = view.findViewById(R.id.button_favorites);
        buttonSchedule = view.findViewById(R.id.button_schedule);

        //opening information fragment
        buttonInfo.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, ProfileInformationFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        });
        //opening favorites fragment
        buttonFavo.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, ProfileFavoritesFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        });
        //opening schedule fragment
        buttonSchedule.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, ProfileScheduleFragment.newInstance())
                    .addToBackStack(null)
                    .commit();
        });

        //initializing favorites part
        favoritesRecycler = view.findViewById(R.id.favorites_recycler);
        buttonEvents = view.findViewById(R.id.events_favorites_button);
        buttonOffers = view.findViewById(R.id.offers_favorites_button);
        progressBar = view.findViewById(R.id.progressBarFavorites);

        eventsAdapter = new FavoriteEventsAdapter(event -> {
            Bundle args = new Bundle();
            args.putInt("eventId", event.getId());
            EventDetailsFragment fragment = EventDetailsFragment.newInstance();
            fragment.setArguments(args);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        offersAdapter = new FavoriteOffersAdapter(offer -> {
            Fragment f = null;
            if (offer.getType() == OfferType.SERVICE)
                f = ServiceDetailsFragment.newInstance(offer.offerId);
            else
                f = ProductDetailsFragment.newInstance(offer.offerId);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, f)
                    .addToBackStack(null)
                    .commit();
        });

        favoritesRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        favoritesRecycler.setAdapter(eventsAdapter);

        viewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);

        buttonEvents.setOnClickListener(v -> switchToEvents());
        buttonOffers.setOnClickListener(v -> switchToOffers());

        favoritesRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (lm != null && !isLoadingPage && lm.findLastVisibleItemPosition() >= getCurrentAdapter().getItemCount() - 1) {
                    loadNextPage();
                }
            }
        });
        switchToEvents();

        viewModel.isLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });

        return view;
    }

    private void switchToEvents() {
        showingEvents = true;
        currentPage = 0;
        eventsAdapter.clearItems();
        favoritesRecycler.setAdapter(eventsAdapter);
        loadNextPage();
    }

    private void switchToOffers() {
        showingEvents = false;
        currentPage = 0;
        offersAdapter.clearItems();
        favoritesRecycler.setAdapter(offersAdapter);
        loadNextPage();
    }

    private void loadNextPage() {
        isLoadingPage = true;
        if (showingEvents) {
            viewModel.loadFavoriteEventsPage(currentPage, pageSize, events -> {
                eventsAdapter.addItems(events);
                currentPage++;
                isLoadingPage = false;
            });
        } else {
            viewModel.loadFavoriteOffersPage(currentPage, pageSize, offers -> {
                offersAdapter.addItems(offers);
                currentPage++;
                isLoadingPage = false;
            });
        }
    }

    private RecyclerView.Adapter<?> getCurrentAdapter() {
        return showingEvents ? eventsAdapter : offersAdapter;
    }
}