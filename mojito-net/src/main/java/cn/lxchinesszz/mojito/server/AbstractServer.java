package cn.lxchinesszz.mojito.server;

import cn.lxchinesszz.mojito.protocol.Protocol;
import cn.lxchinesszz.mojito.protocol.ProtocolHeader;
import cn.lxchinesszz.mojito.utils.LambdaExecute;
import com.hanframework.kit.thread.ThreadHookTools;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author liuxin
 * 2022/8/10 22:16
 */
public abstract class AbstractServer<T extends Server<?>> implements Server<T> {

    private ServerInitializer<T> serverInitializer;

    private Integer port;

    private final AtomicBoolean runningState = new AtomicBoolean(false);

    private Protocol<? extends ProtocolHeader, ? extends ProtocolHeader> protocol;

    @Override
    public void registryProtocol(Protocol<? extends ProtocolHeader, ? extends ProtocolHeader> protocol) {
        this.protocol = protocol;
    }

    @Override
    public void registryHooks(Runnable hookTask) {
        ThreadHookTools.addHook(new Thread(hookTask));
    }

    @Override
    public Protocol<? extends ProtocolHeader, ? extends ProtocolHeader> getProtocol() {
        return protocol;
    }

    @Override
    public void initializer(ServerInitializer<T> initializer) {
        this.serverInitializer = initializer;
    }

    @Override
    public ServerInitializer<T> getServerInitializer() {
        return serverInitializer;
    }

    @Override
    public void start(int port) {
        checked();
        activeAndCreateServer(() -> {
            this.port = port;
            doCreateServer(port, false);
        });
    }

    private void checked() {
        if (Objects.isNull(protocol)) {
            throw new RuntimeException("Protocol不能为空,请Server#registryProtocol");
        }
        if (Objects.isNull(serverInitializer)) {
            throw new RuntimeException("Protocol不能为空,请Server#initializer");
        }
    }

    private void activeAndCreateServer(LambdaExecute execute) {
        if (isRun()) {
            throw new RuntimeException("运行中");
        }
        if (runningState.compareAndSet(false, true)) {
            try {
                execute.execute();
            } catch (Throwable t) {
                runningState.compareAndSet(true, false);
            }
        }
    }

    private void closeAndDestroyServer(LambdaExecute execute) {
        if (isRun()) {
            if (runningState.compareAndSet(true, false)) {
                execute.execute();
            }
        }
    }

    @Override
    public void startAsync(int port) {
        activeAndCreateServer(() -> {
            this.port = port;
            doCreateServer(port, true);
        });
    }

    @Override
    public void close() {
        closeAndDestroyServer(this::doDestroyServer);
    }

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public boolean isRun() {
        return runningState.get();
    }

    public abstract void doCreateServer(int port, boolean async);

    public abstract void doDestroyServer();
}
