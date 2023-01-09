package com.jay.controller;

import com.jay.netty.ChatClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2023/1/4
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@RestController
@RequestMapping("client")
@Slf4j
public class ClientChannelController {

    @Value("${netty.port}")
    private int serverPort;
    @Value("${netty.host}")
    private String serverHost;

    @RequestMapping("add/{channelKey}")
    public void addClient(@PathVariable String channelKey){
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(workGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 设置粘包拆包规则
//                            pipeline.addLast(new LineBasedFrameDecoder(1024));
//                            pipeline.addLast(new LengthFieldBasedFrameDecoder(10 * 1024 * 1024, 0, 4, 0, 4));
//                            pipeline.addLast(new LengthFieldPrepender(4));
                            pipeline.addLast(new ChatClientHandler());
                        }
                    });
            // netty 服务器监听端口
            ChannelFuture cf = bootstrap.connect(serverHost, serverPort).sync();
            cf.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("连接Netty服务端成功");
                } else {
                    log.info("连接失败，进行断线重连");
                    future.channel().eventLoop().schedule(() -> addClient(channelKey), 20, TimeUnit.SECONDS);
                }
            });
            Channel channel = cf.channel();
            String registerMsg = "register:" + channelKey + "\r\n";
            ByteBuf byteBuf = Unpooled.copiedBuffer(registerMsg.getBytes(Charset.forName("GBK")));
            channel.writeAndFlush(byteBuf);
            for (int i = 0; i < 10; i++) {
                String msg = "来自"+ channelKey +"第" + i + "条消息\r\n";
                ByteBuf buf = Unpooled.copiedBuffer(msg.getBytes(Charset.forName("GBK")));
                channel.writeAndFlush(buf);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
