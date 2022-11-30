package com.jay.service;

import com.alibaba.fastjson.JSON;
import com.jay.bean.CustomConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.checkerframework.checker.units.qual.C;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/11/30
 * @description curator框架
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
public class CuratorDemoService {

    /**
     * 最大重试次数
     * 重试间隔时间
     */
    private static final int RETRY_TIMES = 5;
    private static final int ELAPSED_TIME_MS = 5000;
    private static final String CONNECT_STR = "192.168.25.129:2181";
    private static final String CONNECT_CLUSTER_STR = "192.168.25.129:2181,192.168.25.129:2182,192.168.25.129:2183,192.168.25.129:2184";
    private static final int SESSION_TIME_OUT_MS = 60 * 1000;
    private static final int CONNECT_TIME_OUT_MS = 5000;

    private static CuratorFramework CURATOR;
    private static final CountDownLatch count = new CountDownLatch(1);

    private static void createCurator() {
        RetryPolicy retry = new ExponentialBackoffRetry(5000, 5);
        CURATOR = CuratorFrameworkFactory.newClient(
                CONNECT_STR,
                SESSION_TIME_OUT_MS,
                CONNECT_TIME_OUT_MS,
                new RetryNTimes(RETRY_TIMES, ELAPSED_TIME_MS)
        );
        CURATOR.start();
    }

    private static void createClusterCurator() {
        RetryPolicy retry = new ExponentialBackoffRetry(5000, 5);
        CURATOR = CuratorFrameworkFactory.newClient(
                CONNECT_CLUSTER_STR,
                SESSION_TIME_OUT_MS,
                CONNECT_TIME_OUT_MS,
                new RetryNTimes(RETRY_TIMES, ELAPSED_TIME_MS)
        );
        CURATOR.start();
    }

    public static void main(String[] args) {

    }

    /**
     * 在容器节点中创建临时顺序节点
     * 各节点监听自己的前一个节点
     */
    private void testDistributeLock() throws Exception {
        createCurator();
//        new InterProcessMultiLock(CURATOR, Arrays.asList("/path1", "/path2")); // 多重共享锁 （将多个锁作为单个实体管理的容器）
//        new InterProcessSemaphoreMutex(CURATOR, "/LOCK"); // 独占，不可重入
//        new InterProcessReadWriteLock(CURATOR, "/LOCK");
        /**
         * InterProcessMutex：可重入、独占锁
         * InterProcessSemaphoreMutex：不可重入、独占锁
         * –
         * InterProcessReadWriteLock：读写锁
         * InterProcessSemaphoreV2 ： 共享信号量
         * InterProcessMultiLock：多重共享锁 （将多个锁作为单个实体管理的容器）
         */
        InterProcessMutex processMutex = new InterProcessMutex(CURATOR, "/LOCK");
        if (processMutex.acquire(5,TimeUnit.SECONDS)){
            try {
                // 业务处理
                log.info("获取到锁，开始业务操作");
            }finally {
                try {
                    processMutex.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static void clusterTest() {
        createClusterCurator();
        while (true){
            try {
                byte[] data = CURATOR.getData().forPath("/zookeeper");
                log.info("数据信息： {}", new String(data));
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("链接异常： {}", e.getMessage());
            }
        }
    }

    private static void testStandalone() {
        createCurator();
        try {
//            String path = CURATOR.create().forPath("/demo1");
//            String path2 = CURATOR.create().forPath("/demo2", "data".getBytes());
//            String path3 = CURATOR.create().creatingParentsIfNeeded().forPath("/parent/child", "p_c".getBytes());
            /*
              protection 保护模式，防止由于异常创建僵尸节点：
                服务端生成了节点，由于网络原因客户端没有得到响应，导致重复创建节点，调用的幂等性不能保证了
                protection模式会生成随机字符，再次创建时回去校验是否时重复创建，避免多次创建
             */
//            String forPath = CURATOR.create().withProtection().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/protection");

            byte[] bytes = CURATOR.getData().forPath("/demo2");
            byte[] bytes2 = CURATOR.getData().usingWatcher((Watcher) (curatorWatcher) -> {}).forPath("/parent/child");
            // 使用线程池。后台执行，不在使用默认的event-thread线程执行，避免线程阻塞
            CURATOR.getData().inBackground((client, event) -> {
                byte[] data = event.getData();
                log.info("数据信息： {}", new String(data));
            }, Executors.newSingleThreadExecutor()).forPath("/demo2");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
