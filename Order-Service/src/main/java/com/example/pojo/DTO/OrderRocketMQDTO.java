package com.example.pojo.DTO;

import com.example.pojo.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRocketMQDTO {
    private Order order;
    private List<AddCardDTO> list;
}
