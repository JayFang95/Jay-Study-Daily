package com.jay.demo;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/13
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class ProducerTrasactionDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.25.127:9092,192.168.25.128:9092,192.168.25.129:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 设置事务id 全局唯一
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my-transaction-id");

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        producer.initTransactions();
        producer.beginTransaction();
        // 异步发送
        try {
            for (int i = 0; i < 5; i++) {
                producer.send(new ProducerRecord<>("first", "hello-jay-" + i));
            }
            producer.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            producer.abortTransaction();
        } finally {
            producer.close();
        }



    }

}
