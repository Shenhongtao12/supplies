package com.sht.supplies.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sht.supplies.common.PageResult;
import com.sht.supplies.entity.Goods;
import com.sht.supplies.mapper.GoodsMapper;
import net.sf.jsqlparser.expression.TimeValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Aaron
 * @date 2020/12/19 23:00
 */
@Service
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 分页查询数据
     */
    public PageResult<Map<String, Object>> findByPage(int page, int size, String keyword) throws IOException {
        if (page <= 1) {
            page = 1;
        }

        return new PageResult<>( );
    }

    public int save(Goods goods) {
        goodsMapper.insertSelective(goods);
        return goods.getId();
    }





    public int update(Goods goods) {
        return goodsMapper.updateByPrimaryKeySelective(goods);
    }

    public boolean existsWithPrimaryKey(Integer id) {
        return goodsMapper.existsWithPrimaryKey(id);
    }

    public void delete(Integer id) {
        Goods goods = goodsMapper.selectByPrimaryKey(id);
        goodsMapper.deleteByPrimaryKey(id);
        goods.setId(id);
    }

    public Goods findByID(Integer id) {
        return goodsMapper.selectByPrimaryKey(id);
    }

    public PageResult<Goods> findGoodsPage(String keyword, Boolean shelf, Integer page, Integer size) {
        Example example = new Example(Goods.class);
        Example.Criteria criteria = example.createCriteria();
        if (shelf != null) {
            criteria.andEqualTo("shelf", shelf);
        }

        if (keyword != null) {
            criteria.andLike("title", "%" + keyword + "%");
        }
        PageHelper.startPage(page, size);
        Page<Goods> goodsPage = (Page<Goods>) goodsMapper.selectByExample(example);
        return new PageResult<>(goodsPage.getTotal(), goodsPage.getPages(), goodsPage.getResult());
    }

    public Integer countGoods() {
        return goodsMapper.countGoods();
    }
}
