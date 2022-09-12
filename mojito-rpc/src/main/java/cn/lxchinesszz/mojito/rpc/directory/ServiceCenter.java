package cn.lxchinesszz.mojito.rpc.directory;

/**
 * @author liuxin
 * 2022/9/12 20:51
 */
public interface ServiceCenter extends ServerDiscover, ServiceRegister {

    void registerService(Object service);
}
