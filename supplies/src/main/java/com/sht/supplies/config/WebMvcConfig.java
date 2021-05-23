package com.sht.supplies.config;

import com.sht.supplies.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.*;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private CorsInterceptor corsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 跨域拦截器需放在最上面
        registry.addInterceptor(corsInterceptor)
                .addPathPatterns("/**");

        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/admin/login",
                        "/api/admin/getCircleCaptcha"
                );

        WebMvcConfigurer.super.addInterceptors(registry);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
                .allowCredentials(true).maxAge(3600);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        //默认地址（可以是页面或后台请求接口）
        registry.addViewController("/").setViewName("forward:/login");
        //设置过滤优先级最高
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/supplies/**").addResourceLocations("file:/supplies/");
    }

}
