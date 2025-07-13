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

import com.bumptech.glide.Glide;
import com.example.myapplication.models.AuthentifiedUser;
import com.example.myapplication.services.AuthenticationService;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;

public class ProfilePopupFragment extends DialogFragment {

    private Button profileInfoButton, favoritesButton, scheduleButton, notificationsButton, signInButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_popup, container, false);

        profileInfoButton = view.findViewById(R.id.profile_info_button);
        favoritesButton = view.findViewById(R.id.favorites_button);
        scheduleButton = view.findViewById(R.id.schedule_button);
        notificationsButton = view.findViewById(R.id.notifications_button);
        signInButton = view.findViewById(R.id.sign_in_button);
        Button signUpButton = view.findViewById(R.id.sign_up_button);
        Button logOutButton = view.findViewById(R.id.log_out_button);

        AuthentifiedUser user = AuthenticationService.getLoggedInUser();
        ShapeableImageView profileIcon = view.findViewById(R.id.profile_icon);
        TextView emailText = view.findViewById(R.id.email_text);

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
            Log.d("GASGASGASGASG",roleName);
            Log.d("GASGASGASGASG",roleName);
            Log.d("GASGASGASGASG",roleName);
            Log.d("GASGASGASGASG",roleName);
            Log.d("GASGASGASGASG",roleName);
            Log.d("GASGASGASGASG",roleName);
            Log.d("IDK",user.getEmail());


            boolean isOrganizerOrProvider = roleName.equals("ORGANIZER_ROLE") || roleName.equals("PROVIDER_ROLE");

            profileInfoButton.setVisibility(isOrganizerOrProvider ? View.VISIBLE : View.GONE);
            favoritesButton.setVisibility(isOrganizerOrProvider ? View.VISIBLE : View.GONE);
            scheduleButton.setVisibility(isOrganizerOrProvider ? View.VISIBLE : View.GONE);
            notificationsButton.setVisibility(isOrganizerOrProvider ? View.VISIBLE : View.GONE);
            signInButton.setVisibility(View.GONE);
            signUpButton.setVisibility(View.GONE);
            logOutButton.setVisibility(View.VISIBLE);

            profileInfoButton.setOnClickListener(v -> openProfilePage());
            favoritesButton.setOnClickListener(v -> openFavoritesPage());
            scheduleButton.setOnClickListener(v -> openStuffComments());
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

        NotificationsDialog notificationsDialog = new NotificationsDialog(getContext());
        notificationsDialog.show();
    }

    private void openStuffComments(){
        dismiss();
        Intent intent = new Intent(getActivity(),CommentsActivity.class);
        startActivity(intent);
    }
    private void openFavoritesPage() {
        dismiss();

        Intent intent = new Intent(getActivity(), ReportsActivity.class);
        startActivity(intent);
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



}
