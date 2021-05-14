package com.sht.supplies.interceptor;

import com.alibaba.fastjson.JSON;
import com.sht.supplies.common.RestResponse;
import com.sht.supplies.controller.BaseController;
import com.sht.supplies.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Administrator
 */
@Component
public class JwtInterceptor extends BaseController implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.通过request获取请求token信息
        String authorization = request.getHeader("Authorization");
        //判断请求头信息是否为空，或者是否已Bearer开头
        if(StringUtils.isNotEmpty(authorization) && authorization.startsWith("Bearer")) {
            //获取token数据
            String token = authorization.replace("Bearer ","");
            //解析token获取claims
            Claims claims = JwtUtils.checkJWT(token);
            if(claims != null) {
                //通过claims获取到当前用户的可访问API权限字符串
                Integer id = (Integer) claims.get("id");
                if(id != null) {
                    request.setAttribute("user_claims",claims);
                    return true;
                }else {
                    sendJsonMessage(response, new RestResponse(401, null, "鉴权失败，token错误"));
                    return false;
                }
            }
        }
        sendJsonMessage(response, new RestResponse(401, null, "请登录！"));
        return false;
    }

    /**
     * 响应数据给前端
     * @param response
     * @param obj
     */
    public static void sendJsonMessage(HttpServletResponse response, Object obj) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.print(JSON.toJSON(obj));
        writer.close();
        response.setStatus(200);
        try {
            response.flushBuffer();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
