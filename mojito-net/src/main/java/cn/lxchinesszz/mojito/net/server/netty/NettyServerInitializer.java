package cn.lxchinesszz.mojito.net.server.netty;

import cn.lxchinesszz.mojito.net.protocol.Protocol;
import cn.lxchinesszz.mojito.net.exchange.DefaultExchangeChannelHandler;
import cn.lxchinesszz.mojito.net.server.ServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @author liuxin
 * 2022/8/6 14:10
 */
public class NettyServerInitializer implements ServerInitializer<NettyServer> {

    @Override
    public void initializer(NettyServer server) {
        ServerBootstrap serverBootstrap = server.getServerBootstrap();
        Protocol<?, ?> protocol = server.getProtocol();
        // @ChannelHandler.Sharable 修饰允许被多个管道共享,可以在这里做写统计的工作
        NettySharableExchangeInboundHandler sharableExchangeHandler = new NettySharableExchangeInboundHandler(new DefaultExchangeChannelHandler(true, protocol));
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline cp = socketChannel.pipeline();
                // 1. 添加心跳检查(5s没有反应就自动关闭连接)
//                cp.addLast("idleStateHandler", new IdleStateHandler(5, 5, 5, TimeUnit.SECONDS));
                // 2. 请求解码器
                cp.addLast(protocol.getRequestDecoder());
                // 3. 业务转发器（NettyAPI 转自定义API）
                cp.addLast(sharableExchangeHandler);
                // 4. 响应解码器
                cp.addLast(protocol.getResponseEncoder());
            }
        });
    }
}
