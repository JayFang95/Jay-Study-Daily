package com.jay.config;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/2
 * @description 消息监听，带channel
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Component
public class MyAckListener implements ChannelAwareMessageListener {

    private static final Map<String, Integer> MAP = new ConcurrentHashMap<>();

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.println(deliveryTag);
//        try {
            // 消息内容转换
            String msg = new String(message.getBody());
            System.out.println("接收到待处理消息：" + msg);
            // 处理业务逻辑 需要做幂等性验证
            // 根据处理结果确认是否签收消息
            assert channel != null;
//            channel.basicAck(deliveryTag, false);
//        } catch (IOException e) {
//            e.printStackTrace();
//            channel.basicNack(deliveryTag, false, true);
            // 第三个参数标识是否重新放回队列
//            if (MAP.containsKey(deliveryTag + "")){
//                Integer count = MAP.get(deliveryTag + "");
//                if (count <= 2){
//                    channel.basicNack(deliveryTag, false, true);
//                }else {
//                    MAP.put(deliveryTag+"", count+1);
//                    channel.basicNack(deliveryTag, false, false);
//                }
//            }else {
//                MAP.put(deliveryTag + "", 1);
//                channel.basicNack(deliveryTag, false, true);
//            }

//        }

    }

}
