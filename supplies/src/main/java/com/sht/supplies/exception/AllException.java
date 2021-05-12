package com.sht.supplies.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Aaron
 */
@Setter
@Getter
public class AllException extends RuntimeException {

    private Integer code;

    private String msg;

    public AllException (Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
