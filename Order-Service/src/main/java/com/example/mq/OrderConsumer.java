package com.example.mq;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.cache.CardCache;
import com.example.cache.OrderCache;
import com.example.mapper.OrderDetailMapper;
import com.example.mapper.OrderMapper;
import com.example.pojo.DTO.AddCardDTO;
import com.example.pojo.DTO.OrderRocketMQDTO;
import com.example.pojo.entity.Order;
import com.example.pojo.entity.OrderDetail;
import com.example.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RocketMQMessageListener(topic = "ORDER_TOPIC",consumerGroup = "Order_Submit_Group")
public class OrderConsumer implements RocketMQListener<OrderRocketMQDTO> {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper detailMapper;

    @Autowired
    private CardCache cardCache;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void onMessage(OrderRocketMQDTO order) {
        log.info("【消费者】接收到信息，开始执行写入数据库操作:{}",order);
        LambdaQueryWrapper<Order> lqw = new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNo,order.getOrder().getOrderNo());
        Order order1 = orderMapper.selectOne(lqw);
        if (order1 != null){
            log.error("【消费者】该订单已经创建：{}",order);
            return;
        }
        try{
            int insert = orderMapper.insert(order.getOrder());
            List<AddCardDTO> cartList = order.getList();
            if (insert>0){
                List<OrderDetail> orderDetails =  cartList.stream().map(item->{
                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrderId(order.getOrder().getId());
                    orderDetail.setName(item.getName());
                    Long dishId = item.getDishId() != null?item.getDishId():item.getSetmealId();
                    orderDetail.setDishId(dishId);
                    orderDetail.setImage(item.getImage());
                    orderDetail.setPrice(item.getPrice());
                    orderDetail.setQuantity(item.getNumber());
                    return orderDetail;
                }).toList();
                detailMapper.batchInsertByAnnotation(orderDetails);
                CompletableFuture.runAsync(()->{
                    cardCache.clearCart(order.getOrder().getUserId());
                });
                log.info("【提交订单】成功,用户：{},提交订单id：{}",order.getOrder().getUserId(),order.getOrder().getId());
            }else {
                throw new RuntimeException("订单主表写入失败");
            }
        }catch (Exception e){
            log.error("下单消费异常，单号: {}", order.getOrder().getOrderNo(), e);
            throw e;
        }

    }
}
