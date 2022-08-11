package cn.lxchinesszz.mojito.server;

/**
 * @author liuxin
 * 2022/8/5 23:12
 */
public interface Server<T extends Server<?>> extends ConfigurableServer<T> {

    /**
     * 启动服务
     *
     * @param port 服务端口号
     */
    void start(int port);

    /**
     * 非阻塞启动
     *
     * @param port 端口
     */
    void startAsync(int port);

    /**
     * 关闭服务
     */
    void close();

    /**
     * 服务器端口
     *
     * @return int
     */
    int getPort();

    /**
     * 是否运行中
     *
     * @return boolean
     */
    boolean isRun();

}
