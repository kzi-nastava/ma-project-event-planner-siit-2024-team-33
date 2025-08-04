package com.example.myapplication.reports;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.dto.reportDTO.GetReportDTO;
import com.example.myapplication.services.ReportService;
import com.example.myapplication.dto.reportDTO.PostReportDTO;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportDialog {

    private final Context context;
    private final ReportService reportService;
    private final int reportedUserId;
    private final List<String> reportOptions = Arrays.asList(
            "Inappropriate Language",
            "Harassment or Bullying",
            "Spam or Unwanted Advertising",
            "Scamming or Fraudulent Behavior",
            "Inappropriate Content",
            "Hate Speech or Discrimination"
    );

    public ReportDialog(Context context, ReportService reportService, int reportedUserId) {
        this.context = context;
        this.reportService = reportService;
        this.reportedUserId = reportedUserId;
    }

    public void show() {
        Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_report, null);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LinearLayout optionsContainer = view.findViewById(R.id.options_container);
        EditText customReasonInput = view.findViewById(R.id.custom_reason);
        Button cancelButton = view.findViewById(R.id.cancel_button);
        Button confirmButton = view.findViewById(R.id.confirm_button);

        final String[] selectedOption = {null};

        // Dynamically add option cards
        for (String option : reportOptions) {
            TextView optionCard = new TextView(context);
            optionCard.setText(option);
            optionCard.setTextSize(16);
            optionCard.setPadding(20, 20, 20, 20);
            optionCard.setBackgroundColor(Color.parseColor("#2d3748"));
            optionCard.setTextColor(Color.WHITE);
            optionCard.setClickable(true);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 10);
            optionCard.setLayoutParams(params);

            optionCard.setOnClickListener(v -> {
                selectedOption[0] = option;
                customReasonInput.setText(""); // Clear custom reason if selecting predefined
                highlightSelected(optionsContainer, optionCard);
            });

            optionsContainer.addView(optionCard);
        }

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        confirmButton.setOnClickListener(v -> {
            String customReason = customReasonInput.getText().toString().trim();
            String finalReason = !customReason.isEmpty() ? customReason : selectedOption[0];

            if (finalReason == null || finalReason.isEmpty()) {
                Toast.makeText(context, "Please select or write a reason.", Toast.LENGTH_SHORT).show();
                return;
            }

            PostReportDTO dto = new PostReportDTO(reportedUserId, finalReason);

            reportService.submitReport(dto).enqueue(new Callback<GetReportDTO>() {
                @Override
                public void onResponse(Call<GetReportDTO> call, Response<GetReportDTO> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Report submitted successfully", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(context, "Failed to submit report", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<GetReportDTO> call, Throwable t) {
                    Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }

    private void highlightSelected(LinearLayout container, TextView selectedCard) {
        for (int i = 0; i < container.getChildCount(); i++) {
            View child = container.getChildAt(i);
            child.setBackgroundColor(Color.parseColor("#2d3748"));
        }
        selectedCard.setBackgroundColor(Color.parseColor("#3b82f6"));
    }
}
