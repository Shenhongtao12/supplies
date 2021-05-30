package com.sht.supplies.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author Aaron
 * @date 2021/5/30 12:16
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BatchResult {
    private List<BatchResponse> responses;
    private Boolean success;
}
