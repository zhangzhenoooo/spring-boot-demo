package com.zhangz.springbootdemonacoscustomer.controller;

import com.zhangz.springbootdemonacoscustomer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("user/")
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/getTestServiceShardValue")
    public Object getTestServiceShardValue(@RequestParam("str") String str) {
        return userService.getTestServiceShardValue(str);
    }
}
