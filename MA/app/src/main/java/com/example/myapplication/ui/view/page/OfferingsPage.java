package com.example.myapplication.ui.view.page;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.example.myapplication.data.dto.PageResponse;
import com.example.myapplication.data.dto.eventTypeDTO.MinimalEventTypeDTO;
import com.example.myapplication.data.dto.offerDTO.MinimalOfferDTO;
import com.example.myapplication.data.dto.offerDTO.OfferFilterDTO;
import com.example.myapplication.data.models.Availability;
import com.example.myapplication.data.models.OfferType;
import com.example.myapplication.data.services.event.EventTypeService;
import com.example.myapplication.data.services.OfferService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
    private int currentPage = 0;
    private final int pageSize = 5;
    private String mParam1;
    private String mParam2;
    private OfferService offerService;
    private List<MinimalOfferDTO> offers;
    private OfferFilterDTO filter;
    private EventTypeService eventTypeService;

    public OfferingsPage() {
        // Required empty public constructor
    }

    public static OfferingsPage newInstance() {
        OfferingsPage fragment = new OfferingsPage();
        Bundle args = new Bundle();
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

        offerService = new OfferService();
        this.filter = new OfferFilterDTO(false, false, "", "", 0, Availability.AVAILABLE, Collections.emptyList());
        loadAllOffers(view);
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
                        else
                            f = ProductDetailsFragment.newInstance(offer.offerId);
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
        EventTypeService eventTypeService = new EventTypeService();

        eventTypeService.getEventTypes().enqueue(new retrofit2.Callback<List<MinimalEventTypeDTO>>() {
            @Override
            public void onResponse(Call<List<MinimalEventTypeDTO>> call, Response<List<MinimalEventTypeDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MinimalEventTypeDTO> types = filterEventTypesByOffers(response.body());
                    ArrayAdapter<MinimalEventTypeDTO> adapter = new ArrayAdapter<>(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            types
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                } else {
                    Log.e("OfferingsPage", "Failed to load event types: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<MinimalEventTypeDTO>> call, Throwable t) {
                Log.e("OfferingsPage", "Error loading event types: " + t.getMessage());
            }
        });

        BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);

        dialog.setOnShowListener(d -> {
            bottomNav.animate()
                    .translationY(-1800)
                    .setDuration(100)
                    .start();
        });

        dialog.setOnDismissListener(d -> {
            bottomNav.animate()
                    .translationY(0)
                    .setDuration(100)
                    .start();
        });

        Button confirmButton = dialogView.findViewById(R.id.btn_confirm);
        confirmButton.setOnClickListener(v -> {
            handleFilterDialogConfirmation(dialogView);
            dialog.dismiss();
        });
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

    private List<MinimalEventTypeDTO> filterEventTypesByOffers(List<MinimalEventTypeDTO> allTypes) {
        if (offers == null || offers.isEmpty()) return new ArrayList<>();

        List<MinimalEventTypeDTO> filteredTypes = new ArrayList<>();
        for (MinimalEventTypeDTO type : allTypes) {
            boolean typeUsed = false;
            for (MinimalOfferDTO offer : offers) {
                List<MinimalEventTypeDTO> offerTypes = offer.getValidEvents();
                if (offerTypes != null) {
                    for (MinimalEventTypeDTO eventType : offerTypes) {
                        if (eventType.id.equals(type.id)) {
                            typeUsed = true;
                            break;
                        }
                    }
                }
                if (typeUsed) break;
            }
            if (typeUsed) {
                filteredTypes.add(type);
            }
        }

        return filteredTypes;
    }


}
