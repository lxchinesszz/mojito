package cn.lxchinesszz.mojito.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.List;

/**
 * @author liuxin
 * 2022/8/14 21:45
 */
public class Mojito2ChannelDecoder extends ChannelDecoder {

    @Override
    protected void doDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//        LengthFieldBasedFrameDecoder lengthFieldBasedFrameDecoder = new LengthFieldBasedFrameDecoder();
    }
}
