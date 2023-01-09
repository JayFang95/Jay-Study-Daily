package com.jay.message;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2023/1/6
 * @description 自定义协议解码器
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
public class MyMessageDecoder extends ByteToMessageDecoder {

    int length = 0;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) throws Exception {
        log.info("MyMessageDecoder decode方法被调用");
        // 需要将得到的二进制字节码 -> MyMessageProtocol数据包（对象）
        if (in.readableBytes() > 4){
            // 先读取到消息的长度
            if (length == 0){
                length = in.readInt();
            }
            // 读取消息的内容信息，不足传递的长度时等待继续传输读取
            if (in.readableBytes() < length){
                log.info("当前可读数据不足 继续等待。。。");
                return;
            }
            byte[] content = new byte[length];
            if (in.readableBytes() >= length){
                in.readBytes(content);
                // 封装成MyMessageProtocol对象， 传递到下一个handler处理
                MyMessageProtocol messageProtocol = new MyMessageProtocol();
                messageProtocol.setLength(length);
                messageProtocol.setContent(content);
                list.add(messageProtocol);
            }
            length = 0;
        }
    }

}
