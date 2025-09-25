package com.example.myapplication.data.services;

import com.example.myapplication.data.api.ReportApi;
import com.example.myapplication.data.dto.PageResponse;
import com.example.myapplication.data.dto.reportDTO.GetReportDTO;
import com.example.myapplication.data.dto.reportDTO.PostReportDTO;
import com.example.myapplication.utils.Settings;

import retrofit2.Call;

public class ReportService {
    private static final String BASE_URL = Settings.BASE_URL + "/";
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


    public Call<Void> suspendUser(Integer userId){
        return reportApi.suspendUser(userId);
    }

    public Call<Void> unbanUser(Integer userId){
        return reportApi.unbanUser(userId);
    }

    public Call<Long> getSuspensionTimeRemaining(Integer userId){
        return reportApi.getSuspensionTimeRemaining(userId);
    }

}
