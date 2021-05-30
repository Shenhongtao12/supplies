package com.sht.supplies.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sht.supplies.common.BaseCommon;
import com.sht.supplies.common.PageResult;
import com.sht.supplies.common.RestResponse;
import com.sht.supplies.entity.BatchUserResponse;
import com.sht.supplies.entity.User;
import com.sht.supplies.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        if (userMapper.existsWorkNumber(user.getWorkNumber()) != null) {
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
        Integer userId = userMapper.existsWorkNumber(user.getWorkNumber());
        if (userId != null && !userId.equals(user.getId())) {
            return ERROR("工号: '" + user.getWorkNumber()+ "' 已有员工使用");
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

    public Boolean existsByUserId(Integer id) {
        return userMapper.existsWithPrimaryKey(id);
    }

    public RestResponse batchSave(List<User> users) {
        List<BatchUserResponse> responses = new ArrayList<>();
        List<User> userList = selectUser();
        for (User user : users) {
            BatchUserResponse response = new BatchUserResponse();
            response.setWorkNumber(user.getWorkNumber());
            response.setWorkNumber(user.getName());
            if (!checkDataLength(user)) {
                user.setFlag(true);
                response.setErrorMessage("数据长度错误");
                responses.add(response);
                continue;
            }
            Optional<User> first = userList.stream().filter(x -> StringUtils.equals(x.getWorkNumber(), user.getWorkNumber())).findFirst();
            if (first.isPresent()) {
                user.setFlag(true);
                continue;
            }
            user.setInDate(LocalDateTime.now());
            user.setFlag(false);
        }
        userMapper.batchSave(users.stream().filter(x -> !x.getFlag()).collect(Collectors.toList()));
        return SUCCESS(responses, "导入成功");
    }

    public boolean checkDataLength(User user) {
        if (StringUtils.isEmpty(user.getWorkNumber()) || user.getWorkNumber().length() > 20 || user.getWorkNumber().length() < 4) {
            return false;
        }
        if (StringUtils.isEmpty(user.getName()) || user.getName().length() > 10 || user.getName().length() < 2) {
            return false;
        }
        return true;
    }
}
