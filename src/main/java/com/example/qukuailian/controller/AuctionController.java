package com.example.qukuailian.controller;

import com.example.qukuailian.bean.AuctionInformation;
import com.example.qukuailian.bean.Message;
import com.example.qukuailian.service.AuctionService;
import com.example.qukuailian.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auction")

public class AuctionController {

    @Autowired
    AuctionService auctionService;

    @PostMapping("/createAuction")
    public Message<String> createAuction(@RequestParam("auctionid") String auctionId){
        auctionService.insertAuction(auctionId);
        return MessageUtil.ok();
    }

    @RequestMapping("/encBidInfo")
    public Message<AuctionInformation> encBidInfo(@RequestParam("auctionid") String auctionId,
                                                  @RequestParam("bidprice") String bidprice,
                                                  @RequestParam("username") String username) throws Exception {
        AuctionInformation auctionInformation = new AuctionInformation();
        auctionInformation.setAuctionId(auctionId);
        auctionInformation.setBidprice(bidprice);
        auctionInformation.setUsername(username);
        return MessageUtil.ok(auctionService.encrypt(auctionInformation));
    }

    @RequestMapping("/queryInfo")
    public Message<AuctionInformation> queryInfo(@RequestParam("auctionid") String auctionId,
                                                 @RequestParam("highTestPrice") String highTestPrice,
                                                 @RequestParam("username") String username) throws Exception {
        AuctionInformation auctionInformation = new AuctionInformation();
        auctionInformation.setAuctionId(auctionId);
        auctionInformation.setBidprice(highTestPrice);
        auctionInformation.setUsername(username);
        return MessageUtil.ok(auctionService.decrypt(auctionInformation));
    }
}
