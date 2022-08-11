package cn.lxchinesszz.mojito.server;

/**
 * 服务器初始化
 *
 * @author liuxin
 * 2022/8/6 13:30
 */
public interface ServerInitializer<T extends Server<?>> {
    void initializer(T server);
}
