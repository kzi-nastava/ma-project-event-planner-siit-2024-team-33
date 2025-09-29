package com.example.myapplication.ui.view.page.communication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.models.dto.reportDTO.GetReportDTO;
import com.example.myapplication.data.services.communication.ReportService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportViewHolder> {

    private final List<GetReportDTO> reports;
    private final ReportService reportService;
    private final Context context;

    public ReportsAdapter(List<GetReportDTO> reports, ReportService reportService, Context context) {
        this.reports = reports;
        this.reportService = reportService;
        this.context = context;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        GetReportDTO report = reports.get(position);

        holder.reportTitle.setText(report.getAuthor());
        holder.reportDescription.setText(report.getContent());
        holder.reportedUser.setText("Reported User: " + report.getReceiver());
        holder.userEmail.setText("Date Sent: " + report.getDateOfSending());

        reportService.getSuspensionTimeRemaining(report.getReceiverId()).enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Long suspensionHours = response.body();
                    if (suspensionHours == 0) {
                        holder.suspensionTimeText.setText("NOT BANNED");
                    } else {
                        holder.suspensionTimeText.setText("Suspension: " + suspensionHours + "h");
                    }
                    holder.suspensionTimeText.setVisibility(View.VISIBLE);
                } else {
                    holder.suspensionTimeText.setText("NOT BANNED");
                    holder.suspensionTimeText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                holder.suspensionTimeText.setText("NOT BANNED");
                holder.suspensionTimeText.setVisibility(View.VISIBLE);
            }
        });

        holder.banUserButton.setOnClickListener(v -> {
            reportService.suspendUser(report.getReceiverId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "User banned successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to ban user", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        holder.unbanUserButton.setOnClickListener(v -> {
            reportService.unbanUser(report.getReceiverId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "User unbanned successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            String errorBody = response.errorBody().string();
                            Toast.makeText(context, "Failed: " + errorBody, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(context, "Failed with unknown error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    static class ReportViewHolder extends RecyclerView.ViewHolder {

        TextView reportTitle, reportDescription, reportedUser, userEmail, suspensionTimeText;
        Button banUserButton, unbanUserButton;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            reportTitle = itemView.findViewById(R.id.reportTitle);
            reportDescription = itemView.findViewById(R.id.reportDescription);
            reportedUser = itemView.findViewById(R.id.reportedUser);
            userEmail = itemView.findViewById(R.id.userEmail);
            suspensionTimeText = itemView.findViewById(R.id.suspensionTimeText);  // <<<<< HERE
            banUserButton = itemView.findViewById(R.id.banUserButton);
            unbanUserButton = itemView.findViewById(R.id.unbanUserButton);
        }
    }
}
