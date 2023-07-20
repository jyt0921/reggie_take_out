package com.jyt.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jyt.reggie_take_out.common.R;
import com.jyt.reggie_take_out.dto.DishDto;
import com.jyt.reggie_take_out.entity.Category;
import com.jyt.reggie_take_out.entity.Dish;
import com.jyt.reggie_take_out.entity.DishFlavor;
import com.jyt.reggie_take_out.service.CategoryService;
import com.jyt.reggie_take_out.service.DishFlavorService;
import com.jyt.reggie_take_out.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;


//    新增菜单
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("增加成功");
    }

    //查询展示菜单(包含了多表查询数据！！！)
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        log.info("page= {},pageSize={},name={}",page,pageSize,name);
        //构造分页构造器
        Page <Dish> pageInfo=new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage=new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Dish> qw=new LambdaQueryWrapper();
        //添加过滤条件
        qw.like(StringUtils.isNotEmpty(name),Dish::getName,name);
        //添加排序条件
        qw.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo,qw);

        //对象拷贝
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");
        List<Dish> records=pageInfo.getRecords();
        List<DishDto> list=records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();//分类id
            log.info(String.valueOf(categoryId));
            Category category = categoryService.getById(categoryId);
            if (category!=null) {
                String categoryName=category.getName();
                dishDto.setCategoryName(categoryName);
            }
            else
            {
                dishDto.setCategoryName("数据丢失");
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }

    //根据id查询指定的菜品信息和对应的口味信息
    @GetMapping("/{id}")
    public R<DishDto> gete(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    //修改菜品
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info(String.valueOf(dishDto));
        dishService.UpdateWithFlavor(dishDto);
        return R.success("修改成功");
    }

/*    //根据条件查询对应的菜品数据
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        //查询菜品状态为1，即：(起售状态)
        wrapper.eq(Dish::getStatus,1);
        wrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list=dishService.list(wrapper);
        return R.success(list);
    }*/

    //根据id删除菜品数据
    @DeleteMapping
    public R<String> delete(Long ids){
        dishService.removeById(ids);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,ids);
        dishFlavorService.remove(queryWrapper);
        return R.success("删除成功");
    }
    //修改状态（是否停售）
    @PostMapping("/status/{status}")
    public R<String> Updatestatus(@PathVariable Integer status, Long[] ids) {
       log.info(String.valueOf(status));
        for (int i = 0; i < ids.length; i++) {
            Long id=ids[i];
            //根据id得到每个dish菜品
            Dish dish = dishService.getById(id);
            dish.setStatus(status);
            dishService.updateById(dish);
        }
        return R.success("修改状态成功");
    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id

            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }
}

