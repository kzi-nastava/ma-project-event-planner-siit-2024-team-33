package com.example.myapplication.ui.view.page.communication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.models.dto.PageResponse;
import com.example.myapplication.data.models.dto.reportDTO.GetReportDTO;
import com.example.myapplication.data.services.communication.ReportService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportsActivity extends Activity {

    private ReportService reportService;
    private RecyclerView recyclerView;
    private ReportsAdapter adapter;
    private List<GetReportDTO> reports = new ArrayList<>();
    private boolean isLoading = false;
    private int currentPage = 0;
    private final int REPORTS_PER_PAGE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        reportService = new ReportService();
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new ReportsAdapter(reports, reportService, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        Button previousPageButton = findViewById(R.id.previousPageButton);
        Button nextPageButton = findViewById(R.id.nextPageButton);

        previousPageButton.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                loadReports();
            }
        });

        nextPageButton.setOnClickListener(v -> {
            currentPage++;
            loadReports();
        });

        loadReports();
    }

    private void loadReports() {
        isLoading = true;

        reportService.getReports(currentPage,REPORTS_PER_PAGE).enqueue(new Callback<PageResponse<GetReportDTO>>() {
            @Override
            public void onResponse(Call<PageResponse<GetReportDTO>> call, Response<PageResponse<GetReportDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PageResponse<GetReportDTO> pageResponse = response.body();

                    int totalPages = pageResponse.getTotalPages();
                    reports.clear();
                    String json = response.body().toString();

                    Log.d("API_RESPONSE", json);


                    reports.addAll(pageResponse.getContent());
                    adapter.notifyDataSetChanged();

                    updatePaginationControls(totalPages);
                } else {
                    Toast.makeText(ReportsActivity.this, "Failed to load reports", Toast.LENGTH_SHORT).show();
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<PageResponse<GetReportDTO>> call, Throwable t) {
                Toast.makeText(ReportsActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                isLoading = false;
            }
        });
    }

    private void updatePaginationControls(int totalPages) {
        Button previousPageButton = findViewById(R.id.previousPageButton);
        Button nextPageButton = findViewById(R.id.nextPageButton);
        LinearLayout pageNumbersLayout = findViewById(R.id.pageNumbersLayout);

        previousPageButton.setVisibility(currentPage > 0 ? View.VISIBLE : View.INVISIBLE);
        nextPageButton.setVisibility(currentPage < totalPages - 1 ? View.VISIBLE : View.INVISIBLE);

        pageNumbersLayout.removeAllViews();
        for (int i = 0; i < totalPages; i++) {
            TextView pageNumber = new TextView(this);
            pageNumber.setText(String.valueOf(i + 1));
            pageNumber.setPadding(8, 0, 8, 0);
            pageNumber.setTextColor(i == currentPage ? getResources().getColor(R.color.black) : getResources().getColor(R.color.low_opacity));
            int finalI = i;
            pageNumber.setOnClickListener(v -> {
                currentPage = finalI;
                loadReports();
            });
            pageNumbersLayout.addView(pageNumber);
        }
    }

}
