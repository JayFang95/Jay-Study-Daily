package com.jay.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2023/1/4
 * @description 服务端检测空闲channel
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
public class ServerIdleStateHandler extends IdleStateHandler {

    /**
     * 设置空闲检测时间
     */
    private static final int READER_IDLE_TIME = 30;

    public ServerIdleStateHandler() {
        super(READER_IDLE_TIME, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        log.info("客户端:{} {} 秒内没有读取到数据,关闭连接", ctx.channel().remoteAddress(), READER_IDLE_TIME );
        ctx.channel().close();
    }
}
