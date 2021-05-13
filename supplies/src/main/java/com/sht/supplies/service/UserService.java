package com.sht.supplies.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sht.supplies.common.BaseCommon;
import com.sht.supplies.common.PageResult;
import com.sht.supplies.common.RestResponse;
import com.sht.supplies.entity.User;
import com.sht.supplies.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Aaron
 * @date 2020/11/25 20:39
 */
@Service
@Slf4j
public class UserService extends BaseCommon {

    @Autowired
    private UserMapper userMapper;

    public RestResponse save(User user) {
        if (userMapper.existsWorkNumber(user.getWorkNumber()) > 0) {
            return ERROR("员工工号重复");
        }
        user.setInDate(LocalDateTime.now());
        try {
            userMapper.insertSelective(user);
            return SUCCESS("添加成功");
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ERROR("添加失败，请刷新重试");
        }
    }

    public RestResponse update(User user) {
        if (!userMapper.existsWithPrimaryKey(user.getId())) {
            return ERROR("修改失败，该员工已删除");
        }
        user.setInDate(null);
        try {
            userMapper.updateByPrimaryKeySelective(user);
            return SUCCESS("修改成功");
        }catch (Exception e) {
            log.warn(e.getMessage(), user);
            return ERROR("修改失败，请检查数据");
        }
    }

    public PageResult<User> userPage(String  name, Integer page, Integer size) {
        if (size > 100) {
            size = 100;
        }
        PageHelper.startPage(page, size);
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(name)) {
            criteria.andEqualTo("name", name);
        }
        Page<User> users = (Page<User>) userMapper.selectByExample(example);
        return new PageResult<>(users.getTotal(), users.getPages(), users.getResult());
    }

    public RestResponse delete(Integer id) {
        if (!userMapper.existsWithPrimaryKey(id)) {
            return ERROR("该员工不存在");
        }
        userMapper.deleteByPrimaryKey(id);
        return SUCCESS("删除成功");
    }

    public List<User> selectUser() {
        return userMapper.selectUser();
    }
}
