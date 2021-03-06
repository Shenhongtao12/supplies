package com.sht.supplies.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import java.time.LocalDate;

/**
 * @author Aaron
 * @date 2021/5/17 18:55
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Statistical {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    private String date;

    private String partNumber;

    private String title;

    private Integer outStock;

    private Integer inventory;

    private String bigUnit;
    private Integer repertory;
    private String smallUnit;
}
