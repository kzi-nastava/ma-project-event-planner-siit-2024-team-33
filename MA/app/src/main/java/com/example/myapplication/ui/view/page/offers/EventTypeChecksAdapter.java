package com.example.myapplication.ui.view.page.offers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.models.dto.eventTypeDTO.MinimalEventTypeDTO;

import java.util.ArrayList;
import java.util.List;

public class EventTypeChecksAdapter extends RecyclerView.Adapter<EventTypeChecksAdapter.ViewHolder> {

    private final List<MinimalEventTypeDTO> eventTypes;
    private final List<MinimalEventTypeDTO> selectedEventTypes = new ArrayList<>();

    public EventTypeChecksAdapter(List<MinimalEventTypeDTO> eventTypes, List<Integer> preSelectedIds) {
        this.eventTypes = eventTypes;

        if (preSelectedIds != null) {
            for (MinimalEventTypeDTO type : eventTypes) {
                if (preSelectedIds.contains(type.getId())) {
                    selectedEventTypes.add(type);
                }
            }
        }
    }

    public List<MinimalEventTypeDTO> getSelectedEventTypes() {
        return selectedEventTypes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_type_checkbox, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MinimalEventTypeDTO eventType = eventTypes.get(position);
        holder.checkBox.setText(eventType.getName());
        holder.checkBox.setChecked(selectedEventTypes.contains(eventType));

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!selectedEventTypes.contains(eventType)) selectedEventTypes.add(eventType);
            } else {
                selectedEventTypes.remove(eventType);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventTypes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        ViewHolder(View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkboxEventType);
        }
    }
}