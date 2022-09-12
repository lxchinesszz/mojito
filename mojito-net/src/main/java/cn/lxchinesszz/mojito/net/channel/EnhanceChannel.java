package cn.lxchinesszz.mojito.net.channel;

import java.net.InetSocketAddress;

/**
 * 对原始的channel添加了一些新的能力
 *
 * @author liuxin
 * @version Id: Channel.java, v 0.1 2019-05-11 10:32
 */
public interface EnhanceChannel {

    /**
     * 通道唯一ud
     *
     * @return String
     */
    String getChannelId();

    /**
     * 远程地址
     *
     * @return InetSocketAddress
     */
    InetSocketAddress getRemoteAddress();

    /**
     * 是否已连接
     *
     * @return boolean
     */
    boolean isConnected();

    /**
     * 是否包含属性
     *
     * @param key 属性key
     * @return boolean
     */
    boolean hasAttribute(String key);

    /**
     * 获取某一个属性
     *
     * @param key 属性key
     * @return Object
     */
    Object getAttribute(String key);

    /**
     * 设置某一个属性值
     *
     * @param key   属性key
     * @param value 属性value
     */
    void setAttribute(String key, Object value);

    /**
     * 移出某一个属性key
     *
     * @param key 属性key
     */
    void removeAttribute(String key);

    /**
     * 发送数据
     *
     * @param message 数据消息
     */
    void send(Object message);

    /**
     * 发送数据并给一个超时时间
     *
     * @param message 数据消息
     * @param timeout 超时事件
     * @param close   是否关闭
     */
    void send(Object message, long timeout, boolean close);

    /**
     * 发送并关闭
     *
     * @param message 数据信息
     */
    void sendAndClose(Object message);

    /**
     * 端口连接
     */
    void disconnected();

    /**
     * 捕捉异常
     *
     * @param throwable 异常信息
     */
    void exceptionCaught(Throwable throwable);

    /**
     * 标记可在写
     */
    void markWrite();

    /**
     * 标记为不可写
     */
    void markNotWrite();

    /**
     * 是否可写
     *
     * @return boolean
     */
    boolean isWrite();

}

