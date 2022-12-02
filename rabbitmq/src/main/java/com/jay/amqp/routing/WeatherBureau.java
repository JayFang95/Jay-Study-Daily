package com.jay.amqp.routing;

import com.jay.amqp.util.MqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
        Map<String, String> map = new HashMap<>(8);
        map.put("BJ", "北京今天天气雾霾");
        map.put("SH", "上海今天天气晴朗");
        map.put("GZ", "广州今天天气多云");
        map.put("HZ", "杭州今天天气阴天");
        map.put("NJ", "南京今天天气小雨");
        map.put("HF", "合肥今天天气多云");
        Channel channel = MqUtils.getChannel();
        channel.exchangeDeclare(MqUtils.WEATHER_ROUTING_EXCHANGE, BuiltinExchangeType.DIRECT);
        for (String route : map.keySet()) {
            channel.basicPublish(MqUtils.WEATHER_ROUTING_EXCHANGE, route, null, map.get(route).getBytes());
        }

        System.out.println("发送成功");
        MqUtils.releaseResource();
    }

}
