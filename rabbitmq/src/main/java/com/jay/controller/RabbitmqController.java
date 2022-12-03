package com.jay.controller;

import com.jay.config.RabbitmqConfig;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/2
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@RequestMapping("rabbitmq")
public class RabbitmqController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("send/{msg}")
    public String sendMsg(@PathVariable String msg){
        CorrelationData data = new CorrelationData();
//        data.setReturned(new ReturnedMessage(new Message(msg.getBytes()), 1, "", RabbitmqConfig.TOPIC_EXCHANGE, "test.demo.send" ));
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend(RabbitmqConfig.TOPIC_EXCHANGE, "test.demo.send", msg + i);
        }
        return "消息发送成功" ;
    }

    @RequestMapping("send/error/{msg}")
    public String sendErrorMsg(@PathVariable String msg){
        CorrelationData data = new CorrelationData();
        rabbitTemplate.convertAndSend(RabbitmqConfig.TOPIC_EXCHANGE + "123", "test.demo.send", msg, data);
        try {
            return "消息发送:" + Objects.requireNonNull(data.getFuture().get()).isAck() ;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "异常";
        }
    }

    @RequestMapping("send/error2/{msg}")
    public String sendError2Msg(@PathVariable String msg){
        CorrelationData data = new CorrelationData();
        rabbitTemplate.convertAndSend(RabbitmqConfig.DIRECT_EXCHANGE , "123test.demo.send", msg, data);
        try {
            return "消息发送:" + Objects.requireNonNull(data.getFuture().get()).isAck() ;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "异常";
        }
    }

}
