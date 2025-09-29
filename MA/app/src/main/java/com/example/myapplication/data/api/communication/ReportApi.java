package com.example.myapplication.data.api.communication;

import com.example.myapplication.data.models.dto.PageResponse;
import com.example.myapplication.data.models.dto.reportDTO.GetReportDTO;
import com.example.myapplication.data.models.dto.reportDTO.PostReportDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReportApi {

    @POST("/api/reports")
    Call<GetReportDTO> submitReport(@Body PostReportDTO postReportDTO);


    @GET("/api/reports")
    Call<PageResponse<GetReportDTO>> getReports(
            @Query("page") int page,
            @Query("size") int size
    );
    @GET("/api/reports/{reportId}")
    Call<GetReportDTO> getReport(
            @Path("reportId") int reportId
    );

    @POST("/api/reports/suspend/{userId}")
    Call<Void> suspendUser(
            @Path("userId") Integer userId
    );

    @POST("/api/reports/unsuspend/{userId}")
    Call<Void> unbanUser(
            @Path("userId") Integer userId
    );

    @GET("/api/reports/suspension-time/{userId}")
    Call<Long> getSuspensionTimeRemaining(
            @Path("userId") Integer userId
    );
}
