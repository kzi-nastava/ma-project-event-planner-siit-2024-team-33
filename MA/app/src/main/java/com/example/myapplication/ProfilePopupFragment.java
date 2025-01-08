package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

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

        profileInfoButton.setOnClickListener(v -> openProfilePage());
        favoritesButton.setOnClickListener(v -> dismiss());
        scheduleButton.setOnClickListener(v -> openStuffComments());
        signInButton.setOnClickListener(v -> openStuff());
        notificationsButton.setOnClickListener(v -> openNotificationsDialog());

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
    private void openStuff() {
        dismiss();

        Intent intent = new Intent(getActivity(), ReportsActivity.class);
        startActivity(intent);
    }
}
