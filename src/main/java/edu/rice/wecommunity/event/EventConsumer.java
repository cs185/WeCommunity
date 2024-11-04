package edu.rice.wecommunity.event;

import com.alibaba.fastjson.JSONObject;
import edu.rice.wecommunity.entity.Event;
import edu.rice.wecommunity.entity.Message;
import edu.rice.wecommunity.entity.DiscussPost;
import edu.rice.wecommunity.entity.Notice;
import edu.rice.wecommunity.service.*;
import edu.rice.wecommunity.service.DiscussPostService;
import edu.rice.wecommunity.service.ElasticsearchService;
import edu.rice.wecommunity.service.MessageService;
import edu.rice.wecommunity.util.CommunityConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
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

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private GroupService groupService;

//    @Autowired
//    private GroupService groupService;

    @KafkaListener(topics = {TOPIC_COMMENT, TOPIC_LIKE, TOPIC_FOLLOW, TOPIC_REQUEST})
    public void handleCommentMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("Empty Notice!");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
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

    @KafkaListener(topics = {TOPIC_PUBLISH})
    public void handlePublishMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息的内容为空!");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("消息格式错误!");
            return;
        }

        DiscussPost post = discussPostService.findDiscussPostById(event.getEntityId());
        elasticsearchService.saveDiscussPost(post);
    }

    // 消费删帖事件
    @KafkaListener(topics = {TOPIC_DELETE})
    public void handleDeleteMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息的内容为空!");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("消息格式错误!");
            return;
        }

        elasticsearchService.deleteDiscussPost(event.getEntityId());
    }

    @KafkaListener(topics = {TOPIC_CHAT})
    public void sendChatMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息的内容为空!");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("消息格式错误!");
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
