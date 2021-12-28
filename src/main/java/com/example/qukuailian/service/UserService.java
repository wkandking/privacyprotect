package com.example.qukuailian.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.qukuailian.bean.User;
import com.example.qukuailian.dao.UserMapper;
import com.example.qukuailian.util.SM2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.util.Base64;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public boolean checkUserIsEmpty(String json){
        JSONObject o = (JSONObject) JSON.parse(json);
        String userid = o.getString("userid");
        return userMapper.selectByPrimaryKey(userid) == null;
    }

    public User getUserByUserId(String json){
        JSONObject o = (JSONObject) JSON.parse(json);
        String userid = o.getString("userid");
        return userMapper.selectByPrimaryKey(userid);
    }

    public User getUserByUserName(String json){
        JSONObject o = (JSONObject) JSON.parse(json);
        String username = o.getString("issuer");
        return userMapper.selectByUserName(username);
    }

    public int insertUser(String json) throws Exception {
        JSONObject o = (JSONObject) JSON.parse(json);
        String userid = o.getString("userid");
        String username = o.getString("name");
        String org = o.getString("org");
        KeyPair keyPair = SM2.generateSm2KeyPair(userid);
        String pk = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        String sk = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        String nameEncrypt = SM2.encrypt(username, pk);
        User user = new User();
        user.setUserid(userid);
        user.setUsername(username);
        user.setOrg(org);
        user.setPk(pk);
        user.setSk(sk);
        user.setNameencrypt(nameEncrypt);
        return userMapper.insert(user);
    }





}
