package cn.lxchinesszz.mojito.server;

import cn.lxchinesszz.mojito.net.utils.NamedThreadFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.jupiter.api.Test;

/**
 * @author liuxin
 * 2022/8/7 21:22
 */
public class ClientTest {

    @Test
    public void testClient() throws Exception {
        NioEventLoopGroup workGroup = new NioEventLoopGroup(
                Runtime.getRuntime().availableProcessors() + 1, new NamedThreadFactory("work"));
        Bootstrap clientBootstrap = new Bootstrap();
        clientBootstrap.group(workGroup);
        clientBootstrap.channel(NioSocketChannel.class);
        clientBootstrap.option(ChannelOption.TCP_NODELAY, false);
        clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                // 设置我们的管道
                ChannelPipeline pipeline = ch.pipeline();
                // 客户端要将我们发出的Java对象转换成二进制对象输入
                // 客户端要将服务端发送的二进制对象转换成Java对象
            }
        });
        ChannelFuture channelFuture = clientBootstrap.connect("127.0.0.1", 6666).sync();
        channelFuture.channel().write("HelloWord");
    }
}
