package com.sht.supplies.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * @author Aaron.H.Shen
 * @date 5/13/2021 2:59 PM
 */
@Getter
@Setter
@NoArgsConstructor
public class OutStockResponse {
    private int id;

    private Integer userId;
    private Integer userWorkNumber;
    private String userName;

    private Integer goodsId;
    private String partNumber;
    private String title;
    private Integer inventory;
    private String bigUnit;
    private Integer repertory;
    private String smallUnit;
    private String image;

    private Integer amount;

    private Integer adminId;

    private String remark;

    private LocalDateTime inDate;

}
