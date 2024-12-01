package edu.rice.wecommunity.config;

import edu.rice.wecommunity.util.CommunityConstant;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig implements CommunityConstant {

    @Bean
    public NewTopic requestTopic() {
        return new NewTopic(TOPIC_REQUEST, 4, (short) 3);
    }

    @Bean
    public NewTopic commentTopic() {
        return new NewTopic(TOPIC_COMMENT, 4, (short) 3);
    }

    @Bean
    public NewTopic likeTopic() {
        return new NewTopic(TOPIC_LIKE, 4, (short) 3);
    }

    @Bean
    public NewTopic followTopic() {
        return new NewTopic(TOPIC_FOLLOW, 4, (short) 3);
    }

    @Bean
    public NewTopic publishTopic() {
        return new NewTopic(TOPIC_PUBLISH, 4, (short) 3);
    }

    @Bean
    public NewTopic deleteTopic() {
        return new NewTopic(TOPIC_DELETE, 4, (short) 3);
    }

    @Bean
    public NewTopic chatTopic() {
        return new NewTopic(TOPIC_CHAT, 4, (short) 3);
    }
}
