package com.sht.supplies.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * @author Aaron
 * @date 2020/12/19 22:41
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Goods {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    @ApiModelProperty(notes = "物料编号")
    @Length(min = 4, max = 20, message = "物料编号长度应4-20字符")
    private String partNumber;

    @ApiModelProperty(notes = "物料名称")
    @NotBlank(message = "物料名称不能为空")
    @Length(min = 1, max = 50, message = "物料名称长度应在1-50字符")
    private String title;

    @ApiModelProperty(notes = "计量单位")
    @NotBlank(message = "计量单位不能为空")
    @Length(min = 1, max = 10, message = "计量单位长度应在1-10个字符")
    private String unit;

    @ApiModelProperty(notes = "总库存，根据进货量计算，库存最小值为0，最大值为999999")
    @Max(value = 999999, message = "最大值为999999")
    @Min(value = 0, message = "最小值为0")
    private Integer inventory;

    @ApiModelProperty(notes = "类别，就是Excel中每一页的标题")
    @NotBlank(message = "类别不能为空")
    @Length(min = 1, max = 10, message = "类别长度应在1-10个字符")
    private String category;

    @ApiModelProperty(notes = "不需要传该参数，从token中获取")
    private Integer adminId;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inDate;

}
