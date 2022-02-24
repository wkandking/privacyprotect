package com.example.qukuailian.service;

import com.example.qukuailian.bean.Auction;
import com.example.qukuailian.bean.AuctionInformation;
import com.example.qukuailian.bean.Message;
import com.example.qukuailian.bean.User;
import com.example.qukuailian.dao.AuctionMapper;
import com.example.qukuailian.dao.UserMapper;
import com.example.qukuailian.util.OPE.MessageUtil;
import com.example.qukuailian.util.OPE.OPE;
import com.example.qukuailian.util.SM2.SM2;
import com.example.qukuailian.util.SMA;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.KeyPair;
import java.util.Base64;

@Service
public class AuctionService {
    @Autowired
    AuctionMapper auctionMapper;

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CommonService commonService;

    public int insertAuction(String auctionId){
        Auction auction = new Auction();
        auction.setAuctionId(auctionId);
        auctionMapper.insert(auction);
        Integer flag = auctionMapper.selectByAuctionId(auctionId).getFlag();
        System.out.println(flag);
        KeyPair keyPair = SM2.generateSm2KeyPair(String.valueOf(flag));
        String pk = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        String sk = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        auction.setFlag(flag);
        auction.setPk(pk);
        auction.setSk(sk);
        auction.setOk(auctionId);
        return auctionMapper.updateByPrimaryKey(auction);
    }

    public AuctionInformation encrypt(AuctionInformation auctionInformation) throws Exception {
        AuctionInformation result = new AuctionInformation();
        Auction auction = auctionMapper.selectByAuctionId(auctionInformation.getAuctionId());
        result.setAuctionId(auctionInformation.getAuctionId());
        result.setBidprice(OPE.getInstance().encrypt(new BigInteger(auctionInformation.getBidprice())).toString());
        result.setUsername(SM2.encrypt(auctionInformation.getUsername(),auction.getPk()));
        return result;
    }

    public AuctionInformation decrypt(AuctionInformation auctionInformation) throws Exception {
        AuctionInformation result = new AuctionInformation();
        Auction auction = auctionMapper.selectByAuctionId(auctionInformation.getAuctionId());

        result.setAuctionId(auctionInformation.getAuctionId());
        result.setBidprice(OPE.getInstance().decrypt(new BigInteger(auctionInformation.getBidprice())).toString());
        result.setUsername(SM2.decrypt(auctionInformation.getUsername(),auction.getSk()));
        return result;

    }

    public Pair<Boolean, User> insertKey(String algtype, String username) {
        try{
            User user = userMapper.selectByUserName(username);
            if(user == null){
                userService.insertUserFromAuction(username);
                user = userMapper.selectByUserName(username);
                return new Pair<>(true, user);
            }
            return new Pair<>(false, user);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public String pkenc(String algtype, String pk, String username) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = commonService.getEncryptMethod(algtype);
        String key = pk.replaceAll(" +","+");
        String data = (String) method.invoke(null, new Object[]{username, key});
        return data;
    }

    public String decinfo(String algtype, String sk, String enc_username) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = commonService.getEncryptMethod(algtype);
        String key = sk.replaceAll(" +","+");
        String data = (String) method.invoke(null, new Object[]{enc_username, key});
        return data;
    }
}
