package com.jay.amqp.topic;

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
        map.put("ZJ.HZ.2022-12-02", "浙江 杭州 2022-12-02天气预报");
        map.put("ZJ.NB.2022-12-02", "浙江 宁波 2022-12-02天气预报");
        map.put("ZJ.JX.2022-12-02", "浙江 嘉兴 2022-12-02天气预报");
        map.put("JS.NJ.2022-12-01", "江苏 南京 2022-12-01天气预报");
        map.put("JS.SZ.2022-12-01", "江苏 苏州 2022-12-01天气预报");
        map.put("JS.NT.2022-12-01", "江苏 南通 2022-12-01天气预报");
        map.put("JS.ZJ.2022-12-01", "江苏 镇江 2022-12-01天气预报");
        Channel channel = MqUtils.getChannel();
        channel.exchangeDeclare(MqUtils.WEATHER_TOPIC_EXCHANGE, BuiltinExchangeType.TOPIC);
        for (String route : map.keySet()) {
            channel.basicPublish(MqUtils.WEATHER_TOPIC_EXCHANGE, route, null, map.get(route).getBytes());
        }

        System.out.println("发送成功");
        MqUtils.releaseResource();
    }

}
