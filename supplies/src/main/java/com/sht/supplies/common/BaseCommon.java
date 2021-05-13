package com.sht.supplies.common;

/**
 * @author Administrator
 */
public class BaseCommon {

    public RestResponse SUCCESS(String message){
        return new RestResponse(200, message);
    }

    public RestResponse SUCCESS(Object data) {
        return new RestResponse(200, data);
    }

    public RestResponse SUCCESS(Object data, String message) {
        return new RestResponse(200, data, message);
    }

    public RestResponse ERROR(String message) {
        return new RestResponse(400, message);
    }

    public RestResponse ERROR(Integer code, String message) {
        return new RestResponse(code, message);
    }
}
