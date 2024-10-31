package edu.rice.webchat.event;

import com.alibaba.fastjson.JSONObject;
import edu.rice.webchat.entity.Event;
import edu.rice.webchat.entity.Message;
import edu.rice.webchat.entity.post.DiscussPost;
import edu.rice.webchat.service.*;
import edu.rice.webchat.util.WebChatUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static edu.rice.webchat.util.WebChatUtil.ENTITY_TYPE_CHAT;
import static edu.rice.webchat.util.WebChatUtil.TOPIC_DELETE;

@Component
public class EventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private DiscussPostService discussPostService;

//    @Autowired
//    private ElasticsearchService elasticsearchService;

    @Autowired
    private GroupService groupService;

    @KafkaListener(topics = {WebChatUtil.TOPIC_COMMENT, WebChatUtil.TOPIC_LIKE, WebChatUtil.TOPIC_FOLLOW})
    public void handleCommentMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息的内容为空!");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            logger.error("消息格式错误!");
            return;
        }

        // 发送站内通知
        Message message = new Message();
        message.setFromId(WebChatUtil.SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());

        Map<String, Object> content = new HashMap<>();
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());

        if (!event.getData().isEmpty()) {
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }

        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);
    }

    @KafkaListener(topics = {WebChatUtil.TOPIC_PUBLISH})
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
//        elasticsearchService.saveDiscussPost(post);
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

//        elasticsearchService.deleteDiscussPost(event.getEntityId());
    }

    @KafkaListener(topics = {WebChatUtil.TOPIC_CHAT})
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
        if (event.getEntityType() == ENTITY_TYPE_CHAT) {
            messageService.sendPersonalMessage(content, targetId);
            Message message = new Message();
            message.setFromId(event.getUserId());
            message.setToId(targetId);
            if (message.getFromId() < message.getToId()) {
                message.setConversationId(message.getFromId() + "_" + message.getToId());
            } else {
                message.setConversationId(message.getToId() + "_" + message.getFromId());
            }
            message.setContent(content);
            message.setCreateTime(new Date());
            messageService.addMessage(message);
//            DBService.addMsgToPerson(targetId, content, "targetId");
        }
        else {
            groupService.sendGroupMessage(event.getUserId(), content, targetId);
            DBService.addMsgToGroup(targetId, content, "targetId");
        }
    }
}
