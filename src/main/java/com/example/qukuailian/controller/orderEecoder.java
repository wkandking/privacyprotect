package com.example.qukuailian.controller;

import com.example.qukuailian.bean.Message;
import com.example.qukuailian.bean.User;
import com.example.qukuailian.service.UserService;
import com.example.qukuailian.util.MessageUtil;
import com.example.qukuailian.util.OPE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
@RequestMapping("/hello")
public class orderEecoder {
    @Autowired
    UserService userService;

    @RequestMapping("/encoder")
    public String getEncoderString(@RequestParam("string") String s){
        OPE ope = new OPE();
        BigInteger bigInteger = ope.encrypt(new BigInteger(s));

        return bigInteger.toString();
    }

    @RequestMapping("/register")
    public Message<User> say(@RequestParam("userid") String userid,@RequestParam("org") String org){

        boolean result = userService.checkUserIsEmpty(userid);
        if(!result) {
            userService.insertUser(userid, org);
            return MessageUtil.ok(userService.getUserByUserId(userid));
        }
        return MessageUtil.ok(userService.getUserByUserId(userid));
    }
    @RequestMapping("/hello")
    public Message<String> hello(String org){

        return MessageUtil.ok("hello");
    }
}
