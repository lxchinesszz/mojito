package cn.lxchinesszz.mojito.net.client.netty;

import cn.lxchinesszz.mojito.net.channel.DefaultEnhanceChannel;
import cn.lxchinesszz.mojito.net.client.AbstractClient;
import cn.lxchinesszz.mojito.net.future.MojitoFuture;
import cn.lxchinesszz.mojito.net.protocol.ProtocolHeader;
import cn.lxchinesszz.mojito.net.task.AbstractHandlerTask;
import cn.lxchinesszz.mojito.net.utils.OSinfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author liuxin
 * 个人博客：https://java.springlearn.cn
 * 公众号：西魏陶渊明  ｛关注获取学习源码｝
 * 2022/8/5 23:12
 */
@Slf4j
public class NettyClient<REQ extends ProtocolHeader, RES extends ProtocolHeader> extends AbstractClient<REQ, RES> {

    private final Bootstrap clientBootstrap = new Bootstrap();

    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private DefaultEnhanceChannel enhanceChannel;

    @Override
    public void doConnect() throws Throwable {
        clientBootstrap.group(workerGroup);
        clientBootstrap.channel(OSinfo.isLinux() ? EpollSocketChannel.class : NioSocketChannel.class);
        clientBootstrap.option(ChannelOption.TCP_NODELAY, false);
        getClientInitializer().initializer(this);
        ChannelFuture channelFuture = clientBootstrap.connect(getRemoteHost(), getRemotePort()).sync();
        enhanceChannel = DefaultEnhanceChannel.getOrAddChannel(channelFuture.channel());
    }

    public Bootstrap getClientBootstrap() {
        return clientBootstrap;
    }

    @Override
    public void doClose() {
        workerGroup.shutdownGracefully();
        enhanceChannel.disconnected();
        log.info("Client 关闭成功");
    }

    @Override
    public MojitoFuture<RES> doSend(REQ req) {
        // 这里我们也设置,断线重连,后面优化
        // 默认都使用长连接
        req.getAttachments().put(AbstractHandlerTask.KEEPALIVE, "true");
        return getProtocol().getClientPromiseHandler().sendAsync(enhanceChannel, req);
    }

    @Override
    public boolean isConnected() {
        return Objects.nonNull(enhanceChannel) && enhanceChannel.isConnected();
    }


}
