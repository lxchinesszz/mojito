package cn.lxchinesszz.mojito.net.task;

/**
 * 对服务端执行所以的线程封装
 *
 * @author liuxin
 * 2020-09-14 18:08
 */
public interface HandlerTask extends Runnable {

    @Override
    void run();

    void justStart();
}
