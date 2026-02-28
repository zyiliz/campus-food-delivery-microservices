package com.example.service;

import com.example.pojo.VO.*;
import com.example.result.Result;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {

    Result<List<BusinessDataListVO>> turnoverStatistics(LocalDate begin, LocalDate end);

    Result<List<DishTopVO>> top10(LocalDate begin,LocalDate end);

    void export(LocalDate begin, LocalDate end, HttpServletResponse response);

    List<Long> getUserRecommend(Long id);


}
