package com.jay.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2023/1/4
 * @description netty 通信服务端
 * 1。服务端注册 2.心跳保活 3.空闲断开  4.消息粘包拆包
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
@Component
public class NettyWebSocketServer {

    @Value("${netty.port}")
    private int nettyServerPort;

    /**
     * EventLoop接口
     * NioEventLoop中维护了一个线程和任务队列,支持异步提交任务,线程启动时会调用NioEventLoop的run方法,执行I/O任务和非I/O任务
     * I/O任务即selectionKey中的ready的事件,如accept,connect,read,write等,由processSelectedKeys方法触发
     * 非I/O任务添加到taskQueue中的任务,如register0,bind0等任务,由runAllTasks方法触发
     * 两种任务的执行时间比由变量ioRatio控制,默认为50,则表示允许非IO任务执行的事件与IO任务的执行时间相等
     */
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workGroup = new NioEventLoopGroup();

    /**
     * Channel
     * Channel类似Socket,它代表一个实体(如一个硬件设备,一个网络套接字) 的开放连接,如读写操作.通俗的讲,Channel字面意思就是通道,每一个客户端与服务端之间进行通信的一个双向通道.
     * Channel主要工作:
     * 1.当前网络连接的通道的状态(例如是否打开?是否已连接?)
     * 2.网络连接的配置参数(例如接收缓冲区的大小)
     * 3.提供异步的网络I/O操作(如建立连接,读写,绑定端口),异步调用意味着任何I/O调用都将立即返回,并且不保证在调用结束时锁清秋的I/O操作已完成.
     * 调用立即放回一个ChannelFuture实例,通过注册监听器到ChannelFuture上,可以I/O操作成功,失败或取消时回调通知调用方.
     * 4.支持关联I/O操作与对应的处理程序.
     * 不同协议,不同的阻塞类型的连接都有不同的Channel类型与之对应,下面是一些常用的Channel类型
     * NioSocketChannel,异步的客户端 TCP Socket连接
     * NioServerSocketChannel,异步的服务端 TCP Socket 连接
     * NioDatagramChannel,异步的UDP连接
     * NioSctpChannel,异步的客户端Sctp连接
     * NioSctoServerChannel,异步的Sctp服务端连接
     * 这些通道涵盖了UDP 和TCP网络IO以及文件IO
     */
    private Channel channel;

    /**
     * 启动 netty server
     */
    @PostConstruct
    public void start() {
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 设置线程组
            bootstrap.group(bossGroup, workGroup)
                    // 设置channel类型
                    .channel(NioServerSocketChannel.class)
                    // 设置服务端可连接队列数,对应TCP/IP协议listen函数中backlog参数
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 十六进制转换
                            pipeline.addLast(new HexMessageDecoder());
                            // 设置粘包拆包规则
//                            pipeline.addLast(new MyByteToMessageDecoder());
                            pipeline.addLast(new LineBasedFrameDecoder(4 * 1024 * 1024));
//                            ByteBuf delimiter = Unpooled.copiedBuffer("$$".getBytes());
//                            pipeline.addLast(new DelimiterBasedFrameDecoder(4 * 1024 * 1024, true, true, delimiter));
//                            ByteBuf delimiter2 = Unpooled.copiedBuffer("\r\n".getBytes());
//                            pipeline.addLast(new DelimiterBasedFrameDecoder(4 * 1024 * 1024, true, true, delimiter2));
                            // 设置 空闲检测
                            pipeline.addLast(new ServerIdleStateHandler());
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            // 绑定端口，开启连接通道
            channel = bootstrap.bind(nettyServerPort).sync().channel();
            log.info("=================Netty服务启动==================");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("启动连接异常: {}, 尝试重新链接", e.getMessage());
            start();
        }
    }

    @PreDestroy
    public void destroy(){
        log.info("=================Netty服务关闭==================");
        if (channel != null) {
            channel.close();
        }
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }


}
