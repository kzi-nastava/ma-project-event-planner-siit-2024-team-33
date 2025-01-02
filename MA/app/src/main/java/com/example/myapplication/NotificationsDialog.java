package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

public class NotificationsDialog extends Dialog {

    private Context context;

    public NotificationsDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_notifications);

        // Find Views
        Switch toggleNotifications = findViewById(R.id.toggle_notifications);
        Button buttonPrevious = findViewById(R.id.button_previous);
        Button buttonNext = findViewById(R.id.button_next);
        Button buttonDelete = findViewById(R.id.button_delete);
        TextView paginationText = findViewById(R.id.pagination_text);
        LinearLayout notificationList = findViewById(R.id.notification_list);

        // Example: Set up toggle behavior
        toggleNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle toggling notifications
            if (isChecked) {
                // Enable notifications
            } else {
                // Disable notifications
            }
        });

        // Example: Handle pagination buttons
        buttonPrevious.setOnClickListener(v -> {
            // Load the previous page of notifications
        });

        buttonNext.setOnClickListener(v -> {
            // Load the next page of notifications
        });

        // Example: Handle delete button
        buttonDelete.setOnClickListener(v -> {
            // Delete read notifications
        });

        // Dynamically add notifications (placeholder)
        addSampleNotifications(notificationList);
    }

    private void addSampleNotifications(LinearLayout notificationList) {
        for (int i = 0; i < 3; i++) { // Example: Add 3 placeholder notifications
            View notificationView = View.inflate(context, R.layout.notification, null);
            notificationList.addView(notificationView);
        }
    }
}
