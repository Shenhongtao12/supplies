package com.sht.supplies.service;

import com.sht.supplies.common.BaseCommon;
import com.sht.supplies.mapper.OutStockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Aaron.H.Shen
 * @date 5/13/2021 4:42 PM
 */
@Service
public class OutStockService extends BaseCommon {

    @Autowired
    private OutStockMapper outStockMapper;
}
