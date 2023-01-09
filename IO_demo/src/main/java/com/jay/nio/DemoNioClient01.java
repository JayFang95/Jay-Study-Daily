package com.jay.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/17
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class DemoNioClient01 {

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        Selector selector = Selector.open();

        // 客户端连接服务器，需要调用channel.finishConnect();才能实际完成连接。
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 9000));
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        while (true){
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                if(selectionKey.isConnectable()&& selectionKey.isValid()){
                    SocketChannel channel = (SocketChannel) selectionKey.channel();

                    // 如果正在连接，则完成连接
                    if (channel.isConnectionPending()) {
                        channel.finishConnect();
                    }

                    // channel.configureBlocking(false);
                    // 向服务器发送消息
                    channel.write(ByteBuffer.wrap("你好，连接成功".getBytes()));
                    // 连接成功后，注册接收服务器消息的事件
                    channel.register(selector, SelectionKey.OP_READ);
                    System.out.println("客户端连接成功");

                }
                if (selectionKey.isReadable()&& selectionKey.isValid()){
                    SocketChannel socket = (SocketChannel) selectionKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(128);
                    // 设置了非阻塞，此处不会阻塞
                    int len = socket.read(buffer);
                    // 读取到数据
                    if (len > 0){
                        System.out.println("接收到数据：" + new String(buffer.array()));
                        ByteBuffer buffer1 = ByteBuffer.allocate(128);
                        buffer1.put("client back".getBytes());
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

                iterator.remove();

            }
        }
    }

}
