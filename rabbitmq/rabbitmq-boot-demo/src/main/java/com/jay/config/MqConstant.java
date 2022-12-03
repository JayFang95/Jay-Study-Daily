package com.jay.config;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/3
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public interface MqConstant {

    public static final String COMMON_QUEUE = "common_queue";
    public static final String COMMON_ACK_QUEUE = "common_ack_queue";
    public static final String TTL_QUEUE = "ttl_queue";
    public static final String DEAD_LETTER_QUEUE = "dead_letter_queue";

    public static final String DIRECT_EXCHANGE = "direct_exchange";
    public static final String DIRECT_ACK_EXCHANGE = "direct_ack_exchange";
    public static final String TOPIC_EXCHANGE = "topic_exchange";
    public static final String TTL_TOPIC_EXCHANGE = "ttl_topic_exchange";
    public static final String DEAD_LETTER_EXCHANGE = "dead_letter_exchange";

    public static final String DIRECT_ROUTING_KEY = "direct_routing_key";
    public static final String DIRECT_ACK_ROUTING_KEY = "direct_ack_routing_key";
    public static final String TOPIC_ROUTING_KEY = "topic.#";
    public static final String TTL_ROUTING_KEY = "ttl.#";
    public static final String DEAD_ROUTING_KEY = "dead.#";

    public static final Integer TTL_UNIT_TIME_MS = 30 * 1000;
}
