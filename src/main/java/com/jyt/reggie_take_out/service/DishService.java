package com.jyt.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jyt.reggie_take_out.dto.DishDto;
import com.jyt.reggie_take_out.entity.Dish;
import org.springframework.transaction.annotation.Transactional;

public interface DishService extends IService<Dish> {

    //新增菜品，同时插入菜品对应的口味数据，需要操作两张表：要定义新的方法
    public void saveWithFlavor(DishDto dishDto);

    //查询菜品信息，以及其所对应的口味信息
    public DishDto getByIdWithFlavor(Long id);

    public void UpdateWithFlavor(DishDto dishDto);
}
