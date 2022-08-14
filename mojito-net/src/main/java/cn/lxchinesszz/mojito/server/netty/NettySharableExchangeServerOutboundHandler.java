package cn.lxchinesszz.mojito.server.netty;

import cn.lxchinesszz.mojito.channel.DefaultEnhanceChannel;
import cn.lxchinesszz.mojito.protocol.ProtocolHeader;
import cn.lxchinesszz.mojito.exchange.ExchangeChannelHandler;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;


/**
 * Netty 调用 自定义的处理API,主要用于数据交换
 * 原生线程模型非常干净,我们通过重新定义Channel的方式,使其与容器处理逻辑所隔离。通过将Netty原生Channel 转换成 HanChannel
 * 所有的请求,都会在这里被处理。
 * 将Netty通道API转换为自己的API
 * 1. 连接的保存
 * 2. 读写的执行
 * 3. 异常的处理
 *
 * @author liuxin
 * @version Id: ServerHandler.java, v 0.1 2019-03-27 10:58
 */
@ChannelHandler.Sharable
public class NettySharableExchangeServerOutboundHandler extends ChannelDuplexHandler {


    private ExchangeChannelHandler exchangeChannelHandler;

    public NettySharableExchangeServerOutboundHandler(ExchangeChannelHandler exchangeChannelHandler) {
        this.exchangeChannelHandler = exchangeChannelHandler;
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        DefaultEnhanceChannel enhanceChannel = DefaultEnhanceChannel.getOrAddChannel(ctx.channel());
        try {
            exchangeChannelHandler.connected(enhanceChannel);
        } finally {
            //连接断开就移除
            DefaultEnhanceChannel.removeChannelIfDisconnected(ctx.channel());
        }
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        DefaultEnhanceChannel enhanceChannel = DefaultEnhanceChannel.getOrAddChannel(ctx.channel());
        try {
            exchangeChannelHandler.disconnected(enhanceChannel);
        } finally {
            //连接断开就移除
            DefaultEnhanceChannel.removeChannelIfDisconnected(ctx.channel());
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        DefaultEnhanceChannel enhanceChannel = DefaultEnhanceChannel.getOrAddChannel(ctx.channel());
        try {
            exchangeChannelHandler.write(enhanceChannel, (ProtocolHeader) msg);
        } finally {
            //连接断开就移除
            DefaultEnhanceChannel.removeChannelIfDisconnected(ctx.channel());
        }
    }
}
