package com.sht.supplies.controller;

import com.sht.supplies.common.RestResponse;
import com.sht.supplies.entity.OutStock;
import com.sht.supplies.service.OutStockService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Aaron.H.Shen
 * @date 5/13/2021 4:40 PM
 */
@RestController
@RequestMapping("api/outStock")
@Api(tags = "出库")
public class OutStockController extends BaseController {

    @Autowired
    private OutStockService outStockService;

    @PostMapping
    public RestResponse add(@Validated @RequestBody OutStock outStock) {
        return SUCCESS("");
    }

    @PutMapping
    public RestResponse update(@Validated @RequestBody OutStock outStock) {
        return SUCCESS("");
    }
}
