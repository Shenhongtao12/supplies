package com.sht.supplies.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Aaron
 * @date 2021/5/17 11:00
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BatchResponse {

    private Boolean isSuccess;
    private String errorMessage;
    private String partNumber;
    private String title;
}
