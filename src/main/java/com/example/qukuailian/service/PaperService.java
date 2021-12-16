package com.example.qukuailian.service;

import com.example.qukuailian.bean.Paper;
import com.example.qukuailian.bean.PaperInformation;
import com.example.qukuailian.bean.User;
import com.example.qukuailian.dao.PaperMapper;
import com.example.qukuailian.dao.UserMapper;
import com.example.qukuailian.util.SM2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaperService {

    @Autowired
    PaperMapper paperMapper;

    @Autowired
    UserMapper userMapper;

    public int insert(String paperNumber, String issUser){
        Paper paper = new Paper();
        paper.setPaperId(paperNumber);
        paper.setUserId(issUser);
        int result = paperMapper.insert(paper);
        return result;
    }

    public PaperInformation getUserInformation(String paperNumber){
        String userId = paperMapper.selectByPrimaryKey(paperNumber).getUserId();
        User user =  userMapper.getUserByUserId(userId);
        PaperInformation paperInformation = new PaperInformation();
        paperInformation.setIssUser(user.getUserid());
        paperInformation.setPaperNumber(paperNumber);
        paperInformation.setPubKey(user.getPk());
        paperInformation.setPriKey(user.getSk());
        paperInformation.setIssUserOrg(user.getOrg());

        return paperInformation;
    }

    public PaperInformation encrypt(PaperInformation paperInformation) throws Exception {
        PaperInformation result = new PaperInformation();

        result.setPaperNumber(paperInformation.getPaperNumber());
        result.setIssUser(SM2.encrypt(paperInformation.getIssUser(),paperInformation.getPubKey()));
        result.setIssUserOrg(SM2.encrypt(paperInformation.getIssUserOrg(),paperInformation.getPubKey()));
        result.setPrice(SM2.encrypt(paperInformation.getPrice(),paperInformation.getPubKey()));
        result.setOldOwer(SM2.encrypt(paperInformation.getOldOwer(), paperInformation.getPubKey()));
        result.setOldOwnerOrg(SM2.encrypt(paperInformation.getOldOwnerOrg(), paperInformation.getPubKey()));
        result.setRedeemingOwner(SM2.encrypt(paperInformation.getRedeemingOwner(), paperInformation.getPubKey()));
        result.setRedeemingOwnerOrg(SM2.encrypt(paperInformation.getRedeemingOwnerOrg(), paperInformation.getPubKey()));
        result.setPubKey(paperInformation.getPubKey());
        result.setPriKey(paperInformation.getPriKey());
        return result;
    }

    public PaperInformation decrypt(PaperInformation paperInformation) throws Exception {
        PaperInformation result = new PaperInformation();
        result.setPaperNumber(paperInformation.getPaperNumber());
        result.setPrice(SM2.decrypt(paperInformation.getPrice(),paperInformation.getPriKey()));
        result.setIssUser(SM2.decrypt(paperInformation.getIssUser(),paperInformation.getPriKey()));
        result.setIssUserOrg(SM2.decrypt(paperInformation.getIssUserOrg(), paperInformation.getPriKey()));
        result.setOldOwer(SM2.decrypt(paperInformation.getOldOwer(), paperInformation.getPriKey()));
        result.setOldOwnerOrg(SM2.decrypt(paperInformation.getOldOwnerOrg(), paperInformation.getPriKey()));

        result.setRedeemingOwner(SM2.decrypt(paperInformation.getRedeemingOwner(), paperInformation.getPriKey()));
        result.setRedeemingOwnerOrg(SM2.decrypt(paperInformation.getRedeemingOwnerOrg(), paperInformation.getPriKey()));

        result.setPubKey(paperInformation.getPubKey());
        result.setPriKey(paperInformation.getPriKey());
        return result;
    }

    public int updateOwner(String paperNumber, String newOwner){
        Paper paper = new Paper();
        paper.setPaperId(paperNumber);
        paper.setUserId(newOwner);
        return paperMapper.updateByPrimaryKeySelective(paper);

    }


}
