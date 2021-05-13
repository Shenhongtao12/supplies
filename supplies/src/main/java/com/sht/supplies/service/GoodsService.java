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
        if (goods.getPartNumber() != null && !goods.getId().equals(goodsMapper.existsPartNumber(goods.getPartNumber()))) {
            return ERROR(goods.getPartNumber() + " 物料编号已存在");
        }
        if (!goods.getId().equals(goodsMapper.existsTitle(goods.getTitle()))) {
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

    public void delete(Integer id) {
        Goods goods = goodsMapper.selectByPrimaryKey(id);
        goodsMapper.deleteByPrimaryKey(id);
        goods.setId(id);
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
            criteria.andLike("title", "%" + title + "%");
        }

        if (StringUtils.isNotEmpty(category)) {
            criteria.andEqualTo("category", category);
        }
        PageHelper.startPage(page, size);
        Page<Goods> goodsPage = (Page<Goods>) goodsMapper.selectByExample(example);
        return new PageResult<>(goodsPage.getTotal(), goodsPage.getPages(), goodsPage.getResult());
    }

    public List<GoodsSelect> getPartNumberTitle() {
        return goodsMapper.getPartNumberTitle();
    }
}
