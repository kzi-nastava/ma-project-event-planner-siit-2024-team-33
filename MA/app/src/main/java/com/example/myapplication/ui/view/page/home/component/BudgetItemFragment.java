package com.example.myapplication.ui.view.page.home.component;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.data.models.dto.budgetDTO.BudgetItemWrapperDTO;
import com.example.myapplication.data.models.dto.budgetDTO.BudgetOfferDTO;
import com.example.myapplication.data.models.dto.budgetDTO.MinimalBudgetItemDTO;
import com.example.myapplication.data.models.dto.budgetDTO.PutBudgetItemDTO;
import com.example.myapplication.data.models.OfferType;
import com.example.myapplication.ui.view.page.offers.ProductDetailsFragment;
import com.example.myapplication.ui.view.page.services.ServiceDetailsFragment;
import com.example.myapplication.data.services.offer.BudgetService;
import com.example.myapplication.data.services.offer.ProductService;
import com.example.myapplication.utils.OnBudgetSetListener;
import com.example.myapplication.utils.OnReloadRequestListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetItemFragment extends Fragment {

    private final ProductService productService = new ProductService();
    private final BudgetService budgetService = new BudgetService();

    private BudgetItemWrapperDTO data;

    private ImageButton btnExpand;
    private TextView tvName;
    private TextView tvBudgetSummary;
    private LinearLayout offersContainer;

    Boolean isExpanded = false;

    public BudgetItemFragment() {
        // Required empty public constructor
    }

    public static BudgetItemFragment newInstance(BudgetItemWrapperDTO data) {
        BudgetItemFragment fragment = new BudgetItemFragment();
        Bundle args = new Bundle();

        args.putSerializable("item", data);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            data = (BudgetItemWrapperDTO) getArguments().getSerializable("item");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_budget_item, container, false);
        btnExpand = view.findViewById(R.id.btn_expand);
        tvName = view.findViewById(R.id.tv_category_name);
        tvBudgetSummary = view.findViewById(R.id.tv_budget_summary);
        offersContainer = view.findViewById(R.id.offers_container);

        tvName.setText(data.item.offerCategoryName);
        tvBudgetSummary.setText("€"+ data.item.usedBudget + " / €" + data.item.maxBudget);
        tvBudgetSummary.setOnClickListener(v -> showBudgetInputDialog(data.item.maxBudget, newBudget -> changeBudget(newBudget) ));
        btnExpand.setOnClickListener(v -> buttonPressed());

        LoadData();
        return view;
    }

    public void LoadData(){
        offersContainer.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(getContext());

        if(data.offers.isEmpty()){
            btnExpand.setImageResource(R.drawable.x_icon);
            btnExpand.setOnClickListener(v -> deleteBudgetItem());
        }
        else if(isExpanded){
            for(BudgetOfferDTO offerDTO : data.offers){
                View offerView = inflater.inflate(R.layout.fragment_budget_taken_offer, offersContainer, false);
                TextView offerName = offerView.findViewById(R.id.tv_offer_name);
                TextView offerCost = offerView.findViewById(R.id.tv_offer_cost);
                ImageButton cancelButton = offerView.findViewById(R.id.delete_button);
                TextView tvDetails = offerView.findViewById(R.id.tv_details);
                tvDetails.setOnClickListener(v -> openDetails(offerDTO));

                offerName.setText(offerDTO.name);
                offerCost.setText("€" + offerDTO.cost.toString());
                cancelButton.setOnClickListener(v -> cancelOffer(offerDTO));
                offersContainer.addView(offerView);
            }
            btnExpand.setImageResource(R.drawable.arrow_up);
        }
        else
            btnExpand.setImageResource(R.drawable.arrow_down);
    }

    public void openDetails(BudgetOfferDTO offerDTO){
        Fragment f = null;
        if(offerDTO.type == OfferType.SERVICE)
            f = ServiceDetailsFragment.newInstance(offerDTO.versionId);
        else
            f = ProductDetailsFragment.newInstance(offerDTO.versionId);
        FragmentTransaction transaction = requireActivity()
                .getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(R.id.nav_host_fragment, f);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void cancelOffer(BudgetOfferDTO offerDTO){
        if(offerDTO.type == OfferType.PRODUCT)
            productService.cancelProductReservation(offerDTO.versionId, data.eventId)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful())
                                requestReload();
                            else
                                Toast.makeText(getContext(), "Cancel failed", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(getContext(), "Cancel failed", Toast.LENGTH_SHORT).show();
                        }
                    });
    }

    public void deleteBudgetItem(){
        budgetService.deleteBudgetItem(data.eventId, data.item.offerCategoryID)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful())
                            requestReload();
                        else
                            Toast.makeText(getContext(), "Error deleting item", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Error deleting item", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void buttonPressed(){
        isExpanded = !isExpanded;
        LoadData();
    }

    public void requestReload(){
        if(listener != null)
            listener.onReloadRequest();
    }

    OnReloadRequestListener listener = null;
    public void setOnReloadListener(OnReloadRequestListener l){
        listener = l;
    }

    private void changeBudget(double newBudget){
        PutBudgetItemDTO dto = new PutBudgetItemDTO();
        dto.maxBudget = newBudget;
        budgetService.editBudgetItem(data.eventId, data.item.offerCategoryID, dto)
                .enqueue(new Callback<MinimalBudgetItemDTO>() {
                    @Override
                    public void onResponse(Call<MinimalBudgetItemDTO> call, Response<MinimalBudgetItemDTO> response) {
                        if(response.isSuccessful() && response.body() != null){
                            requestReload();
                        }
                        else
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<MinimalBudgetItemDTO> call, Throwable t) {
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showBudgetInputDialog(double currentMaxBudget, OnBudgetSetListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Set Maximum Budget");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setHint("Enter new max budget");
        input.setText(String.valueOf(currentMaxBudget));
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            try {
                double newBudget = Double.parseDouble(input.getText().toString());
                listener.onBudgetSet(newBudget);
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid number", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}