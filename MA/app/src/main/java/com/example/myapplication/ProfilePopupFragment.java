package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.myapplication.dialog.NotificationsDialog;
import com.example.myapplication.models.AuthentifiedUser;
import com.example.myapplication.page.CommentsActivity;
import com.example.myapplication.page.CreateServiceFragment;
import com.example.myapplication.page.LoginActivity;
import com.example.myapplication.page.OfferCategoriesFragment;
import com.example.myapplication.page.ProfilePage;
import com.example.myapplication.page.RegisterActivity;
import com.example.myapplication.reports.ReportsActivity;
import com.example.myapplication.services.AuthenticationService;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

public class ProfilePopupFragment extends DialogFragment {

    private Button profileInfoButton, favoritesButton, scheduleButton, notificationsButton, signInButton;
    private Button createEventButton, offerCategoriesButton, eventTypesButton, eventStatisticsButton, yourOffersButton, reportsButton, commentsButton, createServiceButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_popup, container, false);

        profileInfoButton = view.findViewById(R.id.profile_info_button);
        favoritesButton = view.findViewById(R.id.favorites_button);
        scheduleButton = view.findViewById(R.id.schedule_button);
        notificationsButton = view.findViewById(R.id.notifications_button);
        signInButton = view.findViewById(R.id.sign_in_button);
        createEventButton = view.findViewById(R.id.create_event_button);
        offerCategoriesButton = view.findViewById(R.id.offer_categories_button);
        eventTypesButton = view.findViewById(R.id.event_types_button);
        eventStatisticsButton = view.findViewById(R.id.event_statistics_button);
        yourOffersButton = view.findViewById(R.id.your_offers_button);
        reportsButton = view.findViewById(R.id.reports_button);
        commentsButton = view.findViewById(R.id.comments_button);
        createServiceButton = view.findViewById(R.id.create_service_button);
        Button signUpButton = view.findViewById(R.id.sign_up_button);
        Button logOutButton = view.findViewById(R.id.log_out_button);


        AuthentifiedUser user = AuthenticationService.getLoggedInUser();
        ShapeableImageView profileIcon = view.findViewById(R.id.profile_icon);
        TextView emailText = view.findViewById(R.id.email_text);

        if (user != null) {
            String roleName = user.getRole() != null ? user.getRole().getName() : "";

            boolean isOrganizer = roleName.equals("ORGANIZER_ROLE");
            boolean isProvider = roleName.equals("PROVIDER_ROLE");
            boolean isAdmin = roleName.equals("ADMIN_ROLE");

            // Show/hide buttons as per Angular template logic:

            profileInfoButton.setVisibility(View.VISIBLE);
            favoritesButton.setVisibility(View.VISIBLE);
            scheduleButton.setVisibility(View.VISIBLE);

            // Notifications visible for Organizer or Provider
            notificationsButton.setVisibility((isOrganizer || isProvider) ? View.VISIBLE : View.GONE);

            // Create event only for Organizer
            createEventButton.setVisibility(isOrganizer ? View.VISIBLE : View.GONE);

            // Offer categories only for Admin
            offerCategoriesButton.setVisibility(isAdmin ? View.VISIBLE : View.GONE);
            offerCategoriesButton.setOnClickListener(v -> OpenOfferCategories());

            // Event types only for Admin
            eventTypesButton.setVisibility(isAdmin ? View.VISIBLE : View.GONE);

            // Event statistics for Admin or Organizer
            eventStatisticsButton.setVisibility((isAdmin || isOrganizer) ? View.VISIBLE : View.GONE);

            // Your offers for Provider
            yourOffersButton.setVisibility(isProvider ? View.VISIBLE : View.GONE);

            // Reports for Admin
            reportsButton.setVisibility(isAdmin ? View.VISIBLE : View.GONE);

            // Comments for Admin
            commentsButton.setVisibility(isAdmin ? View.VISIBLE : View.GONE);

            // Create Service for Provider
            createServiceButton.setVisibility(isProvider ? View.VISIBLE : View.GONE);
            createServiceButton.setOnClickListener(v -> openCreateService());

            // Sign in / sign up hidden
            signInButton.setVisibility(View.GONE);
            signUpButton.setVisibility(View.GONE);

            logOutButton.setVisibility(View.VISIBLE);

            // Setup click listeners as needed for the new buttons...
        } else {
            // No user logged in - hide all except sign in / sign up
            profileInfoButton.setVisibility(View.GONE);
            favoritesButton.setVisibility(View.GONE);
            scheduleButton.setVisibility(View.GONE);
            notificationsButton.setVisibility(View.GONE);
            createEventButton.setVisibility(View.GONE);
            offerCategoriesButton.setVisibility(View.GONE);
            eventTypesButton.setVisibility(View.GONE);
            eventStatisticsButton.setVisibility(View.GONE);
            yourOffersButton.setVisibility(View.GONE);
            reportsButton.setVisibility(View.GONE);
            commentsButton.setVisibility(View.GONE);
            createServiceButton.setVisibility(View.GONE);

            logOutButton.setVisibility(View.GONE);

            signInButton.setVisibility(View.VISIBLE);
            signUpButton.setVisibility(View.VISIBLE);
        }


        if (user != null) {
            emailText.setText(user.getEmail());

            if (user.getPicture() != null && !user.getPicture().isEmpty()) {
                Glide.with(requireContext())
                        .load(user.getPicture())
                        .placeholder(R.drawable.trumpshot)
//                        .error(R.drawable.proficon) mozda nesto dodamo
                        .into(profileIcon);
            } else {
                profileIcon.setImageResource(R.drawable.trumpshot);
            }

        } else {
            emailText.setText("MAKE AN ACCOUNT");
            profileIcon.setImageResource(R.drawable.trumpshot);
        }

        if (user == null) {
            profileInfoButton.setVisibility(View.GONE);
            favoritesButton.setVisibility(View.GONE);
            scheduleButton.setVisibility(View.GONE);
            notificationsButton.setVisibility(View.GONE);
            logOutButton.setVisibility(View.GONE);

            signInButton.setVisibility(View.VISIBLE);
            signUpButton.setVisibility(View.VISIBLE);

            signInButton.setOnClickListener(v -> {
                dismiss();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            });

            signUpButton.setOnClickListener(v -> {
                dismiss();
                startActivity(new Intent(getActivity(), RegisterActivity.class));
            });
        } else {
            String roleName = user.getRole() != null ? user.getRole().getName() : "";
            boolean isOrganizerOrProvider = roleName.equals("ORGANIZER_ROLE") || roleName.equals("PROVIDER_ROLE");

            profileInfoButton.setVisibility(isOrganizerOrProvider ? View.VISIBLE : View.GONE);
            favoritesButton.setVisibility(isOrganizerOrProvider ? View.VISIBLE : View.GONE);
            scheduleButton.setVisibility(isOrganizerOrProvider ? View.VISIBLE : View.GONE);
            notificationsButton.setVisibility(isOrganizerOrProvider ? View.VISIBLE : View.GONE);
            signInButton.setVisibility(View.GONE);
            signUpButton.setVisibility(View.GONE);
            logOutButton.setVisibility(View.VISIBLE);

            profileInfoButton.setOnClickListener(v -> openProfilePage());
            reportsButton.setOnClickListener(v -> openReportsPage());
            commentsButton.setOnClickListener(v -> openCommentsPage());
            notificationsButton.setOnClickListener(v -> openNotificationsDialog());

            logOutButton.setOnClickListener(v -> {
                logOut();
                dismiss();
            });
        }

        return view;
    }



    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void openProfilePage() {
        dismiss();

        Intent intent = new Intent(getActivity(), ProfilePage.class);
        startActivity(intent);
    }

    private void openNotificationsDialog() {
        dismiss();

        com.example.myapplication.dialog.NotificationsDialog notificationsDialog = new NotificationsDialog(getContext());
        notificationsDialog.show();
    }

    private void openCommentsPage(){
        dismiss();

        Intent intent = new Intent(getActivity(), CommentsActivity.class);
        startActivity(intent);
    }

    private void openReportsPage() {
        dismiss();
        Intent intent = new Intent(getActivity(), ReportsActivity.class);
        startActivity(intent);
    }

    private void openCreateService() {
        dismiss();
        Log.d("gas", "openCreateService: Gas");
        Fragment f = CreateServiceFragment.newInstance(null);
        FragmentTransaction transaction = requireActivity()
                .getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.nav_host_fragment, f);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void logOut() {
        SharedPreferences prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        prefs.edit().clear().apply();

        Toast.makeText(getContext(), "You have been logged out.", Toast.LENGTH_SHORT).show();
        AuthenticationService.logout();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    private AuthentifiedUser getLoggedInUser() {
        SharedPreferences prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        String json = prefs.getString("user", null);

        if (json != null) {
            Gson gson = new Gson();
            return gson.fromJson(json, AuthentifiedUser.class);
        }
        return null;
    }

    private void OpenOfferCategories(){
        dismiss();
        Fragment f = OfferCategoriesFragment.newInstance();
        FragmentTransaction transaction = requireActivity()
                .getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.nav_host_fragment, f);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
