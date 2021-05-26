package com.sht.supplies.controller;

import com.sht.supplies.common.RestResponse;
import com.sht.supplies.entity.Goods;
import com.sht.supplies.service.GoodsService;
import io.swagger.annotations.Api;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @author Aaron
 * @date 2020/12/19 23:00
 */
@RestController
@RequestMapping("api/goods")
@Api(tags = "物料")
public class GoodsController extends BaseController {

    @Autowired
    private GoodsService goodsService;

    @PostMapping
    public ResponseEntity<RestResponse> add(@Validated @RequestBody Goods goods) {
        goods.setAdminId(userId);
        return ResponseEntity.ok(goodsService.save(goods));
    }

    @GetMapping()
    public ResponseEntity<RestResponse> findByPage(
            @Validated @Length(min = 4, max = 20, message = "物料编号长度应4-20字符") @RequestParam(name = "partNumber", required = false) String partNumber,
            @Validated @Length(min = 1, max = 50, message = "物料名称长度应1-50字符")@RequestParam(name = "title", required = false) String title,
            @Validated @Length(min = 1, max = 10, message = "物料所属分类长度应1-10字符")@RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "20") Integer size) {
        return ResponseEntity.ok(SUCCESS(goodsService.findGoodsPage(partNumber, title, category, page, size)));
    }

    @PutMapping
    public ResponseEntity<RestResponse> update(@Valid @RequestBody Goods goods) {
        if (goods.getId() == null) {
            return ResponseEntity.ok().body(ERROR("数据错误，请刷新重试"));
        }
        goods.setAdminId(userId);
        if (goodsService.existsWithPrimaryKey(goods.getId())) {
            return ResponseEntity.ok().body(goodsService.update(goods));
        }
        return ResponseEntity.ok().body(ERROR("该物料不存在"));
    }

    @DeleteMapping
    public ResponseEntity<RestResponse> delete(@RequestParam(name = "id") Integer id) {
        if (goodsService.existsWithPrimaryKey(id)) {

            if (!goodsService.delete(id)) {
                return ResponseEntity.ok().body(ERROR("该物料存在入库或出库记录，请先删除相关记录"));
            }
            return ResponseEntity.ok().body(SUCCESS("删除成功"));
        }
        return ResponseEntity.ok().body(ERROR("该物料不存在"));
    }

    @GetMapping("select-supplies")
    public RestResponse getPartNumberTitle() {
        return SUCCESS(goodsService.getPartNumberTitle(""));
    }
}
