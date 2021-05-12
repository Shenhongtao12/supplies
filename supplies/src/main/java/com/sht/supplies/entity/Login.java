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
public class Login {

    @ApiModelProperty(notes = "员工工号")
    @NotBlank(message = "工号不能为空")
    @Length(min = 4, max = 20, message = "工号长度应在4-20字符")
    private String workNumber;

    @NotBlank(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度应在6-20字符")
    private String password;

    @NotBlank(message = "验证码不能为空")
    @Length(min = 4, max = 4, message = "验证码长度为4个字符")
    private String code;

}
