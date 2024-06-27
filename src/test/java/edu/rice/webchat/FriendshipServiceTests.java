package edu.rice.webchat;

//import edu.rice.webchat.dao.DiscussPostMapper;

import edu.rice.webchat.service.FriendshipService;
import edu.rice.webchat.service.GroupService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = WebchatApplication.class)
public class FriendshipServiceTests {

    @Autowired
    private FriendshipService friendshipService;

    @Test
    public void testAddFriend() {
        friendshipService.addFriend("nowcoder13", "nowcoder12");
    }

    @Test
    public void testAcceptFriend() {
        friendshipService.acceptFriend("nowcoder12", "nowcoder13");
        friendshipService.acceptFriend("nowcoder12", "nowcoder11");
        friendshipService.acceptFriend("nowcoder13", "nowcoder11");
    }

    @Test
    public void testDeleteFriend() {
        friendshipService.deleteFriend("nowcoder12", "nowcoder13");
    }

    @Test
    public void testGetFriends() {
        System.out.println(friendshipService.getFriends("nowcoder11"));
    }
}
