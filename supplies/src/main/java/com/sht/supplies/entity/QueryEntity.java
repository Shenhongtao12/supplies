package com.sht.supplies.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * @author Aaron
 * @date 2021/5/16 18:11
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QueryEntity {
    private int goodsId;
    private int userId;

    private LocalDate startDateTime;
    private LocalDate endDateTime;

}
