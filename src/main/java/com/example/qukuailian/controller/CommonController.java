package com.example.qukuailian.controller;

import com.example.qukuailian.bean.Message;
import com.example.qukuailian.service.CommonService;
import com.example.qukuailian.util.OPE.CustomException;
import com.example.qukuailian.util.OPE.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/common")
public class CommonController {
    @Autowired
    CommonService commonService;

    @PostMapping(value = "/getSM3Hash")
    public Message<String> getSM3Hash(@RequestParam("message") String message){
        try {
            String hashValue = commonService.getSM3Method(message);
            return MessageUtil.ok(hashValue);
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(120,"SM3加密失败");
        }
    }
}
