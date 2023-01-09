package com.jay.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.netty.util.internal.PlatformDependent;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentMap;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2023/1/4
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<Object> {

    /**
     * channel 集合信息
     */
    private static final ChannelGroup GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private static final ConcurrentMap<String, ChannelId> ONLINE_CHANNELS = PlatformDependent.newConcurrentHashMap(16);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("有客户端接入：{}", ctx.channel().remoteAddress());
        GROUP.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("有客户端断开：{}", ctx.channel().remoteAddress());
        GROUP.remove(ctx.channel());
        // 如果当前在线包含断开channel
        if (ONLINE_CHANNELS.containsValue(ctx.channel().id())){
            // 发送信息通知页面更新
            for (String channelKey : ONLINE_CHANNELS.keySet()) {
                if (ONLINE_CHANNELS.get(channelKey).compareTo(ctx.channel().id()) == 0){
                    ONLINE_CHANNELS.remove(channelKey);
                    // 发送信息
                    log.info("通知页面更新在线信： {}", channelKey);
                }
            }
        }
    }

    /**
     * 消息处理
     * @param ctx channel上下文
     * @param msg 消息对象
     * @throws Exception 异常
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf bf = (ByteBuf)msg;
//        String msgStr = bf.toString(Charset.forName("gbk"));
        String msgStr = (String) msg;
        String registerPrefix = "*DZKJ_DTU_";
        String pingPrefix = "*PING_";
        if (msgStr.startsWith(registerPrefix)){
            log.info("客户端: {} 连接注册信息：{}", ctx.channel().remoteAddress(), msgStr);
            ONLINE_CHANNELS.put(msgStr.substring(1), ctx.channel().id());
            // 更新设备在线状态
        }else if (msgStr.startsWith(pingPrefix)){
            log.info("客户端: {} 心跳信息：{}", ctx.channel().remoteAddress(), msgStr);
        }else {
            log.info("客户端: {} 测量信息：{}", ctx.channel().remoteAddress(), msgStr);
            handlerMessage(msgStr);
        }
    }

    /**
     * 监测信息处理
     * @param message 监测信息
     */
    private void handlerMessage(String message) {
        String[] msgArray = message.split("\\r\\n");
        for (String msg : msgArray) {
            log.info("接收到采集信息：{}", msg);
        }
//        for (Channel channel : GROUP) {
//            ByteBuf buf = Unpooled.copiedBuffer((channel.remoteAddress() +" 接收自动回复信息\r\n").getBytes(Charset.forName("GBK")));
//            channel.writeAndFlush(buf);
//            log.info("{} 发送自动回复信息成功", channel.remoteAddress());
//        }
    }

    /**
     *
     * @param ctx  channel上下文
     * @param cause 异常原因
     * @throws Exception 异常类型
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        // 移除channel
        ctx.channel().close();
        ctx.close();
        log.info("【" + ctx.channel().remoteAddress() + "】已关闭（服务器端）");
    }

    /**
     *
     * @param channelKey channel 标识key
     * @param msg 发送信息
     */
    public static void sendMsgToChannel(String channelKey, String msg){
        if (ONLINE_CHANNELS.containsKey(channelKey)){
            ChannelId channelId = ONLINE_CHANNELS.get(channelKey);
            Channel channel = GROUP.find(channelId);
            if (channel != null){
                ByteBuf buf = Unpooled.copiedBuffer((msg + "\r\n").getBytes(Charset.forName("GBK")));
                channel.writeAndFlush(buf);
                log.info("channelKey:{} 对应的channel接收消息:{}", channelKey, msg);
            }else {
                log.info("channelKey:{} 对应的channel不存在", channelKey);
            }
        }
    }
}
