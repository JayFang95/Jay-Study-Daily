package com.jay.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/9
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
@Slf4j
@RestController
@RequestMapping("mt/common")
public class SocketController {

    /**
     * 添加socket client
     * @param code code
     */
    @RequestMapping("add/{code}")
    public void addSocketClient(@PathVariable String code){
        new Thread(() -> {
            Socket socket = null;
            OutputStream os = null;
            String name = Thread.currentThread().getName();
            try {
                socket = new Socket("127.0.0.1", 9000);
                os = socket.getOutputStream();
                int count = 0;
                String msg = "";
                do {
                    if (count == 0) {
                        msg = "register:test" + code;
                    } else {
//                        if (count % 30 == 0) {
//                            msg = "ping:test" + code;
//                        } else {
                        msg = "data:test" + code +  "_" + count;
//                        }
                    }
                    log.info("线程：{} 发送信息：{}, 循环次数： {}", Thread.currentThread().getName(), msg, count);
                    os.write(msg.getBytes(StandardCharsets.UTF_8));
                    os.flush();
                    count++;
                    TimeUnit.SECONDS.sleep(5);
                } while (count != 40);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            if (os != null){
                try {
                    os.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (socket != null){
                try {
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            log.info("运行结束" + Thread.currentThread().getName());
        }).start();
    }

}
