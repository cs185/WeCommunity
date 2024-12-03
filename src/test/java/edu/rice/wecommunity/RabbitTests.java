package edu.rice.wecommunity;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static edu.rice.wecommunity.util.CommunityConstant.EXCHANGE_SUFFIX;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class RabbitTests {

    @Autowired
    private RabbitProducer rabbitProducer;

    @Test
    public void testRabbit() {
        rabbitProducer.sendMessage("spring-boot", "Hello!");
        rabbitProducer.sendMessage("spring-boot", "Are you there?");

        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

@Component
class RabbitProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(String topic, String content) {
        rabbitTemplate.convertAndSend(topic + EXCHANGE_SUFFIX, topic, content);
    }

}

@Component
class RabbitConsumer {

    @RabbitListener(queues = {"spring-boot"})
    public void handleMessage(String content) {
        System.out.println(content);
    }


}