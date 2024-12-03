package edu.rice.wecommunity.event;

import com.alibaba.fastjson.JSONObject;
import edu.rice.wecommunity.entity.Event;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static edu.rice.wecommunity.util.CommunityConstant.EXCHANGE_SUFFIX;

@Component
public class EventProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 处理事件
    public void fireEvent(Event event) {
        // 将事件发布到指定的主题
        try {
            rabbitTemplate.convertAndSend(event.getTopic() + EXCHANGE_SUFFIX,
                    event.getTopic(),
                    event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
