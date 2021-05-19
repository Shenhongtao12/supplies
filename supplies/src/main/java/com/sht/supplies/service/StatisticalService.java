package com.sht.supplies.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sht.supplies.common.PageResult;
import com.sht.supplies.entity.Statistical;
import com.sht.supplies.entity.User;
import com.sht.supplies.mapper.StatisticalMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDate;

/**
 * @author Aaron
 * @date 2021/5/17 19:15
 */
@Service
public class StatisticalService {

    @Autowired
    private StatisticalMapper statisticalMapper;

    /**
     * 每月1号自动统计当月用货量、库存量
     * 10 10 00 1 * ?
     */
    @Scheduled(cron = "00 00 00 1 * ?")
    public void autoStatistical() {
        statisticalMapper.autoStatistical();
    }

    public PageResult<Statistical> findByMonth(String date, String partNumber, String title, Integer page, Integer size) {
        if (size > 100) {
            size = 100;
        }
        PageHelper.startPage(page, size);
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(date)) {
            criteria.andEqualTo("date", date);
        }

        if (StringUtils.isNotEmpty(partNumber)) {
            criteria.andEqualTo("partNumber", partNumber);
        }

        if (StringUtils.isNotEmpty(title)) {
            criteria.andLike("title", "%" + title + "%");
        }
        Page<Statistical> users = (Page<Statistical>) statisticalMapper.selectByExample(example);
        return new PageResult<>(users.getTotal(), users.getPages(), users.getResult());
    }
}
