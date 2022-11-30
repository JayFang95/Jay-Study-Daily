package com.jay.service;

import com.alibaba.fastjson.JSON;
import com.jay.bean.CustomConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/11/30
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
public class DemoService {

    private static final String CONNECT_STR = "192.168.25.129:2181";
    private static final Integer SESSION_TIMEOUT = 30 * 1000;

    private static final CountDownLatch COUNT_DOWN_LATCH = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeper zk = null;
        Watcher watcher = new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getType().equals(Event.EventType.None) && Event.KeeperState.SyncConnected.equals(event.getState())) {
                    log.info("建立链接完成");
                    COUNT_DOWN_LATCH.countDown();
                }
            }
        };
        zk = new ZooKeeper(CONNECT_STR, SESSION_TIMEOUT, watcher);
        log.info("正在建立连接");
        COUNT_DOWN_LATCH.await();
        CustomConfig config = new CustomConfig();
        config.setId("1");
        config.setName("test_config");
        zk.create("/config", JSON.toJSONBytes(config), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        ZooKeeper finalZk = zk;
        Watcher dataWatch = new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getType().equals(Event.EventType.NodeDataChanged)) {
                    byte[] data = new byte[0];
                    try {
                        // 重复监听
                        data = finalZk.getData("/config", this, null);
                    } catch (KeeperException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info("数据改变：{}" , JSON.parseObject(new String(data), CustomConfig.class));
                }
            }
        };
        Stat stat = new Stat();
        // 设置节点数据变化监听， 只会触发一次
        byte[] getData = zk.getData("/config", dataWatch, stat);
        log.info("元数据信息： {}", JSON.parseObject(new String(getData), CustomConfig.class));
        CustomConfig newConfig = new CustomConfig();
        newConfig.setId("2");
        newConfig.setName("test_config_new");
        // 乐观锁机制
//        zk.setData("/config", JSON.toJSONBytes(newConfig), stat.getVersion());
        // 忽略版本
        zk.setData("/config", JSON.toJSONBytes(newConfig), -1);
        TimeUnit.SECONDS.sleep(60);
    }

}
