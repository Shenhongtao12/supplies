package com.sht.supplies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * @author Administrator
 */
@SpringBootApplication
@MapperScan("com.sht.supplies.mapper")
@EnableScheduling
public class SuppliesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SuppliesApplication.class, args);
    }

    @PostConstruct
    void setDefaultTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }
}
