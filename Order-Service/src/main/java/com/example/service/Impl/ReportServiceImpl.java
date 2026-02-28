package com.example.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.mapper.OrderDetailMapper;
import com.example.mapper.OrderMapper;
import com.example.pojo.DTO.CoPurchasedDishDTO;
import com.example.pojo.DTO.DishFrequencyDTO;
import com.example.pojo.VO.*;
import com.example.pojo.entity.Order;
import com.example.result.Result;
import com.example.service.ReportService;
import com.example.utils.UserContext;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final OrderMapper orderMapper;

    private final OrderDetailMapper detailMapper;

    @Override
    public Result<List<BusinessDataListVO>> turnoverStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = begin.datesUntil(end.plusDays(1)).toList();
        LocalDateTime beginTime = LocalDateTime.of(begin,LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end,LocalTime.MAX);
        List<BusinessDataListVO> dailyBusinessData = orderMapper.getDailyBusinessData(beginTime, endTime, Order.Completed);
        List<BusinessDataListVO> resultList = generate(dateList, dailyBusinessData);
        return Result.success(resultList);
    }

    @Override
    public Result<List<DishTopVO>> top10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin,LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end,LocalTime.MAX);
        List<DishTopVO> top10DishByDateRange = detailMapper.getTop10DishByDateRange(beginTime, endTime, Order.Completed);
        return Result.success(top10DishByDateRange);
    }

    @Override
    public void export(LocalDate begin, LocalDate end, HttpServletResponse response) {
        List<LocalDate> dateList = begin.datesUntil(end.plusDays(1)).toList();
        LocalDateTime beginTime = LocalDateTime.of(begin,LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end,LocalTime.MAX);
        List<BusinessDataListVO> dailyBusinessData = orderMapper.getDailyBusinessData(beginTime, endTime, Order.Completed);
        List<BusinessDataListVO> generate = generate(dateList, dailyBusinessData);
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("数据报表");
        Row row = sheet.createRow(0);
        List<String> header = new ArrayList<>(Arrays.asList(
                "日期", "当日营业额", "有效订单数", "订单完成率", "客单价", "总订单数"
        ));
        for (int i = 0 ;i<header.size();i++){
            Cell cell = row.createCell(i);
            cell.setCellValue(header.get(i));
        }
        for (int i = 1;i<generate.size();i++){
            Row row1 = sheet.createRow(i);
            Cell cell0 = row1.createCell(0);
            cell0.setCellValue(generate.get(i-1).getOrderDate());
            Cell cell1 = row1.createCell(1);
            cell1.setCellValue(generate.get(i-1).getTurnover().toPlainString());
            Cell cell2 = row1.createCell(2);
            cell2.setCellValue(generate.get(i-1).getValidOrderCount());
            Cell cell3 = row1.createCell(3);
            cell3.setCellValue(generate.get(i-1).getUnitPrice());
            Cell cell4 = row1.createCell(4);
            cell4.setCellValue(generate.get(i-1).getNewOrders());
        }
        try{
            ServletOutputStream out = response.getOutputStream();
            // 设置响应头，告诉浏览器下载文件
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode("运营数据报表.xlsx", "UTF-8") + "\"");
            // 写入流
           workbook.write(out);
            // 关闭资源
            workbook.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Long> getUserRecommend(Long id) {
        boolean exist = judgeExist(id);
        if (!exist){
            List<DishTopVO> top5 = getTop5();
            List<Long>  resultList = top5.stream()
                    .map(DishTopVO::getDishId)
                    .toList();
            return resultList;
        }
        List<DishFrequencyDTO> dishFrequencyDTOS = orderMapper.listTop5DishByUserIdAndStatus(id, Order.Completed);
        List<Long> ids = dishFrequencyDTOS.stream()
                .map(DishFrequencyDTO::getDishId)
                .toList();
        List<CoPurchasedDishDTO> top5CoOccurrenceDishes = detailMapper.getTop5CoOccurrenceDishes(ids);
        List<Long> result = top5CoOccurrenceDishes.stream()
                .map(CoPurchasedDishDTO::getDishId)
                .toList();
        return result;
    }


    private List<DishTopVO> getTop5(){
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime begin = end.minusMonths(1);
        List<DishTopVO> top5DishByDateRange = detailMapper.getTop5DishByDateRange(begin, end, Order.Completed);
        return top5DishByDateRange;
    }


    //判断用户是否存在订单
    private boolean judgeExist(Long userId){
        if (userId == null){return false;}
        LambdaQueryWrapper<Order> lqw = new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId,userId);
        return orderMapper.exists(lqw);
    }

    private List<BusinessDataListVO> generate(List<LocalDate> dateList,List<BusinessDataListVO> dailyBusinessData){
        Map<String, BusinessDataListVO> dataMap = dailyBusinessData.stream()
                .collect(Collectors.toMap(BusinessDataListVO::getOrderDate, Function.identity()));
        List<BusinessDataListVO> resultList = new ArrayList<>();
        for (LocalDate localDate:dateList){
            String dateStr = localDate.toString();
            BusinessDataListVO businessDataListVO = dataMap.get(dateStr);
            if (businessDataListVO == null){
                businessDataListVO = BusinessDataListVO.builder()
                        .orderDate(dateStr)
                        .turnover(BigDecimal.ZERO)
                        .validOrderCount(0)
                        .orderCompletionRate(0.0)
                        .unitPrice(0.0)
                        .newOrders(0)
                        .build();
            }else {
                double unitPrice = 0.00;
                if (businessDataListVO.getValidOrderCount() != 0){
                    unitPrice = businessDataListVO.getTurnover()
                            .divide(BigDecimal.valueOf(businessDataListVO.getValidOrderCount()),2,RoundingMode.HALF_UP)
                            .doubleValue();
                }
                double orderCompletionRate = 0.0;
                if (businessDataListVO.getNewOrders() != 0){
                    orderCompletionRate  = businessDataListVO
                            .getValidOrderCount().doubleValue()/businessDataListVO.getNewOrders().doubleValue();
                }
                businessDataListVO.setUnitPrice(unitPrice);
                businessDataListVO.setOrderCompletionRate(orderCompletionRate);
            }
            resultList.add(businessDataListVO);
        }
        return resultList;
    }
}
