package com.example.weibo.dao;

import com.example.common.cache.RedisCache;
import com.example.common.resp.RestResponse;
import com.example.weibo.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Repository("userHandler")
public class UserHandler {

    @Autowired
    private RedisCache redisCache;

    /**
     * global incr-num
     */
    private volatile AtomicInteger globalIncrVal = new AtomicInteger(0);


    public String checkUser(String username) {
        String user = redisCache.get("user:username:" + username + ":userid");
        if (user != null) {
            return "username has been register";
        }
        return "none register username";
    }


    /**
     * @param username
     * @param password
     * @return
     */
    public String register(String username, String password) {
        if (redisCache.get("user:username:" + username + ":userid") != null) {
            return "regist_fail";
        }
        //to make sure the single-auto-incr id
        //through a for-loop to set new user
        for (; ; ) {
            long usernameSeted = redisCache.setnx("user:userid:" + globalIncrVal.incrementAndGet() + ":username", username);
            long passwordSeted = redisCache.setnx("user:userid:" + globalIncrVal.get() + ":password", password);
            if (usernameSeted == 1 && passwordSeted == 1) {
                break;
            }
        }
        //according to the username when login
        redisCache.setnx("user:username:" + username + ":userid", "" + globalIncrVal.get());
        //maintain the newest register user
        redisCache.lpush("users:userids", "" + globalIncrVal.get());
        redisCache.ltrim("users:userids", 0, 50);
        return "regist_succ";
    }

    public RestResponse<User> login(String username, String password) {
        String userid = redisCache.get("user:username:" + username + ":userid");
        if (userid == null) {
            return RestResponse.createByErrorMessage("用户不存在");
        }
        String rdbPass = redisCache.get("user:userid:" + userid + ":password");
        if (password.equals(rdbPass)) {
            User user = new User();
            user.setUsername(username);
            return RestResponse.createBySucc(user);
        } else {
            return RestResponse.createByErrorMessage("用户密码错误");
        }
    }


    /**
     * 获取最新的注册用户列表
     *
     * @return
     */
    public List<User> selectNewestUsers() {
        List<String> userStringList = redisCache.lrange("users:userids", 0, 10);
        List<User> userList = new ArrayList<>();
        userStringList.forEach(userid -> userList.add(new User(redisCache.get("user:userid:" + userid + ":username"))));
        return userList;
    }

    /**
     * 获取粉丝
     *
     * @param username
     * @return
     */
    public Set<User> fans(String username) {
        String userid = redisCache.get("user:username:" + username + ":userid");
        Set<String> smembers = redisCache.smembers("fans:userid:" + userid + ":fansids");
        Set<User> users = new HashSet<>();
        smembers.forEach(fansid -> users.add(new User(redisCache.get("user:userid:" + fansid + ":username"))));
        return users;
    }

    /**
     * 获取关注的人
     *
     * @param username
     * @return
     */
    public Set<User> concerns(String username) {
        String userid = redisCache.get("user:username:" + username + ":userid");
        Set<String> userids = redisCache.smembers("concerns:userid:" + userid + ":ids");
        Set<User> users = new HashSet<>();
        userids.forEach(fansuesrid -> users.add(new User(redisCache.get("user:userid:" + fansuesrid + ":username"))));
        return users;
    }

    /**
     * 关注某个用户
     *
     * @param concerningUsername
     * @param currentUsername
     * @return
     */
    public RestResponse concernOne(String concerningUsername, String currentUsername) {
        String concerningUserId = redisCache.get("user:username:" + concerningUsername + ":userid");
        String userId = redisCache.get("user:username:" + currentUsername + ":userid");
        Long concernsSucc = redisCache.sadd("concerns:userid:" + userId + ":ids", concerningUserId);
        Long fansSucc = redisCache.sadd("fans:userid:" + concerningUserId + ":fansids", userId);
        if (concernsSucc == 1 && fansSucc == 1) {
            return RestResponse.createBySuccMessage("concern_succ");
        }
        return RestResponse.createByErrorMessage("concern_fail");
    }


    /**
     * 是否已关注某用户
     *
     * @param currentUsername    当前登录用户
     * @param concerningUsername 要关注的用户
     * @return
     */
    public String hadConcern(String currentUsername, String concerningUsername) {
        String concerningUserId = redisCache.get("user:username:" + concerningUsername + ":userid");
        String userId = redisCache.get("user:username:" + currentUsername + ":userid");
        Set<String> smembers = redisCache.smembers("fans:userid:" + concerningUserId + ":fansids");
        if (smembers.contains(userId)) {
            return "had_concern";
        } else {
            return "no_concern";
        }
    }

    /**
     * 取消关注
     * @param concerningUsername
     * @param currentUsername
     */
    public void unConcernOne(String concerningUsername, String currentUsername) {
        String concerningUserId = redisCache.get("user:username:" + concerningUsername + ":userid");
        String userId = redisCache.get("user:username:" + currentUsername + ":userid");
        Long concernsSucc = redisCache.srem("concerns:userid:" + userId + ":ids", concerningUserId);
        Long fansSucc = redisCache.srem("fans:userid:" + concerningUserId + ":fansids", userId);
    }
}
