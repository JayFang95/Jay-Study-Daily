package com.jay.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2023/1/5
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
public class MyByteToMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//        System.out.println("解码器接收到消息："+in.);
//
//        int length, command;
//        while (true) {
//            in.markReaderIndex();
//            length = in.readInt();
//            command = in.readInt();
//            log.info("length:{},command:{}, channelId:{}",length,command,ctx.channel().id().asShortText());
//            if (length > BASE_LENGTH && command == 0xF) {
//                LOG.info("find command:{},channelId:{}",command,ctx.channel().id().asShortText());
//                break;
//            }
//            in.resetReaderIndex();
//            byte temp = in.readByte();
//            log.info("skip a byte:{},channelId:{}",temp,ctx.channel().id().asShortText());
//
//            if (in.readableBytes() <= BASE_LENGTH) {
//                log.info("length:{} less than 20,channelId:{}",in.readableBytes(),ctx.channel().id().asShortText());
//                return;
//            }
//        }
//        in.resetReaderIndex();
//        if (in.readableBytes() < length) {
//            log.info("can read:{} less than length:{},channelId:{}",in.readableBytes(),length,ctx.channel().id().asShortText());
//            return;
//        }
//        byte[] data = new byte[length];
//        in.readBytes(data);
//        log.info("success,length:{} data:{},channelId:{}",length,data,ctx.channel().id().asShortText());
//        out.add(data);

    }

}
