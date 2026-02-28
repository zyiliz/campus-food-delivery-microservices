package com.example.mq;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.mapper.OrderIndexMapper;
import com.example.pojo.DTO.OrderRocketMQDTO;
import com.example.pojo.entity.OrderIndex;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RocketMQTransactionListener
public class OrderTransactionListener implements RocketMQLocalTransactionListener {

    @Autowired
    private OrderIndexMapper orderIndexMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        try {
            OrderRocketMQDTO rocketMQDTO = (OrderRocketMQDTO) message.getPayload();
            log.info("【本地事务】执行，开始执行预下单,订单号为：{}",rocketMQDTO.getOrder().getOrderNo());
            OrderIndex orderIndex = OrderIndex.builder()
                    .orderNo(rocketMQDTO.getOrder().getOrderNo())
                    .userId(rocketMQDTO.getOrder().getUserId())
                    .status(1)
                    .amount(rocketMQDTO.getOrder().getTotalAmount())
                    .build();
            orderIndexMapper.insert(orderIndex);
            return RocketMQLocalTransactionState.COMMIT;
        }catch (Exception e){
            log.error("【本地事务】执行失败，回滚消息",e);
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        OrderRocketMQDTO orderRocketMQDTO = (OrderRocketMQDTO) message.getPayload();
        log.info("【本地事务】回查，开始执行预下单,订单号为：{}",orderRocketMQDTO.getOrder().getOrderNo());
        LambdaQueryWrapper<OrderIndex> orderIndexLambdaQueryWrapper = new LambdaQueryWrapper<OrderIndex>()
                .eq(OrderIndex::getOrderNo,orderRocketMQDTO.getOrder().getOrderNo());
        Long l = orderIndexMapper.selectCount(orderIndexLambdaQueryWrapper);
        return l>0?RocketMQLocalTransactionState.COMMIT:RocketMQLocalTransactionState.ROLLBACK;
    }
}
