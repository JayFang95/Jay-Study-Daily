package com.jay.controller;

import com.jay.netty.NettyServerHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("channel")
public class ChannelMessageController {

    @RequestMapping("send/{channelKey}")
    public void sendMsgToChannel(@PathVariable String channelKey, @RequestParam String msgBody){
        NettyServerHandler.sendMsgToChannel(channelKey, msgBody);
    }

}
