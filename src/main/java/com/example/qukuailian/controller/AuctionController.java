package com.example.qukuailian.controller;

import com.example.qukuailian.bean.Auction;
import com.example.qukuailian.bean.AuctionInformation;
import com.example.qukuailian.bean.Message;
import com.example.qukuailian.service.AuctionService;
import com.example.qukuailian.util.CustomException;
import com.example.qukuailian.util.MessageUtil;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auction")

public class AuctionController {

    @Autowired
    AuctionService auctionService;

    @RequestMapping("/createAuction")
    public Message<String> createAuction(@RequestParam("auctionId") String auctionId){
        if(!auctionService.checkAuctionIsEmpty(auctionId)){
            throw new CustomException(120,"auctionId 已存在");
        }
        auctionService.insetAuction(auctionId);
        return MessageUtil.ok();
    }

    @RequestMapping("/encBidInfo")
    public Message<AuctionInformation> encBidInfo(@RequestParam("auctionId") String auctionId,
                                                  @RequestParam("bidprice") String bidprice,
                                                  @RequestParam("username") String username) throws Exception {
        AuctionInformation auctionInformation = new AuctionInformation();
        auctionInformation.setAuctionId(auctionId);
        auctionInformation.setBidprice(bidprice);
        auctionInformation.setUsername(username);
        return MessageUtil.ok(auctionService.encrypt(auctionInformation));
    }

    @RequestMapping("/queryInfo")
    public Message<AuctionInformation> queryInfo(@RequestParam("auctionId") String auctionId,
                                                 @RequestParam("highTestPrice") String highTestPrice,
                                                 @RequestParam("username") String username) throws Exception {
        AuctionInformation auctionInformation = new AuctionInformation();
        auctionInformation.setAuctionId(auctionId);
        auctionInformation.setBidprice(highTestPrice);
        auctionInformation.setUsername(username);
        return MessageUtil.ok(auctionService.decrypt(auctionInformation));
    }
}
