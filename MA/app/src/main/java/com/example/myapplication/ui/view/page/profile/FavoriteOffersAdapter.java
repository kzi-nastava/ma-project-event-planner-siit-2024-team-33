package com.example.myapplication.ui.view.page.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.dto.offerDTO.MinimalOfferDTO;

import java.util.ArrayList;
import java.util.List;

public class FavoriteOffersAdapter extends RecyclerView.Adapter<FavoriteOffersAdapter.ViewHolder> {

    private final List<MinimalOfferDTO> offers = new ArrayList<>();
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDetailsButtonClick(MinimalOfferDTO offer);
    }

    public FavoriteOffersAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void addItems(List<MinimalOfferDTO> newOffers) {
        int start = offers.size();
        offers.addAll(newOffers);
        notifyItemRangeInserted(start, newOffers.size());
    }

    public void clearItems() {
        offers.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offering_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MinimalOfferDTO offer = offers.get(position);
        holder.title.setText(offer.getName());
        holder.description.setText(offer.getDescription());
        holder.detailsButton.setOnClickListener(v -> listener.onDetailsButtonClick(offer));
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        Button detailsButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.offering_title);
            description = itemView.findViewById(R.id.offering_description);
            detailsButton = itemView.findViewById(R.id.offering_button);
        }
    }
}