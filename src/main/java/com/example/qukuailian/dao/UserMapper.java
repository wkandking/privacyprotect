package com.example.qukuailian.dao;

import com.example.qukuailian.bean.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int checkUserIsEmpty(String userid);
    User getUserByUserId(String userid);
    int insert(User user);
}
