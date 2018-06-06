package com.example.weibo.dao;

import com.example.common.cache.RedisCache;
import com.example.common.resp.RestResponse;
import com.example.weibo.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.concurrent.atomic.AtomicInteger;

@Repository("userHandler")
public class UserHandler {

    @Autowired
    private RedisCache redisCache;

    /**
     * 全局自增
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
        //因为登录需要根据用户名登录
        redisCache.setnx("user:username:" + username + ":userid", "" + globalIncrVal.get());
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

}
