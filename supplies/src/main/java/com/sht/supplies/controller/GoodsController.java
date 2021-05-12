package com.sht.supplies.controller;

import com.sht.supplies.common.RestResponse;
import com.sht.supplies.entity.Goods;
import com.sht.supplies.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @author Aaron
 * @date 2020/12/19 23:00
 */
@RestController
@RequestMapping("api/goods")
public class GoodsController extends BaseController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping
    public ResponseEntity<RestResponse> query(@RequestParam(name = "keyword", required = false) String keyword,
                                              @RequestParam(name = "page", defaultValue = "1") Integer page,
                                              @RequestParam(name = "size", defaultValue = "30") Integer size) {
        try {
            return ResponseEntity.ok(SUCCESS(goodsService.findByPage(page, size, keyword)));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(ERROR("请求失败"));
        }
    }


    @GetMapping("findById")
    public ResponseEntity<RestResponse> findById(@RequestParam(name = "id") Integer id) {
        return ResponseEntity.ok(SUCCESS(goodsService.findByID(id)));
    }

    @GetMapping("findGoodsPage")
    public ResponseEntity<RestResponse> findByPage(@RequestParam(name = "keyword", required = false) String keyword,
                                                   @RequestParam(name = "shelf", required = false) Boolean shelf,
                                                   @RequestParam(name = "page", defaultValue = "1") Integer page,
                                                   @RequestParam(name = "size", defaultValue = "20") Integer size) {
        return ResponseEntity.ok(SUCCESS(goodsService.findGoodsPage(keyword, shelf, page, size)));
    }


    @PutMapping
    public ResponseEntity<RestResponse> update(@Valid @RequestBody Goods goods) {
        if (goodsService.existsWithPrimaryKey(goods.getId())) {
            int i = goodsService.update(goods);
            if (i != 0) {
                return ResponseEntity.ok().body(SUCCESS("更新成功"));
            }
            return ResponseEntity.ok().body(ERROR(400, "更新失败"));
        }
        return ResponseEntity.ok().body(ERROR(400, "该商品不存在"));
    }

    @DeleteMapping("delete")
    public ResponseEntity<RestResponse> delete(@RequestParam(name = "id") Integer id) {
        if (goodsService.existsWithPrimaryKey(id)) {
            goodsService.delete(id);
            return ResponseEntity.ok().body(SUCCESS("删除成功"));
        } else {
            return ResponseEntity.ok().body(ERROR(400, "该商品不存在"));
        }
    }
}
