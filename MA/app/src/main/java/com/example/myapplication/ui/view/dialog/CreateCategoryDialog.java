package com.example.myapplication.ui.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.models.dto.OfferCategoryDTO.MinimalOfferCategoryDTO;
import com.example.myapplication.data.models.dto.OfferCategoryDTO.PostOfferCategoryDTO;
import com.example.myapplication.data.models.dto.OfferCategoryDTO.PutOfferCategoryDTO;
import com.example.myapplication.data.services.offer.OfferCategoryService;
import com.example.myapplication.utils.OnDialogDismissListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCategoryDialog extends DialogFragment{
    private static final String ARG_CATEGORY = "arg_category";

    private static final OfferCategoryService categoryService = new OfferCategoryService();

    private EditText nameInput;
    private EditText descInput;
    private CheckBox enabledBox;
    private TextView errorText;
    private Button submitButton;

    private MinimalOfferCategoryDTO category;

    public static CreateCategoryDialog newInstance(@Nullable MinimalOfferCategoryDTO dto) {
        CreateCategoryDialog fragment = new CreateCategoryDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CATEGORY, dto);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_create_category_dialog, null);

        nameInput = view.findViewById(R.id.editTextName);
        descInput = view.findViewById(R.id.editTextDescription);
        enabledBox = view.findViewById(R.id.checkBoxIsEnabled);
        errorText = view.findViewById(R.id.textViewError);
        submitButton = view.findViewById(R.id.buttonSubmit);

        // Get data
        if (getArguments() != null) {
            category = (MinimalOfferCategoryDTO) getArguments().getSerializable(ARG_CATEGORY);
        }

        final boolean editMode = category != null;

        if (editMode) {
            submitButton.setText("Edit");
            enabledBox.setVisibility(View.VISIBLE);
            if (category != null) {
                nameInput.setText(category.name);
                descInput.setText(category.description);
                enabledBox.setChecked(Boolean.TRUE.equals(category.isEnabled));
            }
        } else {
            submitButton.setText("Create");
        }

        submitButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String desc = descInput.getText().toString().trim();

            if (name.isEmpty() || desc.isEmpty()) {
                errorText.setText("All fields must be filled.");
                errorText.setVisibility(View.VISIBLE);
                return;
            }

            if(editMode){
                PutOfferCategoryDTO data = new PutOfferCategoryDTO();
                data.name = name;
                data.description = desc;
                data.isEnabled = enabledBox.isChecked();
                categoryService.editCategory(category.id, data).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful()){
                            dismiss();
                        }
                        else
                            Toast.makeText(getContext(), "Error editing", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Error editing", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else{
                PostOfferCategoryDTO data = new PostOfferCategoryDTO();
                data.name = name;
                data.description = desc;
                categoryService.createCategory(data).enqueue(new Callback<MinimalOfferCategoryDTO>() {
                    @Override
                    public void onResponse(Call<MinimalOfferCategoryDTO> call, Response<MinimalOfferCategoryDTO> response) {
                        if(response.isSuccessful()){
                            dismiss();
                        }
                        else
                            Toast.makeText(getContext(), "Error creating", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<MinimalOfferCategoryDTO> call, Throwable t) {
                        Toast.makeText(getContext(), "Error creating", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return new AlertDialog.Builder(requireActivity())
                .setView(view)
                .create();
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