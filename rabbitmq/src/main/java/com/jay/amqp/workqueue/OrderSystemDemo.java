package com.jay.amqp.workqueue;

import com.jay.amqp.util.MqUtils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/2
 * @description 模拟售票
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class OrderSystemDemo {

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = MqUtils.getChannel();
        channel.queueDeclare(MqUtils.WORK_QUEUE_NAME, false, false, false, null);
        for (int i = 0; i < 50; i++) {
            String msg = "你好，1360000000" + i + ",您已成功购票，取票序号：" + i;
            channel.basicPublish("", MqUtils.WORK_QUEUE_NAME, null, msg.getBytes());
        }
        System.out.println("售票完成");
        MqUtils.releaseResource();
    }

}
