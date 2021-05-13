package com.sht.supplies.mapper;

import com.sht.supplies.entity.User;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Aaron
 * @date 2020/11/25 20:40
 */
public interface UserMapper extends Mapper<User> {

    /**
     * check workNumber
     * @param workNumber 工号
     * @return id
     */
    @Select("select id from user where work_number = #{workNumber} limit 1")
    Integer existsWorkNumber(String workNumber);

    @Select("select id, work_number AS workNumber, name from user")
    List<User> selectUser();
}
