package cn.lxchinesszz.mojito.net.business;


import cn.lxchinesszz.mojito.net.channel.context.ChannelContext;
import cn.lxchinesszz.mojito.net.exception.RemotingException;
import cn.lxchinesszz.mojito.net.protocol.ProtocolHeader;

import java.lang.reflect.InvocationTargetException;

/**
 * 处理请求
 *
 * @author liuxin
 * 2020-09-02 18:11
 */
public interface BusinessHandler<REQ extends ProtocolHeader, RES extends ProtocolHeader> {

    /**
     * 服务端业务处理器
     *
     * @param channelContext 连接通信上下文信息
     * @param request        请求信息
     * @return R
     */
    RES handler(ChannelContext channelContext, REQ request) throws RemotingException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException;

}
