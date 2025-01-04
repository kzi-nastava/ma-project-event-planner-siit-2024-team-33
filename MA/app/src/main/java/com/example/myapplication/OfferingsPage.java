package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.myapplication.dto.eventDTO.MinimalEventTypeDTO;
import com.example.myapplication.dto.offerDTO.MinimalOfferDTO;
import com.example.myapplication.dto.offerDTO.OfferFilterDTO;
import com.example.myapplication.models.Availability;
import com.example.myapplication.services.OfferService;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferingsPage extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private OfferService offerService;
    private List<MinimalOfferDTO> offers;

    public OfferingsPage() {
        // Required empty public constructor
    }

    public static OfferingsPage newInstance(String param1, String param2) {
        OfferingsPage fragment = new OfferingsPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // Ensure OfferService is initialized
        offerService = new OfferService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offerings_page, container, false);

        // Ensure the filter button is not null
        Button filterButton = view.findViewById(R.id.offering_filters_button);
        if (filterButton != null) {
            filterButton.setOnClickListener(v -> showFilterDialog());
        } else {
            Log.e("OfferingsPage", "Filter button not found in layout");
        }

        loadAllOffers(view);
        return view;
    }

    private void loadAllOffers(View view) {
        // Make sure offerService is initialized before calling
        if (offerService != null) {
            offerService.getAllOffers(2).enqueue(new Callback<List<MinimalOfferDTO>>() {
                @Override
                public void onResponse(Call<List<MinimalOfferDTO>> call, Response<List<MinimalOfferDTO>> response) {
                    Log.d("RetrofitDebug", "onResponse called");

                    if (response.isSuccessful() && response.body() != null) {
                        offers = response.body();
                        displayAllOffers(view);
                    } else {
                        Log.e("RetrofitResponse", "Response unsuccessful. Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<List<MinimalOfferDTO>> call, Throwable t) {
                    Log.e("RetrofitError", "Failed to fetch offers");
                    Log.e("RetrofitError", "Error Message: " + t.getMessage(), t);
                }
            });
        } else {
            Log.e("OfferingsPage", "OfferService is not initialized");
        }
    }

    private void displayAllOffers(View view) {
        LinearLayout parentLayout = view.findViewById(R.id.offeringCardsPlace);
        LayoutInflater inflater = LayoutInflater.from(getContext());

        parentLayout.removeAllViews();

        if (offers != null && parentLayout != null) {
            for (MinimalOfferDTO offer : offers) {
                View offerView = inflater.inflate(R.layout.offering_card, parentLayout, false);

                TextView itemTitle = offerView.findViewById(R.id.offering_title);
                TextView itemText = offerView.findViewById(R.id.offering_description);

                if (itemTitle != null && itemText != null) {
                    itemTitle.setText(offer.name);
                    itemText.setText(offer.description);
                }

                parentLayout.addView(offerView);
            }
        }
    }

    private void showFilterDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_products_filter, null);
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        dialog.setContentView(dialogView);

        Button confirmButton = dialogView.findViewById(R.id.btn_confirm);
        if (confirmButton != null) {
            confirmButton.setOnClickListener(v -> {
                handleFilterDialogConfirmation(dialogView);
                dialog.dismiss();
            });
        } else {
            Log.e("OfferingsPage", "Confirm button not found in filter dialog layout");
        }

        dialog.show();
    }

    private void handleFilterDialogConfirmation(View dialogView) {
        EditText etName = dialogView.findViewById(R.id.et_name);
        EditText etCategory = dialogView.findViewById(R.id.et_category);
        SeekBar seekBarPrice = dialogView.findViewById(R.id.seekbar_price);
        RadioGroup rgAvailability = dialogView.findViewById(R.id.rg_availability);
        Spinner spinnerOfferType = dialogView.findViewById(R.id.spinner_event_type);
        CheckBox cbShowProducts = dialogView.findViewById(R.id.cb_show_products);
        CheckBox cbShowServices = dialogView.findViewById(R.id.cb_show_services);

        OfferFilterDTO filter = new OfferFilterDTO();

        filter.name = etName != null && !etName.getText().toString().isEmpty() ? etName.getText().toString() : "";
        filter.category = etCategory != null && !etCategory.getText().toString().isEmpty() ? etCategory.getText().toString() : "";
        filter.lowestPrice = seekBarPrice != null ? seekBarPrice.getProgress() : 0;
        filter.isAvailable = getAvailabilityFromRadioButton(rgAvailability);
        filter.isProduct = cbShowProducts != null && cbShowProducts.isChecked();
        filter.isService = cbShowServices != null && cbShowServices.isChecked();

        filter.eventTypes = getSelectedOfferTypes(spinnerOfferType);

        Integer id = 2;

        getFilteredOffers(filter, id);
    }

    private Availability getAvailabilityFromRadioButton(RadioGroup rgAvailability) {
        int selectedId = rgAvailability.getCheckedRadioButtonId();

        if (selectedId != -1) {
            RadioButton selectedRadioButton = rgAvailability.findViewById(selectedId);
            if (selectedRadioButton != null) {
                switch (selectedRadioButton.getText().toString()) {
                    case "Available":
                        return Availability.AVAILABLE;
                    case "Unavailable":
                        return Availability.UNAVAILABLE;
                    default:
                        return null;
                }
            }
        }
        return null;
    }

    private List<Integer> getSelectedOfferTypes(Spinner spinner) {
        MinimalEventTypeDTO selectedType = (MinimalEventTypeDTO) spinner.getSelectedItem();
        List<Integer> selectedTypes = new ArrayList<>();
        if (selectedType != null) {
            selectedTypes.add(selectedType.id);
        }
        return selectedTypes;
    }

    private void getFilteredOffers(OfferFilterDTO filter, Integer id) {
        Call<List<MinimalOfferDTO>> call = offerService.getOfferList(filter.isProduct, filter.isService, filter.name, filter.category, filter.lowestPrice, filter.isAvailable, filter.eventTypes, id);

        call.enqueue(new Callback<List<MinimalOfferDTO>>() {
            @Override
            public void onResponse(Call<List<MinimalOfferDTO>> call, Response<List<MinimalOfferDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    offers = response.body();
                    View rootView = getView();
                    if (rootView != null) {
                        displayAllOffers(rootView);
                    }
                } else {
                    Log.e("RetrofitError", "Filtering failed. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<MinimalOfferDTO>> call, Throwable t) {
                Log.e("RetrofitError", "Failed to fetch filtered offers: " + t.getMessage());
            }
        });
    }
}
