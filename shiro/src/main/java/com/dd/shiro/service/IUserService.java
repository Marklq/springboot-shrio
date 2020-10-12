package com.dd.shiro.service;


import com.dd.shiro.pojo.User;

public interface IUserService {

    User queryUserByName(String username);

}
