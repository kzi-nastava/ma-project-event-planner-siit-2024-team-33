package com.example.myapplication.ui.view.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;
import com.example.myapplication.data.models.dto.serviceReservationDTO.GetServiceReservationDTO;
import com.example.myapplication.data.services.offer.ServiceReservationService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelReservationDialog extends DialogFragment {

    private Spinner reservationSpinner;
    private TextView errorMessage;
    private Button cancelReservationButton;
    private List<GetServiceReservationDTO> reservations;
    private Integer serviceId;

    private final ServiceReservationService reservationService = new ServiceReservationService();

    public CancelReservationDialog() {
        // Required empty constructor
    }
    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
    }


    public static CancelReservationDialog newInstance(Integer serviceId) {
        CancelReservationDialog dialog = new CancelReservationDialog();
        Bundle args = new Bundle();
        args.putInt("service_id", serviceId);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_cancel_reservation, null);

        reservationSpinner = view.findViewById(R.id.reservation_spinner);
        errorMessage = view.findViewById(R.id.error_message);
        cancelReservationButton = view.findViewById(R.id.btn_cancel_reservation);

        if (getArguments() != null) {
            serviceId = getArguments().getInt("service_id");
        }

        loadReservations(view);

        cancelReservationButton.setOnClickListener(v -> {
            if (reservations == null || reservations.isEmpty()) return;

            int selectedIndex = reservationSpinner.getSelectedItemPosition();
            if (selectedIndex < 0) {
                showError("Please select a reservation.");
                return;
            }

            int reservationId = reservations.get(selectedIndex).getReservationId();

            reservationService
                    .cancelReservation(serviceId, reservationId)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                dismiss();
                            } else {
                                showError("Failed to cancel reservation.");
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            showError("Network error: " + t.getMessage());
                        }
                    });
        });

        return new AlertDialog.Builder(requireContext())
                .setView(view)
                .create();
    }

    private void loadReservations(View view) {
        reservationService.getMyReservationsForService(serviceId).enqueue(new Callback<List<GetServiceReservationDTO>>() {
            @Override
            public void onResponse(Call<List<GetServiceReservationDTO>> call, Response<List<GetServiceReservationDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    reservations = response.body();

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    requireContext(),
                                    android.R.layout.simple_spinner_item,
                                    reservations.stream()
                                            .map(r -> r.getEventName() + " – " + r.getReservationDate() + " " + r.getStartTime() + " - " + r.getEndTime())
                                            .toArray(String[]::new)
                            );
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            reservationSpinner.setAdapter(adapter);
                        });
                    }
                } else {
                    showError("No reservations available.");
                    cancelReservationButton.setEnabled(false);
                }
            }

            @Override
            public void onFailure(Call<List<GetServiceReservationDTO>> call, Throwable t) {
                showError("Error loading reservations: " + t.getMessage());
                cancelReservationButton.setEnabled(false);
            }
        });
    }

    private void showError(String message) {
        if (errorMessage != null) {
            errorMessage.setText(message);
            errorMessage.setVisibility(View.VISIBLE);
        }
    }
}
