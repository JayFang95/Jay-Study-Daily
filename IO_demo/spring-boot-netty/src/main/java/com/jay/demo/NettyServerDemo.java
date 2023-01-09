package com.jay.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2023/1/6
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
public class NettyServerDemo {

    private static EventLoopGroup bossGroup = new NioEventLoopGroup();
    private static EventLoopGroup workGroup = new NioEventLoopGroup();

    public static void main(String[] args) {
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
                            // 设置粘包拆包规则
//                            pipeline.addLast(new LineBasedFrameDecoder(4 * 1024));
                            // 十六进制转换
//                            pipeline.addLast(new HexMessageDecoder());
//                            ByteBuf delimiter2 = Unpooled.copiedBuffer("\r\n".getBytes());
                            pipeline.addLast(new MyDelimiterFrameDecoder(4 * 1024, true, true));
                            pipeline.addLast(new DemoServerHandler());
                        }
                    });
            Channel channel = bootstrap.bind(8008).sync().channel();
            log.info("=================Netty服务启动==================");
            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("启动连接异常: {}, 尝试重新链接", e.getMessage());
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

}
