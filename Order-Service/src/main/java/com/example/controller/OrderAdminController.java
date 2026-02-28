package com.example.controller;

import com.example.pojo.DTO.OrdersPageQueryDTO;
import com.example.pojo.DTO.OrdersRejectionDTO;
import com.example.pojo.VO.BusinessDataVO;
import com.example.pojo.VO.OrderAdminVO;
import com.example.pojo.VO.OrderOverViewVO;
import com.example.pojo.VO.OrderVO;
import com.example.result.PageResult;
import com.example.result.Result;
import com.example.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/admin/order")
public class OrderAdminController {

    private final OrderService orderService;

    @PostMapping("/conditionSearch")
    public Result<PageResult> conditionSearch(@RequestBody OrdersPageQueryDTO ordersPageQueryDTO){
        return orderService.conditionSearch(ordersPageQueryDTO);
    }

    //订单详情查询接口
    @GetMapping("/details/{id}")
    public Result<OrderVO> details(@PathVariable Long id){
        return orderService.details(id);
    }



    @PutMapping("/rejection")
    public Result<String> rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO){
        return orderService.rejection(ordersRejectionDTO);
    }

    @PutMapping("/confirm/{id}")
    public Result<String> confirm(@PathVariable Long id){
        return orderService.confirm(id);
    }

    @PutMapping("/delivery/{id}")
    public Result<String> delivery(@PathVariable Long id){
        return orderService.delivery(id);
    }

    @PutMapping("/complete/{id}")
    public Result<String> complete(@PathVariable Long id){
        return orderService.complete(id);
    }

    @GetMapping("/businessData")
    public Result<BusinessDataVO> businessData(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date){
        if (date == null){
            date = LocalDate.now();
        }
        return orderService.businessData(date);
    }

    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> overviewOrders(){
        return orderService.overviewOrders();
    }


    //订单列表查询接口
    @GetMapping("/listAllOrderAdmin")
    public Result<List<OrderAdminVO>>  listAllOrderAdmin(){
        return orderService.getOrderList();
    }


    //不同状态订单列表查询
    @GetMapping("/listAllOrderAdminByStatus/{status}")
    public Result<List<OrderAdminVO>> listAllOrderAdminByStatus(@PathVariable Integer status){
        return orderService.getOrderListByStatus(status);
    }

}
