package com.jyt.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jyt.reggie_take_out.entity.AddressBook;
import com.jyt.reggie_take_out.mapper.AddressBookMapper;
import com.jyt.reggie_take_out.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>implements AddressBookService {

}
