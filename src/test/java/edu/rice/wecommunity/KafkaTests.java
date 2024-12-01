package edu.rice.wecommunity;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Properties;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class KafkaTests {

    @Autowired
    private KafkaProducer kafkaProducer;

//    @Test
//    public void testDeleteTopics() {
//        String bootstrapServers = "b-1-public.wecommunitycluster1.f7ertd.c7.kafka.us-east-1.amazonaws.com:9198,b-3-public.wecommunitycluster1.f7ertd.c7.kafka.us-east-1.amazonaws.com:9198,b-2-public.wecommunitycluster1.f7ertd.c7.kafka.us-east-1.amazonaws.com:9198";
//        Properties props = new Properties();
//        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        try (AdminClient adminClient = AdminClient.create(props)) {
//            // 删除 Topic
//            adminClient.deleteTopics(Collections.singletonList("chat")).all().get();
//            adminClient.deleteTopics(Collections.singletonList("delete")).all().get();
//            adminClient.deleteTopics(Collections.singletonList("publish")).all().get();
//            adminClient.deleteTopics(Collections.singletonList("follow")).all().get();
//            adminClient.deleteTopics(Collections.singletonList("like")).all().get();
//            adminClient.deleteTopics(Collections.singletonList("comment")).all().get();
//            adminClient.deleteTopics(Collections.singletonList("request")).all().get();
//            System.out.println("Topics deleted successfully");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Test
    public void testKafka() {
        kafkaProducer.sendMessage("chat", "Hello!");
        kafkaProducer.sendMessage("chat", "Are you there?");

        try {
            Thread.sleep(1000 * 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

@Component
class KafkaProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void sendMessage(String topic, String content) {
        kafkaTemplate.send(topic, content);
    }

}

@Component
class KafkaConsumer {

    @KafkaListener(topics = {"chat"})
    public void handleMessage(ConsumerRecord record) {
        System.out.println(record.value());
    }


}