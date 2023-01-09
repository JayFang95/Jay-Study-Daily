package com.jay.demo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LineBasedFrameDecoder;

import java.nio.charset.Charset;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2023/1/6
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class MyDelimiterFrameDecoder extends LineBasedFrameDecoder {


    public MyDelimiterFrameDecoder(int maxLength, boolean stripDelimiter, boolean failFast) {
        super(maxLength, stripDelimiter, failFast);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
        String msg = buffer.toString(Charset.forName("GBK"));
//        if (msg.startsWith("*DZKJ_DTU_") || msg.startsWith("*PING_")){
        if (msg.startsWith("2a445a4b4a5f4454555f") || msg.startsWith("2a50494e475f")){
//            msg += "$$";
            ByteBuf[] nulDelimiter = Delimiters.nulDelimiter();
            for (ByteBuf byteBuf : nulDelimiter) {
                buffer.writeBytes(byteBuf);
            }
        }
        return super.decode(ctx, buffer);
    }
}
