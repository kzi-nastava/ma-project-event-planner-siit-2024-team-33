package com.example.myapplication.ui.view.page.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.models.dto.eventDTO.MinimalEventDTO;

import java.util.ArrayList;
import java.util.List;

public class FavoriteEventsAdapter extends RecyclerView.Adapter<FavoriteEventsAdapter.FavoriteEventViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(MinimalEventDTO event);
    }

    private List<MinimalEventDTO> events = new ArrayList<>();
    private final OnItemClickListener listener;

    public FavoriteEventsAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateData(List<MinimalEventDTO> newEvents) {
        this.events = newEvents;
        notifyDataSetChanged();
    }

    public void addItems(List<MinimalEventDTO> newEvents) {
        int start = events.size();
        events.addAll(newEvents);
        notifyItemRangeInserted(start, newEvents.size());
    }

    public void clearItems() {
        events.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_card, parent, false);
        return new FavoriteEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteEventViewHolder holder, int position) {
        MinimalEventDTO event = events.get(position);
        holder.title.setText(event.getName());
        holder.description.setText(event.getDescription());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(event);
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class FavoriteEventViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;

        public FavoriteEventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            description = itemView.findViewById(R.id.item_text);
        }
    }
}
