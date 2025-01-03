package com.example.myapplication.services;

import com.example.myapplication.api.RatingApi;
import com.example.myapplication.api.ReportApi;
import com.example.myapplication.dto.reportDTO.GetReportDTO;
import com.example.myapplication.dto.reportDTO.PostReportDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class ReportService {
    private static final String BASE_URL = "http://10.0.2.2/api/reports/";
    private final ReportApi reportApi;


    public ReportService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        reportApi = retrofit.create(ReportApi.class);
    }

    Call<String> submitReport(PostReportDTO postReportDTO){
        return reportApi.submitReport(postReportDTO);
    }

    Call<List<GetReportDTO>> getReports(){
        return reportApi.getReports();
    }

    Call<GetReportDTO> getReport(int reportId){
        return reportApi.getReport(reportId);
    }


    Call<String> suspendUser(Integer userId){
        return reportApi.suspendUser(userId);
    }

    Call<Long> getSuspensionTimeRemaining(Integer userId){
        return reportApi.getSuspensionTimeRemaining(userId);
    }

}
