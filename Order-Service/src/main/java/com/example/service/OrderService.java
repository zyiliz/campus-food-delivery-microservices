package com.example.service;

import com.example.pojo.DTO.*;
import com.example.pojo.VO.*;
import com.example.result.PageResult;
import com.example.result.Result;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    Result<OrderSubmitVO> orderSubmit(OrderSubmitDTO orderSubmitDTO);

    Result<String> payment(PaymentDTO paymentDTO);

    Result<PageResult> historyOrders(OrderPageQueryDTO orderPageQueryDTO);

    Result<OrderVO> getOrderDetail(Long id);

    Result<String> cancel(Long id);

    Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    Result<OrderVO> details(Long id);

    Result<String> confirm(Long id);

    Result<String> rejection(OrdersRejectionDTO ordersRejectionDTO);

    Result<String> delivery(Long id);

    Result<String> complete(Long id);

    Result<BusinessDataVO> businessData(LocalDate localDate);

    Result<OrderOverViewVO> overviewOrders();

    Result<List<OrderAdminVO>> getOrderList();

    Result<List<OrderAdminVO>> getOrderListByStatus(Integer status);

    Result<String> submitOrderByRocketmq(OrderSubmitDTO orderSubmitDTO);




}
