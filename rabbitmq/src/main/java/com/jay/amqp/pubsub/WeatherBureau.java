package com.jay.amqp.pubsub;

import com.jay.amqp.util.MqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/2
 * @description 天气预报信息发送
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class WeatherBureau {

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = MqUtils.getChannel();
        channel.exchangeDeclare(MqUtils.WEATHER_EXCHANGE, BuiltinExchangeType.FANOUT);
        channel.basicPublish(MqUtils.WEATHER_EXCHANGE, "", null, "今天的天气很好呀".getBytes());
        System.out.println("发送成功");
        MqUtils.releaseResource();
    }

}
