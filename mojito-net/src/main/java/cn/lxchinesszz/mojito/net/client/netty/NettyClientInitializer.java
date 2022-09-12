package cn.lxchinesszz.mojito.net.client.netty;

import cn.lxchinesszz.mojito.net.client.ClientInitializer;
import cn.lxchinesszz.mojito.net.exchange.DefaultExchangeChannelHandler;
import cn.lxchinesszz.mojito.net.protocol.Protocol;
import cn.lxchinesszz.mojito.net.protocol.ProtocolHeader;
import cn.lxchinesszz.mojito.net.server.netty.NettySharableExchangeInboundHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


/**
 * @author liuxin
 * 个人博客：https://java.springlearn.cn
 * 公众号：西魏陶渊明  ｛关注获取学习源码｝
 * 2022/8/5 23:12
 */
public class NettyClientInitializer implements ClientInitializer<NettyClient<ProtocolHeader, ProtocolHeader>> {

    @Override
    public void initializer(NettyClient<ProtocolHeader, ProtocolHeader> client) {
        Bootstrap clientBootstrap = client.getClientBootstrap();
        Protocol<? extends ProtocolHeader, ? extends ProtocolHeader> protocol = client.getProtocol();
        // @ChannelHandler.Sharable 修饰允许被多个管道共享,可以在这里做写统计的工作
        NettySharableExchangeInboundHandler sharableExchangeHandler = new NettySharableExchangeInboundHandler(new DefaultExchangeChannelHandler(false, protocol));
        clientBootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline cp = ch.pipeline();
                // 1. 添加心跳检查(5s没有反应就自动关闭连接)
//                cp.addLast("idleStateHandler", new IdleStateHandler(5, 5, 5, TimeUnit.SECONDS));
                cp.addLast(protocol.getRequestDecoder());
                cp.addLast(protocol.getResponseEncoder());
                // 3. 业务转发器（NettyAPI 转自定义API）
                cp.addLast(sharableExchangeHandler);
            }
        });
    }
}
