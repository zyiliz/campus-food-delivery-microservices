package com.example.service.Impl;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import apache.rocketmq.v2.MessageOrBuilder;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.cache.CardCache;
import com.example.client.AddressClient;
import com.example.convert.OrderConvert;
import com.example.mapper.OrderDetailMapper;
import com.example.mapper.OrderIndexMapper;
import com.example.mapper.OrderMapper;
import com.example.pojo.DTO.*;
import com.example.pojo.VO.*;
import com.example.pojo.entity.Order;
import com.example.pojo.entity.OrderDetail;
import com.example.pojo.entity.OrderIndex;
import com.example.result.PageResult;
import com.example.result.Result;
import com.example.service.OrderService;
import com.example.utils.UserContext;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.BatchResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.mockito.internal.matchers.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    private final OrderDetailMapper detailMapper;

    private final CardCache cardCache;

    private final OrderConvert orderConvert;

    private final AddressClient addressClient;

    private final RocketMQTemplate rocketMQTemplate;

    private final OrderIndexMapper orderIndexMapper;

    @Transactional
    @Override
    public Result<OrderSubmitVO> orderSubmit(OrderSubmitDTO orderSubmitDTO) {
        Long userId = UserContext.getId();
        log.info("【提交订单】用户：{},提交订单：{}",userId,orderSubmitDTO);
        List<AddCardDTO> cartList = cardCache.getCard(userId);
        if (cartList.isEmpty()){
            return Result.fail("购物车为空！");
        }
        Result<AddressVO> addressById = addressClient.getAddressById(orderSubmitDTO.getAddress());
        if (addressById == null ||addressById.getData() == null){
            return Result.fail("地址存在错误！");
        }
        BigDecimal allNumber = BigDecimal.ZERO;
        for (AddCardDTO addCardDTO:cartList){
            allNumber = allNumber.add(addCardDTO.getPrice().multiply(BigDecimal.valueOf(addCardDTO.getNumber())));
        }
        Order order = new Order();
        String phone = addressById.getData().getPhone();
        order.setPhone(phone);
        order.setUserId(userId);
        order.setTotalAmount(allNumber);
        AddressVO addressVO = addressById.getData();
        String address = addressVO.getConsignee()+":"+addressVO.getPhone()+":"+addressVO.getDetail();
        order.setAddress(address);
        order.setPayMethod(orderSubmitDTO.getPayMethod());
        String orderNo = generateOrderNo(userId);
        order.setOrderNo(orderNo);
        order.setStatus(1);
        int insert = orderMapper.insert(order);

        Long orderId = order.getId();
        if (insert>0){
            List<OrderDetail> orderDetails =  cartList.stream().map(item->{
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderId(orderId);
                orderDetail.setName(item.getName());
                Long dishId = item.getDishId() != null?item.getDishId():item.getSetmealId();
                orderDetail.setDishId(dishId);
                orderDetail.setImage(item.getImage());
                orderDetail.setPrice(item.getPrice());
                orderDetail.setQuantity(item.getNumber());
                return orderDetail;
            }).toList();
            detailMapper.batchInsertByAnnotation(orderDetails);
            log.info("【提交订单】成功,用户：{},提交订单id：{}",userId,orderId);
        }else {
            log.error("【提交订单】成功,用户id:{}",userId);
            return Result.fail("购买失败，请稍后再试！");
        }
        cardCache.clearCart(userId);
        OrderSubmitVO orderSubmitVO = new OrderSubmitVO(orderNo,allNumber,order.getCreateTime());
        return Result.success(orderSubmitVO);
    }


    @Override
    public Result<String> payment(PaymentDTO paymentDTO) {
        Long userId = UserContext.getId();
        log.info("【支付订单】用户：{},支付订单：{}",userId,paymentDTO);
        LambdaQueryWrapper<Order> lqw = new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId,userId)
                .eq(Order::getOrderNo,paymentDTO.getOrderNo());
        Order order = orderMapper.selectOne(lqw);
        if (ObjectUtil.isNull(order)){
            return Result.fail("订单不存在！");
        }
        if (order.getStatus() != Order.Unpaid){
            return Result.fail("该订单已经支付！");
        }
        LambdaUpdateWrapper<Order> luw = new LambdaUpdateWrapper<Order>()
                .eq(Order::getUserId,userId)
                .eq(Order::getOrderNo,paymentDTO.getOrderNo())
                .set(paymentDTO.getPayMethod()!= null,Order::getPayMethod,paymentDTO.getPayMethod())
                .set(Order::getPayTime, LocalDateTime.now())
                .set(Order::getStatus,Order.Waiting);
        int update = orderMapper.update(null, luw);
        if (update>0){
            log.info("【支付订单】成功用户：{}",userId);
            return Result.success("支付成功");
        }else {
            log.error("【支付订单】失败用户：{}",userId);
            return Result.fail("支付失败");
        }
    }

    @Override
    public Result<PageResult> historyOrders(OrderPageQueryDTO orderPageQueryDTO) {
        Long userId = UserContext.getId();
        LambdaQueryWrapper<Order> lqw = new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId,userId)
                .eq(orderPageQueryDTO.getStatus() != null,Order::getStatus,orderPageQueryDTO.getStatus())
                .orderByAsc(Order::getCreateTime);
        Page<Order> page = new Page<>(orderPageQueryDTO.getPage(),orderPageQueryDTO.getPageSize());
        Page<Order> page1 = orderMapper.selectPage(page, lqw);
        List<Order> orders = page1.getRecords();
        List<OrderVO> orderVOList = new ArrayList<>();
        if (!orders.isEmpty()){
            List<Long> orderIDList = orders.stream()
                    .map(Order::getId)
                    .collect(Collectors.toList());
            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<OrderDetail>()
                    .in(OrderDetail::getOrderId,orderIDList);
            List<OrderDetail> orderDetails = detailMapper.selectList(orderDetailLambdaQueryWrapper);
            for (Order order :orders){
                OrderVO orderVO = orderConvert.toOrderVO(order);
                List<OrderDetail> orderDetailList = new ArrayList<>();
                for (OrderDetail orderDetail : orderDetails){
                    if (Objects.equals(orderDetail.getOrderId(), orderVO.getId())){
                        orderDetailList.add(orderDetail);
                    }
                }
                orderVO.setOrderDetails(orderDetailList);
                orderVOList.add(orderVO);
            }
        }
        return Result.success(new PageResult(page1.getTotal(),orderVOList));
    }

    @Override
    public Result<OrderVO> getOrderDetail(Long id) {
        Long userId = UserContext.getId();
        LambdaQueryWrapper<Order> lqw  = new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId,userId)
                .eq(id != null,Order::getId,id);
        Order order = orderMapper.selectOne(lqw);
        if (ObjectUtil.isNull(order)){
            return Result.fail("该订单不存在");
        }
        LambdaQueryWrapper<OrderDetail> lambdaQueryWrapper = new LambdaQueryWrapper<OrderDetail>()
                .eq(OrderDetail::getOrderId,id);
        List<OrderDetail> orderDetailList = detailMapper.selectList(lambdaQueryWrapper);
        OrderVO orderVO = orderConvert.toOrderVO(order);
        orderVO.setOrderDetails(orderDetailList);
        return Result.success(orderVO);
    }

    @Override
    public Result<String> cancel(Long id) {
        Long userId = UserContext.getId();
        log.info("【用户取消订单】,用户id：{},订单id:{}",userId,id);
        LambdaQueryWrapper<Order> lqw = new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId,userId)
                .eq(id != null,Order::getId,id);
        Order order = orderMapper.selectOne(lqw);
        if (ObjectUtil.isNull(order)){
            return Result.fail("订单不存在！");
        }
        if (Objects.equals(order.getStatus(),Order.Delivery)){
            return Result.fail("订单正在派送，无法取消");
        } else if (Objects.equals(order.getStatus(),Order.Completed)) {
            return Result.fail("订单已完成，无法取消");
        } else if (Objects.equals(order.getStatus(),Order.Canceled)) {
            return Result.fail("订单已取消");
        }else{
            LambdaUpdateWrapper<Order> luw = new LambdaUpdateWrapper<Order>()
                    .eq(Order::getUserId,userId)
                    .eq(Order::getId,id)
                    .set(Order::getStatus,Order.Canceled);
            int update = orderMapper.update(null, luw);
            if (update>0){
                log.info("【用户取消订单】成功,用户id：{},订单id:{}",userId,id);
                return Result.success("订单取消成功！");
            }else {
                log.error("【用户取消订单】失败,用户id：{},订单id:{}",userId,id);
                return Result.fail("系统繁忙，请重试！");
            }
        }
    }

    @Override
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        LambdaQueryWrapper<Order> lqw = new LambdaQueryWrapper<Order>()
                .like(ordersPageQueryDTO.getOrderNo()!= null,Order::getOrderNo,ordersPageQueryDTO.getOrderNo())
                .like(ordersPageQueryDTO.getStatus() != null,Order::getStatus,ordersPageQueryDTO.getStatus())
                .like(ordersPageQueryDTO.getPhone() != null,Order::getPhone,ordersPageQueryDTO.getPhone())
                .like(ordersPageQueryDTO.getPayMethod() != null ,Order::getPayMethod,ordersPageQueryDTO.getPayMethod())
                .gt(ordersPageQueryDTO.getCreateTime() != null,Order::getCreateTime,ordersPageQueryDTO.getCreateTime())
                .le(ordersPageQueryDTO.getPayTime() != null,Order::getPayTime,ordersPageQueryDTO.getPayTime())
                .orderByDesc(Order::getCreateTime);
        Page<Order> page = new Page<>(ordersPageQueryDTO.getPage(),ordersPageQueryDTO.getPageSize());
        Page<Order> page1 = orderMapper.selectPage(page, lqw);
        PageResult pageResult = new PageResult(page1.getTotal(),page1.getRecords());
        return Result.success(pageResult);
    }

    @Override
    public Result<OrderVO> details(Long id) {
        LambdaQueryWrapper<Order> lqw  = new LambdaQueryWrapper<Order>()
                .eq(id != null,Order::getId,id);
        Order order = orderMapper.selectOne(lqw);
        if (ObjectUtil.isNull(order)){
            return Result.fail("该订单不存在");
        }
        LambdaQueryWrapper<OrderDetail> lambdaQueryWrapper = new LambdaQueryWrapper<OrderDetail>()
                .eq(OrderDetail::getOrderId,id);
        List<OrderDetail> orderDetailList = detailMapper.selectList(lambdaQueryWrapper);
        OrderVO orderVO = orderConvert.toOrderVO(order);
        orderVO.setOrderDetails(orderDetailList);
        return Result.success(orderVO);
    }

    @Override
    public Result<String> confirm(Long id) {
        log.info("【商家接单】,订单id:{}",id);
        LambdaQueryWrapper<Order> lqw = new LambdaQueryWrapper<Order>()
                .eq(Order::getId,id);
        Order order = orderMapper.selectOne(lqw);
        if (ObjectUtil.isNull(order)){
            return Result.fail("订单不存在！");
        } else if (!Order.Waiting.equals(order.getStatus())) {
            return Result.fail("该订单已经接单！");
        }else {
            LambdaUpdateWrapper<Order> luw = new LambdaUpdateWrapper<Order>()
                    .eq(Order::getId,id)
                    .set(Order::getStatus,Order.Confirm);
            int update = orderMapper.update(null, luw);
            if (update>0){
                log.info("【商家接单】成功,订单id:{}",id);
                return Result.success("成功接单！");
            }else {
                log.error("【商家接单】失败,订单id:{}",id);
                return Result.fail("接单失败，请稍后再试！");
            }
        }
    }

    @Override
    public Result<String> rejection(OrdersRejectionDTO ordersRejectionDTO) {
        log.info("【商家拒单】,请求参数：{}",ordersRejectionDTO);
        LambdaQueryWrapper<Order> lqw = new LambdaQueryWrapper<Order>()
                .eq(Order::getId,ordersRejectionDTO.getId());
        Order order = orderMapper.selectOne(lqw);
        if (ObjectUtil.isNull(order)){
            return Result.fail("订单不存在！");
        }
        if (!order.getStatus().equals(Order.Waiting)){
            return Result.fail("订单不处于待接单状态！");
        }
        LambdaUpdateWrapper<Order> luw = new LambdaUpdateWrapper<Order>()
                .eq(Order::getId,ordersRejectionDTO.getId())
                .set(Order::getStatus, Order.Canceled)
                .set(Order::getCannelTime,LocalDateTime.now());
        int update = orderMapper.update(null, luw);
        if (update>0){
            log.info("【商家拒单】成功,订单id：{}",ordersRejectionDTO.getId());
            return Result.success(ordersRejectionDTO.getRejectionReason());
        }else {
            log.info("【商家拒单】失败,订单id：{}",ordersRejectionDTO.getId());
            return Result.fail("拒单失败，请稍后再试！");
        }
    }

    @Override
    public Result<String> delivery(Long id) {
        log.info("【商家出餐】,订单id：{}",id);
        LambdaQueryWrapper<Order> lqw = new LambdaQueryWrapper<Order>()
                .eq(Order::getId,id);
        Order order = orderMapper.selectOne(lqw);
        if (ObjectUtil.isNull(order)){return Result.fail("订单不存在！");}
        if (!order.getStatus().equals(Order.Confirm)){
            return Result.fail("订单不处于接单状态！");
        }
        LambdaUpdateWrapper<Order> luw = new LambdaUpdateWrapper<Order>()
                .eq(Order::getId,id)
                .set(Order::getStatus,Order.Delivery)
                .set(Order::getDeliveryTime,LocalDateTime.now());
        int update = orderMapper.update(null, luw);
        if (update>0){
            log.info("【商家出餐】成功,订单id：{}",id);
            return Result.success("出餐成功！");
        }else {
            log.error("【商家出餐】失败,订单id：{}",id);
            return Result.fail("出餐失败，请稍后再试！");
        }
    }

    @Override
    public Result<String> complete(Long id) {
        log.info("【订单完成】,订单id：{}",id);
        LambdaQueryWrapper<Order> lqw = new LambdaQueryWrapper<Order>()
                .eq(Order::getId,id);
        Order order = orderMapper.selectOne(lqw);
        if (ObjectUtil.isNull(order)){return Result.fail("订单不存在！");}
        if (!order.getStatus().equals(Order.Delivery)){
            return Result.fail("订单不处于派送状态！");
        }
        LambdaUpdateWrapper<Order> luw = new LambdaUpdateWrapper<Order>()
                .eq(Order::getId,id)
                .set(Order::getStatus,Order.Completed);
        int update = orderMapper.update(null, luw);
        if (update>0){
            log.info("【订单完成】成功,订单id：{}",id);
            return Result.success("出餐成功！");
        }else {
            log.error("【订单完成】失败,订单id：{}",id);
            return Result.fail("出餐失败，请稍后再试！");
        }

    }

    @Override
    public Result<BusinessDataVO> businessData(LocalDate localDate) {
        LocalDateTime begin = LocalDateTime.of(localDate, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(localDate, LocalTime.MAX);
        LambdaQueryWrapper<Order> lqw = new LambdaQueryWrapper<Order>()
                .ge(Order::getCreateTime,begin)
                .le(Order::getCreateTime,end);
        Long all = orderMapper.selectCount(lqw);
        LambdaQueryWrapper<Order> validOrderCountLqw = new LambdaQueryWrapper<Order>()
                .ge(Order::getCreateTime,begin)
                .le(Order::getCreateTime,end)
                .eq(Order::getStatus,Order.Completed);
        Long validCount = orderMapper.selectCount(validOrderCountLqw);

        BigDecimal allNum = orderMapper.selectOrderAmountSumByDateRangeAndStatus(begin,end,Order.Completed);
        if (allNum == null){
            allNum = BigDecimal.ZERO;
        }
        LambdaQueryWrapper<Order> cancelCountLqw = new LambdaQueryWrapper<Order>()
                .ge(Order::getCreateTime,begin)
                .le(Order::getCreateTime,end)
                .eq(Order::getStatus,Order.Canceled);
        Long cancelNum = orderMapper.selectCount(cancelCountLqw);
        double unitPrice = 0.00;
        if (validCount != 0){
            unitPrice = allNum.divide(BigDecimal.valueOf(validCount),2,RoundingMode.HALF_UP).doubleValue();
        }
        double orderCompletionRate = 0.0;
        if (all != 0){
            orderCompletionRate  = validCount.doubleValue()/all.doubleValue();
        }
        BusinessDataVO businessDataVO = BusinessDataVO.builder()
                .turnover(allNum)
                .validOrderCount(Math.toIntExact(validCount))
                .orderCompletionRate(orderCompletionRate)
                .unitPrice(unitPrice)
                .newOrders(Math.toIntExact(all))
                .build();
        return Result.success(businessDataVO);
    }

    @Override
    public Result<OrderOverViewVO> overviewOrders() {
        LocalDate localDate = LocalDate.now();
        LocalDateTime begin = LocalDateTime.of(localDate,LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(localDate,LocalTime.MAX);
        LambdaQueryWrapper<Order> lqw = new LambdaQueryWrapper<Order>()
                .ge(Order::getCreateTime,begin)
                .le(Order::getCreateTime,end);
        Long all = orderMapper.selectCount(lqw);
        LambdaQueryWrapper<Order> waitingOrdersLQW = new LambdaQueryWrapper<Order>()
                .ge(Order::getCreateTime,begin)
                .le(Order::getCreateTime,end)
                .eq(Order::getStatus,Order.Waiting);
        Long waitingOrders = orderMapper.selectCount(waitingOrdersLQW);
        LambdaQueryWrapper<Order> deliveredOrdersLQW = new LambdaQueryWrapper<Order>()
                .ge(Order::getCreateTime,begin)
                .le(Order::getCreateTime,end)
                .eq(Order::getStatus,Order.Delivery);
        Long deliveredOrders = orderMapper.selectCount(deliveredOrdersLQW);
        LambdaQueryWrapper<Order> completedOrdersLQW = new LambdaQueryWrapper<Order>()
                .ge(Order::getCreateTime,begin)
                .le(Order::getCreateTime,end)
                .eq(Order::getStatus,Order.Completed);
        Long completedOrders = orderMapper.selectCount(completedOrdersLQW);
        LambdaQueryWrapper<Order> cancelledOrdersLQW = new LambdaQueryWrapper<Order>()
                .ge(Order::getCreateTime,begin)
                .le(Order::getCreateTime,end)
                .eq(Order::getStatus,Order.Canceled);
        Long cancelledOrders = orderMapper.selectCount(cancelledOrdersLQW);
        OrderOverViewVO orderOverViewVO = OrderOverViewVO.builder()
                .waitingOrders(waitingOrders)
                .deliveredOrders(deliveredOrders)
                .completedOrders(completedOrders)
                .cancelledOrders(cancelledOrders)
                .allOrders(all)
                .build();
        return Result.success(orderOverViewVO);
    }

    @Override
    public Result<List<OrderAdminVO>> getOrderList() {
        List<OrderAdminVO> orderAdminVOS = orderMapper.listAllOrderAdminVO();
        return Result.success(orderAdminVOS);
    }

    @Override
    public Result<List<OrderAdminVO>> getOrderListByStatus(Integer status) {
        List<OrderAdminVO> orderAdminVOS = orderMapper.listOrderAdminVOByStatus(status);
        return Result.success(orderAdminVOS);
    }

    @Override
    public Result<String> submitOrderByRocketmq(OrderSubmitDTO orderSubmitDTO) {
        Long userId = UserContext.getId();
        log.info("【提交订单】用户：{},提交订单：{}",userId,orderSubmitDTO);
        List<AddCardDTO> cartList = cardCache.getCard(userId);
        if (cartList.isEmpty()){
            return Result.fail("购物车为空！");
        }
        Result<AddressVO> addressById = addressClient.getAddressById(orderSubmitDTO.getAddress());
        if (addressById == null ||addressById.getData() == null){
            return Result.fail("地址存在错误！");
        }
        BigDecimal allNumber = BigDecimal.ZERO;
        for (AddCardDTO addCardDTO:cartList){
            allNumber = allNumber.add(addCardDTO.getPrice().multiply(BigDecimal.valueOf(addCardDTO.getNumber())));
        }
        String orderNo = generateOrderNo(userId);
        Order order = new Order();
        String phone = addressById.getData().getPhone();
        order.setPhone(phone);
        order.setUserId(userId);
        order.setTotalAmount(allNumber);
        AddressVO addressVO = addressById.getData();
        String address = addressVO.getConsignee()+":"+addressVO.getPhone()+":"+addressVO.getDetail();
        order.setAddress(address);
        order.setPayMethod(orderSubmitDTO.getPayMethod());
        order.setOrderNo(orderNo);
        order.setStatus(1);
        OrderRocketMQDTO orderRocketMQDTO= OrderRocketMQDTO.builder()
                    .order(order)
                    .list(cartList)
                    .build();
        Message<OrderRocketMQDTO> message = MessageBuilder.withPayload(orderRocketMQDTO)
                .setHeader(RocketMQHeaders.TRANSACTION_ID, orderRocketMQDTO.getOrder().getOrderNo())
                .build();
        rocketMQTemplate.sendMessageInTransaction("ORDER_TOPIC",message,null);
        log.info("【生产者】消息发送成功: {}",orderNo);
        return Result.success("下单成功！");


    }


    //生成订单号
    private static String generateOrderNo(Long userId){
        String userStr = userId.toString();
        String userSuffix;
        if (userStr.length() > 4) {
            userSuffix = userStr.substring(userStr.length() - 4);
        } else {
            userSuffix = String.format("%04d", userId);
        }

        // 2. 处理日期 (yyyyMMddHHmmss -> 14位)
        String dateStr = DateUtil.format(new Date(), "yyyyMMddHHmmss");

        // 处理雪花算法 (取后6位)
        // 雪花算法生成的ID很长(19位)，需要它的"随机性"来防重
        String snowflakeId = IdUtil.getSnowflakeNextIdStr();
        String snowSuffix = snowflakeId.substring(snowflakeId.length() - 6);

        // 4. 拼接返回
        // 最终长度：4 + 14 + 6 = 24位
        return userSuffix + dateStr + snowSuffix;

    }
}
