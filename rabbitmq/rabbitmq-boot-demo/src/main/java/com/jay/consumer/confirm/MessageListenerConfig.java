package com.jay.consumer.confirm;

import com.jay.config.MqConstant;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/3
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Configuration
public class MessageListenerConfig {

    @Autowired
    private CachingConnectionFactory connectionFactory;
    @Autowired
    private MyAckListener ackListener;
    @Autowired
    private MyDeadLetterListener deadLetterListener;

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setConcurrentConsumers(3);
        container.setMaxConcurrentConsumers(3);
        container.setPrefetchCount(1);
        // 绑定队列
        container.setQueueNames(MqConstant.COMMON_ACK_QUEUE, MqConstant.DEAD_LETTER_QUEUE);
        // 设置监听器
        container.setMessageListener(ackListener);
        // 设置消息应答模式： 模式为 NONE自动  AUTO视异常情况  MANUAL手动
//        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return container;
    }

}
