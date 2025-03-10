package edu.rice.wecommunity.controller;

import edu.rice.wecommunity.entity.DiscussPost;
import edu.rice.wecommunity.entity.Page;
import edu.rice.wecommunity.entity.User;
import edu.rice.wecommunity.service.DiscussPostService;
import edu.rice.wecommunity.service.LikeService;
import edu.rice.wecommunity.service.UserService;
import edu.rice.wecommunity.util.CommunityConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mysql.cj.conf.PropertyKey.logger;

@Controller
public class HomeController implements CommunityConstant {
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @RequestMapping(path = "/index", method = RequestMethod.GET)
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
                try {
                    map.put("content", post.getContent().length() > 135
                            ? post.getContent().substring(0, 135) + "..."
                            : post.getContent());
                    User user = userService.findUserById(post.getUserId());
                    map.put("user", user);
                    long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId());
                    map.put("likeCount", likeCount);

                    List<String> imageUrls = discussPostService.findImageUrls(post.getId());
                    map.put("imageUrls", imageUrls);
                }
                catch (Exception e) {
                    logger.error(e.getMessage());
                }

                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        return "index";
    }

    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String getErrorPage() {
        return "error/500";
    }

    @RequestMapping(path = "/denied", method = RequestMethod.GET)
    public String getDeniedPage() {
        return "error/404";
    }
}
