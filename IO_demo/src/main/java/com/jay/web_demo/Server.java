package com.jay.web_demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Copyright(c),2018-2021,合肥市鼎足空间技术有限公司
 *
 * @author jing.fang
 * @date 2022/12/17
 * @description history
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
public class Server {

    //多路复用器
    private Selector selector;
    public Server init(int port) throws IOException {
        // 获取一个ServerSocket通道
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(port));
        // 获取多路复用器对象
        selector = Selector.open();
        // 将通道管理器与通道绑定，并为该通道注册SelectionKey.OP_ACCEPT事件，
        // 只有当该事件到达时，Selector.select()会返回，否则一直阻塞。
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        return this;
    }

    public void listen() throws IOException, InterruptedException {
        System.out.println("服务器端启动成功");
        // 使用轮询访问selector
        while (true) {
            selector.select();
            Iterator<SelectionKey> ite = selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = ite.next();
                ite.remove();
                // 客户端请求连接事件
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    // 获得客户端连接通道
                    SocketChannel channel = server.accept();
                    channel.configureBlocking(false);
                    // 向客户端发消息
                    channel.write(ByteBuffer.wrap(new String("send message to client").getBytes()));
                    // 在与客户端连接成功后，为客户端通道注册SelectionKey.OP_READ事件。
                    channel.register(selector, SelectionKey.OP_READ);
                    System.out.println("客户端请求连接事件");
                } else if (key.isReadable()) {// 有可读数据事件
                    // 获取客户端传输数据可读取消息通道。
                    SocketChannel channel = (SocketChannel) key.channel();
                    // 创建读取数据缓冲器
                    ByteBuffer buffer = ByteBuffer.allocate(10);
                    int read = channel.read(buffer);
                    byte[] data = buffer.array();
                    String message = new String(data);
                    System.out.println(message);
                    ByteBuffer outbuffer = ByteBuffer.wrap("hello".getBytes());
                    channel.write(outbuffer);
                    Thread.sleep(1000);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new Server().init(9981).listen();
    }

}
