package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.myapplication.dto.notificationDTO.GetNotificationDTO;
import com.example.myapplication.services.NotificationService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsDialog extends Dialog {

    private Context context;
    private NotificationService notificationService;
    private LinearLayout notificationList;
    private TextView paginationText;
    private int currentPage = 1;
    private static final int PAGE_SIZE = 4;
    private Set<Integer> selectedNotificationIds = new HashSet<>();
    private List<GetNotificationDTO> allNotifications = null; // Holds all notifications
    private int totalPages = 1;

    public NotificationsDialog(Context context) {
        super(context);
        this.context = context;
        notificationService = new NotificationService();
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
        paginationText = findViewById(R.id.pagination_text);
        notificationList = findViewById(R.id.notification_list);

        fetchAllNotifications();

        toggleNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle toggling notifications
        });

        buttonPrevious.setOnClickListener(v -> {
            if (currentPage > 1) {
                currentPage--;
                displayCurrentPageNotifications();
            }
        });

        buttonNext.setOnClickListener(v -> {
            if (currentPage < totalPages) {
                currentPage++;
                displayCurrentPageNotifications();
            }
        });

        buttonDelete.setOnClickListener(v -> {
            deleteSelectedNotifications();
        });
    }

    private void fetchAllNotifications() {
        notificationService.getNotifications().enqueue(new Callback<List<GetNotificationDTO>>() {
            @Override
            public void onResponse(Call<List<GetNotificationDTO>> call, Response<List<GetNotificationDTO>> response) {
                if (response.isSuccessful()) {
                    allNotifications = response.body();
                    if (allNotifications != null) {
                        calculateTotalPages();
                        displayCurrentPageNotifications();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<GetNotificationDTO>> call, Throwable t) {
            }
        });
    }

    private void calculateTotalPages() {
        if (allNotifications != null) {
            totalPages = (int) Math.ceil((double) allNotifications.size() / PAGE_SIZE);
        }
    }

    private void displayCurrentPageNotifications() {
        if (allNotifications == null || allNotifications.isEmpty()) {
            // Handle empty list case
            notificationList.removeAllViews();
            paginationText.setText("No notifications to display");
            return;
        }

        notificationList.removeAllViews();

        int startIndex = (currentPage - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, allNotifications.size());

        // Ensure startIndex and endIndex are valid
        if (startIndex >= allNotifications.size()) {
            currentPage = Math.max(1, totalPages); // Adjust currentPage if it exceeds the range
            startIndex = (currentPage - 1) * PAGE_SIZE;
            endIndex = Math.min(startIndex + PAGE_SIZE, allNotifications.size());
        }

        List<GetNotificationDTO> currentPageNotifications = allNotifications.subList(startIndex, endIndex);

        for (GetNotificationDTO notification : currentPageNotifications) {
            View notificationView = View.inflate(context, R.layout.notification, null);

            TextView notificationContent = notificationView.findViewById(R.id.notification_description);
            TextView notificationDate = notificationView.findViewById(R.id.notification_time);

            notificationContent.setText(notification.getContent());
            notificationDate.setText(notification.getDateOfSending());

            notificationView.setTag(notification.getIndex());

            notificationView.setOnClickListener(v -> {
                if (selectedNotificationIds.contains(notification.getIndex())) {
                    selectedNotificationIds.remove(notification.getIndex());
                    notificationView.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    selectedNotificationIds.add(notification.getIndex());
                    notificationView.setBackgroundColor(Color.LTGRAY);
                }
            });

            notificationList.addView(notificationView);
        }

        updatePaginationText();
    }


    private void updatePaginationText() {
        paginationText.setText("Page " + currentPage + " of " + totalPages);
    }

    private void deleteSelectedNotifications() {
        for (Integer notificationId : selectedNotificationIds) {
            deleteNotification(notificationId);
        }
        selectedNotificationIds.clear();
    }

    private void deleteNotification(Integer notificationId) {
        notificationService.deleteNotification(notificationId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    removeNotification(notificationId);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }

    private void removeNotification(Integer notificationId) {
        if (allNotifications != null) {
            allNotifications.removeIf(notification -> notification.getIndex().equals(notificationId));

            calculateTotalPages();

            if (currentPage > totalPages) {
                currentPage = Math.max(1, totalPages);
            }

            displayCurrentPageNotifications();
        }
    }

}
