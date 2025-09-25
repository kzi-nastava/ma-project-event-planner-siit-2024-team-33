package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.myapplication.ui.view.dialog.NotificationsDialog;

public class ShowProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_popup, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button notificationsButton = view.findViewById(R.id.notifications_button);

        notificationsButton.setOnClickListener(v -> {
            NotificationsDialog dialog = new NotificationsDialog(requireContext());
            dialog.show();
        });
    }
}
