package com.jay.consumer.confirm;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/3
 * @description 自定义消息监听器，通过channel进行消息应答确认
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Component
@Slf4j
public class MyAckListener implements ChannelAwareMessageListener {

    private static final Map<Long, Integer> map = new ConcurrentHashMap<>(16);

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        // 自动消息应答时，如果出现异常会自定消息重试
        try {
            log.info("接收到投递的消息：{}", new String(message.getBody()));
            // todo 幂等性验证
            // todo 处理逻辑，成功后应答
            TimeUnit.SECONDS.sleep(1);
//            int i = 10 /0 ;
            channel.basicAck(deliveryTag, false);
        }catch (Exception exception){
            // 消息消费失败处理逻辑，是重新入队还是逻辑处理
            // todo 消息重新入队需要做幂等性验证
            if (map.containsKey(deliveryTag)){
                Integer count = map.get(deliveryTag);
                if (count < 3){
                    map.put(deliveryTag, count + 1);
                    channel.basicReject(deliveryTag, true);
                    log.error("deliveryTag:{} 消费异常, 发起第{}重试", deliveryTag, count + 1);
                }else {
                    map.remove(deliveryTag);
                    channel.basicReject(deliveryTag, false);
                    log.error("deliveryTag:{} 消费异常，且重试已达到最大次数，消息不再入队，请手动处理", deliveryTag);
                }
            }else {
                map.put(deliveryTag, 1);
                channel.basicReject(deliveryTag, true);
                log.error("deliveryTag:{} 消费异常, 发起第1重试", deliveryTag);
            }
        }
    }

}
