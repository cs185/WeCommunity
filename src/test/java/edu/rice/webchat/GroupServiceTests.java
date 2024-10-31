//package edu.rice.webchat;
//
////import edu.rice.webchat.dao.DiscussPostMapper;
//
//import edu.rice.webchat.dao.GroupMapper;
//import edu.rice.webchat.dao.UserMapper;
//import edu.rice.webchat.entity.group.AChatGroup;
//import edu.rice.webchat.entity.user.User;
//import edu.rice.webchat.service.GroupService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.Arrays;
//import java.util.List;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@ContextConfiguration(classes = WebchatApplication.class)
//public class GroupServiceTests {
//
//    @Autowired
//    private GroupService groupService;
//
////    @Autowired
////    private DiscussPostMapper discussPostMapper;
//
//    @Test
//    public void testGetUserGroupNames() {
//        System.out.println(groupService.getUserGroupNames("nowcoder11"));
//    }
//
//    @Test
//    public void testGetGroup() {
//        System.out.println(groupService.getGroup(1));
//    }
//
//    @Test
//    public void testAddUserToGroup() {
//        groupService.addUserToGroup("nowcoder12", 4);
//    }
//
//    @Test
//    public void testGetGroupMemberNames() {
//        System.out.println(Arrays.toString(groupService.getGroupMemberNames(1)));
//    }
//}
