package com.sht.supplies.controller;

import com.sht.supplies.common.RestResponse;
import com.sht.supplies.entity.OutStock;
import com.sht.supplies.service.GoodsService;
import com.sht.supplies.service.OutStockService;
import com.sht.supplies.service.UserService;
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

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private UserService userService;

    @PostMapping
    public RestResponse add(@Validated @RequestBody OutStock outStock) {
        if (!goodsService.existsWithPrimaryKey(outStock.getGoodsId())) {
            return ERROR("物料不存在，请刷新页面重试");
        }
        if (!userService.existsByUserId(outStock.getUserId())) {
            return ERROR("员工不存在，请刷新页面重试");
        }
        if (outStock.getAdminId() == null) {
            outStock.setAdminId(userId);
        }
        return SUCCESS("");
    }

    @PutMapping
    public RestResponse update(@Validated @RequestBody OutStock outStock) {
        return SUCCESS("");
    }
}
