package com.example.myapplication.ui.view.page.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.ui.view.page.communication.ReportsActivity;
import com.example.myapplication.ui.view.dialog.NotificationsDialog;
import com.example.myapplication.data.models.AuthentifiedUser;
import com.example.myapplication.ui.view.page.provider.BudgetPage;
import com.example.myapplication.ui.view.page.communication.CommentsActivity;
import com.example.myapplication.ui.view.page.event.EventTypeCRUDFragment;
import com.example.myapplication.ui.view.page.offers.OfferCategoriesFragment;
import com.example.myapplication.ui.view.page.offers.ProvidersOffersFragment;
import com.example.myapplication.ui.view.page.profile.UpgradeSelectionActivity;
import com.example.myapplication.ui.view.page.authentication.LoginFragment;
import com.example.myapplication.ui.view.page.event.EventCreationFragment;
import com.example.myapplication.ui.view.page.offers.ProductCRUDFragment;
import com.example.myapplication.ui.view.page.profile.ProfileFavoritesFragment;
import com.example.myapplication.ui.view.page.profile.ProfileInformationFragment;
import com.example.myapplication.ui.view.page.authentication.ProfileTypeFragment;
import com.example.myapplication.ui.view.page.profile.ProfileScheduleFragment;
import com.example.myapplication.ui.view.page.services.CreateServiceFragment;
import com.example.myapplication.ui.viewmodel.navigation.ProfilePopupViewModel;
import com.google.android.material.imageview.ShapeableImageView;

public class ProfilePopupFragment extends Fragment {

    private ProfilePopupViewModel viewModel;

    private Button profileInfoButton, favoritesButton, scheduleButton, notificationsButton;
    private Button signInButton, signUpButton, logOutButton;
    private Button upgradeButton, createEventButton, budgetButton, offerCategoriesButton;
    private Button eventTypesButton, eventStatisticsButton, yourOffersButton, reportsButton;
    private Button commentsButton, createServiceButton, myProductsButton;

