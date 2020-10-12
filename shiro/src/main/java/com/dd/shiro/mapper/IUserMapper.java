package com.dd.shiro.mapper;

import com.dd.shiro.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface IUserMapper {

     User queryUserByName(String username);


}
