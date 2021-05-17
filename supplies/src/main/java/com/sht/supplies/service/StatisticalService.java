package com.sht.supplies.service;

import com.sht.supplies.mapper.StatisticalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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
    @Scheduled(cron = "10 10 00 1 * ?")
    public void autoStatistical() {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1);



    }
}
