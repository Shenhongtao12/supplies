package com.sht.supplies.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author Aaron.H.Shen
 * @date 5/13/2021 2:59 PM
 */
@Getter
@Setter
@NoArgsConstructor
public class OutStockResponse {
    private int id;

    private Integer userId;
    private Integer userWorkNumber;
    private String userName;

    private Integer goodsId;
    private String partNumber;
    private String title;

    private Integer amount;

    private Integer adminId;

    private String remark;

    private LocalDateTime inDate;

}
