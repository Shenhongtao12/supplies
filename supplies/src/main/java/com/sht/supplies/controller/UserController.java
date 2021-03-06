package com.sht.supplies.controller;

import com.alibaba.fastjson.JSONObject;
import com.sht.supplies.common.PageResult;
import com.sht.supplies.common.RestResponse;
import com.sht.supplies.entity.User;
import com.sht.supplies.service.UserService;
import com.sht.supplies.utils.JwtUtils;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author Aaron
 * @date 2020/11/25 20:38
 */
@RestController
@RequestMapping("api/user")
@Validated
@Api(tags = "员工")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @PostMapping("save")
    public ResponseEntity<RestResponse> saveUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @PostMapping("batchSave")
    public ResponseEntity<RestResponse> batchSave(@Validated @Size(max = 100, min = 1, message = "批量一次请求最多100条数据") @RequestBody List<User> users) {
        return ResponseEntity.ok(userService.batchSave(users));
    }


    @PutMapping
    public ResponseEntity<RestResponse> update(@Validated @RequestBody User user) {
        if (user.getId() == null) {
            ResponseEntity.ok().body(ERROR("请输入员工id"));
        }
        return ResponseEntity.ok().body(userService.update(user));
    }

    @GetMapping
    public ResponseEntity<RestResponse> userPage(@RequestParam(name = "name",required = false) String name,
                                                 @RequestParam(name = "page", defaultValue = "1") Integer page,
                                                 @RequestParam(name = "size", defaultValue = "10") Integer size) {
        PageResult<User> userPageResult = userService.userPage(name, page, size);
        return ResponseEntity.ok(SUCCESS(userPageResult));
    }

    @DeleteMapping
    public ResponseEntity<RestResponse> delete(@RequestParam(name = "id") Integer id) {

        return ResponseEntity.ok(userService.delete(id));
    }

    @GetMapping("select-user")
    public ResponseEntity<RestResponse> selectUser() {
        return ResponseEntity.ok(SUCCESS(userService.selectUser()));
    }
}
