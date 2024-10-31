package edu.rice.webchat;

//import edu.rice.webchat.dao.DiscussPostMapper;
import edu.rice.webchat.dao.DiscussPostMapper;
import edu.rice.webchat.dao.GroupMapper;
import edu.rice.webchat.dao.UserMapper;
//import edu.rice.webchat.entity.DiscussPost;
import edu.rice.webchat.entity.group.AChatGroup;
import edu.rice.webchat.entity.group.GroupFac;
import edu.rice.webchat.entity.post.DiscussPost;
import edu.rice.webchat.entity.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = WebchatApplication.class)
public class MapperTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testSelectUser() {
        User user = userMapper.selectById(101);
        System.out.println(user);

        user = userMapper.selectByName("liubei");
        System.out.println(user);

        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println(user);
    }

    @Test
    public void testSelectGroup() {
        AChatGroup group = groupMapper.selectById(1);
        System.out.println(group);

        group = groupMapper.selectByOwnerId(25);
        System.out.println(group);

        group = groupMapper.selectByName("Group 1");
        System.out.println(group);
    }

    @Test
    public void testInsertUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123");
        user.setSalt("abc");
        user.setEmail("test@test.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");

        int rows = userMapper.insertUser(user);
        System.out.println(rows);
        System.out.println(user.getId());
    }

    @Test
    public void updateUser() {
        int rows = userMapper.updateUserStatus(150, 1);
        System.out.println(rows);

        rows = userMapper.updateHeader(150, "http://www.nowcoder.com/102.png");
        System.out.println(rows);

        rows = userMapper.updatePassword(150, "hello");
        System.out.println(rows);
    }

    @Test
    public void testSelectGroups() {
        List<Integer> list = groupMapper.selectGroupIdsByUserId(11);
        System.out.println(list);
    }

    @Test
    public void testAddUserToGroups() {
        groupMapper.addUserToGroup("nowcoder11", 4);
//        groupMapper.addUserToGroup("nowcoder12", 1);
//        groupMapper.addUserToGroup("nowcoder13", 1);
//
//        groupMapper.addUserToGroup("nowcoder11", 2);
//        groupMapper.addUserToGroup("nowcoder11", 3);
    }

    @Test
    public void testSelectPosts() {
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(149, 0, 10);
        for(DiscussPost post : list) {
            System.out.println(post);
        }

        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println(rows);
    }
}
