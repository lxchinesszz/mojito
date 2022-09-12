package cn.lxchinesszz.mojito.net.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author liuxin
 * @version Id: HanChannel.java, v 0.1 2019-05-11 10:31
 */
public class DefaultEnhanceChannel extends AbstractEnhanceChannel {

    /**
     * netty 网络通道
     */
    private final Channel channel;

    /**
     * 为每个通道设置一个通道id
     */
    private final String channelId;

    /**
     * 是否可以写
     */
    private boolean write = true;

    /**
     * 通道信息
     */
    private static final ConcurrentMap<Channel, DefaultEnhanceChannel> channelMap = new ConcurrentHashMap<Channel, DefaultEnhanceChannel>();

    private final Map<String, Object> attributes = new ConcurrentHashMap<>();

    private DefaultEnhanceChannel(Channel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("netty channel == null;");
        }
        this.channel = channel;
        this.channelId = buildId(channel);
    }

    public String buildId(Channel channel) {
        return channel.id().asLongText();
    }

    @Override
    public String getChannelId() {
        return channelId;
    }

    public static DefaultEnhanceChannel getOrAddChannel(Channel ch) {
        if (ch == null) {
            return null;
        }
        DefaultEnhanceChannel ret = channelMap.get(ch);
        if (ret == null) {
            DefaultEnhanceChannel nettyChannel = new DefaultEnhanceChannel(ch);
            if (ch.isActive()) {
                ret = channelMap.putIfAbsent(ch, nettyChannel);
            }
            if (ret == null) {
                ret = nettyChannel;
            }
        }
        return ret;
    }

    public static void removeChannelIfDisconnected(Channel ch) {
        if (ch != null && !ch.isActive()) {
            channelMap.remove(ch);
        }
    }


    @Override
    public InetSocketAddress getLocalAddress() {
        return (InetSocketAddress) channel.localAddress();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) channel.remoteAddress();
    }


    @Override
    public boolean isConnected() {
        return channel.isActive();
    }

    @Override
    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }

    @Override
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        if (value == null) {
            attributes.remove(key);
        } else {
            attributes.put(key, value);
        }
    }

    @Override
    public void removeAttribute(String key) {
        attributes.remove(key);
    }


    @Override
    public void send(Object message) {
        send(message, 0, false);
    }

    @Override
    public void send(Object message, long timeout, boolean close) {
        ChannelFuture channelFuture = channel.writeAndFlush(message);
        channel.flush();
        if (close) {
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void sendAndClose(Object message) {
        send(message, 0, true);
    }

    @Override
    public void disconnected() {
        channel.disconnect();
        attributes.clear();
        channel.close();
        removeChannelIfDisconnected(channel);
    }

    @Override
    public void exceptionCaught(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void markWrite() {
        this.write = true;
    }

    @Override
    public void markNotWrite() {
        this.write = false;
    }

    @Override
    public boolean isWrite() {
        return this.write;
    }
}
