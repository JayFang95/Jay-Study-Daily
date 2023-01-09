package com.jay.message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2023/1/6
 * @description 自定义协议编码器
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
public class MyMessageEncoder extends MessageToByteEncoder<MyMessageProtocol> {

    @Override
    protected void encode(ChannelHandlerContext ctx, MyMessageProtocol myMessageProtocol, ByteBuf out) throws Exception {
        log.info("MyMessageEncoder encode方法被调用");
        out.writeInt(myMessageProtocol.getLength());
        out.writeBytes(myMessageProtocol.getContent());
    }

}
