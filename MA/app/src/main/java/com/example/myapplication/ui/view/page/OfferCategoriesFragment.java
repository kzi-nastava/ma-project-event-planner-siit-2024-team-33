package com.example.myapplication.ui.view.page;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.ui.view.dialog.CreateCategoryDialog;
import com.example.myapplication.ui.view.dialog.HandleCategoryDialog;
import com.example.myapplication.data.dto.OfferCategoryDTO.MinimalOfferCategoryDTO;
import com.example.myapplication.data.services.OfferCategoryService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfferCategoriesFragment extends Fragment {

    private final OfferCategoryService categoryService = new OfferCategoryService();
    List<MinimalOfferCategoryDTO> acceptedCategories = null;
    List<MinimalOfferCategoryDTO> pendingCategories = null;


    Button createButton;
    LinearLayout acceptedCategoriesLayout;
    TextView tvPending;
    LinearLayout pendingCategoriesLayout;

    public OfferCategoriesFragment() {
        // Required empty public constructor
    }

    public static OfferCategoriesFragment newInstance() {
        OfferCategoriesFragment fragment = new OfferCategoriesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offer_categories, container, false);

        acceptedCategoriesLayout = view.findViewById(R.id.acceptedCategoriesLayout);
        createButton = view.findViewById(R.id.createCategoryButton);
        createButton.setOnClickListener(v -> openCreateDialog());
        pendingCategoriesLayout = view.findViewById(R.id.pendingCategoriesLayout);
        tvPending = view.findViewById(R.id.pendingCategories);

        LoadData();
        return view;
    }

    public void LoadData(){
        categoryService.getCategories(true, null).enqueue(new Callback<List<MinimalOfferCategoryDTO>>() {
            @Override
            public void onResponse(Call<List<MinimalOfferCategoryDTO>> call, Response<List<MinimalOfferCategoryDTO>> response) {
                if(response.isSuccessful() && response.body() != null){
                    acceptedCategories = response.body();
                    acceptedCategoriesLayout.removeAllViews();
                    for (MinimalOfferCategoryDTO cat : acceptedCategories) {
                        View card = LayoutInflater.from(getContext()).inflate(R.layout.fragment_offercategory_card, acceptedCategoriesLayout, false);

                        ((TextView) card.findViewById(R.id.categoryId)).setText("ID: " + cat.id);
                        ((TextView) card.findViewById(R.id.categoryName)).setText("Name: " + cat.name);
                        ((TextView) card.findViewById(R.id.categoryDescription)).setText("Description: " + cat.description);
                        ((TextView) card.findViewById(R.id.categoryIsEnabled)).setText("Is Enabled: " + cat.isEnabled);

                        card.findViewById(R.id.edit_button).setOnClickListener(v -> openEditDialog(cat));
                        card.findViewById(R.id.delete_button).setOnClickListener(v -> deleteCategory(cat));

                        acceptedCategoriesLayout.addView(card);
                    }
                }
                else
                    Toast.makeText(getContext(), "Error loading accepted categories", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<MinimalOfferCategoryDTO>> call, Throwable t) {
                Toast.makeText(getContext(), "Error loading accepted categories", Toast.LENGTH_SHORT).show();
            }
        });
        categoryService.getPendingCategories().enqueue(new Callback<List<MinimalOfferCategoryDTO>>() {
            @Override
            public void onResponse(Call<List<MinimalOfferCategoryDTO>> call, Response<List<MinimalOfferCategoryDTO>> response) {
                if(response.isSuccessful() && response.body() != null){
                    pendingCategories = response.body();
                    pendingCategoriesLayout.removeAllViews();

                    if(pendingCategories.size() == 0)
                        tvPending.setVisibility(View.GONE);
                    else
                        tvPending.setVisibility(View.VISIBLE);

                    for (MinimalOfferCategoryDTO cat : pendingCategories) {
                        View card = LayoutInflater.from(getContext()).inflate(R.layout.fragment_pending_category_card, pendingCategoriesLayout, false);

                        ((TextView) card.findViewById(R.id.categoryName)).setText("Name: " + cat.name);
                        ((TextView) card.findViewById(R.id.categoryDescription)).setText("Description: " + cat.description);

                        card.findViewById(R.id.acceptBtn).setOnClickListener(v -> openHandleDialog(true, cat));
                        card.findViewById(R.id.rejectBtn).setOnClickListener(v -> openHandleDialog(false, cat));

                        pendingCategoriesLayout.addView(card);
                    }
                }
                else
                    Toast.makeText(getContext(), "Error loading pending categories", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<List<MinimalOfferCategoryDTO>> call, Throwable t) {
                Toast.makeText(getContext(), "Error loading pending categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openCreateDialog(){
        CreateCategoryDialog f = CreateCategoryDialog.newInstance(null);
        f.setOnDismissListener(() -> LoadData());
        f.show(getActivity().getSupportFragmentManager(), "CreateDialog");
    }

    public void openEditDialog(MinimalOfferCategoryDTO data){
        CreateCategoryDialog f = CreateCategoryDialog.newInstance(data);
        f.setOnDismissListener(() -> LoadData());
        f.show(getActivity().getSupportFragmentManager(), "CreateDialog");
    }

    public void deleteCategory(MinimalOfferCategoryDTO data){
        categoryService.deleteCategory(data.id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    LoadData();
                }
                else
                    Toast.makeText(getContext(), "Error deleting", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error deleting", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openHandleDialog(Boolean isAccepted, MinimalOfferCategoryDTO data){
        HandleCategoryDialog f = HandleCategoryDialog.newInstance(data, isAccepted);
        f.setOnDismissListener(() -> LoadData());
        f.show(getActivity().getSupportFragmentManager(), "HandleSuggestion");
    }
}