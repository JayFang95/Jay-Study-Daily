package com.jay.amqp.helloword;

import com.jay.amqp.util.MqUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/1
 * @description 简单模式消息消费
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class HelloConsumer {

    public static void main(String[] args) throws IOException, TimeoutException {
        // 建立连接通道
        Channel channel = MqUtils.getChannel();
        // 绑定队列
        channel.queueDeclare(MqUtils.HELLO_QUEUE_NAME, false, false, false, null);
        DefaultConsumer consumer = new MyConsumer(channel);
        /*
         * 消费消息
         * 消费的队列名称
         * 是否自动应答
         * consumer对象
         */
        channel.basicConsume(MqUtils.HELLO_QUEUE_NAME, false, consumer);

        /*
         * 消息队列
         * 消息投递回调
         * 消息取消回调
         */
        channel.basicConsume(MqUtils.HELLO_QUEUE_NAME, (consumerTag,  message) -> {
            System.out.println("接收到投递消息：" + new String(message.getBody()));
        }, (consumerTag) -> {
            System.out.println("消息投递失败：" + consumerTag);
        });
        System.out.println("正在监听消息");
        // 释放资源
//        MqUtils.releaseResource();
    }

}

class MyConsumer extends DefaultConsumer{

    private final Channel channel;

    public MyConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.out.println("接收到消息：" + new String(body));
        System.out.println("消息TagId：" + envelope.getDeliveryTag());

        // 消息应答， false 表示只对当前消息应答 true表示对当前消费者所有未签收的消息进行应答
        channel.basicAck(envelope.getDeliveryTag(), false);
    }

    @Override
    public void handleConsumeOk(String consumerTag) {
        super.handleConsumeOk(consumerTag);
        System.out.println("handleConsumeOk:" + consumerTag);
    }

    @Override
    public void handleCancelOk(String consumerTag) {
        super.handleCancelOk(consumerTag);
        System.out.println("handleCancelOk:" + consumerTag);
    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {
        super.handleCancel(consumerTag);
        System.out.println("handleCancel:" + consumerTag);
    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
        super.handleShutdownSignal(consumerTag, sig);
        System.out.println("handleShutdownSignal:" + consumerTag);
    }


}
