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


    @PutMapping
    public ResponseEntity<RestResponse> update(@Validated @RequestBody User user) {
        if (user.getId() == null) {
            ResponseEntity.ok().body(ERROR("请输入员工id"));
        }
        return ResponseEntity.ok().body(userService.update(user));
    }

    @GetMapping("userPage")
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
}
