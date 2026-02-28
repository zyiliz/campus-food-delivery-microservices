package com.example.controller;

import com.example.pojo.VO.BusinessDataListVO;
import com.example.pojo.VO.BusinessDataVO;
import com.example.pojo.VO.DishTopVO;
import com.example.result.Result;
import com.example.service.OrderService;
import com.example.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin/report")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/turnoverStatistics")
    public Result<List<BusinessDataListVO>> turnoverStatistics(
            @NotNull(message = "开始日期不能为空")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @RequestParam("begin") LocalDate begin,

            @NotNull(message = "结束日期不能为空")
            @DateTimeFormat(pattern = "yyyy-MM-dd")
            @RequestParam("end") LocalDate end
    ){
        return reportService.turnoverStatistics(begin,end);
    }

    @GetMapping("/top10")
    public Result<List<DishTopVO>> top10(@NotNull(message = "开始日期不能为空")
                                             @DateTimeFormat(pattern = "yyyy-MM-dd")
                                             @RequestParam("begin") LocalDate begin,

                                         @NotNull(message = "结束日期不能为空")
                                             @DateTimeFormat(pattern = "yyyy-MM-dd")
                                             @RequestParam("end") LocalDate end){
        return reportService.top10(begin,end);
    }

    @GetMapping("/export")
    public void export(@NotNull(message = "开始日期不能为空")
                           @DateTimeFormat(pattern = "yyyy-MM-dd")
                           @RequestParam("begin") LocalDate begin,

                       @NotNull(message = "结束日期不能为空")
                           @DateTimeFormat(pattern = "yyyy-MM-dd")
                           @RequestParam("end") LocalDate end,
                       HttpServletResponse response){
        reportService.export(begin,end,response);
    }

    @GetMapping("/recommendList/{id}")
    public List<Long> recommend(@PathVariable Long id){
        return reportService.getUserRecommend(id);
    }
}
