package com.example.convert;


import com.example.pojo.DTO.OrderSubmitDTO;
import com.example.pojo.VO.OrderVO;
import com.example.pojo.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderConvert {

    Order toOrder(OrderSubmitDTO orderSubmitDTO);

    OrderVO toOrderVO(Order order);
}
