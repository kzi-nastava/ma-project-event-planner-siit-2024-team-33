package com.example.myapplication.ui.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;
import com.example.myapplication.data.models.dto.OfferCategoryDTO.HandleSuggestionDTO;
import com.example.myapplication.data.models.dto.OfferCategoryDTO.MinimalOfferCategoryDTO;
import com.example.myapplication.data.services.offer.OfferCategoryService;
import com.example.myapplication.utils.OnDialogDismissListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HandleCategoryDialog extends DialogFragment {
    private static final String ARG_CATEGORY = "arg_category";

    private static final OfferCategoryService categoryService = new OfferCategoryService();

    private Spinner categorySpinner;
    private EditText nameInput;
    private EditText descInput;
    private TextView errorText;
    private Button submitButton;

    private MinimalOfferCategoryDTO category;
    private Boolean isAccepted;
    private List<MinimalOfferCategoryDTO> categories = new ArrayList<>();

    public static HandleCategoryDialog newInstance(@Nullable MinimalOfferCategoryDTO dto, Boolean isAccepted) {
        HandleCategoryDialog fragment = new HandleCategoryDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CATEGORY, dto);
        args.putBoolean("accepted", isAccepted);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.category_suggestion_dialog, null);

        nameInput = view.findViewById(R.id.editTextName);
        descInput = view.findViewById(R.id.editTextDescription);
        errorText = view.findViewById(R.id.textViewError);
        submitButton = view.findViewById(R.id.buttonSubmit);
        categorySpinner = view.findViewById(R.id.availableCategoriesSpinner);

        category = (MinimalOfferCategoryDTO) getArguments().getSerializable(ARG_CATEGORY);
        isAccepted = getArguments().getBoolean("accepted");

        if (isAccepted) {
            categorySpinner.setVisibility(View.GONE);
            nameInput.setText(category.name);
            descInput.setText(category.description);
        } else {
            nameInput.setVisibility(View.GONE);
            descInput.setVisibility(View.GONE);

            categoryService.getAvailableCategories().enqueue(new Callback<List<MinimalOfferCategoryDTO>>() {
                @Override
                public void onResponse(Call<List<MinimalOfferCategoryDTO>> call, Response<List<MinimalOfferCategoryDTO>> response) {
                    if(response.isSuccessful() && response.body() != null){
                        categories.addAll(response.body());

                        ArrayAdapter<MinimalOfferCategoryDTO> adapter = new ArrayAdapter<>(
                                getContext(), android.R.layout.simple_spinner_item, categories
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
        }

        submitButton.setOnClickListener(v -> {
            if(isAccepted)
                AcceptSuggestion();
            else
                RejectSuggestion();
        });

        return new AlertDialog.Builder(requireActivity())
                .setView(view)
                .create();
    }

    private void AcceptSuggestion(){
        HandleSuggestionDTO data = new HandleSuggestionDTO();
        data.isAccepted = true;
        data.name = nameInput.getText().toString();
        data.description = descInput.getText().toString();

        if(data.name.trim().equals("") || data.description.trim().equals("")){
            errorText.setText("Please fill all fields");
            return;
        }

        categoryService.handlePendingCategory(category.id, data).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    dismiss();
                }
                else
                    Toast.makeText(getContext(), "Error submitting data", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error submitting data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void RejectSuggestion(){
        HandleSuggestionDTO data = new HandleSuggestionDTO();
        data.isAccepted = false;
        MinimalOfferCategoryDTO cat = (MinimalOfferCategoryDTO)categorySpinner.getSelectedItem();
        data.newId = cat.id;

        categoryService.handlePendingCategory(category.id, data).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    dismiss();
                }
                else
                    Toast.makeText(getContext(), "Error submitting data", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error submitting data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private OnDialogDismissListener dismissListener;

    public void setOnDismissListener(OnDialogDismissListener listener) {
        this.dismissListener = listener;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if(dismissListener != null)
            dismissListener.onDismiss();
    }
}
