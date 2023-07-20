package com.jyt.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jyt.reggie_take_out.entity.Employee;
import com.jyt.reggie_take_out.mapper.EmployeeMapper;
import com.jyt.reggie_take_out.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
