package com.jay.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/21
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class NettyServerDemo {

    public static void main(String[] args) {
        // 主管连接线程
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 主管业务的线程：不指定线程时，线程数默认为cpu核数的两倍
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            ServerBootstrap serverBootstrap = bootstrap.group(bossGroup, workGroup)
                    // 设置通信通道的实现类型
                    .channel(NioServerSocketChannel.class)
                    // 配置选项：初始化服务器初始连接大小，服务器处理客户端是按照顺序来得，所以同一时间只能处理一个客户端请求，
                    // 多个客户端同时连接时，服务端会将不能处理的客户端请求放入队列中等待处理
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 创建通道初始化对象，设置初始化参数
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 对workGroup 的 socketChannel设置处理器
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    });
            System.out.println("server start ...");
            // netty 服务器监听端口
            ChannelFuture cf = serverBootstrap.bind(9999).sync();
            // 等待服务端关闭，closeFuture异步操作，内部调用了object.await()方法
            cf.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

}
