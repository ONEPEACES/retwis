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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        //关注的用户的粉丝及其关注的人
        Map<String, Set<?>> concerInfo = getConcerInfo(user.getUsername());
        Set<User> fans = (Set<User>) concerInfo.get("fans");
        Set<User> concerns = (Set<User>) concerInfo.get("concerns");
        model.addAttribute("fansNum", fans.size());
        model.addAttribute("concernsNum", concerns.size());

        //添加当前用户到concerns以便展示当前用户发表微博
        concerns.add(user);
        List<Status> statuses = weiboService.getUserWeiboWithconcerns(concerns);
        model.addAttribute("statuses", statuses);
        return "home";
    }

    @RequestMapping(value = "/toTimelinePage")
    public void toTimelinePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/user/getNewestUsers").forward(request, response);
    }

    @RequestMapping(value = "/toProfilePage")
    public String toProfilePage(HttpSession session, @RequestParam(value = "username") String username, Model model) {
        User user = (User) session.getAttribute("user");
        //排除其本身关注
        if (username.equals(user.getUsername())) {
            model.addAttribute("concerned","1");
        }
        //排除已关注再进行关注
        String isConcerned = userService.hadConcern(user.getUsername(),username);
        if("had_concern".equals(isConcerned)){
            model.addAttribute("concerned","1");
        }
        model.addAttribute("profileUsername", username);
        Map<String, Set<?>> concerInfo = getConcerInfo(username);
        Set<User> fans = (Set<User>) concerInfo.get("fans");
        Set<User> concerns = (Set<User>) concerInfo.get("concerns");
        model.addAttribute("fansNum", fans.size());
        model.addAttribute("concernsNum", concerns.size());

        List<Status> statuses = weiboService.getUserWeibo(username);
        model.addAttribute("statuses", statuses);
        return "profile";
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
        //关注的用户的粉丝及其关注的人
        Map<String, Set<?>> concerInfo = getConcerInfo(username);
        Set<User> fans = (Set<User>) concerInfo.get("fans");
        Set<User> concerns = (Set<User>) concerInfo.get("concerns");
        model.addAttribute("fansNum", fans.size());
        model.addAttribute("concernsNum", concerns.size());

        //查找关注用户微博
        User user = (User) session.getAttribute("user");
        concerns.add(user);
        List<Status> statuses = weiboService.getUserWeiboWithconcerns(concerns);
        model.addAttribute("statuses", statuses);
        return "home";
    }


    @RequestMapping(value = "/getNewestUsers")
    public String getNewestUsers(Model model) {
        List<User> userList = userService.newestUsers();
        model.addAttribute("users", userList);
        return "timeline";
    }


    @RequestMapping(value = "/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "index";
    }

    /**
     * 获取关注信息
     *
     * @param session
     * @param concerningUsername
     * @return
     */
    @RequestMapping(value = "/concer")
    public String concern(HttpSession session, @RequestParam(value = "username") String concerningUsername, Model model) {
        User user = (User) session.getAttribute("user");
        RestResponse res = userService.concernOne(concerningUsername, user.getUsername());
        //用户自己关注自己
        if (user.getUsername().equals(concerningUsername)) {
            model.addAttribute("concerned", "1");
            return "profile";
        }
        //关注的用户的粉丝及其关注的人
        Map<String, Set<?>> concerInfo = getConcerInfo(concerningUsername);
        Set<User> fans = (Set<User>) concerInfo.get("fans");
        Set<User> concerns = (Set<User>) concerInfo.get("concerns");
        model.addAttribute("profileUsername", concerningUsername);
        model.addAttribute("fansNum", fans.size());
        model.addAttribute("concernsNum", concerns.size());
        model.addAttribute("concerned", "1");
        return "profile";
    }


    private Map<String, Set<?>> getConcerInfo(String currentUsername) {
        //当前用户的粉丝
        Set<User> fans = userService.fans(currentUsername);
        //当前用户关注的人
        Set<User> concerns = userService.concerns(currentUsername);
        Map<String, Set<?>> map = new HashMap<>();
        map.put("fans", fans);
        map.put("concerns", concerns);
        return map;
    }

}
