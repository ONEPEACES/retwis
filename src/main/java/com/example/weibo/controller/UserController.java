package com.example.weibo.controller;

import com.example.common.resp.RestResponse;
import com.example.weibo.service.UserService;
import com.example.weibo.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private UserService userService;



    @RequestMapping(value = "/redirectToIndex")
    public String toIndexPage(){
        return "index";
    }

    @RequestMapping(value = "/register")
    public String register(HttpServletRequest request){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String result = userService.register(username,password);
        return "index";
    }


    @RequestMapping(value = "/login")
    public String login(HttpServletRequest request, HttpSession session){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        RestResponse<User> resp = userService.login(username,password);
        if(resp.getData() == null){
            return "index";
        }
        //记录用户登录状态
        session.setAttribute("user",resp.getData());
        return "home";
    }


    @RequestMapping(value = "/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "index";
    }

}
