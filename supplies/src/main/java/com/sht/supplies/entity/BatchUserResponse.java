package com.sht.supplies.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Aaron
 * @date 2021/5/30 14:53
 */
@Getter
@Setter
@NoArgsConstructor
public class BatchUserResponse {
    private String workNumber;
    private String name;
    private String errorMessage;
}
