package com.example.qukuailian.service;

import com.example.qukuailian.bean.Auction;
import com.example.qukuailian.bean.AuctionInformation;
import com.example.qukuailian.dao.AuctionMapper;
import com.example.qukuailian.util.OPE;
import com.example.qukuailian.util.SM2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.KeyPair;
import java.util.Base64;

@Service
public class AuctionService {
    @Autowired
    AuctionMapper auctionMapper;

    public boolean checkAuctionIsEmpty(String auctionId){
        return auctionMapper.selectByPrimaryKey(auctionId) == null;
    }
    public int insetAuction(String auctionId){
        KeyPair keyPair = SM2.generateSm2KeyPair(auctionId);
        String pk = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        String sk = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());
        Auction auction = new Auction();
        auction.setAuctionId(auctionId);
        auction.setPk(pk);
        auction.setSk(sk);
        auction.setOk(auctionId);
        return auctionMapper.insertSelective(auction);
    }

    public AuctionInformation encrypt(AuctionInformation auctionInformation) throws Exception {
        AuctionInformation result = new AuctionInformation();
        Auction auction = auctionMapper.selectByPrimaryKey(auctionInformation.getAuctionId());
        result.setAuctionId(auctionInformation.getAuctionId());
        result.setBidprice(OPE.getInstance().encrypt(new BigInteger(auctionInformation.getBidprice())).toString());
        result.setUsername(SM2.encrypt(auctionInformation.getUsername(),auction.getPk()));
        return result;
    }

    public AuctionInformation decrypt(AuctionInformation auctionInformation) throws Exception {
        AuctionInformation result = new AuctionInformation();
        Auction auction = auctionMapper.selectByPrimaryKey(auctionInformation.getAuctionId());
        result.setAuctionId(auctionInformation.getAuctionId());
        result.setBidprice(OPE.getInstance().decrypt(new BigInteger(auctionInformation.getBidprice())).toString());
        result.setUsername(SM2.decrypt(auctionInformation.getUsername(),auction.getSk()));
        return result;

    }
}
