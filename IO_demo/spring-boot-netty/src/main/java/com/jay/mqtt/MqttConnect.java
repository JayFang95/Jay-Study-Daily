package com.jay.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/12
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
@Component
public class MqttConnect {

    /**
     * mqtt服务器的地址和端口号
     */
    private final String host = "tcp://192.168.25.130:61613";
    private final String clientId = "DC" + (int) (Math.random() * 100000000);
    private MqttClient mqttClient;

//    @PostConstruct
    public void init() throws MqttException {
        MqttConnect mqttConnect = new MqttConnect();
        mqttConnect.setMqttClient("admin", "password", new MyMqttCallback());
        mqttConnect.sub("dz_pub");
        mqttConnect.sub("dz_out");
        for (int i = 0; i < 10; i++) {
            mqttConnect.pub("test", "哈哈哈，N好hello");
            try {
                mqttConnect.pub("dz_sub", new String("哈哈哈，N好hello".getBytes("GBK")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 客户端connect连接mqtt服务器
     *
     * @param userName     用户名
     * @param passWord     密码
     * @param mqttCallback 回调函数
     **/
    public void setMqttClient(String userName, String passWord, MqttCallback mqttCallback) throws MqttException {
        MqttConnectOptions options = mqttConnectOptions(userName, passWord);
        if (mqttCallback == null) {
            mqttClient.setCallback(new MyMqttCallback());
        } else {
            mqttClient.setCallback(mqttCallback);
        }
        mqttClient.connect(options);
    }

    /**
     * MQTT连接参数设置
     */
    private MqttConnectOptions mqttConnectOptions(String userName, String passWord) throws MqttException {
        mqttClient = new MqttClient(host, clientId, new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        //默认：30
        options.setConnectionTimeout(15);
        //默认：false
        options.setAutomaticReconnect(true);
        //默认：true 867296052610603
        options.setCleanSession(false);
        //默认：60
        options.setKeepAliveInterval(20);
        return options;
    }

    /**
     * 关闭MQTT连接
     */
    public void close() throws MqttException {
        mqttClient.disconnect();
        mqttClient.close();
    }

    /**
     * 向某个主题发布消息 默认qos：1
     *
     * @param topic:发布的主题
     * @param msg：发布的消息
     */
    public void pub(String topic, String msg) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage();
//         mqttMessage.setQos(2);
        mqttMessage.setPayload(msg.getBytes());
        MqttTopic mqttTopic = mqttClient.getTopic(topic);
        MqttDeliveryToken token = mqttTopic.publish(mqttMessage);
        token.waitForCompletion();
    }

    /**
     * 向某个主题发布消息
     *
     * @param topic: 发布的主题
     * @param msg:   发布的消息
     * @param qos:   消息质量    Qos：0、1、2
     */
    public void pub(String topic, String msg, int qos) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(qos);
        mqttMessage.setPayload(msg.getBytes());
        MqttTopic mqttTopic = mqttClient.getTopic(topic);
        MqttDeliveryToken token = mqttTopic.publish(mqttMessage);
        token.waitForCompletion();
    }

    /**
     * 订阅某一个主题 ，此方法默认的的Qos等级为：1
     *
     * @param topic 主题
     */
    public void sub(String topic) throws MqttException {
        mqttClient.subscribe(topic);
    }

    /**
     * 订阅某一个主题，可携带Qos
     *
     * @param topic 所要订阅的主题
     * @param qos   消息质量：0、1、2
     */
    public void sub(String topic, int qos) throws MqttException {
        mqttClient.subscribe(topic, qos);
    }

    /**
     * main函数自己测试用
     */
    public static void main(String[] args) throws MqttException {
        MqttConnect mqttConnect = new MqttConnect();
        mqttConnect.setMqttClient("admin", "public", new MyMqttCallback());
        mqttConnect.sub("com/iot/init");
        mqttConnect.pub("dz_sub", "AT+GSN");
    }

}
