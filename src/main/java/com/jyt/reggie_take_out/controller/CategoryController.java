package com.jyt.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jyt.reggie_take_out.common.R;
import com.jyt.reggie_take_out.entity.Category;
import com.jyt.reggie_take_out.entity.Employee;
import com.jyt.reggie_take_out.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    //添加菜品种类或者套餐种类
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    //展示分类管理页面
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page= {},pageSize={},name={}",page,pageSize,name);
        //构造分页构造器
        Page pageInfo=new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Category> qw=new LambdaQueryWrapper();
        //添加过滤条件
        qw.like(StringUtils.isNotEmpty(name),Category::getName,name);
        //添加排序条件
        qw.orderByAsc(Category::getUpdateTime);
        categoryService.page(pageInfo,qw);
        log.info(String.valueOf(pageInfo));
        return R.success(pageInfo);
    }
    //删除分类管理页面的信息
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("{}",ids);
        categoryService.remove(ids);
        return R.success("删除成功");
    }
    //修改分类管理页面的信息
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息：{}",category);
        categoryService.updateById(category);
        return R.success("修改信息成功");
    }
    //根据条件查询分类数据
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        log.info(String.valueOf(category));

        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);


    }
}
