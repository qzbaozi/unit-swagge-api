package com.controller;

import com.bean.User;
import com.service.UserServiceIml;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @author sfh
 * @date 2023/7/4 17:23
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    UserServiceIml userService;

    @PostMapping("get")
    public User test(@RequestBody User user) {
        return userService.get(user.getName());
    }

}
