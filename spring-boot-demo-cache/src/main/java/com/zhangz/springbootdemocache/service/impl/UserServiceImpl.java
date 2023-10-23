package com.zhangz.springbootdemocache.service.impl;

import com.zhangz.springbootdemocache.entity.User;
import com.zhangz.springbootdemocache.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    /**
     * value 缓存名称，可指定多个缓存，返回其中命中的第一个；
     * key 缓存的key ，显示指定
     * condition 条件，只缓存满足条件的 condition = "#userId.equals('123456')"
     * @param userId
     * @return
     */
    @Override
    @Cacheable(value = {"user"}, key = "'user_' + #userId" )
    public User getOneDemoUser(String userId) {
        User user = User.builder().userId(userId).userName("testname").build();
        return user;
    }
}
