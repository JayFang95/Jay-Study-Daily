package com.jay.controller;

import com.jay.config.MqConstant;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/3
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@RequestMapping("mq")
public class MqController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("common/msg")
    public String sendCommonMsg(){
        String helloCommonMsg = "你好，我是一条来自己topic交换机的消息";
        try {
            CorrelationData data = new CorrelationData();
            ReturnedMessage message = new ReturnedMessage(new Message(helloCommonMsg.getBytes()),
                    1, "replyText",
                    MqConstant.DIRECT_EXCHANGE, MqConstant.DIRECT_ROUTING_KEY);
            data.setReturned(message);
            rabbitTemplate.convertAndSend(MqConstant.DIRECT_EXCHANGE, MqConstant.DIRECT_ROUTING_KEY, helloCommonMsg.getBytes(), data);
        } catch (AmqpException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "common msg send success";
    }

    @RequestMapping("common/ack/msg")
    public String sendAckCommonMsg(){
        String helloAckCommonMsg = "你好，我是一条来自己ack topic交换机的消息";
        try {
            CorrelationData data = new CorrelationData();
            ReturnedMessage message = new ReturnedMessage(new Message(helloAckCommonMsg.getBytes()),
                    1, "replyText",
                    MqConstant.DIRECT_ACK_EXCHANGE, MqConstant.DIRECT_ACK_ROUTING_KEY);
            data.setReturned(message);
            rabbitTemplate.convertAndSend(MqConstant.DIRECT_ACK_EXCHANGE, MqConstant.DIRECT_ACK_ROUTING_KEY, helloAckCommonMsg.getBytes(), data);
        } catch (AmqpException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "common ack msg send success";
    }

    @RequestMapping("common/ack/batch/msg")
    public String sendAckCommonBatchMsg(){
        try {
            for (int i = 0; i < 10; i++) {
                String helloAckCommonBatchMsg = "你好，我是一条来自己ack topic交换机的消息：" + i;
                CorrelationData data = new CorrelationData();
                ReturnedMessage message = new ReturnedMessage(new Message(helloAckCommonBatchMsg.getBytes()),
                        1, "replyText",
                        MqConstant.DIRECT_ACK_EXCHANGE, MqConstant.DIRECT_ACK_ROUTING_KEY);
                data.setReturned(message);
                rabbitTemplate.convertAndSend(MqConstant.DIRECT_ACK_EXCHANGE, MqConstant.DIRECT_ACK_ROUTING_KEY, helloAckCommonBatchMsg.getBytes(), data);
            }
        } catch (AmqpException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "common ack msg send success";
    }

    @RequestMapping("ttl/ack/msg")
    public String sendTtlMsg(){
        String helloTtlMsg = "你好，我是一条来自己ttl 交换机的消息";
        try {
            CorrelationData data = new CorrelationData();
            ReturnedMessage message = new ReturnedMessage(new Message(helloTtlMsg.getBytes()),
                    1, "replyText",
                    MqConstant.TTL_TOPIC_EXCHANGE, MqConstant.TTL_ROUTING_KEY);
            data.setReturned(message);
            rabbitTemplate.convertAndSend(MqConstant.TTL_TOPIC_EXCHANGE, MqConstant.TTL_ROUTING_KEY, helloTtlMsg.getBytes(), data);
        } catch (AmqpException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "ttl msg send success";
    }

}
