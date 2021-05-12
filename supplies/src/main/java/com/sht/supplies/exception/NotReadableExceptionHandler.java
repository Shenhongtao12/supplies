package com.sht.supplies.exception;

import com.sht.supplies.common.RestResponse;
import com.sht.supplies.controller.BaseController;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 异常处理控制器
 * @author Administrator
 */
@ControllerAdvice
public class NotReadableExceptionHandler extends BaseController {

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus()
    public RestResponse notReadableException(Exception e) {
        RestResponse restResponse = new RestResponse();
        return ERROR("请求数据格式错误，请检查.");
    }

}
