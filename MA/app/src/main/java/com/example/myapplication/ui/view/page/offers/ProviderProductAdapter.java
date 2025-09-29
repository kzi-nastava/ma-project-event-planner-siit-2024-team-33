package com.example.myapplication.ui.view.page.offers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.models.ProviderProductDTO;

import java.util.ArrayList;
import java.util.List;

public class ProviderProductAdapter extends RecyclerView.Adapter<ProviderProductAdapter.ProductViewHolder> {

    private List<ProviderProductDTO> products = new ArrayList<>();
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onEditClick(ProviderProductDTO product);
        void onDeleteClick(ProviderProductDTO product);
    }

    public ProviderProductAdapter(OnProductClickListener listener) {
        this.listener = listener;
    }

    public void updateList(List<ProviderProductDTO> products) {
        this.products = products != null ? products : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product_full_card, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProviderProductDTO product = products.get(position);

        holder.productName.setText(product.getName());
        holder.productDescription.setText(product.getDescription());

        if (product.getDiscount() != null && product.getDiscount() > 0) {
            holder.productDiscount.setVisibility(View.VISIBLE);
            holder.productDiscount.setText("-" + product.getDiscount().intValue() + "$");
        } else {
            holder.productDiscount.setVisibility(View.GONE);
        }
        holder.productPrice.setText("$" + product.getPrice());

        if (product.getAvailability() != null) {
            holder.productAvailability.setText("Availability: " + product.getAvailability().name());
        } else {
            holder.productAvailability.setText("Availability: N/A");
        }

        if (Boolean.TRUE.equals(product.getPending())) {
            holder.productStatus.setVisibility(View.VISIBLE);
            holder.productStatus.setText("Pending Approval");
            holder.productStatus.setTextColor(
                    holder.itemView.getResources().getColor(android.R.color.holo_orange_dark)
            );
        } else if (Boolean.TRUE.equals(product.getDeleted())) {
            holder.productStatus.setVisibility(View.VISIBLE);
            holder.productStatus.setText("Deleted");
            holder.productStatus.setTextColor(
                    holder.itemView.getResources().getColor(android.R.color.holo_red_dark)
            );
            holder.btnEdit.setEnabled(false);
            holder.btnDelete.setEnabled(false);
            holder.btnEdit.setAlpha(0.5f);
            holder.btnDelete.setAlpha(0.5f);
        } else {
            holder.productStatus.setVisibility(View.GONE);

            holder.btnEdit.setEnabled(true);
            holder.btnDelete.setEnabled(true);
            holder.btnEdit.setAlpha(1f);
            holder.btnDelete.setAlpha(1f);
        }

        holder.btnEdit.setOnClickListener(v -> {
            if (!Boolean.TRUE.equals(product.getDeleted())) {
                listener.onEditClick(product);
            }
        });
        holder.btnDelete.setOnClickListener(v -> {
            if (!Boolean.TRUE.equals(product.getDeleted())) {
                listener.onDeleteClick(product);
            }
        });
    }
    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productDescription, productPrice, productDiscount, productAvailability, productStatus;
        Button btnEdit, btnDelete;

        ProductViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            productPrice = itemView.findViewById(R.id.productPrice);
            productDiscount = itemView.findViewById(R.id.productDiscount);
            productAvailability = itemView.findViewById(R.id.productAvailability);
            productStatus = itemView.findViewById(R.id.productStatus);
            btnEdit = itemView.findViewById(R.id.btnEditProduct);
            btnDelete = itemView.findViewById(R.id.btnDeleteProduct);
        }
    }
}
