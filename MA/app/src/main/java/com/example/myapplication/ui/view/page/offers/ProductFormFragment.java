package com.example.myapplication.ui.view.page.offers;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.models.dto.OfferCategoryDTO.MinimalOfferCategoryDTO;
import com.example.myapplication.data.models.dto.eventTypeDTO.MinimalEventTypeDTO;
import com.example.myapplication.data.models.Availability;
import com.example.myapplication.data.models.dto.CreateProductDTO;
import com.example.myapplication.data.models.dto.ProviderProductDTO;
import com.example.myapplication.data.models.dto.UpdateProductDTO;
import com.example.myapplication.data.services.offer.OfferCategoryService;
import com.example.myapplication.data.services.offer.ProductService;
import com.example.myapplication.data.services.event.EventTypeService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductFormFragment extends Fragment {

    private static final String ARG_PRODUCT = "arg_product";
    private ProviderProductDTO product;
    private TextInputEditText inputName, inputDescription, inputPrice, inputDiscount;
    //event type
    private RecyclerView rvEventTypes;
    private EventTypeChecksAdapter adapter;
    //radio availability
    private RadioGroup radioGroupAvailability;
    //offer categories
    private Spinner spinnerCategories;
    private TextView spinnerLabel;
    private List<MinimalOfferCategoryDTO> offerCategories = new ArrayList<>();
    //confirmation
    private Button btnConfirm;

    private final ProductService productService = new ProductService();

    public ProductFormFragment() {}

    public static ProductFormFragment newInstance() {
        return new ProductFormFragment();
    }

    public static ProductFormFragment newInstance(ProviderProductDTO product) {
        ProductFormFragment fragment = new ProductFormFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCT, product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(ARG_PRODUCT)) {
            product = (ProviderProductDTO) getArguments().getSerializable(ARG_PRODUCT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_form, container, false);

        rvEventTypes = view.findViewById(R.id.rvEventTypes);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        inputName = view.findViewById(R.id.inputName);
        inputDescription = view.findViewById(R.id.inputDescription);
        inputDiscount = view.findViewById(R.id.inputDiscount);
        inputPrice = view.findViewById(R.id.inputPrice);
        radioGroupAvailability = view.findViewById(R.id.radioGroupAvailability);

        //setting up offer categories
        spinnerCategories = view.findViewById(R.id.spinnerOfferCategories);
        spinnerLabel = view.findViewById(R.id.spinnerOfferCategoriesText);
        fetchOfferCategories();

        //setting event types up
        rvEventTypes.setLayoutManager(new LinearLayoutManager(requireContext()));
        fetchActiveEventTypes();

        if (product != null) {
            fillFormForEdit();
        }

        btnConfirm.setOnClickListener(v -> handleConfirm());

        return view;
    }


    //getting event types and filling out if edit option is on
    private void fetchActiveEventTypes() {
        new EventTypeService().getEventTypes()
                .enqueue(new Callback<List<MinimalEventTypeDTO>>() {
                    @Override
                    public void onResponse(Call<List<MinimalEventTypeDTO>> call, Response<List<MinimalEventTypeDTO>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            //if edit check already checked event types
                            List<Integer> preSelectedIds = new ArrayList<>();
                            if (product != null && product.getEventTypeIds() != null) {
                                for (Integer type : product.getEventTypeIds()) {
                                    preSelectedIds.add(type);
                                }
                            }

                            adapter = new EventTypeChecksAdapter(response.body(), preSelectedIds);
                            rvEventTypes.setAdapter(adapter);
                        } else {
                            Toast.makeText(requireContext(), "Failed to load event types", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<MinimalEventTypeDTO>> call, Throwable t) {
                        Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //getting offer categories
    private void fetchOfferCategories() {
        new OfferCategoryService().getAvailableCategories().enqueue(new Callback<List<MinimalOfferCategoryDTO>>() {
            @Override
            public void onResponse(Call<List<MinimalOfferCategoryDTO>> call, Response<List<MinimalOfferCategoryDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    offerCategories.clear();

                    for (MinimalOfferCategoryDTO category : response.body()) {
                        if ("PRODUCT".equalsIgnoreCase(category.type.name())) {
                            offerCategories.add(category);
                        }
                    }

                    //getting name for spinner display
                    List<String> categoryNames = new ArrayList<>();
                    for (MinimalOfferCategoryDTO category : offerCategories) {
                        categoryNames.add(category.name);
                    }

                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            categoryNames
                    );
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategories.setAdapter(spinnerAdapter);

                    if (product == null)
                        spinnerCategories.setSelection(0);

                } else {
                    Toast.makeText(requireContext(), "Failed to load categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MinimalOfferCategoryDTO>> call, Throwable t) {
                Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillFormForEdit() {
        inputName.setText(product.getName());
        inputDescription.setText(product.getDescription());
        inputPrice.setText(String.valueOf(product.getPrice()));
        inputDiscount.setText(String.valueOf(product.getDiscount()));
        spinnerLabel.setVisibility(View.GONE);
        spinnerCategories.setVisibility(View.GONE);

        //setting availability
        switch (product.getAvailability()) {
            case AVAILABLE: ((RadioButton) radioGroupAvailability.findViewById(R.id.radioAvailable)).setChecked(true); break;
            case UNAVAILABLE: ((RadioButton) radioGroupAvailability.findViewById(R.id.radioUnavailable)).setChecked(true); break;
            case CURRENTLY_UNAVAILABLE: ((RadioButton) radioGroupAvailability.findViewById(R.id.radioCurrentlyUnavailable)).setChecked(true); break;
            case INVISIBLE: ((RadioButton) radioGroupAvailability.findViewById(R.id.radioInvisible)).setChecked(true); break;
        }
    }


    private void handleConfirm() {
        boolean isValid = true;
        String name = inputName.getText().toString().trim();
        String description = inputDescription.getText().toString().trim();
        double price = 0;
        double discount = 0;

        int selectedCategoryPos = spinnerCategories.getSelectedItemPosition();
        Integer categoryId = selectedCategoryPos >= 0 ? offerCategories.get(selectedCategoryPos).id : null;

        Availability availability = Availability.AVAILABLE;
        int checkedId = radioGroupAvailability.getCheckedRadioButtonId();
        if (checkedId == R.id.radioAvailable) availability = Availability.AVAILABLE;
        else if (checkedId == R.id.radioUnavailable) availability = Availability.UNAVAILABLE;
        else if (checkedId == R.id.radioCurrentlyUnavailable) availability = Availability.CURRENTLY_UNAVAILABLE;
        else if (checkedId == R.id.radioInvisible) availability = Availability.INVISIBLE;

        List<Integer> selectedEventTypeIds = adapter.getSelectedEventTypes()
                .stream()
                .map(MinimalEventTypeDTO::getId)
                .collect(Collectors.toList());

        //validation check
        if (TextUtils.isEmpty(name) || !Pattern.matches("^.{1,50}$", name)) {
            inputName.setError("Name must be between 1 and 50 characters.");
            isValid = false;
        } else {
            inputName.setError(null);
        }

        if (TextUtils.isEmpty(description) || !Pattern.matches("^.{1,250}$", description)) {
            inputDescription.setError("Description must be between 1 and 250 characters.");
            isValid = false;
        } else {
            inputDescription.setError(null);
        }

        String priceText = inputPrice.getText().toString().trim();
        String discountText = inputDiscount.getText().toString().trim();
        try {
            price = Double.parseDouble(priceText);
            if (price < 0) {
                inputPrice.setError("Price must be >= 0.");
                isValid = false;
            } else {
                inputPrice.setError(null);
            }
        } catch (NumberFormatException e) {
            inputPrice.setError("Price must be a number.");
            isValid = false;
        }

        try {
            discount = Double.parseDouble(discountText);
            if (discount > price) {
                inputDiscount.setError("Discount must be less than or equal to price.");
                isValid = false;
            } else {
                inputDiscount.setError(null);
            }
        } catch (NumberFormatException e) {
            inputDiscount.setError("Discount must be a number.");
            isValid = false;
        }
        if (!isValid) return;

        if (product != null) {
            //update
            UpdateProductDTO dto = new UpdateProductDTO();
            dto.setName(name);
            dto.setDescription(description);
            dto.setPrice(price);
            dto.setDiscount(discount);
            dto.setPictures(product.getPictures());
            dto.setAvailability(availability);
            dto.setEventTypeIds(selectedEventTypeIds);

            productService.updateProduct(product.getId(), dto).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(requireContext(), "Product updated", Toast.LENGTH_SHORT).show();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(requireContext(), "Failed to update product", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            //create
            CreateProductDTO dto = new CreateProductDTO();
            dto.setName(name);
            dto.setDescription(description);
            dto.setPrice(price);
            dto.setDiscount(discount);
            dto.setAvailability(availability);
            dto.setExistingCategoryId(categoryId);
            dto.setEventTypeIds(selectedEventTypeIds);
            dto.setPending(false);

            productService.createProduct(dto).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(requireContext(), "Product created", Toast.LENGTH_SHORT).show();
                        requireActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(requireContext(), "Failed to create product", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}