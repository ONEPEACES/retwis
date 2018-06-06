package com.example.weibo.controller;

import com.example.weibo.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequestMapping(value = "weibo")
@Controller
public class WeiboController {

    @RequestMapping(value = "/post")
    public String postWeibo(HttpServletRequest request){
        String status = request.getParameter("status");
        User user = (User) request.getSession().getAttribute("user");
        String username = user.getUsername();

        return "";
    }
}
