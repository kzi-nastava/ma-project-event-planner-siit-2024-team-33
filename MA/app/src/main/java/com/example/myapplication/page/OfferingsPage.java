package com.example.myapplication.page;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;
import com.example.myapplication.dto.PageResponse;
import com.example.myapplication.dto.eventTypeDTO.MinimalEventTypeDTO;
import com.example.myapplication.dto.offerDTO.MinimalOfferDTO;
import com.example.myapplication.dto.offerDTO.OfferFilterDTO;
import com.example.myapplication.models.Availability;
import com.example.myapplication.models.OfferType;
import com.example.myapplication.services.AuthenticationService;
import com.example.myapplication.services.OfferService;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferingsPage extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private AuthenticationService authService;
    private int currentPage = 0;
    private final int pageSize = 5;
    private String mParam1;
    private String mParam2;
    private OfferService offerService;
    private List<MinimalOfferDTO> offers;
    private OfferFilterDTO filter;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offerings_page, container, false);

        Button createButton = view.findViewById(R.id.offerings_create_button);
        createButton.setOnClickListener(v -> {
            Fragment f = new CreateServiceFragment();
            FragmentTransaction transaction = requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction();

            transaction.replace(R.id.nav_host_fragment, f);
            transaction.commit();
        });

        Button filterButton = view.findViewById(R.id.offering_filters_button);
        if (filterButton != null) {
            filterButton.setOnClickListener(v -> showFilterDialog());
        } else {
            Log.e("OfferingsPage", "Filter button not found in layout");
        }
        Button btnNext = view.findViewById(R.id.btn_next);
        Button btnPrev = view.findViewById(R.id.btn_previous);

        btnNext.setOnClickListener(v -> {
            currentPage++;
            loadAllOffers(view);
        });

        btnPrev.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                loadAllOffers(view);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        authService = new AuthenticationService(requireContext());

        SharedPreferences prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        String token = prefs.getString("jwt", null);

        if (token != null) {
            offerService = new OfferService();
            this.filter = new OfferFilterDTO(false, false, "", "", 0, Availability.AVAILABLE, Collections.emptyList());
            loadAllOffers(view);
        } else {
            Log.w("OffersPage", "JWT token is not yet available, skipping API call.");
        }
    }

    private void loadAllOffers(View view) {
        Call<PageResponse<MinimalOfferDTO>> call  = offerService.getOfferList(this.filter, this.currentPage, this.pageSize);;


        call.enqueue(new Callback<PageResponse<MinimalOfferDTO>>() {
            @Override
            public void onResponse(Call<PageResponse<MinimalOfferDTO>> call, Response<PageResponse<MinimalOfferDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PageResponse<MinimalOfferDTO> page = response.body();
                    List<MinimalOfferDTO> newOffers = page.getContent();

                    if (newOffers != null && !newOffers.isEmpty()) {
                        offers = newOffers;
                        displayAllOffers(view);
                    } else {
                        offers = Collections.emptyList();
                        displayAllOffers(view);
                        Toast.makeText(requireContext(), "No events found for this filter.", Toast.LENGTH_SHORT).show();
                    }

                    boolean isLastPage = page.getNumber() + 1 >= page.getTotalPages();
                    updatePaginationButtons(view, isLastPage);
                }else {
                    Log.e("Offerspage", "Failed to load offers: " + response.code());
                    Toast.makeText(requireContext(), "Failed to load offers.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PageResponse<MinimalOfferDTO>> call, Throwable t) {
                Log.e("Offerspage", "Error loading offers: " + t.getMessage());
                Toast.makeText(requireContext(), "Error loading offers.", Toast.LENGTH_SHORT).show();            }
        });
    }



    private void displayAllOffers(View view) {
        LinearLayout parentLayout = view.findViewById(R.id.offeringCardsPlace);
        LayoutInflater inflater = LayoutInflater.from(getContext());

        parentLayout.removeAllViews();

        if (offers != null && !offers.isEmpty()) {
            for (MinimalOfferDTO offer : offers) {
                View offerView = inflater.inflate(R.layout.offering_card, parentLayout, false);

                TextView itemTitle = offerView.findViewById(R.id.offering_title);
                TextView itemText = offerView.findViewById(R.id.offering_description);
                Button infoButton = offerView.findViewById(R.id.offering_button);

                if (itemTitle != null && itemText != null) {
                    itemTitle.setText(offer.getName());
                    itemText.setText(offer.getDescription());
                    infoButton.setOnClickListener(v -> {
                        Fragment f = null;
                        if(offer.getType() == OfferType.SERVICE)
                            f = ServiceDetailsFragment.newInstance(offer.offerId);
                        FragmentTransaction transaction = requireActivity()
                                .getSupportFragmentManager()
                                .beginTransaction();

                        transaction.replace(R.id.nav_host_fragment, f);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    });
                }

                parentLayout.addView(offerView);
            }
        }
    }

    private void showFilterDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_products_filter, null);
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        dialog.setContentView(dialogView);

        Spinner spinner = dialogView.findViewById(R.id.spinner_event_type);
        //Kad Milos ili ko vec implementira
//        List<MinimalEventTypeDTO> types = ...
//        ArrayAdapter<MinimalEventTypeDTO> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, types);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);

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


        this.filter = new OfferFilterDTO(
                cbShowProducts != null && cbShowProducts.isChecked(),
                cbShowServices != null && cbShowServices.isChecked(),
                etName != null && !etName.getText().toString().isEmpty() ? etName.getText().toString() : "",
                etCategory != null && !etCategory.getText().toString().isEmpty() ? etCategory.getText().toString() : "",
                seekBarPrice != null ? seekBarPrice.getProgress() : 0,
                getAvailabilityFromRadioButton(rgAvailability),
                getSelectedOfferTypes(spinnerOfferType)
        );
        View rootView = getView();
        if (rootView != null) {
            loadAllOffers(rootView);
        }
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

    private void updatePaginationButtons(View view, boolean isLastPage) {
        Button btnNext = view.findViewById(R.id.btn_next);
        Button btnPrev = view.findViewById(R.id.btn_previous);
        TextView pageIndicator = view.findViewById(R.id.page_indicator);

        btnPrev.setEnabled(currentPage > 0);
        btnNext.setEnabled(!isLastPage);

        pageIndicator.setText("Page " + (currentPage + 1));
    }

}
