package com.example.controller;

import com.example.pojo.DTO.OrderPageQueryDTO;
import com.example.pojo.DTO.OrderSubmitDTO;
import com.example.pojo.DTO.PaymentDTO;
import com.example.pojo.VO.OrderSubmitVO;
import com.example.pojo.VO.OrderUserVO;
import com.example.pojo.VO.OrderVO;
import com.example.result.PageResult;
import com.example.result.Result;
import com.example.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/user/order")
public class OrderUserController {
    private final OrderService orderService;

    @PostMapping("")
    public Result<OrderSubmitVO> submitOrder(@RequestBody OrderSubmitDTO orderSubmitDTO){
        return orderService.orderSubmit(orderSubmitDTO);
    }

    @PutMapping("/payment")
    public Result<String> payment(@Validated @RequestBody PaymentDTO paymentDTO){
        return orderService.payment(paymentDTO);
    }

    @GetMapping("/historyOrders")
    public Result<PageResult> historyOrders(@Validated @RequestBody OrderPageQueryDTO orderPageQueryDTO){
        return orderService.historyOrders(orderPageQueryDTO);
    }

    @GetMapping("/orderDetail/{id}")
    public Result<OrderVO> getOrderDetail(@PathVariable Long id){
        return orderService.getOrderDetail(id);
    }

    @PutMapping("/cancel/{id}")
    public Result<String> cancel(@PathVariable Long id){
        return orderService.cancel(id);
    }
}
