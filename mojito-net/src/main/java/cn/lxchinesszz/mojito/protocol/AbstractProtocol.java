package cn.lxchinesszz.mojito.protocol;

import cn.lxchinesszz.mojito.business.BusinessHandler;
import cn.lxchinesszz.mojito.client.ClientPromiseHandler;
import cn.lxchinesszz.mojito.client.DefaultClientPromiseHandler;
import io.netty.util.internal.TypeParameterMatcher;

/**
 * @author liuxin
 * 2022/8/8 21:52
 */
public abstract class AbstractProtocol<R extends ProtocolHeader, V extends ProtocolHeader> implements Protocol<R, V> {

    private final TypeParameterMatcher matcher;

    private BusinessHandler<R, V> businessHandler;

    private final ClientPromiseHandler<R, V> clientPromiseHandler;

    public AbstractProtocol() {
        this.matcher = TypeParameterMatcher.find(this, AbstractProtocol.class, "R");
        this.clientPromiseHandler = new DefaultClientPromiseHandler<>();
    }

    public AbstractProtocol(BusinessHandler<R, V> businessHandler) {
        this.businessHandler = businessHandler;
        this.matcher = TypeParameterMatcher.find(this, AbstractProtocol.class, "R");
        this.clientPromiseHandler = new DefaultClientPromiseHandler<>();
    }

    public void setBusinessHandler(BusinessHandler<R, V> businessHandler) {
        this.businessHandler = businessHandler;
    }

    public BusinessHandler<R, V> getBusinessHandler() {
        return businessHandler;
    }


    @Override
    public ClientPromiseHandler<R, V> getClientPromiseHandler() {
        return clientPromiseHandler;
    }

    @Override
    public boolean acceptInboundMessage(Object msg) {
        return matcher.match(msg);
    }
}
