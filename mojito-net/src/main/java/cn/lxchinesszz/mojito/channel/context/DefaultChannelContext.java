package cn.lxchinesszz.mojito.channel.context;


import cn.lxchinesszz.mojito.channel.EnhanceChannel;

/**
 * @author liuxin
 * 9/30/20 6:18 PM
 */
public class DefaultChannelContext implements ChannelContext {

    private final EnhanceChannel enhanceChannel;

    public DefaultChannelContext(EnhanceChannel enhanceChannel) {
        this.enhanceChannel = enhanceChannel;
    }

    @Override
    public EnhanceChannel getChannel() {
        return enhanceChannel;
    }

    @Override
    public boolean hasAttribute(String key) {
        return enhanceChannel.hasAttribute(key);
    }

    @Override
    public Object getAttribute(String key) {
        return enhanceChannel.getAttribute(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        enhanceChannel.setAttribute(key, value);
    }

    @Override
    public void removeAttribute(String key) {
        enhanceChannel.removeAttribute(key);
    }
}
