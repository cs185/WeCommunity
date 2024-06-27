package edu.rice.webchat.service;

import edu.rice.webchat.cache.GroupCache;
import edu.rice.webchat.dao.FriendshipMapper;
import edu.rice.webchat.dao.GroupMapper;
import edu.rice.webchat.entity.group.AChatGroup;
import edu.rice.webchat.entity.user.User;
import edu.rice.webchat.repository.UserRepository;
import edu.rice.webchat.util.FriendshipStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FriendshipService {
    private final FriendshipMapper friendshipMapper;
    private final UserRepository userRepository;

    @Autowired
    public FriendshipService(FriendshipMapper friendshipMapper, UserRepository userRepository) {
        this.friendshipMapper = friendshipMapper;
        this.userRepository = userRepository;
    }

    // 0: user1 sent request to user2
    // 1: user 2 sent request to user1
    public FriendshipStatus getStatus(String username1, String username2) {
        int userId1 = userRepository.getUser(username1).getId();
        int userId2 = userRepository.getUser(username2).getId();
        Integer status =  getStatus(userId1, userId2);
        FriendshipStatus friendshipStatus;

        if (userId1 - userId2 <= 0)
            switch (status) {
                case 0:
                    friendshipStatus = FriendshipStatus.REQUEST_SENDER;
                    break;
                case 1:
                    friendshipStatus = FriendshipStatus.REQUEST_RECEIVER;
                    break;
                case 2:
                    friendshipStatus = FriendshipStatus.FRIEND;
                    break;
                default:
                    friendshipStatus = FriendshipStatus.NO_FRIENDSHIP;
                    break;
            }
        else
            switch (status) {
                case 0:
                    friendshipStatus = FriendshipStatus.REQUEST_RECEIVER;
                    break;
                case 1:
                    friendshipStatus = FriendshipStatus.REQUEST_SENDER;
                    break;
                case 2:
                    friendshipStatus = FriendshipStatus.FRIEND;
                    break;
                default:
                    friendshipStatus = FriendshipStatus.NO_FRIENDSHIP;
                    break;
            }
        return friendshipStatus;
    }

    public Integer getStatus(int userId1, int userId2) {
        int id1 = Math.min(userId1, userId2);
        int id2 = Math.max(userId1, userId2);
        return friendshipMapper.selectFriendshipStatusByIds(id1, id2);
    }

    public void addFriend(String username1, String username2) {
        int userId1 = userRepository.getUser(username1).getId();
        int userId2 = userRepository.getUser(username2).getId();
        addFriend(userId1, userId2);
        //todo: send the request to the receiver
    }

    public void addFriend(int userId1, int userId2) {
        // 0: user with less id send the request, otherwise 1
        int id1 = Math.min(userId1, userId2);
        int id2 = Math.max(userId1, userId2);

        friendshipMapper.insertFriendship(Map.of("user_id1", id1, "user_id2", id2, "status", id1 == userId1 ? 0 : 1));
    }

    public void acceptFriend(String username1, String username2) {
        int userId1 = userRepository.getUser(username1).getId();
        int userId2 = userRepository.getUser(username2).getId();
        acceptFriend(userId1, userId2);
    }

    public void acceptFriend(int userId1, int userId2) {
        int id1 = Math.min(userId1, userId2);
        int id2 = Math.max(userId1, userId2);

        Integer status = friendshipMapper.selectFriendshipStatusByIds(id1, id2);
        if (status == null) {
            System.out.println("No friend request from " + userId1 + " and " + userId2 + " pending!");
            return;
        }

        if ((userId1 <= userId2 && status == 0) || (userId1 > userId2 && status == 1)) {
            System.out.println("Invalid friend request from " + userId1 + " and " + userId2);
            return;
        }

        friendshipMapper.updateFriendshipStatus(id1, id2, 2);
    }

    public void deleteFriend(String username1, String username2) {
        int userId1 = userRepository.getUser(username1).getId();
        int userId2 = userRepository.getUser(username2).getId();
        deleteFriend(userId1, userId2);
    }

    public void deleteFriend(int userId1, int userId2) {

        Integer status = friendshipMapper.selectFriendshipStatusByIds(Math.min(userId1, userId2), Math.max(userId1, userId2));
        if (status != 2) {
            System.out.println("No friendship from " + userId1 + " and " + userId2);
            return;
        }
        friendshipMapper.deleteFriendship(userId1, userId2);
    }

    public HashMap<Integer, String> getFriends(String username) {
        HashMap<Integer, String> userMap = new HashMap<>();
        for (User user : friendshipMapper.selectFriendsByUsername(username)) {
            userMap.put(user.getId(), user.getUsername());
        }
        return userMap;
    }
}
