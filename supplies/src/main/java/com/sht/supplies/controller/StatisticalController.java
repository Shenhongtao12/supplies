package com.sht.supplies.controller;

import com.sht.supplies.common.RestResponse;
import com.sht.supplies.service.StatisticalService;
import io.swagger.annotations.Api;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


/**
 * @author Aaron.H.Shen
 * @date 5/19/2021 2:15 PM
 */
@RestController
@RequestMapping("api/statistical")
@Api(tags = "数据统计")
@Validated
public class StatisticalController extends BaseController {

    @Autowired
    private StatisticalService statisticalService;

    @GetMapping
    public RestResponse userPage(@RequestParam(name = "date",required = false) String date,
                                 @RequestParam(name = "partNumber", required = false) String partNumber,
                                 @RequestParam(name = "title", required = false) String title,
                                 @RequestParam(name = "page", defaultValue = "1") Integer page,
                                 @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return SUCCESS(statisticalService.findByMonth(date, partNumber, title, page, size));
    }
}
