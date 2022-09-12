package cn.lxchinesszz.mojito.net.server.netty;

import cn.lxchinesszz.mojito.net.channel.DefaultEnhanceChannel;
import cn.lxchinesszz.mojito.net.exchange.ExchangeChannelHandler;
import cn.lxchinesszz.mojito.net.exception.RemotingException;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.concurrent.atomic.AtomicLong;


/**
 * Netty 调用 自定义的处理API,主要用于数据交换
 * 原生线程模型非常干净,我们通过重新定义Channel的方式,使其与容器处理逻辑所隔离。通过将Netty原生Channel 转换成 EnhanceChannel
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
public class NettySharableExchangeInboundHandler extends SimpleChannelInboundHandler<Object> {


    private final ExchangeChannelHandler exchangeChannelHandler;

    private final AtomicLong count = new AtomicLong(0);

    public NettySharableExchangeInboundHandler(ExchangeChannelHandler exchangeChannelHandler) {
        this.exchangeChannelHandler = exchangeChannelHandler;
    }

    /**
     * 接受连接
     *
     * @param ctx channel.
     * @throws Exception 远程调用异常
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        DefaultEnhanceChannel channel = DefaultEnhanceChannel.getOrAddChannel(ctx.channel());
        try {
            exchangeChannelHandler.connected(channel);
        } finally {
            //连接断开就移除
            DefaultEnhanceChannel.removeChannelIfDisconnected(ctx.channel());
        }
    }


    /**
     * 断开连接
     *
     * @param ctx channel.
     * @throws Exception 远程调用异常
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        DefaultEnhanceChannel channel = DefaultEnhanceChannel.getOrAddChannel(ctx.channel());
        try {
            exchangeChannelHandler.disconnected(channel);
        } finally {
            DefaultEnhanceChannel.removeChannelIfDisconnected(ctx.channel());
        }
    }

    /**
     * 接受消息
     *
     * @param ctx channel.
     * @param msg message.
     * @throws RemotingException 远程调用异常
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        DefaultEnhanceChannel channel = DefaultEnhanceChannel.getOrAddChannel(ctx.channel());
        try {
            exchangeChannelHandler.read(channel, msg);
        } finally {
            DefaultEnhanceChannel.removeChannelIfDisconnected(ctx.channel());
        }

    }

    /**
     * 异常情况
     *
     * @param ctx   上下文
     * @param cause 异常类
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        DefaultEnhanceChannel channel = DefaultEnhanceChannel.getOrAddChannel(ctx.channel());
        try {
            exchangeChannelHandler.caught(channel, cause);
        } finally {
            DefaultEnhanceChannel.removeChannelIfDisconnected(ctx.channel());
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        DefaultEnhanceChannel channel = DefaultEnhanceChannel.getOrAddChannel(ctx.channel());
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                System.err.println("读超时，关闭通道");
                channel.disconnected();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                System.err.println("写超时，关闭通道");
                channel.disconnected();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

}
