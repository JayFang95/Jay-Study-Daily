package com.jay.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/5
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
public class SimpleProducer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setNamesrvAddr("192.168.25.129:9876");
        producer.setProducerGroup("simple-producer");
        producer.start();

        Message message = new Message();
        message.setTopic("hello-simple");
        message.setBody("hello".getBytes());
        // 同步发送
        producer.send(message);
        // 单向发送： 没有响应，速度最快
        producer.sendOneway(message, new MessageQueueSelector() {
            @Override
            public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                return null;
            }
        }, null);

        // 异步发送
        producer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });

        log.info("消息发生成功，关闭生产者");
        producer.shutdown();
    }

}
