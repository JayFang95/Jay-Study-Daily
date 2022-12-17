package com.jay.consumer;

import com.jay.config.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/3
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Component
@Slf4j
public class SimplerConsumer {

    @RabbitListener(queues = MqConstant.COMMON_QUEUE)
    public void simpleListener(Message message){
        log.info("接收到队列： {} 的消息： {}", MqConstant.COMMON_QUEUE, new String(message.getBody()));
//        int i = 5 / 0;
    }

}
