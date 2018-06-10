package com.example.weibo.service;

import com.example.common.resp.RestResponse;
import com.example.weibo.vo.Status;
import com.example.weibo.vo.User;

import java.util.List;
import java.util.Set;

public interface WeiboService {
    RestResponse<String> postWeibo(String status,String username);

    List<Status> getUserWeibo(String username);

    List<Status> getUserWeiboWithconcerns(Set<User> concerns);
}
