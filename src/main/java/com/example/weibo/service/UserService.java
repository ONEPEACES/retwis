package com.example.weibo.service;

import com.example.common.resp.RestResponse;
import com.example.weibo.vo.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    String register(String username, String password);

    RestResponse<User> login(String username, String password);

    String chechkUser(String username);

    List<User> newestUsers();

    RestResponse concernOne(String concerningUsername, String currentUsername);

    Set<User> concerns(String username);

    Set<User> fans(String username);

    String hadConcern(String currentUsername, String concerningUsername);
}
