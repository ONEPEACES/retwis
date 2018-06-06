package com.example.weibo.service;

import com.example.common.resp.RestResponse;
import com.example.weibo.vo.User;

public interface UserService {
    String register(String username, String password);

    RestResponse<User> login(String username, String password);

    String chechkUser(String username);
}
