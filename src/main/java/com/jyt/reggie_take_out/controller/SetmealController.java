package com.jyt.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jyt.reggie_take_out.common.R;
import com.jyt.reggie_take_out.dto.SetmealDto;
import com.jyt.reggie_take_out.entity.Category;
import com.jyt.reggie_take_out.entity.Setmeal;
import com.jyt.reggie_take_out.entity.SetmealDish;
import com.jyt.reggie_take_out.service.CategoryService;
import com.jyt.reggie_take_out.service.SetmealDishService;
import com.jyt.reggie_take_out.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/*
    套餐管理
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;
    @Autowired
    private CategoryService categoryService;

    //新增套餐
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info(String.valueOf(setmealDto));
        setmealService.saveWithDish(setmealDto);
        return R.success("新增成功");
    }

    //展示套餐
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        //构造分页构造器
        Page<Setmeal> page1 = new Page<>(page,pageSize);
        Page<SetmealDto> dtoPage=new Page<>(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(name!=null,Setmeal::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Setmeal::getCreateTime);
        //到这一块已经查出除了种类之外的所有值
        setmealService.page(page1,queryWrapper);

        //因为records中没有菜品种类需要自己设置所以这块就不填充了
        BeanUtils.copyProperties(page1,dtoPage,"records");
        List<Setmeal> records = page1.getRecords();
        List<SetmealDto> list=records.stream().map((item)->{
            SetmealDto dto = new SetmealDto();
            BeanUtils.copyProperties(item,dto);
            Long categoryId = item.getCategoryId();
            Category category=categoryService.getById(categoryId);
            if(category!=null){
                String name1 = category.getName();
                dto.setCategoryName(name1);
            }
            return dto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(list);

        return R.success(dtoPage);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids:{}",ids);
        setmealService.removeWithDish(ids);
        return R.success("套餐数据删除成功");
    }

    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        wrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        wrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(wrapper);
        return R.success(list);
    }
}
