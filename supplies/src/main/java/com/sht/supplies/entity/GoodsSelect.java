package com.sht.supplies.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Aaron.H.Shen
 * @date 5/13/2021 4:05 PM
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoodsSelect {

    private Integer id;
    private String partNumber;
    private String title;
    private String bigUnit;
    private Integer repertory;
    private String smallUnit;
}
