package com.sht.supplies;

import com.sht.supplies.entity.Goods;
import com.sht.supplies.entity.OutStock;
import com.sht.supplies.service.GoodsService;
import com.sht.supplies.service.InStockService;
import com.sht.supplies.service.OutStockService;
import com.sht.supplies.service.StatisticalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SuppliesApplicationTests {

    @Autowired
    private OutStockService outStockService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private StatisticalService statisticalService;

    @Test
    public void tranTest() {
        OutStock outStock = new OutStock();
        outStock.setGoodsId(1);
        outStock.setUserId(1);
        outStock.setAdminId(5);
        outStock.setAmount(1);
        outStock.setRemark("123123123123123");
        Boolean add = outStockService.add(outStock);
        System.out.println(add);
    }

    @Test
    public void goodsTest() {
        Goods goods = new Goods();
        goods.setPartNumber("9999");
        goods.setTitle("9999test");
        goods.setUnit("坐");
        goods.setCategory("test");
        Integer integer = goodsService.saveOne(goods);
        System.out.println(integer);
    }

    @Test
    public void statisticalServiceTest() {
        statisticalService.autoStatistical();
    }

}
