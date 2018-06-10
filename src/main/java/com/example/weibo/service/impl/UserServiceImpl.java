package com.example.weibo.service.impl;

import com.example.common.resp.RestResponse;
import com.example.weibo.dao.UserHandler;
import com.example.weibo.service.UserService;
import com.example.weibo.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service(value = "userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserHandler userHandler;

    @Override
    public String register(String username, String password) {
        return userHandler.register(username, password);
    }

    @Override
    public RestResponse<User> login(String username, String password) {
        return userHandler.login(username, password);
    }

    @Override
    public String chechkUser(String username) {
        return userHandler.checkUser(username);
    }

    @Override
    public List<User> newestUsers() {
        return userHandler.selectNewestUsers();
    }

    @Override
    public RestResponse concernOne(String concernUsername, String currentUsername) {
        return userHandler.concernOne(concernUsername, currentUsername);
    }

    @Override
    public Set<User> concerns(String username) {
        return userHandler.concerns(username);
    }

    @Override
    public Set<User> fans(String username) {
        return userHandler.fans(username);
    }

    @Override
    public String hadConcern(String currentUsername, String concerningUsername) {
        return userHandler.hadConcern(currentUsername, concerningUsername);
    }
}
