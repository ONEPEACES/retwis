package com.example.weibo.dao;

import com.example.common.cache.RedisCache;
import com.example.weibo.vo.Status;
import com.example.weibo.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


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
     *
     * @param username
     * @return
     */
    public List<Status> getStatuses(String username) {
        String userid = redisCache.get("user:username:" + username + ":userid");
        List<String> postids = redisCache.lrange("statuses:userid:" + userid + ":postids", 0, -1);
        List<Status> statuses = new ArrayList<>();
        postids.forEach(postid -> statuses.add(new Status(redisCache.get("post:postid:" + postid + ":content"),
                redisCache.get("post:postid:" + postid + ":time"), username)));
        statuses.forEach(status -> status.setTime(new SimpleDateFormat().format(new Date(Long.valueOf(status.getTime())))));
        return statuses;
    }

    /**
     * 获取关注用户发表的微博
     * 拉取关注用户发布微博的前5条
     *
     * @param concerns
     * @return
     */
    public List<Status> getUserWeiboWithconcerns(Set<User> concerns) {
        List<Status> resp = new ArrayList<>();
        List<List<Status>> res = new ArrayList<>();
        for (User concernUser : concerns) {
            List<Status> statuses = new ArrayList<>();
            String userid = redisCache.get("user:username:" + concernUser.getUsername() + ":userid");
            List<String> postids = redisCache.lrange("statuses:userid:" + userid + ":postids", 0, -1);
            postids.stream().limit(5).forEach(postid -> statuses.add(new Status(redisCache.get("post:postid:" + postid + ":content"),
                    redisCache.get("post:postid:" + postid + ":time"), concernUser.getUsername())));
            res.add(statuses);
        }
        res.forEach(resp::addAll);
        //实现了comparable调用无参排序
        List<Status> collect = resp.stream().sorted().collect(Collectors.toList());
        collect.forEach(status -> status.setTime(new SimpleDateFormat().format(new Date(Long.valueOf(status.getTime())))));
        return collect;
    }
}
