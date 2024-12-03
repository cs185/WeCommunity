package edu.rice.wecommunity.config;

import edu.rice.wecommunity.util.CommunityConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RabbitMQConfig implements CommunityConstant {
    @Bean
    Queue chatQueue() {
        return new Queue(TOPIC_CHAT, false, false, false);
    }

    @Bean
    DirectExchange chatExchange() {
        return new DirectExchange(TOPIC_CHAT + EXCHANGE_SUFFIX);
    }

    @Bean
    Binding bindingDirectChat(Queue chatQueue, DirectExchange chatExchange) {
        return BindingBuilder.bind(chatQueue).to(chatExchange).with(TOPIC_CHAT);
    }


    @Bean
    Queue requestQueue() {
        return new Queue(TOPIC_REQUEST, false, false, false);
    }

    @Bean
    DirectExchange requestExchange() {
        return new DirectExchange(TOPIC_REQUEST + EXCHANGE_SUFFIX);
    }

    @Bean
    Binding bindingDirectRequest(Queue requestQueue, DirectExchange requestExchange) {
        return BindingBuilder.bind(requestQueue).to(requestExchange).with(TOPIC_REQUEST);
    }


    @Bean
    Queue commentQueue() {
        return new Queue(TOPIC_COMMENT, false, false, false);
    }

    @Bean
    DirectExchange commentExchange() {
        return new DirectExchange(TOPIC_COMMENT + EXCHANGE_SUFFIX);
    }

    @Bean
    Binding bindingDirectComment(Queue commentQueue, DirectExchange commentExchange) {
        return BindingBuilder.bind(commentQueue).to(commentExchange).with(TOPIC_COMMENT);
    }


    @Bean
    Queue likeQueue() {
        return new Queue(TOPIC_LIKE, false, false, false);
    }

    @Bean
    DirectExchange likeExchange() {
        return new DirectExchange(TOPIC_LIKE + EXCHANGE_SUFFIX);
    }

    @Bean
    Binding bindingDirectLike(Queue likeQueue, DirectExchange likeExchange) {
        return BindingBuilder.bind(likeQueue).to(likeExchange).with(TOPIC_LIKE);
    }


    @Bean
    Queue followQueue() {
        return new Queue(TOPIC_FOLLOW, false, false, false);
    }

    @Bean
    DirectExchange followExchange() {
        return new DirectExchange(TOPIC_FOLLOW + EXCHANGE_SUFFIX);
    }

    @Bean
    Binding bindingDirectFollow(Queue followQueue, DirectExchange followExchange) {
        return BindingBuilder.bind(followQueue).to(followExchange).with(TOPIC_FOLLOW);
    }


    @Bean
    Queue publishQueue() {
        return new Queue(TOPIC_PUBLISH, false, false, false);
    }

    @Bean
    DirectExchange publishExchange() {
        return new DirectExchange(TOPIC_PUBLISH + EXCHANGE_SUFFIX);
    }

    @Bean
    Binding bindingDirectPublish(Queue publishQueue, DirectExchange publishExchange) {
        return BindingBuilder.bind(publishQueue).to(publishExchange).with(TOPIC_PUBLISH);
    }

    @Bean
    Queue deleteQueue() {
        return new Queue(TOPIC_DELETE, false, false, false);
    }

    @Bean
    DirectExchange deleteExchange() {
        return new DirectExchange(TOPIC_DELETE + EXCHANGE_SUFFIX);
    }

    @Bean
    Binding bindingDirectDelete(Queue deleteQueue, DirectExchange deleteExchange) {
        return BindingBuilder.bind(deleteQueue).to(deleteExchange).with(TOPIC_DELETE);
    }


    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
