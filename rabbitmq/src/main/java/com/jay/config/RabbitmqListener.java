package com.jay.config;

import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/2
 * @description 简单消息监听
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Component
public class RabbitmqListener {

//    @RabbitListener(queues = "queue_boot")
    public void listener(Message message){
        System.out.println("接收到消息：" + new String(message.getBody()));
    }

}
