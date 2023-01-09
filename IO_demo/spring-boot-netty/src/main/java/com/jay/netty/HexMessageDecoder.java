package com.jay.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
public class HexMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        String HEXES = "0123456789ABCDEF";
        byte[] req = new byte[msg.readableBytes()];
        msg.readBytes(req);
        final StringBuilder hex = new StringBuilder(2 * req.length);

        for (byte b : req) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4))
                    .append(HEXES.charAt((b & 0x0F)));
        }

        log.info("装换前信息: {}", hex);
        String transStr = hexString2String(hex.toString());
//        for (ByteBuf buf : Delimiters.lineDelimiter()) {
//            transStr += buf.toString(Charset.defaultCharset());
//        }
//        transStr += "$$";
        log.info("装换后信息: {}", transStr);
        ByteBuf byteBuf = Unpooled.copiedBuffer(transStr.getBytes(Charset.forName("GBK")));
        out.add(byteBuf);

    }

    /**
     * 十六进制转正常字符串
     * @param hexStr 16进制字符串
     * @return 转后字符串
     */
    private String hexString2String(String hexStr) {
        byte[] baKeyword = new byte[hexStr.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(hexStr.substring(
                        i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            hexStr = new String(baKeyword, StandardCharsets.UTF_8);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return hexStr;
    }



}
