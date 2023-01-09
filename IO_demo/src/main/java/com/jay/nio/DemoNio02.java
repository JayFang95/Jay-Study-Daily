package com.jay.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/17
 * @description nio模型测试——优化无限循环消耗问题
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class DemoNio02 {

    static List<SocketChannel> list = new ArrayList<SocketChannel>();

    public static void main(String[] args) throws IOException {
        // 创建nio socket channel
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        // 绑定端口
        serverSocket.socket().bind(new InetSocketAddress(9000));
        // 设置非阻塞
        serverSocket.configureBlocking(false);
        // 开启selector
        Selector selector = Selector.open();
        // 将serverSocket注册到selector上,并添加对应的注册事件
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        // 会一直空转循环
        while (true){
            // 阻塞等待注册监听事件
            selector.select();
            // 获取所有的注册监听事件实例
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()){
                SelectionKey selectionKey = keyIterator.next();
                // 注册事件
                if (selectionKey.isAcceptable() && selectionKey.isValid()){
                    ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                    // 获取注册的客户端对象
                    SocketChannel socket = server.accept();
                    socket.configureBlocking(false);
                    // 向客户端发消息
                    socket.write(ByteBuffer.wrap(new String("send message to client").getBytes()));
                    // 注册客户端数据读事件，如果需要给客户端发送数据可以注册写事件
                    socket.register(selector, SelectionKey.OP_READ);
                    System.out.println("客户端连接成功");

                }
                // 客户端数据读取事件
                if(selectionKey.isReadable()&& selectionKey.isValid()){
                    SocketChannel socket = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(128);
                    // 设置了非阻塞，此处不会阻塞
                    int len = socket.read(buffer);
                    // 读取到数据
                    if (len > 0){
                        System.out.println("接收到数据：" + new String(buffer.array()));
                        ByteBuffer buffer1 = ByteBuffer.allocate(128);
                        buffer1.put("server back".getBytes());
                        buffer1.flip();
                        while (buffer1.hasRemaining()){
                            socket.write(buffer1);
                        }
                        buffer1.compact();
                    }
                    // 连接断开,关闭socket
                    else if (len == -1){
                        socket.close();
                        System.out.println("客户端断开连接");
                    }
                }

                keyIterator.remove();

            }

        }
    }

}
