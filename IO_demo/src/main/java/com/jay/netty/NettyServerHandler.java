package com.jay.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;


/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/21
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 通道数据读取
     * @param ctx 上下文对象
     * @param msg 客户端发送的消息内容
     * @throws Exception 异常
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 将msg 转为一个ByteBuffer ，类型于NIO 中的ByteBuffer
        ByteBuf bf = (ByteBuf)msg;
        System.out.println("客户端发送来得消息：" + bf.toString(CharsetUtil.UTF_8));
    }

    /**
     * 数据读取完毕处理方法
     * @param ctx 上下文对象
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ByteBuf buf = Unpooled.copiedBuffer("hello client".getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(buf);
    }

    /**
     * 异常处理，一般需要关闭通道
     * @param ctx 上下文对象
     * @param cause 异常原因
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
