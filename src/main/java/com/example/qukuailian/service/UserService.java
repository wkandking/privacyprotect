package com.example.qukuailian.service;

import com.example.qukuailian.bean.User;
import com.example.qukuailian.dao.UserMapper;
import com.example.qukuailian.util.SM2;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.util.Base64;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public boolean checkUserIsEmpty(String userid){
        return userMapper.checkUserIsEmpty(userid) > 0 ? true :false;
    }

    public User getUserByUserId(String userid){
        return userMapper.getUserByUserId(userid);
    }

    public int insertUser(String userid, String org){
        KeyPair keyPair = SM2.generateSm2KeyPair(userid);
        String pk = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        String sk = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        User user = new User();
        user.setUserid(userid);
        user.setOrg(org);
        user.setPk(pk);
        user.setSk(sk);
        return userMapper.insert(user);
    }





}
