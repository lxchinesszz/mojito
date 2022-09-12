package cn.lxchinesszz.mojito.net.channel.context;


import cn.lxchinesszz.mojito.net.channel.EnhanceChannel;

/**
 * Attribute信息会从相应头中
 * @author liuxin
 * 9/30/20 6:15 PM
 */
public interface ChannelContext {

    /**
     * 获取通道连接
     *
     * @return 通道连接
     */
    EnhanceChannel getChannel();


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
}
