package cn.lxchinesszz.mojito.client;

import cn.lxchinesszz.mojito.future.MojitoFuture;
import cn.lxchinesszz.mojito.protocol.Protocol;
import cn.lxchinesszz.mojito.protocol.ProtocolHeader;
import com.hanframework.kit.thread.ThreadHookTools;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author liuxin
 * 2022/8/10 23:32
 */
public abstract class AbstractClient<REQ extends ProtocolHeader, RES extends ProtocolHeader> implements Client<REQ, RES> {


    /**
     * 将要连接的远程地址
     */
    private String remoteHost;

    /**
     * 将要连接的远程端口
     */
    private int remotePort;

    private Protocol<REQ, RES> protocol;

    private ClientInitializer<Client<REQ, RES>> clientInitializer;

    private final AtomicBoolean running = new AtomicBoolean(false);

    @Override
    public void connect(String host, Integer port) {
        if (!running.get()) {
            this.remoteHost = host;
            this.remotePort = port;
        }
        try {
            if (running.compareAndSet(false, true)) {
                doConnect();
            }
        } catch (Throwable t) {
            t.printStackTrace();
            running.compareAndSet(true, false);
        }
    }

    @Override
    public MojitoFuture<RES> send(REQ req) {
        return doSend(req);
    }

    @Override
    public void close() {
        doClose();
    }

    public int getRemotePort() {
        return remotePort;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    @Override
    public Protocol<REQ, RES> getProtocol() {
        return this.protocol;
    }


    @Override
    public void registryProtocol(Protocol<REQ, RES> protocol) {
        this.protocol = protocol;
    }

    @Override
    public void registryHooks(Runnable hookTask) {
        ThreadHookTools.addHook(new Thread(hookTask));
    }

    @Override
    public void initializer(ClientInitializer initializer) {
        this.clientInitializer = initializer;
    }

    @Override
    public boolean isRun() {
        return running.get();
    }

    @Override
    public ClientInitializer<Client<REQ, RES>> getClientInitializer() {
        return clientInitializer;
    }

    public abstract void doConnect();

    public abstract void doClose();

    public abstract MojitoFuture<RES> doSend(REQ req);
}
