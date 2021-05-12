package com.sht.supplies.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sht.supplies.common.PageResult;
import com.sht.supplies.common.RestResponse;
import com.sht.supplies.entity.User;
import com.sht.supplies.mapper.UserMapper;
import com.sht.supplies.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;

/**
 * @author Aaron
 * @date 2020/11/25 20:39
 */
@Service
@Slf4j
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public RestResponse save(User user) {
        if (userMapper.existsWorkNumber(user.getWorkNumber()) > 0) {
            return new RestResponse(400, "员工工号重复");
        }
        user.setInDate(LocalDateTime.now());
        try {
            userMapper.insertSelective(user);
            return new RestResponse(200, "添加成功");
        } catch (Exception e) {
            log.warn(e.getMessage());
            return new RestResponse(400, "添加失败，请刷新重试");
        }
    }

    public RestResponse update(User user) {
        if (!userMapper.existsWithPrimaryKey(user.getId())) {
            return new RestResponse(400, "修改失败，该员工已删除");
        }
        user.setInDate(null);
        try {
            userMapper.updateByPrimaryKeySelective(user);
            return new RestResponse(200, "修改成功");
        }catch (Exception e) {
            log.warn(e.getMessage(), user);
            return new RestResponse(200, "修改成功");
        }
    }

    public PageResult<User> userPage(String  name, Integer page, Integer size) {
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
            return new RestResponse(400, "该员工不存在");
        }
        userMapper.deleteByPrimaryKey(id);
        return new RestResponse(200, "删除成功");
    }

}
