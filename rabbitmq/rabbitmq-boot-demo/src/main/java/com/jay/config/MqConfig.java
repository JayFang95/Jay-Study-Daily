package com.jay.config;

import lombok.extern.slf4j.Slf4j;
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
 * @date 2022/12/3
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Configuration
@Slf4j
public class MqConfig {

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMandatory(true);
        // 确认回调： 是否正确发送到broker
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            log.info("关联的数据信息：{}", correlationData == null ? "" : correlationData.getReturned());
            String msg = ack ? "消息成功投递到broker" : "消息无法投递到broker:" + cause;
            log.info(msg);
            // todo 处理不能成功发送的消息
        });
        // 返回回调： 是否存在指定队列
        rabbitTemplate.setReturnsCallback((returnedMessage) -> {
            log.info("消息成功投递到broker，但是无队列可接收，返回信息：{}", returnedMessage);
            // todo 处理不能投递的信息逻辑
        });
        return rabbitTemplate;
    }

    @Bean("commonQueue")
    public Queue commonQueue(){
        return QueueBuilder.durable(MqConstant.COMMON_QUEUE).build();
    }
    @Bean("commonAckQueue")
    public Queue commonAckQueue(){
        return QueueBuilder.durable(MqConstant.COMMON_ACK_QUEUE).build();
    }
    @Bean("ttlQueue")
    public Queue ttlQueue(){
        Map<String, Object> map = new HashMap<>(16);
        map.put("x-dead-letter-exchange", MqConstant.DEAD_LETTER_EXCHANGE);
        map.put("x-dead-letter-routing-key", MqConstant.DEAD_ROUTING_KEY);
        map.put("x-message-ttl", MqConstant.TTL_UNIT_TIME_MS);
        return QueueBuilder.durable(MqConstant.TTL_QUEUE).withArguments(map).build();
    }
    @Bean("deadQueue")
    public Queue deadQueue(){
        return QueueBuilder.durable(MqConstant.DEAD_LETTER_QUEUE).build();
    }

    @Bean("directExchange")
    public Exchange directExchange(){
        return ExchangeBuilder.directExchange(MqConstant.DIRECT_EXCHANGE).durable(true).build();
    }
    @Bean("directAckExchange")
    public Exchange directAckExchange(){
        return ExchangeBuilder.directExchange(MqConstant.DIRECT_ACK_EXCHANGE).durable(true).build();
    }
    @Bean("ttlExchange")
    public Exchange ttlExchange(){
        return ExchangeBuilder.topicExchange(MqConstant.TTL_TOPIC_EXCHANGE).durable(true).build();
    }
    @Bean("deadExchange")
    public Exchange deadExchange(){
        return ExchangeBuilder.topicExchange(MqConstant.DEAD_LETTER_EXCHANGE).durable(true).build();
    }

    @Bean
    public Binding bindCommonToDirect(@Qualifier("commonQueue")Queue queue, @Qualifier("directExchange")Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(MqConstant.DIRECT_ROUTING_KEY).noargs();
    }
    @Bean
    public Binding bindAckCommonToDirect(@Qualifier("commonAckQueue")Queue queue, @Qualifier("directAckExchange")Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(MqConstant.DIRECT_ACK_ROUTING_KEY).noargs();
    }
    @Bean
    public Binding bindTtlToTopic(@Qualifier("ttlQueue")Queue queue, @Qualifier("ttlExchange")Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(MqConstant.TTL_ROUTING_KEY).noargs();
    }
    @Bean
    public Binding bindDeadToTopic(@Qualifier("deadQueue")Queue queue, @Qualifier("deadExchange")Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(MqConstant.DEAD_ROUTING_KEY).noargs();
    }

}
