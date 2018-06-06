package com.example.weibo.service.impl;

import com.example.common.resp.RestResponse;
import com.example.weibo.dao.WeiboHandler;
import com.example.weibo.service.WeiboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "weiboServiceImpl")
public class WeiboServiceImpl implements WeiboService {

    @Autowired
    private WeiboHandler weiboHandler;

    @Override
    public RestResponse<String> postWeibo(String status, String username) {
        
        return null;
    }
}
