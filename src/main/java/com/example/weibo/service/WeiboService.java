package com.example.weibo.service;

import com.example.common.resp.RestResponse;

public interface WeiboService {
    RestResponse<String> postWeibo(String status,String username);
}
