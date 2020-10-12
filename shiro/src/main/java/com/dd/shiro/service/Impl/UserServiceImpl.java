package com.dd.shiro.service.Impl;

import com.dd.shiro.mapper.IUserMapper;
import com.dd.shiro.pojo.User;
import com.dd.shiro.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    IUserMapper userMapper;

    @Override
    public User queryUserByName(String username) {
        return userMapper.queryUserByName(username);
    }
}