    private TextView emailText;
    private ShapeableImageView profileIcon;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_popup, container, false);

        initViews(view);

        viewModel = new ViewModelProvider(this).get(ProfilePopupViewModel.class);
        viewModel.loadUser(requireContext());

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            updateUI(user);
        });

        setListeners();

        return view;
    }


    private void initViews(View view) {
        profileInfoButton = view.findViewById(R.id.profile_info_button);
        favoritesButton = view.findViewById(R.id.favorites_button);
        scheduleButton = view.findViewById(R.id.schedule_button);
        notificationsButton = view.findViewById(R.id.notifications_button);
        signInButton = view.findViewById(R.id.sign_in_button);
        signUpButton = view.findViewById(R.id.sign_up_button);
        logOutButton = view.findViewById(R.id.log_out_button);
        createEventButton = view.findViewById(R.id.create_event_button);
        budgetButton = view.findViewById(R.id.budget_button);
        offerCategoriesButton = view.findViewById(R.id.offer_categories_button);
        eventTypesButton = view.findViewById(R.id.event_types_button);
        eventStatisticsButton = view.findViewById(R.id.event_statistics_button);
        yourOffersButton = view.findViewById(R.id.your_offers_button);
        reportsButton = view.findViewById(R.id.reports_button);
        commentsButton = view.findViewById(R.id.comments_button);
        createServiceButton = view.findViewById(R.id.create_service_button);
        upgradeButton = view.findViewById(R.id.upgrade_button);
        myProductsButton = view.findViewById(R.id.my_products_button);

        emailText = view.findViewById(R.id.email_text);
        profileIcon = view.findViewById(R.id.profile_icon);
    }

    private void updateUI(AuthentifiedUser user) {
        if (user != null) {
            emailText.setText(user.getEmail());
            Glide.with(this)
                    .load(user.getPicture())
                    .placeholder(R.drawable.proficon)
                    .error(R.drawable.proficon)
                    .into(profileIcon);

            profileInfoButton.setVisibility(viewModel.isLoggedIn() ? View.VISIBLE : View.GONE);
            favoritesButton.setVisibility(viewModel.isOrganizer() || viewModel.isProvider() ? View.VISIBLE : View.GONE);
            scheduleButton.setVisibility(viewModel.isOrganizer() || viewModel.isProvider() ? View.VISIBLE : View.GONE);
            notificationsButton.setVisibility(viewModel.isOrganizer() || viewModel.isProvider() ? View.VISIBLE : View.GONE);

            createEventButton.setVisibility(viewModel.isOrganizer() ? View.VISIBLE : View.GONE);
            budgetButton.setVisibility(viewModel.isOrganizer() ? View.VISIBLE : View.GONE);
            offerCategoriesButton.setVisibility(viewModel.isAdmin() ? View.VISIBLE : View.GONE);
            eventTypesButton.setVisibility(viewModel.isAdmin() ? View.VISIBLE : View.GONE);
            eventStatisticsButton.setVisibility(viewModel.isAdmin() || viewModel.isOrganizer() ? View.VISIBLE : View.GONE);
            yourOffersButton.setVisibility(viewModel.isProvider() ? View.VISIBLE : View.GONE);
            reportsButton.setVisibility(viewModel.isAdmin() ? View.VISIBLE : View.GONE);
            commentsButton.setVisibility(viewModel.isAdmin() ? View.VISIBLE : View.GONE);
            createServiceButton.setVisibility(viewModel.isProvider() ? View.VISIBLE : View.GONE);
            myProductsButton.setVisibility(viewModel.isProvider() ? View.VISIBLE : View.GONE);

            upgradeButton.setVisibility(viewModel.isRegularUser() ? View.VISIBLE : View.GONE);

            signInButton.setVisibility(View.GONE);
            signUpButton.setVisibility(View.GONE);
            logOutButton.setVisibility(View.VISIBLE);
        } else {
            emailText.setText("MAKE AN ACCOUNT");
            profileIcon.setImageResource(R.drawable.proficon);

            profileInfoButton.setVisibility(View.GONE);
            favoritesButton.setVisibility(View.GONE);
            scheduleButton.setVisibility(View.GONE);
            notificationsButton.setVisibility(View.GONE);
            createEventButton.setVisibility(View.GONE);
            budgetButton.setVisibility(View.GONE);
            offerCategoriesButton.setVisibility(View.GONE);
            eventTypesButton.setVisibility(View.GONE);
            eventStatisticsButton.setVisibility(View.GONE);
            yourOffersButton.setVisibility(View.GONE);
            reportsButton.setVisibility(View.GONE);
            commentsButton.setVisibility(View.GONE);
            createServiceButton.setVisibility(View.GONE);
            upgradeButton.setVisibility(View.GONE);

            signInButton.setVisibility(View.VISIBLE);
            signUpButton.setVisibility(View.VISIBLE);
            logOutButton.setVisibility(View.GONE);
        }
    }

    private void setListeners() {
        signInButton.setOnClickListener(v -> {
            closeDrawer();
            openFragment(LoginFragment.newInstance());
        });

        signUpButton.setOnClickListener(v -> {
            closeDrawer();
            openFragment(ProfileTypeFragment.newInstance());
        });

        logOutButton.setOnClickListener(v -> {
            viewModel.logout(requireContext());
            closeDrawer();
            openFragment(LoginFragment.newInstance());
        });

        profileInfoButton.setOnClickListener(v -> {
            closeDrawer();
            openFragment(ProfileInformationFragment.newInstance());
        });

        favoritesButton.setOnClickListener(v -> {
            closeDrawer();
            openFragment(ProfileFavoritesFragment.newInstance());
        });

        scheduleButton.setOnClickListener(v -> {
            closeDrawer();
            openFragment(ProfileScheduleFragment.newInstance());
        });

        createEventButton.setOnClickListener(v -> {
            closeDrawer();
            openFragment(EventCreationFragment.newInstance());
        });
      
        reportsButton.setOnClickListener(v -> {
            closeDrawer();
            startActivity(new Intent(getContext(), ReportsActivity.class));
        });

        commentsButton.setOnClickListener(v -> {
            closeDrawer();
            startActivity(new Intent(getContext(), CommentsActivity.class));
        });

        yourOffersButton.setOnClickListener(v -> {
            closeDrawer();
            openFragment(ProvidersOffersFragment.newInstance());
        });

        eventTypesButton.setOnClickListener(v -> {
            closeDrawer();
            openFragment(EventTypeCRUDFragment.newInstance());
        });

        myProductsButton.setOnClickListener(v -> {
            closeDrawer();
            openFragment(ProductCRUDFragment.newInstance());
        });

        budgetButton.setOnClickListener(v -> openFragment(BudgetPage.newInstance()));

        offerCategoriesButton.setOnClickListener(v -> openFragment(OfferCategoriesFragment.newInstance()));

        createServiceButton.setOnClickListener(v -> openFragment(CreateServiceFragment.newInstance(null)));

        upgradeButton.setOnClickListener(v -> {
            closeDrawer();
            startActivity(new Intent(getContext(), UpgradeSelectionActivity.class));
        });

        notificationsButton.setOnClickListener(v -> {
            closeDrawer();
            new NotificationsDialog(getContext()).show();
        });
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        closeDrawer();
    }

    private void closeDrawer() {
        androidx.drawerlayout.widget.DrawerLayout drawerLayout = requireActivity().findViewById(R.id.main);
        drawerLayout.closeDrawer(GravityCompat.END);
    }
}
