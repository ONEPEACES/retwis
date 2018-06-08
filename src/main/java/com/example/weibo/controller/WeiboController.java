package com.example.weibo.controller;

import com.example.common.resp.Const;
import com.example.common.resp.RestResponse;
import com.example.weibo.service.WeiboService;
import com.example.weibo.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequestMapping(value = "weibo")
@Controller
public class WeiboController {

    @Autowired
    private WeiboService weiboService;

    @RequestMapping(value = "/post")
    public void postWeibo(HttpServletRequest request, HttpServletResponse response, Model model) throws ServletException, IOException {
        String status = request.getParameter("status");
        User user = (User) request.getSession().getAttribute("user");
        String username = user.getUsername();
        RestResponse<String> res = weiboService.postWeibo(status, username);
        if (res.getMessage().equals(Const.POST_SUCC)) {
            model.addAttribute("status", status);
        }
        //due to the pre-view station, the return there be simple
        request.getRequestDispatcher("/user/toHomePage").forward(request, response);
    }
}
