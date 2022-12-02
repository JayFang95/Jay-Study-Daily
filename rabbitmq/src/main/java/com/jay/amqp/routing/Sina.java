package com.jay.amqp.routing;

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
 * @date 2022/12/2
 * @description 新浪接收天气
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class Sina {

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = MqUtils.getChannel();
        channel.queueDeclare(MqUtils.SINA_QUEUE_NAME, false, false, false, null);
        channel.queueBind(MqUtils.SINA_QUEUE_NAME, MqUtils.WEATHER_ROUTING_EXCHANGE, "HZ");
        channel.queueBind(MqUtils.SINA_QUEUE_NAME, MqUtils.WEATHER_ROUTING_EXCHANGE, "NJ");
        channel.queueBind(MqUtils.SINA_QUEUE_NAME, MqUtils.WEATHER_ROUTING_EXCHANGE, "HF");
        channel.basicConsume(MqUtils.SINA_QUEUE_NAME, false, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("sina 接收到天气信息：" + new String(body));
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
    }

}
