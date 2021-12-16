package com.example.qukuailian.bean;

import lombok.Data;

@Data
public class PaperInformation {
    private String paperNumber;
    private String issUser;
    private String pubKey;
    private String priKey;

    private String issUserOrg;
    private String price;
    private String oldOwer;
    private String oldOwnerOrg;
    private String redeemingOwner;
    private String redeemingOwnerOrg;
}
