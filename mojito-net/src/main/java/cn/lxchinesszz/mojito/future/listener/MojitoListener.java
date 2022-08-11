package cn.lxchinesszz.mojito.future.listener;

/**
 * @author liuxin
 * 2020-08-15 18:01
 */
public interface MojitoListener<F> {

    /**
     * 成功执行器
     *
     * @param result 增强异步对象
     * @throws Exception 未知
     */
    void onSuccess(F result);

    /**
     * 执行失败处理方法
     *
     * @param throwable 错误信息
     * @throws Exception 未知异常
     */
    void onThrowable(Throwable throwable);
}
