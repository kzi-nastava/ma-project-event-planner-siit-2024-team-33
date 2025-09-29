package com.example.myapplication.ui.view.page.offers;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.models.ProviderProductDTO;
import com.example.myapplication.ui.viewmodel.offer.ProductCRUDViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class ProductCRUDFragment extends Fragment {

    private RecyclerView rvProducts;
    private ProgressBar progressBar;
    private ProviderProductAdapter adapter;
    private ProductCRUDViewModel viewModel;
    private Button btnCreateProduct;
    private TextInputEditText inputSearch;
    private Handler searchHandler = new Handler();
    private Runnable searchRunnable;
    private static final long SEARCH_DELAY = 400;
    private String currentQuery = "";

    public ProductCRUDFragment() {}

    public static ProductCRUDFragment newInstance() {
        return new ProductCRUDFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_c_r_u_d, container, false);

        rvProducts = view.findViewById(R.id.rvProviderProducts);
        progressBar = view.findViewById(R.id.progressBarProviderProducts);
        inputSearch = view.findViewById(R.id.inputSearch);
        btnCreateProduct = view.findViewById(R.id.btnCreateProduct);

        viewModel = new ViewModelProvider(this).get(ProductCRUDViewModel.class);

        adapter = new ProviderProductAdapter(new ProviderProductAdapter.OnProductClickListener() {
            @Override
            public void onEditClick(ProviderProductDTO product) {
                ProductFormFragment fragment = ProductFormFragment.newInstance(product);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onDeleteClick(ProviderProductDTO product) {
                showDeleteConfirmationDialog(product);
            }
        });

        rvProducts.setLayoutManager(new LinearLayoutManager(requireContext()));
        rvProducts.setAdapter(adapter);

        //pagination
        rvProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (lm != null && lm.findLastVisibleItemPosition() >= adapter.getItemCount() - 2) {
                    viewModel.loadNextPage(currentQuery);
                }
            }
        });

        //observers
        viewModel.getProducts().observe(getViewLifecycleOwner(), adapter::updateList);
        viewModel.getLoading().observe(getViewLifecycleOwner(),
                isLoading -> progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE)
        );
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
        });

        //searching with debounce
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchRunnable != null) searchHandler.removeCallbacks(searchRunnable);

                currentQuery = s.toString();
                searchRunnable = () -> viewModel.fetchFirstPage(currentQuery);
                searchHandler.postDelayed(searchRunnable, SEARCH_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //opening form for creation of product
        btnCreateProduct.setOnClickListener(v -> {
            ProductFormFragment fragment = ProductFormFragment.newInstance();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        //loading first page
        viewModel.fetchFirstPage("");

        return view;
    }

    private void showDeleteConfirmationDialog(ProviderProductDTO product) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete \"" + product.getName() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    viewModel.deleteProduct(product.getId());
                    Toast.makeText(requireContext(), "Product deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
}