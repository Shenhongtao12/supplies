package com.sht.supplies.entity;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

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

    @NotBlank(message = "物料编号不能为空")
    @Length(min = 6, max = 20, message = "物料编号长度应6-20字符")
    private String partNumber;

    @NotBlank(message = "物料名称不能为空")
    @Length(min = 1, max = 100, message = "物料名称长度应在1-100字符")
    public String title;

    public BigDecimal price;

    @NotBlank(message = "库存不能为空")
    @Size(min = 0, max = 99999, message = "库存最小为0，最大为99999")
    private Integer inventory;


    @NotBlank(message = "单位不能为空")
    @Length(min = 1, max = 10, message = "分类名长度应在1-10个字符")
    public String unit;

    /*@NotBlank(message = "品牌不能为空")
    @Length(min = 1, max = 100, message = "品牌长度应大于1，小于200")*/
    public String brand;

    @Transient
    public BigDecimal originalPrice;
}
