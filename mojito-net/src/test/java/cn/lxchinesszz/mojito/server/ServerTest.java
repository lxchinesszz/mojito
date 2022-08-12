package cn.lxchinesszz.mojito.server;

import cn.lxchinesszz.mojito.business.BusinessHandler;
import cn.lxchinesszz.mojito.channel.EnhanceChannel;
import cn.lxchinesszz.mojito.channel.context.ChannelContext;
import cn.lxchinesszz.mojito.client.Client;
import cn.lxchinesszz.mojito.client.ClientInitializer;
import cn.lxchinesszz.mojito.client.ClientPromiseHandler;
import cn.lxchinesszz.mojito.client.netty.NettyClient;
import cn.lxchinesszz.mojito.client.netty.NettyClientInitializer;
import cn.lxchinesszz.mojito.exception.RemotingException;
import cn.lxchinesszz.mojito.future.MojitoFuture;
import cn.lxchinesszz.mojito.future.Promise;
import cn.lxchinesszz.mojito.future.listener.MojitoListener;
import cn.lxchinesszz.mojito.protocol.Protocol;
import cn.lxchinesszz.mojito.protocol.ProtocolEnum;
import cn.lxchinesszz.mojito.protocol.ProtocolHeader;
import cn.lxchinesszz.mojito.protocol.mojito.MojitoProtocol;
import cn.lxchinesszz.mojito.protocol.mojito.model.RpcRequest;
import cn.lxchinesszz.mojito.protocol.mojito.model.RpcResponse;
import cn.lxchinesszz.mojito.server.netty.NettyServer;
import cn.lxchinesszz.mojito.server.netty.NettyServerInitializer;
import cn.lxchinesszz.mojito.utils.NamedThreadFactory;
import cn.lxchinesszz.mojito.utils.OSinfo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.protostuff.Rpc;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author liuxin
 * 2022/8/7 21:03
 */
public class ServerTest {


    @Test
    @DisplayName("构建一个服务端")
    public void testServer() throws Exception {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // io 线程一个进行轮训即可
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1, new NamedThreadFactory("boss"));
        // 业务处理线程组, CPU线程数 + 1 即可: (同一个核心同一时刻只能执行一个任务,所以创建多了也没用,建议给N+1个)
        NioEventLoopGroup workGroup = new NioEventLoopGroup(
                Runtime.getRuntime().availableProcessors() + 1, new NamedThreadFactory("work"));
        serverBootstrap.group(bossGroup, workGroup)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 设置我们的管道信息
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                Channel channel = ctx.channel();
                                SocketAddress socketAddress = channel.remoteAddress();
                                System.out.println("收到了一个链接:" + socketAddress);
                                ctx.fireChannelActive();
                            }

                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                Channel channel = ctx.channel();
                                SocketAddress socketAddress = channel.remoteAddress();
                                System.out.println(socketAddress + ":关闭连接");
                            }
                        });
                    }
                }).channel(OSinfo.isLinux() ? EpollServerSocketChannel.class : NioServerSocketChannel.class);
        ChannelFuture sync = serverBootstrap.bind(8080).sync();
        sync.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                System.out.println("端口绑定成功");
            } else {
                System.out.println("端口绑定失败:" + future.cause().getCause());
            }
        });
        Channel channel = sync.channel();
        // 添加一个关闭时间监听器
        channel.closeFuture().addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                System.out.println("服务关闭成功");
            } else {
                System.out.println("服务关闭失败:" + future.cause().getCause());
            }
        }).sync();
        channel.close();
    }

    @Test
    public void javaSelectorProvider() throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 获取 Selector 对象
        Selector selector = Selector.open();
        // 绑定端口 6666 在服务端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 注册我们监听的事件,监听连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            // 等待1秒，如果没有事件发生，返回
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待了1秒，客户端无连接");
                continue;
            }
            // 获取发生关注事件的channel的SelectionKey集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                // 获取 SelectionKey
                SelectionKey key = keyIterator.next();
                // 根据 SelectionKey 对应的 channel 发生的事件做相应的处理
                if (key.isAcceptable()) { // 如果是 OP_ACCEPT，代表有新的客户端连接Server
                    // 分配一个 SocketChannel 给客户端
                    java.nio.channels.SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("客户端连接成功，生成了一个socketChannel " + socketChannel.hashCode());
                    // 将 socketChannel 设置为 非阻塞
                    socketChannel.configureBlocking(false);
                    // 将这个 SocketChannel 注册到 selector，关注事件为 OP_READ，
                    // 同时关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if (key.isReadable()) {// 发生 事件为 OP_READ
                    // 通过 key 反向获取到 channel
                    java.nio.channels.SocketChannel socketChannel = (java.nio.channels.SocketChannel) key.channel();
                    // 获取到该channel关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    // 将 channel 中的数据读入到 Buffer 中
                    socketChannel.read(buffer);
                    System.out.println("客户端的数据：" + new String(buffer.array()));
                    socketChannel.close();
                }
                // 手动从集合中移除当前的 selectionKey，防止重复操作
                keyIterator.remove();
            }
        }

    }

    @Test
    public void createServer() {
        Protocol<RpcRequest, RpcResponse> protocol = new MojitoProtocol(new BusinessHandler<RpcRequest, RpcResponse>() {
            @Override
            public RpcResponse handler(ChannelContext channelContext, RpcRequest request) throws RemotingException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
                return new RpcResponse();
            }
        });
        NettyServer nettyServer = new NettyServer();
        nettyServer.registryProtocol(protocol);
        nettyServer.initializer(new NettyServerInitializer());
        nettyServer.start(6666);
    }

    @Test
    public void client() throws Exception {
        Client<RpcRequest, RpcResponse> client = new NettyClient<>();
        client.registryProtocol(new MojitoProtocol(null));
        client.initializer(new NettyClientInitializer());
        client.connect("127.0.0.1", 6666);
        RpcRequest rpcRequest = new RpcRequest();
        MojitoFuture<RpcResponse> send = client.sendAsync(rpcRequest);
        send.addListeners(new MojitoListener<RpcResponse>() {
            @Override
            public void onSuccess(RpcResponse result) {
                System.out.println("结果:" + result);
            }

            @Override
            public void onThrowable(Throwable throwable) {

            }
        });
        System.out.println(send.get());
    }

}
