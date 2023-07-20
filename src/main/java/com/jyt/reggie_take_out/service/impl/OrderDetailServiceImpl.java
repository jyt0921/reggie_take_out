package com.jyt.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jyt.reggie_take_out.entity.OrderDetail;
import com.jyt.reggie_take_out.mapper.OrderDetailMapper;
import com.jyt.reggie_take_out.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService{
}
