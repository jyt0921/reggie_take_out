package com.jyt.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jyt.reggie_take_out.common.CustomException;
import com.jyt.reggie_take_out.entity.Category;
import com.jyt.reggie_take_out.entity.Dish;
import com.jyt.reggie_take_out.entity.Setmeal;
import com.jyt.reggie_take_out.mapper.CategoryMapper;
import com.jyt.reggie_take_out.service.CategoryService;
import com.jyt.reggie_take_out.service.DishService;
import com.jyt.reggie_take_out.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{
    /*
        根据id删除分类，删除之前需要进行判断
     */
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        //查询当前分类是否关联了菜品，如果已经失联，抛出一个业务异常
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        wrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(wrapper);
        if(count1>0){
            //已经关联菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }


        //查询当前分类是否关联了套餐，如果已经失联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> wrapper1 = new LambdaQueryWrapper<>();
        //添加查询条件，根据分类id进行查询
        wrapper1.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(wrapper1);
        if(count2>0){
            //已经关联菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        //正常删除分类
        super.removeById(id);
    }
}
