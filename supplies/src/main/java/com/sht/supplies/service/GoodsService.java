package com.sht.supplies.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sht.supplies.common.BaseCommon;
import com.sht.supplies.common.PageResult;
import com.sht.supplies.common.RestResponse;
import com.sht.supplies.entity.Goods;
import com.sht.supplies.entity.GoodsSelect;
import com.sht.supplies.mapper.GoodsMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Aaron
 * @date 2020/12/19 23:00
 */
@Slf4j
@Service
public class GoodsService extends BaseCommon {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private InStockService inStockService;
    @Autowired
    private OutStockService outStockService;

    public RestResponse save(Goods goods) {
        if (StringUtils.isNotEmpty(goods.getPartNumber()) && goodsMapper.existsPartNumber(goods.getPartNumber()) != null) {
            return ERROR(goods.getPartNumber() + " 物料编号已存在");
        }
        if (goodsMapper.existsTitle(goods.getTitle()) != null) {
            return ERROR(goods.getTitle() + " 物料名称已存在");
        }
        goods.setId(null);
        goods.setInventory(0);
        goods.setInDate(LocalDateTime.now());
        try {
            goodsMapper.insertSelective(goods);
            return SUCCESS("添加成功");
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ERROR("添加失败，系统异常");
        }
    }

    public RestResponse update(Goods goods) {
        Integer id = goodsMapper.existsPartNumber(goods.getPartNumber());
        if (goods.getPartNumber() != null && id != null && !goods.getId().equals(id)) {
            return ERROR(goods.getPartNumber() + " 物料编号已存在");
        }
        Integer goodsId = goodsMapper.existsTitle(goods.getTitle());
        if (goodsId != null && !goods.getId().equals(goodsId)) {
            return ERROR(goods.getTitle() + " 物料名称已存在");
        }
        goods.setInDate(null);
        goods.setInventory(null);
        try {
            goodsMapper.updateByPrimaryKeySelective(goods);
            return SUCCESS("更新成功");
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ERROR("更新失败");
        }
    }

    public boolean existsWithPrimaryKey(Integer id) {
        return goodsMapper.existsWithPrimaryKey(id);
    }

    public boolean delete(Integer id) {
        if (inStockService.existsByGoods(id) || outStockService.existsByGoods(id)) {
            return false;
        }
        goodsMapper.deleteByPrimaryKey(id);
        return true;
    }

    public PageResult<Goods> findGoodsPage(String partNumber, String title, String category, Integer page, Integer size) {
        if (size > 100) {
            size = 100;
        }
        Example example = new Example(Goods.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(partNumber)) {
            criteria.andEqualTo("partNumber", partNumber);
        }

        if (StringUtils.isNotEmpty(title)) {
            criteria.andEqualTo("title", title);
        }

        if (StringUtils.isNotEmpty(category)) {
            criteria.andEqualTo("category", category);
        }
        example.setOrderByClause("id DESC");
        PageHelper.startPage(page, size);
        Page<Goods> goodsPage = (Page<Goods>) goodsMapper.selectByExample(example);
        return new PageResult<>(goodsPage.getTotal(), goodsPage.getPages(), goodsPage.getResult());
    }

    public List<GoodsSelect> getPartNumberTitle(String category) {
        Goods goods = new Goods();
        goods.setCategory(category);
        return goodsMapper.getPartNumberTitle(goods);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public synchronized Boolean updateInventory(Integer goodsId, Integer amount) {
        if (amount == 0) {
            return true;
        }
        if (amount < 0) {
            Integer inventory = goodsMapper.selectInventory(goodsId);
            if (inventory == null || inventory < Math.abs(amount)) {
                log.warn("出库失败，商品编号: " + goodsId + " ,现有库存: " + inventory + " ,出库数量: " + amount);
                return false;
            }
        }
        goodsMapper.updateInventory(goodsId, amount);
        return true;
    }

    public Integer saveOne(Goods goods) {
        goods.setId(null);
        goods.setInventory(0);
        goods.setInDate(LocalDateTime.now());
        try {
            goodsMapper.insertSelective(goods);
            return goods.getId();
        } catch (Exception e) {
            log.warn("batch save one: " + e.getMessage());
            return 0;
        }

    }
}
