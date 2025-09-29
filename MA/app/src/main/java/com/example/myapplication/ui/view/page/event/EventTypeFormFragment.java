package com.example.myapplication.ui.view.page.event;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.dto.OfferCategoryDTO.MinimalOfferCategoryDTO;
import com.example.myapplication.data.models.CreateEventTypeDTO;
import com.example.myapplication.data.models.CreatedEventTypeDTO;
import com.example.myapplication.data.models.GetEventTypeDTO;
import com.example.myapplication.data.models.UpdateEventTypeDTO;
import com.example.myapplication.data.models.UpdatedEventTypeDTO;
import com.example.myapplication.data.services.OfferCategoryService;
import com.example.myapplication.data.services.event.EventTypeService;
import com.example.myapplication.ui.viewmodel.events.EventTypeFormViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventTypeFormFragment extends Fragment {
    public abstract class SimpleTextWatcher implements TextWatcher {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void afterTextChanged(Editable s) {}
    }

    private static final String ARG_EVENT_TYPE = "event_type";
    private GetEventTypeDTO eventType;
    private TextInputEditText inputName, inputDescription;
    private Button btnConfirm;

    private EventTypeFormViewModel viewModel;

    public static EventTypeFormFragment newInstance(GetEventTypeDTO eventType) {
        EventTypeFormFragment fragment = new EventTypeFormFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EVENT_TYPE, eventType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventType = (GetEventTypeDTO) getArguments().getSerializable(ARG_EVENT_TYPE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_type_form, container, false);

        RecyclerView rvOfferCategories = view.findViewById(R.id.rvOfferCategories);
        OfferCategoryAdapter adapter = new OfferCategoryAdapter(new ArrayList<>());
        rvOfferCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        rvOfferCategories.setAdapter(adapter);
        inputName = view.findViewById(R.id.inputName);
        inputDescription = view.findViewById(R.id.inputDescription);
        btnConfirm = view.findViewById(R.id.btnConfirm);

        if (eventType != null) {
            inputName.setText(eventType.getName());
            inputName.setEnabled(false);
            inputDescription.setText(eventType.getDescription());
        }

        viewModel = new ViewModelProvider(this).get(EventTypeFormViewModel.class);

        //observing categories
        viewModel.getCategories().observe(getViewLifecycleOwner(), list -> {
            adapter.categories.clear();
            adapter.categories.addAll(list);
            adapter.notifyDataSetChanged();

            if (eventType != null) {
                Set<Integer> selectedIds = eventType.getRecommendedCategories();
                for (MinimalOfferCategoryDTO cat : adapter.categories) {
                    if (selectedIds.contains(cat.id)) adapter.selectedIds.add(cat.id);
                }
                adapter.notifyDataSetChanged();
            }
        });

        //is it successful observing
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        });
        viewModel.getSuccessMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
            }
        });

        //loading categories
        viewModel.loadCategories(eventType, new HashSet<>());

        //error specific observers
        viewModel.getNameError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                inputName.setError(error);
            } else {
                inputName.setError(null);
            }
        });
        viewModel.getDescriptionError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                inputDescription.setError(error);
            } else {
                inputDescription.setError(null);
            }
        });
        inputName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputName.setError(null);
            }
        });
        inputDescription.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputDescription.setError(null);
            }
        });

        //confirm button
        btnConfirm.setOnClickListener(v -> viewModel.confirmEventType(
                eventType,
                inputName.getText().toString().trim(),
                inputDescription.getText().toString().trim(),
                adapter.selectedIds
        ));

        return view;
    }
}