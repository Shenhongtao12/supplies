package com.sht.supplies.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
@Table(name = "in_stock")
public class InStock {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    @ApiModelProperty(notes = "物料id")
    @Min(value = 1, message = "物料id必须大于0")
    private Integer goodsId;

    @ApiModelProperty(notes = "数量")
    @NotNull(message = "出库数量不能为空")
    @Max(value = 999999, message = "最大值为999999")
    @Min(value = 1, message = "最小值为1")
    private Integer amount;

    @ApiModelProperty(notes = "管理员id，后台可以从token中获取")
    private Integer adminId;

    @ApiModelProperty(notes = "备注")
    @Length(max = 200, message = "备注最多200字符")
    private String remark;

    private LocalDateTime inDate;

    @ApiModelProperty(notes = "批量和新增物料并且直接设置库存时需要该参数")
    private Goods goods;
}
