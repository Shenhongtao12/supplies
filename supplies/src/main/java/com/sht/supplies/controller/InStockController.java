package com.sht.supplies.controller;

import com.sht.supplies.common.RestResponse;
import com.sht.supplies.entity.BatchResponse;
import com.sht.supplies.entity.InStock;
import com.sht.supplies.service.GoodsService;
import com.sht.supplies.service.InStockService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Aaron
 * @date 2021/5/16 22:17
 */
@RestController
@RequestMapping("api/inStock")
@Api(tags = "入库")
public class InStockController extends BaseController {

    @Autowired
    private InStockService inStockService;

    @Autowired
    private GoodsService goodsService;

    @PostMapping
    public RestResponse batchAdd(@Validated @Size(max = 100, min = 1, message = "批量一次请求最多100条数据") @RequestBody List<InStock> inStocks) {
        if (inStocks.size() == 1 && inStocks.get(0).getGoods() == null) {
            if (!goodsService.existsWithPrimaryKey(inStocks.get(0).getGoodsId())) {
                return ERROR("物料不存在，请刷新页面重试");
            }
            if (inStockService.batchInsert(inStocks)) {
                return SUCCESS("入库成功");
            }
            return ERROR("数据异常，请刷新页面重试");
        } else {
            List<BatchResponse> responses = inStockService.batchAdd(inStocks, userId);
            if (responses == null) {
                return ERROR("数据异常导致批处理失败");
            }
            return SUCCESS(responses);
        }
    }

    @PutMapping
    public RestResponse update(@Validated @RequestBody InStock inStock) {
        if (inStock.getId() == null || !inStockService.exists(inStock.getId())) {
            return ERROR("id不存在，请刷新页面重新选择");
        }
        if (!goodsService.existsWithPrimaryKey(inStock.getGoodsId())) {
            return ERROR("物料不存在，请刷新页面重试");
        }

        if (inStock.getAdminId() == null) {
            inStock.setAdminId(userId);
        }
        if (inStockService.update(inStock)) {
            return SUCCESS("更新成功");
        }
        return ERROR("更新失败");
    }

    @DeleteMapping
    public RestResponse delete(@RequestParam(name = "id") Integer id) {
        if (!inStockService.exists(id)) {
            return ERROR("id不存在，请刷新页面重新选择");
        }
        if (inStockService.delete(id)) {
            return SUCCESS("删除成功");
        }
        return ERROR("删除失败");
    }

    @GetMapping
    public RestResponse findByPage(
            @RequestParam(name = "goodsId", required = false, defaultValue = "0") Integer goodsId,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(name = "startDateTime", required = false) LocalDate startDateTime,
            @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam(name = "endDateTime", required = false) LocalDate endDateTime,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size
    ) {
        if (startDateTime != null && endDateTime != null && endDateTime.isBefore(startDateTime)) {
            return ERROR("开始时间不能小于结束时间");
        }
        return SUCCESS(inStockService.findByPage(goodsId, startDateTime, endDateTime, page, size));
    }
}
