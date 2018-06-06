package com.example.weibo.dao;

import com.example.common.cache.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("weiboHandler")
public class WeiboHandler {
    @Autowired
    private RedisCache redisCache;

    public String postWeibo(String content, String username){
        //global post-id
        if(redisCache.get("postid") == null){
            redisCache.set("postid","0");
        }
        redisCache.set()
    }
}
