package com.jay.consumer.confirm;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

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
public class MyDeadLetterListener implements ChannelAwareMessageListener {


    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        // 自动消息应答时，如果出现异常会自定消息重试
        try {
            log.info("接收到死信投递的消息：{}", new String(message.getBody()));
            // todo 幂等性验证
            // todo 处理逻辑，成功后应答
            TimeUnit.SECONDS.sleep(1);
            channel.basicAck(deliveryTag, false);
        }catch (Exception exception){
            exception.printStackTrace();
            // 消息消费失败处理逻辑，是重新入队还是逻辑处理
            // todo 消息重新入队需要做幂等性验证

        }
    }

}
