package com.jay.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/5
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
public class SimpleProducer {

    /*
     * Apache RocketMQ 服务端5.x版本中，消息本身不可编辑，消费端获取的消息都是只读消息视图。 但在历史版本3.x和4.x版本中消息不可变性没有强约束，因此如果您需要在使用过程中对消息进行中转操作，务必将消息重新初始化。
     * 正确使用示例如下：
     *
     * Message m = Consumer.receive();
     * Message m2= MessageBuilder.buildFrom(m);
     * Producer.send(m2);
     *
     * 错误使用示例如下：
     *
     * Message m = Consumer.receive();
     * m.update()；
     * Producer.send(m);
     */
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setNamesrvAddr("192.168.25.129:9876");
        producer.setProducerGroup("simple-producer");
        producer.start();

        Message message = new Message();
        message.setTopic("hello-simple");
        message.setBody("hello".getBytes());
        producer.send(message);

        log.info("消息发生成功，关闭生产者");
        producer.shutdown();
    }

}
