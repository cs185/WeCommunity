package edu.rice.wecommunity;

import edu.rice.wecommunity.dao.*;
import edu.rice.wecommunity.entity.DiscussPost;
import edu.rice.wecommunity.entity.LoginTicket;
import edu.rice.wecommunity.entity.Message;
import edu.rice.wecommunity.entity.Notice;
import edu.rice.wecommunity.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class DataMigrationTests {

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void tesNoticeMigration()  {
        List<Message> mList = messageMapper.findNotices();
        List<Notice> nList = new ArrayList<>();
        for (Message msg : mList) {
            ObjectMapper mapper = new ObjectMapper();
            // 将JSON字符串解析为HashMap

            HashMap<String, Object> map = null;
            try {
                map = mapper.readValue(msg.getContent(), HashMap.class);
            } catch (IOException e) {
                continue;
            }


            Notice notice = new Notice();
            notice.setFromId((Integer) map.get("userId"));
            notice.setToId(msg.getToId());
            notice.setTopic(msg.getConversationId());
            notice.setStatus(msg.getStatus());
            notice.setEntityType((Integer) map.get("entityType"));
            notice.setEntityId((Integer) map.get("entityId"));
            notice.setCreateTime(msg.getCreateTime());

            System.out.println(notice);
            nList.add(notice);
        }
        noticeMapper.batchInsertNoticess(nList);
    }
}
