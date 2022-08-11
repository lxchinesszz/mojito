package cn.lxchinesszz.mojito.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 请求解码器负责将二进制数据转换成能处理的协议
 *
 * @author liuxin
 * 2020-07-25 22:03
 */
@Slf4j
public abstract class ChannelDecoder extends ByteToMessageDecoder {

    /**
     * 解码方法
     *
     * @param ctx 通道上下文信息
     * @param in  网络传过来的信息(注意粘包和拆包问题)
     * @param out in中的数据转换成对象调用out.add方法
     * @throws Exception 未知异常
     */
    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        doDecode(ctx, in, out);
    }


    /**
     * 解码方法
     *
     * @param ctx 通道上下文信息
     * @param in  网络传过来的信息(注意粘包和拆包问题)
     * @param out in中的数据转换成对象调用out.add方法
     * @throws Exception 未知异常
     */
    protected abstract void doDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception;
}
