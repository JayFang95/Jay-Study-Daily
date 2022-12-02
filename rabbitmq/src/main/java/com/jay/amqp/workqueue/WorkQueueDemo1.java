package com.jay.amqp.workqueue;

import com.jay.amqp.util.MqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/1
 * @description 工作队列 one to more
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class WorkQueueDemo1 {

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = MqUtils.getChannel();
        channel.queueDeclare(MqUtils.WORK_QUEUE_NAME, false, false, false, null);

        /*
         * 每次只消费一条消息，消费完再拉取新消息
         * 不设置basicQos(1)，MQ会默认将将所有请求平均发给所有消费者
         */
        channel.basicQos(1);
        channel.basicConsume(MqUtils.WORK_QUEUE_NAME, false, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("QUEUE1 接收到消息：" + new String(body));

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 消息应答， false 表示只对当前消息应答 true表示对当前消费者所有未签收的消息进行应答
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
        System.out.println("QUEUE1 等待");
    }

}
