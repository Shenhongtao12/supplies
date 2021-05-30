package com.sht.supplies.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author Aaron
 * @date 2020/11/25 20:31
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    @ApiModelProperty(notes = "员工工号")
    @NotBlank(message = "工号不能为空")
    @Length(min = 4, max = 20, message = "工号长度应在4-20字符")
    private String workNumber;

    @ApiModelProperty(notes = "真实姓名")
    @NotBlank(message = "真实姓名不能为空")
    @Length(min = 2, max = 10, message = "姓名长度应在2-10字符")
    private String name;

    private LocalDateTime inDate;

    @Transient
    private Boolean flag;

}
