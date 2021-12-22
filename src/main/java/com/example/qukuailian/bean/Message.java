package com.example.qukuailian.bean;

import java.io.Serializable;

public class Message<T> implements Serializable {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 返回的数据类
     */
    private T data;

    /**
     * 时间
     */
    private Long time;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Message(Integer code, String message, T data, Long time) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.time = time;
    }

    public Message() {
    }
    // getter、setter 以及 构造方法略。。。
}
