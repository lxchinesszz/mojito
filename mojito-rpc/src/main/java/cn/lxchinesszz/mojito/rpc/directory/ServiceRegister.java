package cn.lxchinesszz.mojito.rpc.directory;

import cn.lxchinesszz.mojito.rpc.invoker.Invoker;


/**
 * @author liuxin
 * 2022/8/28 20:10
 */
public interface ServiceRegister {

    /**
     * 服务端将接口注册到这里
     *
     * @param cls     类型
     * @param invoker 执行器
     */
    void registerInvoker(Invoker<?> invoker);
}

