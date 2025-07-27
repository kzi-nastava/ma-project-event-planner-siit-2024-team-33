package com.example.myapplication.api;

import com.example.myapplication.dto.PageResponse;
import com.example.myapplication.dto.reportDTO.GetReportDTO;
import com.example.myapplication.dto.reportDTO.PostReportDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReportApi {

    @POST("reports")
    Call<GetReportDTO> submitReport(
            @Body PostReportDTO postReportDTO
    );

    @GET("reports")
    Call<PageResponse<GetReportDTO>> getReports(
            @Query("page") int page,
            @Query("size") int size
    );
    @GET("{reportId}")
    Call<GetReportDTO> getReport(
            @Path("reportId") int reportId
    );

    @POST("suspend/{userId}")
    Call<String> suspendUser(
            @Path("userId") Integer userId
    );

    @GET("suspension-time/{userId}")
    Call<Long> getSuspensionTimeRemaining(
            @Path("userId") Integer userId
    );
}
