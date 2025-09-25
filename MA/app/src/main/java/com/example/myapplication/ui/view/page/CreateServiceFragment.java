package com.example.myapplication.ui.view.page;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.ui.view.component.ImageCarouselFragment;
import com.example.myapplication.data.dto.OfferCategoryDTO.MinimalOfferCategoryDTO;
import com.example.myapplication.data.dto.eventTypeDTO.MinimalEventTypeDTO;
import com.example.myapplication.data.dto.serviceDTO.PostServiceDTO;
import com.example.myapplication.data.dto.serviceDTO.PutServiceDTO;
import com.example.myapplication.data.dto.serviceDTO.ServiceDetailsDTO;
import com.example.myapplication.data.models.Availability;
import com.example.myapplication.data.services.EventTypeService;
import com.example.myapplication.data.services.OfferCategoryService;
import com.example.myapplication.data.services.ServiceService;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateServiceFragment extends Fragment {
    private static final int PICK_IMAGES_REQUEST = 1;
    private static final EventTypeService eventTypesService = new EventTypeService();
    private static final OfferCategoryService offerCategoryService = new OfferCategoryService();
    private static final ServiceService serviceService = new ServiceService();

    private Spinner categorySpinner;
    private EditText categoryNameInput, categoryDescriptionInput;
    private EditText nameInput, priceInput, descriptionInput, discountInput;
    private EditText reservationHoursInput, cancellationHoursInput;
    private EditText minDurationInput, maxDurationInput;
    private CheckBox isVisibleCheckbox, isAvailableCheckbox, isAutomaticCheckbox;
    private Button selectImagesButton, submitButton;
    private LinearLayout eventTypesLayout;

    private List<String> imageBase64List = new ArrayList<>();
    private List<Integer> selectedEventTypeIds = new ArrayList<>();

    //If null -> we are creating a new service
    //If non-null -> we are editing an existing service
    private Integer serviceId = null;
    private Boolean editMode = false;

    public static CreateServiceFragment newInstance(Integer optionalId) {
        Bundle args = new Bundle();
        if(optionalId != null)
            args.putInt("id", optionalId);
        CreateServiceFragment fragment = new CreateServiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey("id")) {
            serviceId = getArguments().getInt("id");
            editMode = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_service, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categorySpinner = view.findViewById(R.id.spinner_category);
        categoryNameInput = view.findViewById(R.id.input_category_name);
        categoryDescriptionInput = view.findViewById(R.id.input_category_description);
        nameInput = view.findViewById(R.id.input_name);
        priceInput = view.findViewById(R.id.input_price);
        descriptionInput = view.findViewById(R.id.input_description);
        discountInput = view.findViewById(R.id.input_discount);
        reservationHoursInput = view.findViewById(R.id.input_reservation_hours);
        cancellationHoursInput = view.findViewById(R.id.input_cancellation_hours);
        minDurationInput = view.findViewById(R.id.input_min_duration);
        maxDurationInput = view.findViewById(R.id.input_max_duration);
        isVisibleCheckbox = view.findViewById(R.id.checkbox_visible);
        isAvailableCheckbox = view.findViewById(R.id.checkbox_available);
        isAutomaticCheckbox = view.findViewById(R.id.checkbox_automatic);
        selectImagesButton = view.findViewById(R.id.button_select_images);
        submitButton = view.findViewById(R.id.button_submit);
        eventTypesLayout = view.findViewById(R.id.layout_event_types);

        selectImagesButton.setOnClickListener(v -> openImagePicker());

        submitButton.setOnClickListener(v -> submitForm());

        List<MinimalOfferCategoryDTO> categoryList = new ArrayList<>();
        MinimalOfferCategoryDTO placeholder = new MinimalOfferCategoryDTO();
        placeholder.id = null;
        placeholder.name = "Select a category...";
        categoryList.add(placeholder);

        if(!editMode){
            offerCategoryService.getAvailableCategories().enqueue(new Callback<List<MinimalOfferCategoryDTO>>() {
                @Override
                public void onResponse(Call<List<MinimalOfferCategoryDTO>> call, Response<List<MinimalOfferCategoryDTO>> response) {
                    if(response.isSuccessful() && response.body() != null){
                        categoryList.addAll(response.body());

                        ArrayAdapter<MinimalOfferCategoryDTO> adapter = new ArrayAdapter<>(
                                getContext(), android.R.layout.simple_spinner_item, categoryList
                        ) {
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                View view = super.getView(position, convertView, parent);
                                TextView text = view.findViewById(android.R.id.text1);
                                text.setText(getItem(position).name);
                                return view;
                            }

                            @Override
                            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView text = view.findViewById(android.R.id.text1);
                                text.setText(getItem(position).name);
                                return view;
                            }
                        };

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        categorySpinner.setAdapter(adapter);
                        categorySpinner.setSelection(0);
                    }
                }

                @Override
                public void onFailure(Call<List<MinimalOfferCategoryDTO>> call, Throwable t) {
                    Toast.makeText(getContext(), "Failed getting categories", Toast.LENGTH_SHORT).show();
                }
            });
            categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position > 0){
                        categoryNameInput.setVisibility(View.GONE);
                        categoryDescriptionInput.setVisibility(View.GONE);
                    }else{
                        categoryNameInput.setVisibility(View.VISIBLE);
                        categoryDescriptionInput.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    categoryNameInput.setVisibility(View.VISIBLE);
                    categoryDescriptionInput.setVisibility(View.VISIBLE);
                }
            });
            eventTypesService.getEventTypes().enqueue(new Callback<List<MinimalEventTypeDTO>>() {
                @Override
                public void onResponse(Call<List<MinimalEventTypeDTO>> call, Response<List<MinimalEventTypeDTO>> response) {
                    if(response.isSuccessful() && response.body() != null){
                        renderEventTypeCheckboxes(response.body(), null);
                    }
                    else
                        Toast.makeText(getContext(), "Error :(", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<List<MinimalEventTypeDTO>> call, Throwable t) {
                    Toast.makeText(getContext(), "Error :(", Toast.LENGTH_SHORT).show();
                }
            });
        } else{
            categorySpinner.setVisibility(View.GONE);
            categoryNameInput.setVisibility(View.GONE);
            categoryDescriptionInput.setVisibility(View.GONE);

            serviceService.getServiceDetails(serviceId).enqueue(new Callback<ServiceDetailsDTO>() {
                @Override
                public void onResponse(Call<ServiceDetailsDTO> call, Response<ServiceDetailsDTO> response) {
                    if(response.isSuccessful() && response.body() != null){
                        ServiceDetailsDTO data = response.body();

                        nameInput.setText(data.name);
                        priceInput.setText(data.price.toString());
                        descriptionInput.setText(data.description.toString());
                        discountInput.setText(data.discount.toString());
                        reservationHoursInput.setText(data.reservationInHours.toString());
                        cancellationHoursInput.setText(data.cancellationInHours.toString());
                        minDurationInput.setText(data.minLengthInMins.toString());
                        maxDurationInput.setText(data.maxLengthInMins.toString());
                        isVisibleCheckbox.setChecked(data.isVisible);
                        isAvailableCheckbox.setChecked(data.isAvailable);
                        isAutomaticCheckbox.setChecked(data.isAutomatic);

                        selectedImages = new ArrayList<>(data.picturesDataURI);
                        ImageCarouselFragment carouselFrag = ImageCarouselFragment.newInstance(new ArrayList<>(selectedImages));
                        getChildFragmentManager()
                                .beginTransaction()
                                .replace(R.id.carouselContainer, carouselFrag)
                                .commit();

                        eventTypesService.getEventTypes().enqueue(new Callback<List<MinimalEventTypeDTO>>() {
                            @Override
                            public void onResponse(Call<List<MinimalEventTypeDTO>> call, Response<List<MinimalEventTypeDTO>> response) {
                                if(response.isSuccessful() && response.body() != null){
                                    renderEventTypeCheckboxes(response.body(), data.validEventCategories);
                                }
                                else
                                    Toast.makeText(getContext(), "Error :(", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<List<MinimalEventTypeDTO>> call, Throwable t) {
                                Toast.makeText(getContext(), "Error :(", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                        Toast.makeText(getContext(), "Error loading service :(", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ServiceDetailsDTO> call, Throwable t) {
                    Toast.makeText(getContext(), "Error loading service :(", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private String encodeImageToBase64(Uri imageUri) {
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(imageUri);
            byte[] bytes = IOUtils.toByteArray(inputStream); // Apache Commons IO
            return Base64.encodeToString(bytes, Base64.NO_WRAP);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    ArrayList<String> selectedImages;
    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    ClipData clipData = result.getData().getClipData();
                    List<String> base64Images = new ArrayList<>();

                    if (clipData != null) {
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            Uri imageUri = clipData.getItemAt(i).getUri();
                            base64Images.add( "data:image/png;base64," + encodeImageToBase64(imageUri));
                        }
                    } else {
                        Uri imageUri = result.getData().getData();
                        base64Images.add(encodeImageToBase64(imageUri));
                    }

                    selectedImages = new ArrayList<>(base64Images);
                    Log.w("IMG", "SelectedImages: " + selectedImages.get(0));
                    ImageCarouselFragment carouselFrag = ImageCarouselFragment.newInstance(new ArrayList<>(selectedImages));

                    getChildFragmentManager()
                            .beginTransaction()
                            .replace(R.id.carouselContainer, carouselFrag)
                            .commit();
                }
            });

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        imagePickerLauncher.launch(Intent.createChooser(intent, "Select Images"));
    }

    private void renderEventTypeCheckboxes(List<MinimalEventTypeDTO> eventTypes, @Nullable List<MinimalEventTypeDTO> checkedTypes) {
        LinearLayout container = requireView().findViewById(R.id.layout_event_types);
        container.removeAllViews();

        for (MinimalEventTypeDTO eventType : eventTypes) {
            CheckBox checkBox = new CheckBox(requireContext());
            checkBox.setText(eventType.name);
            checkBox.setTag(eventType.id);
            if(checkedTypes != null)
                checkBox.setChecked(checkedTypes.stream().anyMatch(o -> o.name.equals(eventType.name)));

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                Integer id = (Integer) buttonView.getTag();
                if (isChecked) {
                    selectedEventTypeIds.add(id);
                } else {
                    selectedEventTypeIds.remove(id);
                }
            });

            container.addView(checkBox);
        }
    }

    private boolean validateEventTypeSelection() {
        if (selectedEventTypeIds.isEmpty()) {
            Toast.makeText(requireContext(), "Please select at least one event type.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void submitForm() {
        if(!editMode)
            PostData();
        else
            PutData();
    }

    private void PutData(){
        Boolean hasError = false;

        if(!validateEventTypeSelection())
            return;

        String name = nameInput.getText().toString().trim();
        String priceStr = priceInput.getText().toString().trim();
        String discountStr = discountInput.getText().toString().trim();
        String description = descriptionInput.getText().toString();

        if(TextUtils.isEmpty(reservationHoursInput.getText())){
            reservationHoursInput.setError("Can't be empty");
            hasError = true;
        }
        if(TextUtils.isEmpty(cancellationHoursInput.getText())){
            cancellationHoursInput.setError("Can't be empty");
            hasError = true;
        }
        if(TextUtils.isEmpty(minDurationInput.getText())){
            minDurationInput.setError("Can't be empty");
            hasError = true;
        }
        if(TextUtils.isEmpty(maxDurationInput.getText())){
            maxDurationInput.setError("Can't be empty");
            hasError = true;
        }


        Availability availability;
        if(!isVisibleCheckbox.isChecked())
            availability = Availability.INVISIBLE;
        else
            availability = (isAvailableCheckbox.isChecked())? Availability.AVAILABLE : Availability.CURRENTLY_UNAVAILABLE;

        if (TextUtils.isEmpty(name)) {
            nameInput.setError("Name is required");
            hasError = true;
        }

        double price = Double.parseDouble(priceStr);
        double discount = TextUtils.isEmpty(discountStr) ? 0.0 : Double.parseDouble(discountStr);
        if (price <= 0) {
            priceInput.setError("The price must be positive");
            hasError = true;
        }
        if (discount > price) {
            discountInput.setError("The discount is too big");
            hasError = true;
        }

        if(hasError) return;

        Integer reservation = Integer.parseInt(reservationHoursInput.getText().toString());
        Integer cancellation = Integer.parseInt(cancellationHoursInput.getText().toString());
        Integer minDuration = Integer.parseInt(minDurationInput.getText().toString());
        Integer maxDuration = Integer.parseInt(maxDurationInput.getText().toString());
        Boolean isAutomatic = isAutomaticCheckbox.isChecked();

        PutServiceDTO data = new PutServiceDTO();
        data.name = name;
        data.description = description;
        data.price = price;
        data.discount = discount;
        data.availability = availability;
        data.reservationInHours = reservation;
        data.cancellationInHours = cancellation;
        data.minDurationInMins = minDuration;
        data.maxDurationInMins = maxDuration;
        data.isAutomatic = isAutomatic;
        data.picturesDataURI = selectedImages;
        data.validEventTypeIDs = selectedEventTypeIds;

        serviceService.editService(serviceId, data).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), "SUCCESS!", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStackImmediate();
                }
                else
                    Toast.makeText(getContext(), "Error :(", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void PostData(){
        Boolean hasError = false;

        if(!validateEventTypeSelection())
            return;

        MinimalOfferCategoryDTO category = (MinimalOfferCategoryDTO)categorySpinner.getSelectedItem();
        String categoryName = categoryNameInput.getText().toString().trim();
        String categoryDescription = categoryDescriptionInput.getText().toString().trim();

        String name = nameInput.getText().toString().trim();
        String priceStr = priceInput.getText().toString().trim();
        String discountStr = discountInput.getText().toString().trim();
        String description = descriptionInput.getText().toString();

        if(TextUtils.isEmpty(reservationHoursInput.getText())){
            reservationHoursInput.setError("Can't be empty");
            hasError = true;
        }
        if(TextUtils.isEmpty(cancellationHoursInput.getText())){
            cancellationHoursInput.setError("Can't be empty");
            hasError = true;
        }
        if(TextUtils.isEmpty(minDurationInput.getText())){
            minDurationInput.setError("Can't be empty");
            hasError = true;
        }
        if(TextUtils.isEmpty(maxDurationInput.getText())){
            maxDurationInput.setError("Can't be empty");
            hasError = true;
        }


        Availability availability;
        if(!isVisibleCheckbox.isChecked())
            availability = Availability.INVISIBLE;
        else
            availability = (isAvailableCheckbox.isChecked())? Availability.AVAILABLE : Availability.CURRENTLY_UNAVAILABLE;

        if (TextUtils.isEmpty(name)) {
            nameInput.setError("Name is required");
            hasError = true;
        }

        double price = Double.parseDouble(priceStr);
        double discount = TextUtils.isEmpty(discountStr) ? 0.0 : Double.parseDouble(discountStr);
        if (price <= 0) {
            priceInput.setError("The price must be positive");
            hasError = true;
        }
        if (discount > price) {
            discountInput.setError("The discount is too big");
            hasError = true;
        }

        if(hasError) return;

        Integer reservation = Integer.parseInt(reservationHoursInput.getText().toString());
        Integer cancellation = Integer.parseInt(cancellationHoursInput.getText().toString());
        Integer minDuration = Integer.parseInt(minDurationInput.getText().toString());
        Integer maxDuration = Integer.parseInt(maxDurationInput.getText().toString());
        Boolean isAutomatic = isAutomaticCheckbox.isChecked();

        PostServiceDTO data = new PostServiceDTO();
        if(category.id != null)
            data.categoryID = category.id;
        else{
            data.categoryName = categoryName;
            data.categoryDescription = categoryDescription;
        }
        data.name = name;
        data.price = price;
        data.discount = discount;
        data.minDurationInMins = minDuration;
        data.maxDurationInMins = maxDuration;
        data.description = description;
        data.reservationInHours = reservation;
        data.cancellationInHours = cancellation;
        data.isAutomatic = isAutomatic;
        data.availability = availability;
        data.validEventTypeIDs = selectedEventTypeIds;
        data.picturesDataURI = selectedImages;
        serviceService.createService(data).enqueue(new Callback<ServiceDetailsDTO>() {
            @Override
            public void onResponse(Call<ServiceDetailsDTO> call, Response<ServiceDetailsDTO> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), "SUCCESS!", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStackImmediate();
                }
                else
                    Toast.makeText(getContext(), "Error :(", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ServiceDetailsDTO> call, Throwable t) {
                Toast.makeText(getContext(), "Error :(", Toast.LENGTH_SHORT).show();

            }
        });
    }
}