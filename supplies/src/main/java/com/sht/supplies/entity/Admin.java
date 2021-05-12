package com.sht.supplies.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author Aaron
 * @date 2021/1/6 20:40
 */
@Getter
@Setter
@NoArgsConstructor
public class Admin {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Integer id;

    @ApiModelProperty(notes = "员工工号,登录使用")
    @NotBlank(message = "工号不能为空")
    @Length(min = 4, max = 20, message = "工号长度应在4-20字符")
    private String workNumber;

    @ApiModelProperty(notes = "真实姓名")
    @NotBlank(message = "真实姓名不能为空")
    @Length(min = 2, max = 10, message = "姓名长度应在2-10字符")
    private String name;

    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度应在6-20字符")
    private String password;

    @ApiModelProperty(notes = "手机号")
    @Length(min = 6, max = 20, message = "联系方式长度应在6-20字符")
    private String phone;

    @ApiModelProperty(notes = "创建时间")
    private LocalDateTime inDate;


}
