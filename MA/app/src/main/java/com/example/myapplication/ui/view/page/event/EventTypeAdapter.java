package com.example.myapplication.ui.view.page.event;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.models.dto.GetEventTypeDTO;

import java.util.List;

public class EventTypeAdapter extends RecyclerView.Adapter<EventTypeAdapter.EventTypeViewHolder> {

    public interface OnEditClickListener {
        void onEdit(GetEventTypeDTO eventType);
    }

    public interface OnToggleClickListener {
        void onToggle(GetEventTypeDTO eventType);
    }

    private List<GetEventTypeDTO> items;
    private final OnEditClickListener editListener;
    private final OnToggleClickListener toggleListener;

    public EventTypeAdapter(List<GetEventTypeDTO> items, OnEditClickListener editListener, OnToggleClickListener toggleListener) {
        this.items = items;
        this.editListener = editListener;
        this.toggleListener = toggleListener;
    }

    @NonNull
    @Override
    public EventTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_type, parent, false);
        return new EventTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventTypeViewHolder holder, int position) {
        GetEventTypeDTO eventType = items.get(position);
        holder.tvName.setText(eventType.getName());
        holder.tvDescription.setText(eventType.getDescription());

        // Show the correct button
        if (Boolean.TRUE.equals(eventType.getIsActive())) {
            holder.btnActivate.setVisibility(View.GONE);
            holder.btnDeactivate.setVisibility(View.VISIBLE);
        } else {
            holder.btnActivate.setVisibility(View.VISIBLE);
            holder.btnDeactivate.setVisibility(View.GONE);
        }

        holder.btnEdit.setOnClickListener(v -> editListener.onEdit(eventType));
        holder.btnActivate.setOnClickListener(v -> toggleListener.onToggle(eventType));
        holder.btnDeactivate.setOnClickListener(v -> toggleListener.onToggle(eventType));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateList(List<GetEventTypeDTO> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    public static class EventTypeViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription;
        Button btnEdit, btnActivate, btnDeactivate;

        public EventTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvEventTypeName);
            tvDescription = itemView.findViewById(R.id.tvEventTypeDescription);
            btnEdit = itemView.findViewById(R.id.btnEditEventType);
            btnActivate = itemView.findViewById(R.id.btnActivateEventType);
            btnDeactivate = itemView.findViewById(R.id.btnDeactivateEventType);
        }
    }
}