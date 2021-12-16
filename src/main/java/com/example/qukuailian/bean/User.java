package com.example.qukuailian.bean;

import lombok.Data;

@Data
public class User {
    /** 用户 id **/
    private String userid;

    /**  组织 **/
    private String org;
    /** 公钥 **/
    private String pk;
    /**  私钥 **/
    private String sk;


}
