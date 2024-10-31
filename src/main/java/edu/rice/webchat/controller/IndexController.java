package edu.rice.webchat.controller;

import edu.rice.webchat.entity.Page;
import edu.rice.webchat.entity.post.DiscussPost;
import edu.rice.webchat.entity.user.User;
import edu.rice.webchat.service.DiscussPostService;
import edu.rice.webchat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    private final DiscussPostService discussPostService;

    private final UserService userService;

    @Autowired
    public IndexController(DiscussPostService discussPostService, UserService userService) {
        this.discussPostService = discussPostService;
        this.userService = userService;
    }

    @GetMapping("/index")
    public String getIndexPage(Model model, Page page) {
        // 方法调用钱,SpringMVC会自动实例化Model和Page,并将Page注入Model.
        // 所以,在thymeleaf中可以直接访问Page对象中的数据.
        page.setRows(discussPostService.findDiscussPostRows(0));
        page.setPath("/index");

        List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (list != null) {
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                User user = userService.findUserById(post.getUserId());
                map.put("user", user);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        return "/index";
    }

    @GetMapping(path = "/error")
    public String getErrorPage() {
        return "/error/500";
    }

    @GetMapping(path = "/denied")
    public String getDeniedPage() {
        return "/error/404";
    }

}
