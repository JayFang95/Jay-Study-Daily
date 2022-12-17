package com.jay.demo;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/16
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class ConsumerDemo {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.25.127:9092,192.168.25.128:9092,192.168.25.129:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        // 设置消费组id
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"TEST_GROUP");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        List<String> topics = new ArrayList<>();
        topics.add("first");
        consumer.subscribe(topics);

        while (true){
            // 每隔1秒拉取一次消息
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            System.out.println(records.count());
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record);
            }
        }
    }

}
