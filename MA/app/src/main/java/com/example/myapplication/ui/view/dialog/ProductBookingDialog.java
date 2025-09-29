package com.example.myapplication.ui.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;
import com.example.myapplication.data.models.dto.eventDTO.MinimalEventDTO;
import com.example.myapplication.data.models.dto.productDTO.MinimalProductDTO;
import com.example.myapplication.data.services.event.EventService;
import com.example.myapplication.data.services.offer.ProductService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductBookingDialog extends DialogFragment {

    private static int productId;

    private Spinner eventSpinner;
    private Button bookButton;

    private final ProductService productService = new ProductService();
    private final EventService eventService = new EventService();
    private List<MinimalEventDTO> events = new ArrayList<>();
    private MinimalEventDTO selectedEvent;

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            DisplayMetrics metrics = new DisplayMetrics();
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

            int width = (int) (metrics.widthPixels * 0.8);
            window.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    public static ProductBookingDialog newInstance(int id) {
        ProductBookingDialog dialog = new ProductBookingDialog();
        Bundle args = new Bundle();
        args.putInt("id", id);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.product_booking, null);

        productId = getArguments().getInt("id");

        eventSpinner = view.findViewById(R.id.spinner_events);
        bookButton = view.findViewById(R.id.button_book_service);

        bookButton.setOnClickListener(v -> onBuyProduct());

        fetchEvents();

        builder.setView(view);
        return builder.create();
    }

    private void onBuyProduct() {
        if (selectedEvent == null) {
            Toast.makeText(requireContext(), "Please select an event", Toast.LENGTH_SHORT).show();
            return;
        }

        productService.buyProduct(productId, selectedEvent.getId()).enqueue(new Callback<MinimalProductDTO>() {
            @Override
            public void onResponse(Call<MinimalProductDTO> call, Response<MinimalProductDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Product bought successfully", Toast.LENGTH_LONG).show();
                    dismiss();
                } else {
                    Toast.makeText(requireContext(), "Buying failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MinimalProductDTO> call, Throwable t) {
                Toast.makeText(requireContext(), "Buying failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchEvents() {
        eventService.getEventsForOrganizerUpdated()
                .enqueue(new Callback<List<MinimalEventDTO>>() {
                    @Override
                    public void onResponse(Call<List<MinimalEventDTO>> call, Response<List<MinimalEventDTO>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            events.clear();
                            events.addAll(response.body());
                            populateEventSpinner();
                        } else {
                            Toast.makeText(getContext(), "No events found for this product.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<MinimalEventDTO>> call, Throwable t) {
                        Toast.makeText(getContext(), "Failed to load events: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void populateEventSpinner() {
        ArrayAdapter<MinimalEventDTO> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, events);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventSpinner.setAdapter(adapter);

        eventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedEvent = events.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedEvent = null;
            }
        });
    }
}
