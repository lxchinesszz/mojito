package cn.lxchinesszz.mojito.future;

import cn.lxchinesszz.mojito.future.listener.MojitoListener;

/**
 * @author liuxin
 * 2022/8/9 20:49
 */
public interface Promise<V> {

    /**
     * 是否成功
     *
     * @return boolean
     */
    boolean isSuccess();

    /**
     * 设置成功表示
     *
     * @param data 数据
     */
    void setSuccess(V data);

    /**
     * 设置失败标识
     *
     * @param cause 异常
     */
    void setFailure(Throwable cause);

    /**
     * 添加监听器
     *
     * @param listener 监听器
     */
    void addListeners(MojitoListener<V> listener);
}
