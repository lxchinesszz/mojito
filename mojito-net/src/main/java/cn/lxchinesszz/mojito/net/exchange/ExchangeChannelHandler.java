package cn.lxchinesszz.mojito.net.exchange;

import cn.lxchinesszz.mojito.net.channel.EnhanceChannel;
import cn.lxchinesszz.mojito.net.exception.RemotingException;

/**
 * 屏蔽Netty底层众多的API方法,仅仅提供我们可能关注的
 *
 * @author liuxin
 * 2022/8/6 16:16
 */
public interface ExchangeChannelHandler {

    /**
     * 接受连接
     *
     * @param channel channel.
     * @throws RemotingException 远程调用异常
     */
    void connected(EnhanceChannel channel) throws RemotingException;

    /**
     * 断开连接
     *
     * @param channel channel.
     * @throws RemotingException 远程调用异常
     */
    void disconnected(EnhanceChannel channel) throws RemotingException;

    /**
     * 发送消息
     *
     * @param channel channel.
     * @param message message.
     * @throws RemotingException 远程调用异常
     */
     void write(EnhanceChannel channel, Object message) throws RemotingException;

    /**
     * 接受消息
     *
     * @param channel channel.
     * @param message message.
     * @throws RemotingException 远程调用异常
     */
    void read(EnhanceChannel channel, Object message) throws RemotingException;

    /**
     * 捕捉异常
     *
     * @param channel   channel.
     * @param exception exception.
     * @throws RemotingException 远程调用异常
     */
    void caught(EnhanceChannel channel, Throwable exception) throws RemotingException;
}
