package cn.lxchinesszz.mojito.utils;


/**
 * @author liuxin
 * 2022/8/5 13:46
 */
public class ServiceOption<T> extends AbstractConstant<ServiceOption<T>> {


    public static final ServiceOption<Boolean> SO_KEEPALIVE = valueOf("SO_KEEPALIVE");

    public static final ServiceOption<Boolean> TCP_NO_DELAY = valueOf("TCP_NO_DELAY");

    public static final ServiceOption<Integer> SO_BACKLOG = valueOf("SO_BACKLOG");

    private static final ConstantPool<ServiceOption<Object>> pool = new ConstantPool<ServiceOption<Object>>() {
        @Override
        protected ServiceOption<Object> newConstant(int id, String name) {
            return new ServiceOption<Object>(id, name);
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> ServiceOption<T> valueOf(String name) {
        return (ServiceOption<T>) pool.valueOf(name);
    }

    protected ServiceOption(int id, String name) {
        super(id, name);
    }

    @Override
    public long getUniqueId() {
        return super.getUniqueId();
    }

    @Override
    public int id() {
        return super.id();
    }

    @Override
    public String name() {
        return super.name();
    }

}
