package com.example.qukuailian.controller;

import com.example.qukuailian.bean.Message;
import com.example.qukuailian.util.MessageUtil;
import com.example.qukuailian.util.OPE;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
@RequestMapping("/auction")
public class AuctionController {

    @RequestMapping("/encoder")
    public Message<String> getEncoderString(@RequestParam("string") String s){
        OPE ope = new OPE();
        BigInteger bigInteger = ope.encrypt(new BigInteger(s));

        return MessageUtil.ok(bigInteger.toString());
    }
}
