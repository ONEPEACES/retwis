package com.example.weibo.service;

import com.example.common.resp.RestResponse;
import com.example.weibo.vo.Status;

import java.util.List;

public interface WeiboService {
    RestResponse<String> postWeibo(String status,String username);

    List<Status> getUserWeibo(String username);
}
