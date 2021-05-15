package com.sht.supplies.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * @author Aaron.H.Shen
 * @date 5/13/2021 2:59 PM
 */
@Getter
@Setter
@NoArgsConstructor
@Table(name = "out_stock")
public class OutStock {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    @ApiModelProperty(notes = "员工id")
    @NotBlank(message = "员工Id不能为空")
    private Integer userId;

    @ApiModelProperty(notes = "物料id")
    @NotBlank(message = "物料Id不能为空")
    private Integer goodsId;

    @ApiModelProperty(notes = "数量")
    @NotNull(message = "出库数量不能为空")
    @Size(min = 1, max = 999999, message = "数量应在1-999999之间")
    private Integer amount;

    @ApiModelProperty(notes = "管理员id，后台可以从token中获取")
    private Integer adminId;

    @ApiModelProperty(notes = "备注")
    @Length(max = 200, message = "备注最多200字符")
    private String remark;

    private LocalDateTime inDate;

}
