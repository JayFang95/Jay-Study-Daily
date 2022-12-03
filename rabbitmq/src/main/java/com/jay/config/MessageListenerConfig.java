package com.jay.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/2
 * @description 设置消息监听容器
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Configuration
public class MessageListenerConfig {

    @Autowired
    private CachingConnectionFactory connectionFactory;
    @Autowired
    private MyAckListener myAckListener;

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(1);
        // 设置了批量拉去消息时，必须要手动应答消息
        container.setPrefetchCount(4);
        // 默认rabbitmq是自动应答，修改为MANUAL手动应答消息， 默认为AcknowledgeMode.AUTO
//        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setQueueNames("queue_boot");
        container.setMessageListener(myAckListener);
        return container;
    }

}
