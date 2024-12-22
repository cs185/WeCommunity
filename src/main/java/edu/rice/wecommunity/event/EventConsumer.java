package edu.rice.wecommunity.event;

import com.alibaba.fastjson.JSONObject;
import edu.rice.wecommunity.entity.Event;
import edu.rice.wecommunity.entity.Message;
import edu.rice.wecommunity.entity.DiscussPost;
import edu.rice.wecommunity.entity.Notice;
import edu.rice.wecommunity.service.*;
import edu.rice.wecommunity.service.DiscussPostService;
//import edu.rice.wecommunity.service.ElasticsearchService;
import edu.rice.wecommunity.service.MessageService;
import edu.rice.wecommunity.util.CommunityConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class EventConsumer implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private DiscussPostService discussPostService;

//    @Autowired
//    private ElasticsearchService elasticsearchService;

    @RabbitListener(queues = {TOPIC_COMMENT, TOPIC_LIKE, TOPIC_FOLLOW, TOPIC_REQUEST})
    public void handleCommentMessage(Event event) {
        if (event == null) {
            logger.error("Unsupported Format!");
            return;
        }

        // 发送站内通知
        Notice notice = new Notice();
        notice.setFromId(event.getUserId());
        notice.setToId(event.getEntityUserId());
        notice.setTopic(event.getTopic());
        notice.setEntityType(event.getEntityType());
        notice.setEntityId(event.getEntityId());
        notice.setCreateTime(new Date());

        noticeService.addNotice(notice);
    }

    @RabbitListener(queues = {TOPIC_PUBLISH})
    public void handlePublishMessage(Event event) {
        if (event == null) {
            logger.error("Unsupported Format!");
            return;
        }

        DiscussPost post = discussPostService.findDiscussPostById(event.getEntityId());
//        elasticsearchService.saveDiscussPost(post);
    }

    // 消费删帖事件
    @RabbitListener(queues = {TOPIC_DELETE})
    public void handleDeleteMessage(Event event) {
        if (event == null) {
            logger.error("Unsupported Format!");
            return;
        }

//        elasticsearchService.deleteDiscussPost(event.getEntityId());
    }

    @RabbitListener(queues = {TOPIC_CHAT})
    public void sendChatMessage(Event event) {
        if (event == null) {
            logger.error("Unsupported Format!");
            return;
        }

        int targetId = (int) event.getData().get("targetId");
        String content = (String) event.getData().get("content");
        Message message = new Message();
        if (event.getEntityType() == ENTITY_TYPE_CHAT) {
            message.setFromId(event.getUserId());
            message.setToId(targetId);
            if (message.getFromId() < message.getToId()) {
                message.setConversationId(message.getFromId() + "_" + message.getToId());
            } else {
                message.setConversationId(message.getToId() + "_" + message.getFromId());
            }
            message.setContent(content);
            message.setCreateTime(new Date());
        }
        else {
            message.setFromId(event.getUserId());
            message.setToId(targetId);
            message.setConversationId("_");
            message.setContent(content);
            message.setCreateTime(new Date());
        }
        messageService.addMessage(message);
    }
}
