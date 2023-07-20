package com.jyt.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jyt.reggie_take_out.entity.Orders;

public interface OrderService extends IService<Orders> {

    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);
}
