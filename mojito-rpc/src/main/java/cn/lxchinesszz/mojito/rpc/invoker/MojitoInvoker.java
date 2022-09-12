package cn.lxchinesszz.mojito.rpc.invoker;


/**
 * @author liuxin
 * 2022/8/28 23:25
 */
public class MojitoInvoker<T> extends AbstractInvoker<T> {

    /**
     * 原始对象
     */
    private Object source;

    private MojitoInvoker() {
    }

    public MojitoInvoker(Object source) {
        this.source = source;
    }

    @Override
    public Object getSource() {
        return source;
    }
}
