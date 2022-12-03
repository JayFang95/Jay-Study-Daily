package com.jay.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/2
 * @description 配置交换机 队列和绑定关系
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Configuration
public class RabbitmqConfig {

    public static final String TOPIC_EXCHANGE = "topic_boot_exchange";
    public static final String DIRECT_EXCHANGE = "direct_boot_exchange";
    public static final String QUEUE_NAME = "queue_boot";
    public static final String DEAD_QUEUE_NAME = "dead_queue";
    public static final String DEAD_EXCHANGE_NAME = "dead_exchange";

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMandatory(true);
        // 确认回调： 是否正确发送到broker
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            System.out.println("接收确认信息：" + correlationData);
            if (ack){
                System.out.println("消息发送成功");
            }else {
                System.out.println("消息发送失败：" + cause);
            }
        });
        // 返回回调： 是否存在指定队列
        rabbitTemplate.setReturnsCallback((returnedMessage) -> {
            System.out.println("消息投递成功，但是无队列接收");
            System.out.println(returnedMessage);
        });

        return rabbitTemplate;
    }

    @Bean("bootExchange")
    public Exchange bootExchange(){
        //alternate 替换，备选
        ExchangeBuilder.topicExchange(TOPIC_EXCHANGE).alternate("alternate_exchange");
        return ExchangeBuilder.topicExchange(TOPIC_EXCHANGE).durable(true).build();
    }

    @Bean("bootExchange2")
    public Exchange bootExchange2(){
        //alternate 替换，备选
        ExchangeBuilder.directExchange(DIRECT_EXCHANGE).alternate("alternate_exchange");
        return ExchangeBuilder.topicExchange(DIRECT_EXCHANGE).durable(true).build();
    }

    @Bean("bootQueue")
    public Queue bootQueue(){
        Map<String, Object> map = new HashMap<>(16);
        map.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        map.put("x-dead-letter-routing-key", "#");
        map.put("x-message-ttl", 30000);
        return QueueBuilder.durable(QUEUE_NAME).withArguments(map).build();
    }

    @Bean
    public Binding bindQueueToExchange(
            @Qualifier("bootExchange") Exchange exchange
            , @Qualifier("bootQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("test.#").noargs();
    }

    @Bean
    public Binding bindQueueToExchange2(
            @Qualifier("bootExchange2") Exchange exchange
            , @Qualifier("bootQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("direct-routing").noargs();
    }


    @Bean("dlExchange")
    public Exchange dlExchange(){
        //alternate 替换，备选
        ExchangeBuilder.directExchange(DEAD_EXCHANGE_NAME).alternate("alternate_exchange");
        return ExchangeBuilder.directExchange(DEAD_EXCHANGE_NAME).durable(true).build();
    }

    @Bean("dlQueue")
    public Queue dlQueue(){
        return QueueBuilder.durable(DEAD_QUEUE_NAME).build();
    }

    @Bean
    public Binding bindDeadQueueToExchange(
            @Qualifier("dlExchange") Exchange exchange
            , @Qualifier("dlQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with("#").noargs();
    }


}
