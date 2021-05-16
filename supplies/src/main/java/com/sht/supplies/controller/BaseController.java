package com.sht.supplies.controller;

import com.sht.supplies.common.BaseCommon;
import com.sht.supplies.common.RestResponse;
import com.sht.supplies.utils.DateUtils;
import io.jsonwebtoken.Claims;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.util.Date;

/**
 * @author Administrator
 */
public class BaseController extends BaseCommon {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected Integer userId;
    protected Claims claims;

    @ModelAttribute
    public void setResAnReq(HttpServletRequest request,HttpServletResponse response) {
        this.request = request;
        this.response = response;

        Object obj = request.getAttribute("user_claims");

        if(obj != null) {
            this.claims = (Claims) obj;
            this.userId = (Integer)claims.get("id");
        }
    }

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    /*@InitBinder
    public void initBinder(WebDataBinder binder)
    {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport()
        {
            @Override
            public void setAsText(String text)
            {
                setValue(DateUtils.parseDate(text));
            }
        });
    }*/
}
