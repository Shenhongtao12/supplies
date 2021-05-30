package com.sht.supplies.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sht.supplies.common.PageResult;
import com.sht.supplies.entity.*;
import com.sht.supplies.mapper.InStockMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Aaron.H.Shen
 * @date 5/13/2021 4:42 PM
 */
@Service
@Slf4j
public class InStockService {

    @Autowired
    private InStockMapper inStockMapper;

    @Autowired
    private GoodsService goodsService;

    public BatchResult batchAdd(List<InStock> inStocks, Integer adminId) {
        BatchResult result = new BatchResult();
        List<GoodsSelect> goodsList = goodsService.getPartNumberTitle(inStocks.get(0).getGoods().getCategory());
        List<BatchResponse> responses = new ArrayList<>();

        for (InStock stock : inStocks) {
            BatchResponse response = new BatchResponse();

            response.setPartNumber(stock.getGoods().getPartNumber());
            response.setTitle(stock.getGoods().getTitle());
            responses.add(response);
            Optional<GoodsSelect> select = goodsList.stream().filter(item -> StringUtils.equals(item.getPartNumber(),stock.getGoods().getPartNumber()) || item.getTitle().equals(stock.getGoods().getTitle())).findFirst();
            if (select.isPresent()) {
                stock.setGoodsId(select.get().getId());
                stock.setAmount(stock.getAmount() * select.get().getRepertory());
            } else {
                if (!checkGoods(stock.getGoods(), response)) {
                    continue;
                }
                stock.getGoods().setAdminId(adminId);
                Integer id = goodsService.saveOne(stock.getGoods());
                if (id == null || id == 0) {
                    stock.setGoodsId(0);
                    response.setIsSuccess(false);
                    response.setErrorMessage("物料保存失败，入库失败");
                    continue;
                } else {
                    stock.setGoodsId(id);
                    stock.setAmount(stock.getAmount() * stock.getGoods().getRepertory());
                }
            }
            response.setIsSuccess(true);
            stock.setAdminId(adminId);
            stock.setInDate(LocalDateTime.now());

        }
        result.setResponses(responses);
        List<InStock> stocks = inStocks.stream().filter(item -> (item.getGoodsId() != 0 && item.getAmount() > 0)).collect(Collectors.toList());
        if (stocks.size() > 0) {
            if (!batchInsert(stocks)) {
                result.setSuccess(false);
                return result;
            }
        }
        result.setSuccess(true);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean batchInsert(List<InStock> stocks) {
        try {
            inStockMapper.batchInsert(stocks);
            stocks.forEach(item -> goodsService.updateInventory(item.getGoodsId(), item.getAmount()));
            return true;
        } catch (Exception e) {
            log.warn("batch error" + e.getMessage());
            return false;
        }
    }

    public boolean checkGoods(Goods goods, BatchResponse response) {
        boolean bool = StringUtils.isNotEmpty(goods.getPartNumber()) && (goods.getPartNumber().length() > 20 || goods.getPartNumber().length() < 4);
        if (bool) {
            response.setIsSuccess(false);
            response.setErrorMessage("物料编号长度错误");
            return false;
        }

        if (StringUtils.isEmpty(goods.getTitle()) || goods.getTitle().length() > 50 || goods.getTitle().length() < 1) {
            response.setIsSuccess(false);
            response.setErrorMessage("物料名称长度错误");
            return false;
        }

        return true;
    }

    public boolean exists(Integer id) {
        return inStockMapper.existsWithPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean update(InStock inStock) {
        InStock origin = inStockMapper.selectByPrimaryKey(inStock.getId());
        int inventory = inStock.getAmount() - origin.getAmount();
        inStock.setInDate(null);
        try {
            if (goodsService.updateInventory(inStock.getGoodsId(), inventory)) {
                inStockMapper.updateByPrimaryKeySelective(inStock);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.warn("update入库插入数据异常：" + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Integer id) {
        InStock origin = inStockMapper.selectByPrimaryKey(id);
        try {
            if (goodsService.updateInventory(origin.getGoodsId(), -origin.getAmount())) {
                inStockMapper.deleteByPrimaryKey(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.warn("delete出库插入数据异常：" + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    public PageResult<OutStockResponse> findByPage(Integer goodsId, LocalDate startDateTime, LocalDate endDateTime, Integer page, Integer size) {
        if (size > 100) {
            size = 100;
        }
        PageHelper.startPage(page, size);
        Page<OutStockResponse> inStockPage = (Page<OutStockResponse>) inStockMapper.findByPage(new QueryEntity(goodsId, 0, startDateTime, endDateTime));
        return new PageResult<>(inStockPage.getTotal(), inStockPage.getPages(), inStockPage.getResult());
    }

    public boolean existsByGoods(Integer id) {
        return inStockMapper.existsByGoods(id) != 0;
    }
}
