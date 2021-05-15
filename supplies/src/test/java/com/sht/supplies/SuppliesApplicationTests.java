package com.sht.supplies;

import com.sht.supplies.entity.OutStock;
import com.sht.supplies.service.OutStockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SuppliesApplicationTests {

    @Autowired
    private OutStockService outStockService;

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

}
