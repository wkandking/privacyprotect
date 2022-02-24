package com.example.qukuailian.controller;

import com.example.qukuailian.bean.AuctionInformation;
import com.example.qukuailian.bean.Message;
import com.example.qukuailian.bean.User;
import com.example.qukuailian.service.AuctionService;
import com.example.qukuailian.util.OPE.MessageUtil;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auction")

public class AuctionController {

    @Autowired
    AuctionService auctionService;

    @PostMapping("/createkey")
    public Message<User> createKey(@RequestParam("algtype") String algtype,
                                     @RequestParam("username") String username) throws Exception {
        Pair<Boolean, User> pair = auctionService.insertKey(algtype, username);
        if(pair.getKey()){
            return MessageUtil.ok(pair.getValue());
        }
        return MessageUtil.error(pair.getValue());
    }

    @PostMapping("/pkenc")
    public Message<String> pkenc(@RequestParam("algtype") String algtype,
                                   @RequestParam("pk") String pk,
                                   @RequestParam("username") String username) throws Exception {

        String enc_username = auctionService.pkenc(algtype, pk ,username);
        return MessageUtil.ok(enc_username);
    }


    @PostMapping("/decinfo")
    public Message<String> decinfo(@RequestParam("algtype") String algtype,
                                 @RequestParam("sk") String sk,
                                 @RequestParam("enc_username") String enc_username) throws Exception {
        String username = auctionService.decinfo(algtype, sk, enc_username);
        return MessageUtil.ok(username);
    }


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
