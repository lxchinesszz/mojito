package cn.lxchinesszz.mojito.client;

import cn.lxchinesszz.mojito.channel.EnhanceChannel;
import cn.lxchinesszz.mojito.future.MojitoFuture;
import cn.lxchinesszz.mojito.future.Promise;
import cn.lxchinesszz.mojito.protocol.ProtocolEnum;
import cn.lxchinesszz.mojito.protocol.ProtocolHeader;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 异步转同步
 *
 * @author liuxin
 * 2022/8/11 09:32
 */
public class DefaultClientPromiseHandler<REQ extends ProtocolHeader, RES extends ProtocolHeader> implements ClientPromiseHandler<REQ, RES> {

    /**
     * 消息发送可以使用无锁环形队列Disruptor
     * 当前通道正在发送的数据信息
     */
    private final Map<String, Promise<RES>> futureMap = new ConcurrentHashMap<>();

    /**
     * 通用的
     */
    private final List<Promise<RES>> messageList = new CopyOnWriteArrayList<>();

    @Override
    public MojitoFuture<RES> sendAsync(EnhanceChannel enhanceChannel, REQ rpcRequest) {
        MojitoFuture<RES> future = new MojitoFuture<>();
        enhanceChannel.send(rpcRequest);
        if (ProtocolEnum.MQ_REG == ProtocolEnum.byType(rpcRequest.getProtocolType())) {
            messageList.add(future);
            futureMap.put(rpcRequest.getId(), future);
        } else {
            futureMap.put(rpcRequest.getId(), future);
        }

        return future;
    }

    @Override
    public void received(RES rpcResponse) {
        // 从中拿到请求的future进行回告通知
        Promise<RES> promise = futureMap.remove(rpcResponse.getId());
        promise.setSuccess(rpcResponse);
    }
}
