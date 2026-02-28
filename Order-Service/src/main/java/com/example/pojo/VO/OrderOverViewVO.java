package com.example.pojo.VO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderOverViewVO {
    private Long waitingOrders;
    private Long deliveredOrders;
    private Long completedOrders;
    private Long cancelledOrders;
    private Long allOrders;
}
