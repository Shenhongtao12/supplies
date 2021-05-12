package com.sht.supplies.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.AES;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sht.supplies.common.PageResult;
import com.sht.supplies.entity.Admin;
import com.sht.supplies.entity.Login;
import com.sht.supplies.mapper.AdminMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;


/**
 * @author Aaron.H.Shen
 * @date 1/6/2021 10:10 AM
 */
@Service
@Slf4j
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    public Boolean existsWorkNumber(String workNumber) {
        return adminMapper.existsWorkNumber(workNumber) > 0;
    }

    public Boolean add(Admin admin) {
        admin.setId(null);
        admin.setInDate(LocalDateTime.now());
        try {
            adminMapper.insertSelective(admin);
            return true;
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }
    }

    public Admin findById(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    public boolean updatePassword(Admin admin, String oldPassword, String password) {
        if (!admin.getPassword().equals(aesEncrypt(oldPassword))) {
            return false;
        }
        admin.setPassword(aesEncrypt(password));
        adminMapper.updateByPrimaryKeySelective(admin);
        return true;
    }

    public Admin login(Login admin) {
        String password = aesEncrypt(admin.getPassword());
        Example example = new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workNumber", admin.getWorkNumber());
        criteria.andEqualTo("password", password);
        return adminMapper.selectOneByExample(example);
    }

    public PageResult<Admin> queryPage(String name, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        Example example = new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(name)) {
            criteria.andEqualTo("name", name);
        }
        Page<Admin> adminPage = (Page<Admin>) adminMapper.selectByExample(example);
        return new PageResult<>(adminPage.getTotal(), adminPage.getPages(), adminPage.getResult());
    }

    public boolean update(Admin admin) {
        admin.setPassword(null);
        admin.setWorkNumber(null);
        admin.setInDate(null);
        try {
            adminMapper.updateByPrimaryKeySelective(admin);
            return true;
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }
    }

    public boolean delete(Integer id) {
        try {
            adminMapper.deleteByPrimaryKey(id);
            return true;
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }
    }

    public void circleCaptcha(HttpServletResponse response, HttpSession session) throws IOException {
        //定义图形验证码的长、宽、验证码字符数、干扰元素个数
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);
        //CircleCaptcha captcha = new CircleCaptcha(200, 100, 4, 20);
        //图形验证码写出，可以写出到文件，也可以写出到流
        captcha.write(response.getOutputStream());
        session.setAttribute("code", captcha.getCode());
        log.warn(captcha.getCode());
    }

    public String aesEncrypt(String password) {
        return DigestUtil.md5Hex(new String(Base64Utils.decode(password.getBytes())));
    }
}
