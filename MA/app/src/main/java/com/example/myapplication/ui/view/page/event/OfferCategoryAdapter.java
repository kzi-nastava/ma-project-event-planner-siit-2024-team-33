package com.example.myapplication.ui.view.page.event;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.models.dto.OfferCategoryDTO.MinimalOfferCategoryDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OfferCategoryAdapter extends RecyclerView.Adapter<OfferCategoryAdapter.ViewHolder> {

    public final List<MinimalOfferCategoryDTO> categories;
    public final Set<Integer> selectedIds = new HashSet<>();

    public OfferCategoryAdapter(List<MinimalOfferCategoryDTO> categories) {
        this.categories = categories;
    }

    public Set<Integer> getSelectedIds() {
        return selectedIds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_offer_category_check, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MinimalOfferCategoryDTO category = categories.get(position);
        holder.checkBox.setText(category.name);
        holder.checkBox.setChecked(selectedIds.contains(category.id));

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) selectedIds.add(category.id);
            else selectedIds.remove(category.id);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.chkCategory);
        }
    }
}
