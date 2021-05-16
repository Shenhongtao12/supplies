package com.sht.supplies.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sht.supplies.common.RestResponse;
import com.sht.supplies.entity.OutStock;
import com.sht.supplies.entity.QueryEntity;
import com.sht.supplies.service.GoodsService;
import com.sht.supplies.service.OutStockService;
import com.sht.supplies.service.UserService;
import io.swagger.annotations.Api;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
        if (outStockService.add(outStock)) {
            return SUCCESS("出货成功");
        }
        return ERROR("出货失败，可能库存不足");
    }

    @PutMapping
    public RestResponse update(@Validated @RequestBody OutStock outStock) {
        if (outStock.getId() == null || !outStockService.exists(outStock.getId())) {
            return ERROR("id不存在，请刷新页面重新选择");
        }
        if (!goodsService.existsWithPrimaryKey(outStock.getGoodsId())) {
            return ERROR("物料不存在，请刷新页面重试");
        }
        if (!userService.existsByUserId(outStock.getUserId())) {
            return ERROR("员工不存在，请刷新页面重试");
        }
        if (outStock.getAdminId() == null) {
            outStock.setAdminId(userId);
        }
        if (outStockService.update(outStock)) {
            return SUCCESS("更新成功");
        }
        return ERROR("更新失败");
    }

    @GetMapping
    public RestResponse findByPage(
            @RequestParam(name = "goodsId", required = false, defaultValue = "0") Integer goodsId,
            @RequestParam(name = "userId", required = false, defaultValue = "0") Integer userId,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(name = "startDateTime", required = false) LocalDateTime startDateTime,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam(name = "endDateTime", required = false) LocalDateTime endDateTime,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size
    ) {
        if (endDateTime.isBefore(startDateTime)) {
            return ERROR("开始时间不能小于结束时间");
        }
        return SUCCESS(outStockService.findByPage(goodsId, userId, startDateTime, endDateTime, page, size));
    }

    @DeleteMapping
    public RestResponse delete(@RequestParam(name = "id") Integer id) {
        if (!outStockService.exists(id)) {
            return ERROR("id不存在，请刷新页面重新选择");
        }
        if (outStockService.delete(id)) {
            return SUCCESS("删除成功");
        }
        return ERROR("删除失败");
    }

}
