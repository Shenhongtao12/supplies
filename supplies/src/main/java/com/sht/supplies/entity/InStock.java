package com.sht.supplies.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Aaron.H.Shen
 * @date 5/13/2021 2:59 PM
 */
@Getter
@Setter
@NoArgsConstructor
@Table(name = "in_stock")
public class InStock {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;
}
