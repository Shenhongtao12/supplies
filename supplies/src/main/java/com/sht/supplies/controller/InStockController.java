package com.sht.supplies.controller;

import com.sht.supplies.service.InStockService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Aaron
 * @date 2021/5/16 22:17
 */
@RestController
@RequestMapping("api/inStock")
@Api(tags = "入库")
public class InStockController {

    @Autowired
    private InStockService inStockService;
}
