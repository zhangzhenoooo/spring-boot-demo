package com.zhangz.springbootdemocache.controller;

import com.alibaba.fastjson.JSON;
import com.zhangz.springbootdemocache.entity.User;
import com.zhangz.springbootdemocache.service.UserService;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUser")
    @ResponseBody
    public String getUser(@Param("userId") String userId) {
        User u = userService.getOneDemoUser(userId);
        return JSON.toJSONString(u);
    }
}
