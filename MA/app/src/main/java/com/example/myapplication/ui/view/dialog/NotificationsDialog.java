package com.example.myapplication.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.data.dto.notificationDTO.GetNotificationDTO;
import com.example.myapplication.data.services.NotificationService;

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
    private List<GetNotificationDTO> allNotifications = null;
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
            notificationList.removeAllViews();
            paginationText.setText("No notifications to display");
            return;
        }

        notificationList.removeAllViews();

        int startIndex = (currentPage - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, allNotifications.size());

        if (startIndex >= allNotifications.size()) {
            currentPage = Math.max(1, totalPages);
            startIndex = (currentPage - 1) * PAGE_SIZE;
            endIndex = Math.min(startIndex + PAGE_SIZE, allNotifications.size());
        }

        List<GetNotificationDTO> currentPageNotifications = allNotifications.subList(startIndex, endIndex);

        for (GetNotificationDTO notification : currentPageNotifications) {
            View notificationView = View.inflate(context, R.layout.notification, null);

            TextView notificationContent = notificationView.findViewById(R.id.notification_description);
            TextView notificationDate = notificationView.findViewById(R.id.notification_time);
            CheckBox checkBox = notificationView.findViewById(R.id.notification_checkbox);

            notificationContent.setText(notification.getContent());
            notificationDate.setText(notification.getDateOfSending());

            int notificationId = notification.getIndex();

            checkBox.setChecked(selectedNotificationIds.contains(notificationId));

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    selectedNotificationIds.add(notificationId);
                } else {
                    selectedNotificationIds.remove(notificationId);
                }
            });

            notificationView.setOnClickListener(v -> showNotificationDetailsDialog(
                    notification.getContent(), notification.getContent(), notification.getDateOfSending()
            ));

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

    private void showNotificationDetailsDialog(String title, String content, String time) {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_notification_details);

        TextView titleView = dialog.findViewById(R.id.full_title);
        TextView contentView = dialog.findViewById(R.id.full_description);
        TextView timeView = dialog.findViewById(R.id.full_time);
        Button closeBtn = dialog.findViewById(R.id.close_button);

        titleView.setText(title);
        contentView.setText(content);
        timeView.setText("Sent at: " + time);

        closeBtn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
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
