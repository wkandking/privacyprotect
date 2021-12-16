package com.example.qukuailian.controller;

import com.example.qukuailian.bean.Message;
import com.example.qukuailian.bean.PaperInformation;
import com.example.qukuailian.service.PaperService;
import com.example.qukuailian.service.UserService;
import com.example.qukuailian.util.CustomException;
import com.example.qukuailian.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/paper")
public class PaperController {
    @Autowired
    UserService userService;

    @Autowired
    PaperService paperService;

    /** 根据接受的参数生成密钥对保存在数据库表 users
     **/
    @RequestMapping("/register")
    public Message<String> register(@RequestParam("name") String userid,
                                    @RequestParam("org") String org){

        boolean result = userService.checkUserIsEmpty(userid);
        if(!result) {
            try {
                userService.insertUser(userid, org);
                return MessageUtil.ok();
            }catch (Exception e){
                throw new CustomException(120,"register encrypy failed!");
            }

        }
        return MessageUtil.error(120,"userid&org已存在");
    }

    @RequestMapping("/issue")
    public Message<PaperInformation> issue(@RequestParam("paperNumber") String paperNumber,
                                 @RequestParam("issUser") String issUser,
                                 @RequestParam("issUserOrg") String issUserOrg){
        int result = paperService.insert(paperNumber, issUser);
        if(result == 0) throw  new CustomException(100,"issue insert failed!");
        PaperInformation paperInformation = paperService.getUserInformation(paperNumber);
        System.out.println(paperInformation.getIssUserOrg());
        try{
            PaperInformation p = paperService.encrypt(paperInformation);
            return MessageUtil.ok(p);
        }catch (Exception e){
            throw new CustomException(120,e.getMessage());
        }
    }

    @RequestMapping("/buy")
    public Message<PaperInformation> buy(@RequestParam("paperNumber") String paperNumber,
                                         @RequestParam("price") String price,
                                         @RequestParam("newOwner")String newOwner,
                                         @RequestParam("newOwnerOrg") String newOwnerOrg){
        PaperInformation paperInformation = paperService.getUserInformation(paperNumber);
        paperInformation.setPrice(price);
        paperInformation.setIssUser(newOwner);
        paperInformation.setIssUserOrg(newOwnerOrg);
        PaperInformation p;
        try{
            p = paperService.encrypt(paperInformation);
            p.setPriKey(null);
            return MessageUtil.ok(p);
        }catch (Exception e){
            throw new CustomException(120,"issue encrypt failed!");
        }

    }
    @RequestMapping("check")
    public Message<PaperInformation> check(@RequestParam("paperNumber") String paperNumber,
                                           @RequestParam("price") String price,
                                           @RequestParam("newOwner")String newOwner,
                                           @RequestParam("newOwnerOrg") String newOwnerOrg){
        PaperInformation paperInformation = paperService.getUserInformation(paperNumber);
        paperInformation.setPrice(price);
        paperInformation.setIssUser(newOwner);
        paperInformation.setIssUserOrg(newOwnerOrg);
        PaperInformation p;
        try{
            p = paperService.decrypt(paperInformation);
            p.setPriKey(null);
            return MessageUtil.ok(p);
        }catch (Exception e){
            throw new CustomException(120,"issue decrypt failed!");
        }
    }

    @RequestMapping("/transfer")
    public Message<PaperInformation> transfer(@RequestParam("paperNumber") String paperNumber,
                                              @RequestParam("issUser") String issUser,
                                              @RequestParam("newOwner") String newOwner){
        int result = paperService.updateOwner(paperNumber, newOwner);
        if(result == 0){
            throw  new CustomException(120,"tansfer update failed!");
        }
        PaperInformation paperInformation = paperService.getUserInformation(paperNumber);
        paperInformation.setOldOwer(issUser);
        paperInformation.setOldOwnerOrg(newOwner);

        PaperInformation p;
        try{
            p = paperService.encrypt(paperInformation);
            p.setPriKey(null);
            return MessageUtil.ok(p);
        }catch (Exception e){
            throw new CustomException(120,"transfer encrypt failed!");
        }
    }

    @RequestMapping("/reedem")
    public Message<PaperInformation> reedem(@RequestParam("paperNumber") String paperNumber,
                                            @RequestParam("redeemingOwner") String redeemingOwner,
                                            @RequestParam("issuingOwnerOrg") String issuingOwnerOrg){
        PaperInformation paperInformation = paperService.getUserInformation(paperNumber);
        paperInformation.setRedeemingOwner(redeemingOwner);
        paperInformation.setRedeemingOwnerOrg(issuingOwnerOrg);
        PaperInformation p;
        try{
            p = paperService.encrypt(paperInformation);
            p.setPriKey(null);
            return MessageUtil.ok(p);
        }catch (Exception e){
            throw new CustomException(120,"reedem encrypt failed!");
        }

    }








}
