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
public class ProducerDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.25.127:9092,192.168.25.128:9092,192.168.25.129:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // 设置缓冲区大小 默认32m
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        // 批次大小 默认16k
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        // linger.ms 默认0ms
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 5);
        // 压缩
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        // ack 默认 all (0 ,1 ,all)
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        // 发送失败重试 默认 Integer.max()
        properties.put(ProducerConfig.RETRIES_CONFIG, 10);

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);

        // 异步发送
        for (int i = 0; i < 5; i++) {
            producer.send(new ProducerRecord<>("first", "hello-jay-" + i));
        }
        // 异步发送回调
        for (int i = 0; i < 5; i++) {
            producer.send(new ProducerRecord<>("first", "hello-jay-" + i), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    System.out.println("异步回结果 主题：" + recordMetadata.topic() + " 分区：" + recordMetadata.partition());
                }
            });
        }
        //同步发送
        for (int i = 0; i < 5; i++) {
            producer.send(new ProducerRecord<>("first", "hello-jay-" + i)).get();
        }
        //异步指定分区回调
        for (int i = 0; i < 5; i++) {
            producer.send(new ProducerRecord<String, String>("first", i%3, "","hello-jay-" + i), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    System.out.println("异步回结果 主题：" + recordMetadata.topic() + " 分区：" + recordMetadata.partition());
                }
            });
        }

        for (int i = 0; i < 5; i++) {
            producer.send(new ProducerRecord<String, String>("first", "a","hello-jay-" + i), new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    System.out.println("异步回结果 主题：" + recordMetadata.topic() + " 分区：" + recordMetadata.partition());
                }
            });
        }

        producer.close();
    }

}
