package com.jay.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/17
 * @description nio模型测试
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class DemoNio01 {

    static List<SocketChannel> list = new ArrayList<SocketChannel>();

    public static void main(String[] args) throws IOException {
        // 创建nio socket channel
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        // 绑定端口
        serverSocket.socket().bind(new InetSocketAddress(9000));
        // 设置非阻塞
        serverSocket.configureBlocking(false);

        // 会一直空转循环
        while (true){
            // 设置了非阻塞，此处不会阻塞
            SocketChannel socket = serverSocket.accept();
            // 如果有客户端连接
            if (socket != null){
                // 设置客户端为非阻塞模式， 并加入列表
                socket.configureBlocking(false);
                list.add(socket);
            }
            // 遍历集合获取数据
            Iterator<SocketChannel> iterator = list.iterator();
            // 会遍历所有的socket,不管是否有数据接收
            while (iterator.hasNext()){
                SocketChannel channel = iterator.next();
                ByteBuffer buffer = ByteBuffer.allocate(128);
                // 设置了非阻塞，此处不会阻塞
                int len = channel.read(buffer);
                // 读取到数据
                if (len > 0){
                    System.out.println("接收到数据：" + new String(buffer.array()));
                }
                // 连接断开
                else if (len == -1){
                    iterator.remove();
                    System.out.println("客户端断开连接");
                }
            }
        }
    }

}
