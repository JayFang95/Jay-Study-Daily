package com.jay.web_demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
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
public class Client {

    //多路复用器、选择器（具体看使用的操作系统以及jdk版本，1.5有可能就是select而1.7就是epoll）
    private Selector selector;
    public Client init(String serverIp, int port) throws IOException {
        // 获取socket通道、
        // 在reactor模式中的资源
        // select、epoll函数中的fd（个人理解如有错误请求指正）
        SocketChannel channel = SocketChannel.open();
        // 将该通道设置为非阻塞
        channel.configureBlocking(false);
        // 获取多路复用器实例
        selector = Selector.open();
        // 客户端连接服务器，需要调用channel.finishConnect();才能实际完成连接。
        channel.connect(new InetSocketAddress(serverIp, port));
        // 为该通道注册SelectionKey.OP_CONNECT事件，也就是将channel的fd和感兴趣的事件添加到多路复用器中
        channel.register(selector, SelectionKey.OP_CONNECT);
        return this;
    }

    public void listen() throws IOException, InterruptedException {
        System.out.println("客户端启动");
        // 轮询访问selector
        while (true) {
            // 选择注册过的io操作的事件(第一次为SelectionKey.OP_CONNECT)
            // 看过之前文章的话，就明白这是获取注册到该复用器中的通道的相关事件，获取的过程也就是select()方法遍历获取事件不同的系统、jdk也不同，如果是epoll则是从一个fd就绪队列获取
            // 而select和poll则是遍历存放channel和事件的集合所以效率低下一些,还有其他的效率问题可以看看之前epoll和select的讲解
            selector.select();
            //获取注册在该复用器上的channel和channelEvent
            Iterator<SelectionKey> ite = selector.selectedKeys().iterator();
            while (ite.hasNext()) {
                SelectionKey key = ite.next();
                // 删除已选的key，防止重复处理
                ite.remove();
                if (key.isConnectable()) {
                    SocketChannel channel = (SocketChannel) key.channel();

                    // 如果正在连接，则完成连接
                    if (channel.isConnectionPending()) {
                        channel.finishConnect();
                    }

                    // channel.configureBlocking(false);
                    // 向服务器发送消息
                    channel.write(ByteBuffer.wrap(new String("send message to server.").getBytes()));
                    // 连接成功后，注册接收服务器消息的事件
                    channel.register(selector, SelectionKey.OP_READ);
                    System.out.println("客户端连接成功");
                } else if (key.isReadable()) { // 判断该channel的channelEvent事件类型，也就是reactor模式中的分发器，如果把里面处理过程进行封装就是处理器了
                    // 因为selectionKey中存放的是channel和channelEvent所以可以通过selectionKey就能获取channel
                    SocketChannel channel = (SocketChannel) key.channel();

                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    channel.read(buffer);
                    byte[] data = buffer.array();
                    String message = new String(data);

                    System.out.println("recevie message from server:, size:"
                            + buffer.position() + " msg: " + message);
                    ByteBuffer outbuffer = ByteBuffer.wrap(("client.".concat(message)).getBytes());
                    channel.write(outbuffer);
                    Thread.sleep(1000);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new Client().init("127.0.0.1", 9981).listen();

    }

}
