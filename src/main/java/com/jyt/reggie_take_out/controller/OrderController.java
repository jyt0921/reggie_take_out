package com.jyt.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jyt.reggie_take_out.common.BaseContext;
import com.jyt.reggie_take_out.common.R;
import com.jyt.reggie_take_out.dto.DishDto;
import com.jyt.reggie_take_out.entity.Category;
import com.jyt.reggie_take_out.entity.Dish;
import com.jyt.reggie_take_out.entity.Orders;
import com.jyt.reggie_take_out.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<Page> page(int page, int pageSize){
        log.info("page= {},pageSize={}",page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Orders> qw=new LambdaQueryWrapper();
        //添加过滤条件
        qw.eq(Orders::getUserId, BaseContext.getCurrentId());
        List<Orders> list = orderService.list(qw);
        pageSize=list.size();
        //构造分页构造器
        Page <Orders> pageInfo=new Page<>(page,pageSize);
        //添加排序条件
        qw.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo,qw);
        return R.success(pageInfo);
    }
    @GetMapping("/page")
    public R<Page> page1(int page, int pageSize){
        log.info("page= {},pageSize={}",page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Orders> qw=new LambdaQueryWrapper();
        //添加过滤条件//构造分页构造器
        Page <Orders> pageInfo=new Page<>(page,pageSize);
        //添加排序条件
        qw.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo,qw);
        return R.success(pageInfo);
    }
}
