package com.example.weibo.dao;

import com.example.common.cache.RedisCache;
import com.example.weibo.vo.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Repository("weiboHandler")
public class WeiboHandler {


    @Autowired
    private RedisCache redisCache;

    public String postWeibo(String content, String username) {
        //global post-id
        if (redisCache.get("postid") == null) {
            redisCache.set("postid", "0");
        }
        //Date date = new Date();
        //DateFormat format = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        Long postid = redisCache.incr("postid");
        //store timestamp
        redisCache.set("post:postid:" + postid + ":time", String.valueOf(System.currentTimeMillis()));
        redisCache.set("post:postid:" + postid + ":content", content);

        //to store a userid,we can get userid by username
        String userid = redisCache.get("user:username:" + username + ":userid");
        redisCache.set("post:postid:" + postid + ":userid", userid);

        //the list-data-structure to store the statuses will show in page,show the new one
        redisCache.lpush("statuses:userid:" + userid + ":postids", String.valueOf(postid));
        return "post_succ";
    }


    /**
     * get statuses shown in pages
     * @param username
     * @return
     */
    public List<Status> getStatuses(String username) {
        String userid = redisCache.get("user:username:" + username + ":userid");
        List<String> postids = redisCache.lrange("statuses:userid:" + userid + ":postids", 0, -1);
        List<Status> statuses = new ArrayList<>();
        postids.forEach(postid -> statuses.add(new Status(redisCache.get("post:postid:" + postid + ":content"),
                redisCache.get("post:postid:" + postid + ":time"),username)));
        statuses.forEach(status -> status.setTime(new SimpleDateFormat().format(new Date(Long.valueOf(status.getTime())))));
        return statuses;
    }
}
