package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: ZLey
 * @description
 * @date: 2023/10/11 15:45
 */

@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;
    @Scheduled(cron = "0 * * * * ?")
    public void processTimeoutOrder(){
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> list=orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT,time);

        if(list!=null && list.size()>0){
            for (Orders orders : list) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelTime(LocalDateTime.now());
                orders.setCancelReason("订单支付超市，自动取消支付");
                orderMapper.update(orders);
            }
        }
    }
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryTime() {
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> list = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);
        if (list != null && list.size() > 0) {
            for (Orders orders : list) {
                orders.setStatus(Orders.COMPLETED);

                orderMapper.update(orders);
            }
        }
    }

}
