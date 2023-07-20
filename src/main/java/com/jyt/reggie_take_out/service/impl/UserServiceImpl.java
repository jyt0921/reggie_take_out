package com.jyt.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jyt.reggie_take_out.entity.User;
import com.jyt.reggie_take_out.mapper.UserMapper;
import com.jyt.reggie_take_out.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
