package com.example.myapplication.services;

import com.example.myapplication.api.RatingApi;
import com.example.myapplication.api.ReportApi;
import com.example.myapplication.dto.PageResponse;
import com.example.myapplication.dto.reportDTO.GetReportDTO;
import com.example.myapplication.dto.reportDTO.PostReportDTO;
import com.example.myapplication.utils.Settings;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class ReportService {
    private static final String BASE_URL = Settings.BASE_URL + "/api/reports/";
    private final ReportApi reportApi;


    public ReportService() {reportApi = ApiClient.getRetrofit(BASE_URL).create(ReportApi.class);}

    public Call<GetReportDTO> submitReport(PostReportDTO postReportDTO){
        return reportApi.submitReport(postReportDTO);
    }

    public Call<PageResponse<GetReportDTO>> getReports(int page, int size) {
        return reportApi.getReports(page, size);
    }

    Call<GetReportDTO> getReport(int reportId){
        return reportApi.getReport(reportId);
    }


    public Call<String> suspendUser(Integer userId){
        return reportApi.suspendUser(userId);
    }

    public Call<Long> getSuspensionTimeRemaining(Integer userId){
        return reportApi.getSuspensionTimeRemaining(userId);
    }

}
