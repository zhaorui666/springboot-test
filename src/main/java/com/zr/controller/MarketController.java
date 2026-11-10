package com.zr.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.zr.annotation.CurrentLimiter;
import com.zr.constant.LimitType;
import com.zr.pojo.StockBaseInfo;
import com.zr.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class MarketController {

    @Autowired
    private MarketService service;

    @Value("${valueInject}")
    private String valueInject;

    @CurrentLimiter(limit = 2, expire = 60, limitType = LimitType.IP)
    @RequestMapping("/insertMarketData")
    public void insertMarketData() throws IOException, InterruptedException {
        service.insertMarketData();
    }

    @RequestMapping("/countSingleResult")
    public void countSingleResult() throws ParseException, ExecutionException, InterruptedException {
        service.countSingleResult();
    }

    @RequestMapping("/countHandleOfCupResult")
    public void countHandleOfCupResult() throws IOException, ParseException, ExecutionException, InterruptedException {
        service.countHandleOfCupResult();
    }

    @RequestMapping("/insertFinaSta")
    public void insertFinaSta() throws IOException {
        service.insertFinaSta();
    }

    @RequestMapping("/collectPlateMarketData")
    public void collectPlateMarketData() throws IOException {
        service.collectPlateMarketData();
    }

    @RequestMapping("/countPlateResult")
    public void countPlateResult() throws IOException {
        service.countPlateResult();
    }

    @PostMapping("/shardingTest/{id}")
    public List<StockBaseInfo> shardingTest(@PathVariable Integer id) {
        List<StockBaseInfo> stockBaseInfos = service.shardingTest(id);
        return stockBaseInfos;
    }

    @RequestMapping("/selectTest")
    public List<StockBaseInfo> selectTest(@RequestBody StockBaseInfo param) throws IOException {
//        throw new NullPointerException();
        List<StockBaseInfo> stockBaseInfos = service.selectTest(param.getCode());
        if (CollectionUtil.isNotEmpty(stockBaseInfos)) {
            stockBaseInfos.forEach(f -> f.setCurrentDate(new Date()));
        }
        return stockBaseInfos;
    }

    @RequestMapping("/voidTest")
    public void voidTest() throws IOException {

    }
}
