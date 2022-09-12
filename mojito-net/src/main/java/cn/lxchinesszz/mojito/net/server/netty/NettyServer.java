package cn.lxchinesszz.mojito.net.server.netty;

import cn.lxchinesszz.mojito.net.banner.Banner;
import cn.lxchinesszz.mojito.net.server.AbstractServer;
import cn.lxchinesszz.mojito.net.utils.NamedThreadFactory;
import cn.lxchinesszz.mojito.net.utils.OSinfo;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author liuxin
 * 2022/8/6 13:06
 */
@Slf4j
public class NettyServer extends AbstractServer<NettyServer> {

    private final ServerBootstrap serverBootstrap = new ServerBootstrap();

    private Channel serverChannel;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private static final int DEFAULT_EVENT_THREADS = Math.min(Runtime.getRuntime().availableProcessors() + 1, 32);

    public ServerBootstrap getServerBootstrap() {
        return this.serverBootstrap;
    }

    @Override
    @SneakyThrows
    public void doCreateServer(int port, boolean async) {
        // 1. io线程数 = cpu * 2
        bossGroup = new NioEventLoopGroup(1, new NamedThreadFactory("mojito-boss", true));
        // 2. 业务线程数 = cpu + 1
        workerGroup = new NioEventLoopGroup(DEFAULT_EVENT_THREADS, new NamedThreadFactory("mojito-work", true));
        serverBootstrap.group(bossGroup, workerGroup)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.SO_BACKLOG, 128)
                .handler(new LoggingHandler(LogLevel.INFO))
                .channel(OSinfo.isLinux() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                .localAddress(port).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        getServerInitializer().initializer(this);
        // 3. 阻塞绑定端口
        ChannelFuture bindFuture = serverBootstrap.bind().addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                Banner.print();
                log.info("Mojito启动成功,端口号:" + port);
            } else {
                Throwable cause = channelFuture.cause();
                throw new RuntimeException(cause);
            }
        }).sync();
        serverChannel = bindFuture.channel();
        if (async) {
            log.info("异步服务启动成功");
        } else {
            serverChannel.closeFuture().sync();
            log.info("阻塞服务启动成功");
        }
    }

    @Override
    public void doDestroyServer() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        serverChannel.close();
    }

}
