package com.sht.supplies.mapper;

import com.sht.supplies.entity.Admin;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author Aaron
 * @date 2021/1/6 20:43
 */
public interface AdminMapper extends Mapper<Admin> {

    /**
     * check workNumber
     * @param workNumber 工号
     * @return id
     */
    @Select("select id from admin where work_number = #{workNumber} limit 1")
    Integer existsWorkNumber(String workNumber);

    @Select("select count(1) from admin limit 1")
    int countAdmin();
}
