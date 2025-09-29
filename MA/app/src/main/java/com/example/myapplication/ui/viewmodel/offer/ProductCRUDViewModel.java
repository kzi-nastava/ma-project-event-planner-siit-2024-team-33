package com.example.myapplication.ui.viewmodel.offer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.data.dto.PageResponse;
import com.example.myapplication.data.models.ProviderProductDTO;
import com.example.myapplication.data.services.ProductService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductCRUDViewModel extends ViewModel {
    private final ProductService service = new ProductService();

    private final MutableLiveData<List<ProviderProductDTO>> products = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private int currentPage = 0;
    private final int pageSize = 10;
    private boolean isLastPage = false;
    private boolean isLoadingPage = false;

    public LiveData<List<ProviderProductDTO>> getProducts() {
        return products;
    }
    public LiveData<Boolean> getLoading() {
        return loading;
    }
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void fetchFirstPage(String query) {
        currentPage = 0;
        isLastPage = false;
        products.setValue(new ArrayList<>());
        loadNextPage(query);
    }

    public void loadNextPage(String query) {
        if (isLoadingPage || isLastPage) return;

        isLoadingPage = true;
        loading.setValue(true);

        service.getProviderProducts(currentPage, pageSize, query, null, null, null, null)
                .enqueue(new Callback<PageResponse<ProviderProductDTO>>() {
                    @Override
                    public void onResponse(Call<PageResponse<ProviderProductDTO>> call, Response<PageResponse<ProviderProductDTO>> response) {
                        isLoadingPage = false;
                        loading.setValue(false);

                        if (response.isSuccessful() && response.body() != null) {
                            PageResponse<ProviderProductDTO> page = response.body();
                            List<ProviderProductDTO> currentList = products.getValue();
                            if (currentList == null) currentList = new ArrayList<>();

                            currentList.addAll(page.getContent());
                            products.setValue(currentList);

                            isLastPage = page.getNumber() >= page.getTotalPages() - 1;
                            if (!isLastPage) currentPage++;
                        } else {
                            errorMessage.setValue("Failed to load products.");
                        }
                    }

                    @Override
                    public void onFailure(Call<PageResponse<ProviderProductDTO>> call, Throwable t) {
                        isLoadingPage = false;
                        loading.setValue(false);
                        errorMessage.setValue("Error: " + t.getMessage());
                    }
                });
    }

    public void deleteProduct(int id) {
        service.deleteProduct(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    //removing from list locally
                    List<ProviderProductDTO> currentList = products.getValue();
                    if (currentList != null) {
                        currentList.removeIf(p -> p.getId().equals(id));
                        products.setValue(currentList);
                    }
                } else {
                    errorMessage.setValue("Failed to delete product.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                errorMessage.setValue("Error: " + t.getMessage());
            }
        });
    }
}