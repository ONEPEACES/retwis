package com.example.weibo.controller;

import com.example.common.resp.RestResponse;
import com.example.weibo.service.UserService;
import com.example.weibo.service.WeiboService;
import com.example.weibo.vo.Status;
import com.example.weibo.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private WeiboService weiboService;


    @RequestMapping(value = "/redirectToIndex")
    public String toIndexPage() {
        return "index";
    }

    @RequestMapping(value = "/toHomePage")
    public String toHomePage(Model model, HttpSession session) {
        // homepage has user's List<status>
        // show personal status list
        User user = (User) session.getAttribute("user");
        List<Status> statuses = weiboService.getUserWeibo(user.getUsername());
        model.addAttribute("statuses", statuses);
        return "home";
    }

    @RequestMapping(value = "/register")
    public String register(HttpServletRequest request) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String result = userService.register(username, password);
        return "index";
    }


    @RequestMapping(value = "/login")
    public String login(HttpServletRequest request, HttpSession session, Model model) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        RestResponse<User> resp = userService.login(username, password);
        if (resp.getData() == null) {
            return "index";
        }
        //contain user-status
        session.setAttribute("user", resp.getData());
        List<Status> statuses = weiboService.getUserWeibo(resp.getData().getUsername());
        model.addAttribute("statuses", statuses);
        return "home";
    }


    @RequestMapping(value = "/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

}
