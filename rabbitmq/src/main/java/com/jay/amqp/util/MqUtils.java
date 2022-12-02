package com.jay.amqp.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/1
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class MqUtils {

    private static Connection connection;
    private static Channel channel;

    private static final String HOST_NAME = "192.168.25.129";
    private static final Integer PORT_NAME = 5672;
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "123456";
    public static final String VIRTUAL_HOST = "/";
    public static final String HELLO_QUEUE_NAME = "hello_queue";
    public static final String WORK_QUEUE_NAME = "work_queue";
    public static final String SINA_QUEUE_NAME = "sina";
    public static final String BAIDU_QUEUE_NAME = "baidu";
    public static final String HELLO_EXCHANGE_NAME = "hello_exchange";
    public static final String WEATHER_EXCHANGE = "weather";
    public static final String WEATHER_ROUTING_EXCHANGE = "weather_routing";
    public static final String WEATHER_TOPIC_EXCHANGE = "weather_topic";
    public static final String HELLO_TOPIC_NAME = "hello_topic";
    public static final String HELLO_ROUTE_A = "route_a";
    public static final String HELLO_ROUTE_B = "route_b";

    /**
     * 创建 mq tcp长连接
     * @return Connection
     * @throws IOException io
     * @throws TimeoutException 超时
     */
    public static Connection getConnect() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST_NAME);
        factory.setPort(PORT_NAME);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setVirtualHost(VIRTUAL_HOST);
        return factory.newConnection();
    }

    /**
     * 创建通信 “通道” 相当于TCP中的虚拟连接
     * @return Channel
     * @throws IOException IOException
     * @throws TimeoutException 超时
     */
    public static Channel getChannel() throws IOException, TimeoutException {
        connection = getConnect();
        channel = connection.createChannel();
        return channel;
    }

    /**
     * 释放资源
     * @throws IOException io
     * @throws TimeoutException 超时
     */
    public static void releaseResource() throws IOException, TimeoutException {
        if (channel != null){
            channel.close();
        }
        if (connection != null){
            connection.close();
        }
    }

}
