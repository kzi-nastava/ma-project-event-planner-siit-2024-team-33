package com.example.myapplication.page;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.dto.pricesDTO.PriceItemDTO;
import com.example.myapplication.dto.pricesDTO.PutPriceDTO;
import com.example.myapplication.services.PriceService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProvidersPrices extends Fragment {
    private final PriceService priceService = new PriceService();

    LinearLayout offerContainer;

    List<PriceItemDTO> offers;

    public ProvidersPrices() {
    }

    public static ProvidersPrices newInstance() {
        ProvidersPrices fragment = new ProvidersPrices();
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
        View view = inflater.inflate(R.layout.fragment_providers_prices, container, false);

        offerContainer = view.findViewById(R.id.offers_container);

        priceService.getLoggedUsersPrices().enqueue(new Callback<List<PriceItemDTO>>() {
            @Override
            public void onResponse(Call<List<PriceItemDTO>> call, Response<List<PriceItemDTO>> response) {
                if(response.isSuccessful() && response.body() != null){
                    offers = response.body();
                    ShowOffers();
                }
            }

            @Override
            public void onFailure(Call<List<PriceItemDTO>> call, Throwable t) {

            }
        });

        Button pdfBtn = view.findViewById(R.id.pdfBtn);
        pdfBtn.setOnClickListener(v -> {
            priceService.getPdfData().enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful() && response.body() != null){
                        String filename = "export";
                        File pdfFile = new File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                                filename + ".pdf"
                        );

                        try (FileOutputStream fos = new FileOutputStream(pdfFile)) {
                            fos.write(response.body().bytes());
                            fos.flush();
                            Toast.makeText(getContext(), "PDF saved: " + pdfFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Failed to save PDF", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                        Toast.makeText(getContext(), "Error exporting", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        });

        return view;
    }

    void ShowOffers(){
        offerContainer.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (PriceItemDTO offer : offers) {
            View offerView = inflater.inflate(R.layout.offering_card, offerContainer, false);


            TextView itemTitle = offerView.findViewById(R.id.offering_title);
            TextView itemText = offerView.findViewById(R.id.offering_description);
            Button infoButton = offerView.findViewById(R.id.offering_button);
            infoButton.setText("Edit");

            if (itemTitle != null && itemText != null) {
                itemTitle.setText(offer.name);
                Double trueCost = offer.fullPrice - offer.discount;
                itemText.setText(
                        "Price: " + offer.fullPrice.toString() +
                        "\nDiscount: " + offer.discount.toString() +
                        "\nTrue cost: " + trueCost.toString()
                );
                infoButton.setOnClickListener(v -> {
                    showEditDialog(offer);
                });
            }

            offerContainer.addView(offerView);
        }
    }

    private void showEditDialog(PriceItemDTO item) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_price, null);

        EditText fullPriceInput = dialogView.findViewById(R.id.edit_full_price);
        EditText discountInput = dialogView.findViewById(R.id.edit_discount);

        fullPriceInput.setText(String.valueOf(item.fullPrice));
        discountInput.setText(String.valueOf(item.discount));

        AlertDialog d = new AlertDialog.Builder(getContext())
                .setTitle("Edit Price")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    try {
                        double newPrice = Double.parseDouble(fullPriceInput.getText().toString());
                        double newDiscount = Double.parseDouble(discountInput.getText().toString());

                        item.fullPrice = newPrice;
                        item.discount = newDiscount;

                        if(newPrice < 0 || newDiscount < 0 || newDiscount > newPrice){
                            Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        PutPriceDTO dto = new PutPriceDTO();
                        dto.Discount = newDiscount;
                        dto.FullPrice = newPrice;

                        priceService.editPrice(item.offerId, dto).enqueue(new Callback<List<PriceItemDTO>>() {
                            @Override
                            public void onResponse(Call<List<PriceItemDTO>> call, Response<List<PriceItemDTO>> response) {
                                if(response.isSuccessful() && response.body() != null){
                                    offers = response.body();
                                    ShowOffers();
                                }else
                                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<List<PriceItemDTO>> call, Throwable t) {
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (NumberFormatException e) {
                        Toast.makeText(getContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null).show();

        d.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corners);
    }

}