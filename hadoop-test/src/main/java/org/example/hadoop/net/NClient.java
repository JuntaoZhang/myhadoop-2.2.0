package org.example.hadoop.net;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NClient {
    private final static InetSocketAddress SERVER_ADDRESS = new InetSocketAddress("127.0.0.1", 6666);
    /*缓冲区大小*/
    private static int BLOCK = 4096;
    /*接受数据缓冲区*/
    private static ByteBuffer sendBuffer = ByteBuffer.allocate(BLOCK);
    /*发送数据缓冲区*/
    private static ByteBuffer receiveBuffer = ByteBuffer.allocate(BLOCK);

    public static void main(String[] args) throws Exception {
        SocketChannel channel = SocketChannel.open();
        // 设置为非阻塞方式
        channel.configureBlocking(false);
        // 打开选择器
        Selector selector = Selector.open();
        // 注册连接服务端socket动作
        channel.register(selector, SelectionKey.OP_CONNECT);
        // 连接
        channel.connect(SERVER_ADDRESS);

        // 分配缓冲区大小内存
        Set<SelectionKey> selectionKeys;
        Iterator<SelectionKey> iterator;
        SelectionKey key;
        //选择一组键，其相应的通道已为 I/O 操作准备就绪。
        //此方法执行处于阻塞模式的选择操作。
        selector.select();
        //返回此选择器的已选择键集。
        selectionKeys = selector.selectedKeys();
        iterator = selectionKeys.iterator();
        while (iterator.hasNext()) {
            key = iterator.next();
            if (key.isConnectable()) {
                SocketChannel socketChannel;
                System.out.println("client connect");
                socketChannel = (SocketChannel) key.channel();
                // 判断此通道上是否正在进行连接操作。
                // 完成套接字通道的连接过程。
                if (socketChannel.isConnectionPending()) {
                    socketChannel.finishConnect();
                    System.out.println("完成连接!");
                    sendBuffer.clear();
                    sendBuffer.put("Hello Server".getBytes());
                    sendBuffer.flip();
                    socketChannel.write(sendBuffer);
                }
            }
        }
        selectionKeys.clear();
    }

}
