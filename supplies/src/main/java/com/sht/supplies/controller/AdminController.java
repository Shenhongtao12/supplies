package com.sht.supplies.controller;

import com.alibaba.fastjson.JSONObject;
import com.sht.supplies.common.RestResponse;
import com.sht.supplies.entity.Admin;
import com.sht.supplies.entity.Login;
import com.sht.supplies.entity.User;
import com.sht.supplies.service.AdminService;
import com.sht.supplies.utils.JwtUtils;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Aaron.H.Shen
 * @date 1/6/2021 10:10 AM
 */
@RestController
@RequestMapping("api/admin")
@Api(tags = "管理员")
public class AdminController extends BaseController {

    @Autowired
    private AdminService adminService;

    @PostMapping("add")
    public ResponseEntity<RestResponse> add(@Validated @RequestBody Admin admin) {
        if (adminService.existsWorkNumber(admin.getWorkNumber())) {
            return ResponseEntity.ok().body(ERROR(admin.getWorkNumber() + " 工号已存在，请重新输入"));
        }
        if (!adminService.add(admin)) {
            return ResponseEntity.ok().body(ERROR("数据异常，请刷新重试"));
        }
        return ResponseEntity.ok(SUCCESS("添加成功"));
    }

    @PostMapping("login")
    public ResponseEntity<RestResponse> login(@Validated @RequestBody Login admin, HttpSession session) {
        //验证图形验
        Object code = session.getAttribute("code");
        if (code == null) {
            return ResponseEntity.ok().body(ERROR("验证码已过期，请刷新页面"));
        }
        if (StringUtils.equals(code.toString().toLowerCase(), admin.getCode().toLowerCase())) {
            Admin adminInfo = adminService.login(admin);
            if (adminInfo == null) {
                return ResponseEntity.ok().body(ERROR("用户名或密码错误"));
            }
            JSONObject response = new JSONObject();
            response.put("admin", adminInfo);
            response.put("token", JwtUtils.geneJsonWebToken(adminInfo.getId()));
            return ResponseEntity.ok(SUCCESS(response));
        }
        return ResponseEntity.ok().body(ERROR("验证码错误"));
    }

    @PutMapping("updatePas")
    public ResponseEntity<RestResponse> updatePas(@Validated @Length(max = 20, min = 6, message = "密码长度应在6-20字符") @RequestParam(name = "oldPassword") String oldPassword,
                                                  @Validated @Length(max = 20, min = 6, message = "密码长度应在6-20字符") @RequestParam(name = "password") String password) {
        Admin admin = adminService.findById(userId);
        if (admin == null) {
            return ResponseEntity.ok().body(ERROR("该账号已被删除，请找其他管理员确认"));
        }
        if (adminService.updatePassword(admin, oldPassword, password)) {
            return ResponseEntity.ok(SUCCESS("修改成功，请重新登录"));
        }
        return ResponseEntity.ok().body(ERROR("密码错误，修改失败"));
    }

    @GetMapping
    public ResponseEntity<RestResponse> queryPage(@RequestParam(name = "name", required = false) String name,
                                                  @RequestParam(name = "page", defaultValue = "1") Integer page,
                                                  @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(SUCCESS(adminService.queryPage(name, page, size)));
    }

    @PutMapping
    public ResponseEntity<RestResponse> update(@Validated @RequestBody Admin admin) {
        if (admin.getId() == null) {
            return ResponseEntity.ok().body(ERROR("系统错误，缺少id，请重新登录"));
        }
        Admin admin1 = adminService.findById(admin.getId());
        if (admin1 == null) {
            return ResponseEntity.ok().body(ERROR("该账号已被删除，请联系其他管理员"));
        }
        if (adminService.update(admin)) {
            return ResponseEntity.ok(SUCCESS("修改成功"));
        }
        return ResponseEntity.ok().body(ERROR(400,"修改失败"));
    }

    @DeleteMapping
    public ResponseEntity<RestResponse> delete(@RequestParam(name = "id") Integer id) {
        if (adminService.delete(id)) {
            return ResponseEntity.ok(SUCCESS("成功"));
        }
        return ResponseEntity.ok().body(ERROR(400,"删除失败"));
    }

    @GetMapping("refreshToken")
    public ResponseEntity<RestResponse> refreshToken() {
        JSONObject response = new JSONObject();
        response.put("token", JwtUtils.geneJsonWebToken(userId));
        return ResponseEntity.ok(SUCCESS(response, "成功"));
    }

    @GetMapping("getCircleCaptcha")
    public void getCircleCaptcha(HttpServletResponse response, HttpSession session) throws IOException {
        adminService.circleCaptcha(response, session);
    }
}
