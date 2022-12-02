package com.jay.amqp.helloword;

import com.jay.amqp.util.MqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.ReturnListener;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/1
 * @description 简单模式 one to one
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class HelloDemo {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 建立连接通道
        Channel channel = MqUtils.getChannel();
        /*
         * 队列名称
         * 是否持久化消息 false mq关闭时丢失消息
         * 是否私有化队列 true 只有第一次拥有它的消费者才能一直消费消息
         * 是否自动删除队列  false 连接关闭后不会自动删除队列
         * 其他格外参数 map
         */
        channel.queueDeclare(MqUtils.HELLO_QUEUE_NAME, false, false, false, null);
        /*
         * 开启消息发送确认机制
         * confirm 是确认消息是否发送到broker
         * return 是发送到broker后是否有队列可以接收
         */
        channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                // 第二个参数：否是是批量发送
                System.out.println("消息发送成功：" + deliveryTag);
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("消息发送失败");
            }
        });

        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {

            }
        });
        /*
         * 发送交换机名称
         * 发送队列名称
         * 基础属性信息
         * 消息内容
         */
        channel.basicPublish("", MqUtils.HELLO_QUEUE_NAME, null, "hello".getBytes());
        System.out.println("消息发送成功");
        // 释放资源
        MqUtils.releaseResource();
    }

}
