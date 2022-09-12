package cn.lxchinesszz.mojito.net.codec;

import cn.lxchinesszz.mojito.net.protocol.ProtocolHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author liuxin
 * 2020-07-25 22:03
 */
public abstract class ChannelEncoder<T extends ProtocolHeader> extends MessageToByteEncoder<T> {

    @Override
    public void encode(ChannelHandlerContext ctx, T msg, ByteBuf out) throws Exception {
        doEncode(ctx, msg, out);
    }

    protected abstract void doEncode(ChannelHandlerContext ctx, T msg, ByteBuf out) throws Exception;
}
