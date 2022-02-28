package com.example.qukuailian.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.qukuailian.bean.Paper;
import com.example.qukuailian.bean.PaperInformation;
import com.example.qukuailian.bean.User;
import com.example.qukuailian.dao.PaperMapper;
import com.example.qukuailian.dao.UserMapper;
import com.example.qukuailian.util.OPE.CustomException;
import com.example.qukuailian.util.SM2.SM2;
import com.example.qukuailian.util.SMA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

@Service
public class PaperService {

    @Autowired
    PaperMapper paperMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CommonService commonService;

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
        paperInformation.setSm4Key(user.getSm4key());

        return paperInformation;
    }

    public PaperInformation encrypt(PaperInformation paperInformation, String algType) throws Exception {

        Method method = commonService.getEncryptMethod(algType);
        PaperInformation result = new PaperInformation();
        String key = "";
        if(algType.equals("2")){
            key = paperInformation.getPk();
        }else{
            key = paperInformation.getSm4Key();
        }
        System.out.println(paperInformation.getNewOwner());
        System.out.println(paperInformation.getNewOwnerOrg());
        result.setPaperNumber(paperInformation.getPaperNumber());
        result.setIssuer((String) method.invoke(null, new Object[]{paperInformation.getIssuer(), key}));
        result.setIssuerOrg((String) method.invoke(null,  new Object[]{paperInformation.getIssuerOrg(),key}));
        result.setPrice((String) method.invoke(null,  new Object[]{paperInformation.getPrice(),key}));
        result.setNewOwner((String) method.invoke(null,  new Object[]{paperInformation.getNewOwner(), key}));
        result.setNewOwnerOrg((String) method.invoke(null,  new Object[]{paperInformation.getNewOwnerOrg(), key}));
        result.setPk(paperInformation.getPk());
        result.setSk(paperInformation.getSk());
        result.setSm4Key(paperInformation.getSm4Key());
        return result;
    }

    public PaperInformation decrypt(PaperInformation paperInformation, String algtype) throws Exception {

        Method method = commonService.getDecryptMethod(algtype);
        String key = "";
        if(algtype.equals("2")){
            key = paperInformation.getSk();
        }else{
            key = paperInformation.getSm4Key();
        }
        PaperInformation result = new PaperInformation();
        System.out.println(paperInformation.getNewOwner());
        result.setPaperNumber(paperInformation.getPaperNumber());
        result.setPrice((String) method.invoke(null, new Object[]{paperInformation.getPrice(),key}));
        result.setIssuer((String) method.invoke(null, new Object[]{paperInformation.getIssuer(),key}));
        result.setIssuerOrg((String) method.invoke(null, new Object[]{paperInformation.getIssuerOrg(), key}));
        result.setNewOwner((String) method.invoke(null, new Object[]{paperInformation.getNewOwner(), key}));
        result.setNewOwnerOrg((String) method.invoke(null, new Object[]{paperInformation.getNewOwnerOrg(), key}));


        result.setPk(paperInformation.getPk());
        result.setSk(paperInformation.getSk());
        result.setSm4Key(paperInformation.getSm4Key());
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
        p.setSm4Key(newUser.getSm4key());
        return p;

    }


    public PaperInformation convertPaperInforMation(String json) {
        JSONObject o = (JSONObject) JSON.parse(json);
        String paperNumber = o.getString("paperNumber");
        String price = o.getString("price");
        String newOwner = o.getString("newOwner");

        String userid = paperMapper.selectByPrimaryKey(paperNumber).getUserId();
        User user = userMapper.selectByPrimaryKey(userid);

        PaperInformation p = new PaperInformation();
        p.setPaperNumber(paperNumber);
        p.setNewOwner(newOwner);
        p.setPrice(price);
        p.setPk(user.getPk());
        p.setSk(user.getSk());
        p.setSm4Key(user.getSm4key());
        return p;
    }

    public String encrypt(String json) throws Exception {
        JSONObject o = (JSONObject) JSON.parse(json);
        String username = o.getString("username");
        String origintext = o.getString("origintext");
        String algType = o.getString("algType");
        Method method = commonService.getEncryptMethod(algType);
        User user = userMapper.selectByUserName(username);
        return (String) method.invoke(null, new Object[]{origintext, user.getPk()});
    }
    public String decrypt(String json) throws Exception {
        JSONObject o = (JSONObject) JSON.parse(json);
        String username = o.getString("username");
        String encrypttext = o.getString(("encrypttext"));
        String algType = o.getString("algType");
        Method method = commonService.getDecryptMethod(algType);
        User user = userMapper.selectByUserName(username);
        return (String) method.invoke(null, new Object[]{encrypttext, user.getSk()});
    }

    public String owner(String json){
        JSONObject o = (JSONObject) JSON.parse(json);
        String owner = o.getString("owner");
        return userMapper.selectByUserName(owner).getNameencrypt();
    }

}
