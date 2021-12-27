package com.example.qukuailian.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.qukuailian.bean.Paper;
import com.example.qukuailian.bean.PaperInformation;
import com.example.qukuailian.bean.User;
import com.example.qukuailian.dao.PaperMapper;
import com.example.qukuailian.dao.UserMapper;
import com.example.qukuailian.util.CustomException;
import com.example.qukuailian.util.SM2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaperService {

    @Autowired
    PaperMapper paperMapper;

    @Autowired
    UserMapper userMapper;

    public Paper insert(String json){
        JSONObject o = (JSONObject) JSON.parse(json);
        String paperNumber = o.getString("paperNumber");
        String name = o.getString("issuer");
        User user = userMapper.selectByUserName(name);
        Paper paper = new Paper();
        paper.setPaperId(paperNumber);
        paper.setUserId(user.getUserid());
        int result = paperMapper.insert(paper);
        if(result == 0) throw new CustomException(120,"insert paper failed");
        return paper;
    }

    public PaperInformation getPaperInformation(Paper paper){
        User user =  userMapper.selectByPrimaryKey(paper.getUserId());
        PaperInformation paperInformation = new PaperInformation();
        paperInformation.setIssuer(user.getUsername());
        paperInformation.setPaperNumber(paper.getPaperId());
        paperInformation.setPk(user.getPk());
        paperInformation.setSk(user.getSk());
        paperInformation.setIssuerOrg(user.getOrg());

        return paperInformation;
    }

    public PaperInformation encrypt(PaperInformation paperInformation) throws Exception {
        PaperInformation result = new PaperInformation();

        result.setPaperNumber(paperInformation.getPaperNumber());
        result.setIssuer(SM2.encrypt(paperInformation.getIssuer(),paperInformation.getPk()));
        result.setIssuerOrg(SM2.encrypt(paperInformation.getIssuerOrg(),paperInformation.getPk()));
        result.setPrice(SM2.encrypt(paperInformation.getPrice(),paperInformation.getPk()));
        result.setNewOwner(SM2.encrypt(paperInformation.getNewOwner(), paperInformation.getPk()));
        result.setNewOwnerOrg(SM2.encrypt(paperInformation.getNewOwnerOrg(), paperInformation.getPk()));
        result.setPk(paperInformation.getPk());
        result.setSk(paperInformation.getSk());
        return result;
    }

    public PaperInformation decrypt(PaperInformation paperInformation) throws Exception {
        PaperInformation result = new PaperInformation();
        result.setPaperNumber(paperInformation.getPaperNumber());
        result.setPrice(SM2.decrypt(paperInformation.getPrice(),paperInformation.getSk()));
        result.setIssuer(SM2.decrypt(paperInformation.getIssuer(),paperInformation.getSk()));
        result.setIssuerOrg(SM2.decrypt(paperInformation.getIssuerOrg(), paperInformation.getSk()));
        result.setNewOwner(SM2.decrypt(paperInformation.getNewOwner(), paperInformation.getSk()));
        result.setNewOwnerOrg(SM2.decrypt(paperInformation.getNewOwnerOrg(), paperInformation.getSk()));


        result.setPk(paperInformation.getPk());
        result.setSk(paperInformation.getSk());
        return result;
    }

    public PaperInformation updateOwner(String json){
        JSONObject o = (JSONObject) JSON.parse(json);
        String paperNumber = o.getString("paperNumber");
        String issuer = o.getString("issuer");
        String issuerOrg = o.getString("issuerOrg");
        String newOwner = o.getString("newOwner");
        String newOwnerOrg = o.getString("newOwnerOrg");

        User newUser = userMapper.selectByUserName(newOwner);
        Paper paper = new Paper();
        paper.setPaperId(paperNumber);
        paper.setUserId(newUser.getUserid());
        paperMapper.updateByPrimaryKey(paper);

        PaperInformation p = new PaperInformation();
        p.setPaperNumber(paperNumber);
        p.setIssuer(issuer);
        p.setIssuerOrg(issuerOrg);
        p.setNewOwner(newOwner);
        p.setNewOwnerOrg(newOwnerOrg);
        p.setPk(newUser.getPk());
        p.setSk(newUser.getSk());

        return p;

    }


    public PaperInformation convertPaperInforMation(String json) {
        JSONObject o = (JSONObject) JSON.parse(json);
        String paperNumber = o.getString("paperNumber");
        String price = o.getString("price");
        String newOwner = o.getString("newOwner");
        String newOwnerOrg = o.getString("newOwnerOrg");

        String userid = paperMapper.selectByPrimaryKey(paperNumber).getUserId();
        User user = userMapper.selectByPrimaryKey(userid);

        PaperInformation p = new PaperInformation();
        p.setPaperNumber(paperNumber);
        p.setNewOwner(newOwner);
        p.setPrice(price);
        p.setNewOwnerOrg(newOwnerOrg);
        p.setPk(user.getPk());
        p.setSk(user.getSk());

        return p;
    }

    public String encrypt(String json) throws Exception {
        JSONObject o = (JSONObject) JSON.parse(json);
        String username = o.getString("username");
        String origintext = o.getString("origintext");
        User user = userMapper.selectByUserName(username);
        return SM2.encrypt(origintext, user.getPk());
    }
    public String decrypt(String json) throws Exception {
        JSONObject o = (JSONObject) JSON.parse(json);
        String username = o.getString("username");
        String encrypttext = o.getString(("encrypttext"));
        User user = userMapper.selectByUserName(username);
        return SM2.decrypt(encrypttext, user.getSk());
    }
}
