package com.example.qukuailian.bean;

public class Auction {
    private String auctionId;
    private String userId;
    private String encryptAlg;
    private String orderEncryptAlg;
    private String encryptAlgParam;
    private String orderEncryptAlgParam;

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEncryptAlg() {
        return encryptAlg;
    }

    public void setEncryptAlg(String encryptAlg) {
        this.encryptAlg = encryptAlg;
    }

    public String getOrderEncryptAlg() {
        return orderEncryptAlg;
    }

    public void setOrderEncryptAlg(String orderEncryptAlg) {
        this.orderEncryptAlg = orderEncryptAlg;
    }

    public String getEncryptAlgParam() {
        return encryptAlgParam;
    }

    public void setEncryptAlgParam(String encryptAlgParam) {
        this.encryptAlgParam = encryptAlgParam;
    }

    public String getOrderEncryptAlgParam() {
        return orderEncryptAlgParam;
    }

    public void setOrderEncryptAlgParam(String orderEncryptAlgParam) {
        this.orderEncryptAlgParam = orderEncryptAlgParam;
    }
}
