package cn.lxchinesszz.mojito.rpc.invoker;

import java.util.Map;

/**
 * @author liuxin
 * 2022/8/17 18:50
 */
public interface Invocation {

    /**
     * 获取执行的方法名
     *
     * @return String
     */
    String getMethodName();

    /**
     * 参数
     *
     * @return Object[]
     */
    Object[] getArguments();

    /**
     * 通道附加信息
     *
     * @return Map<String, String>
     */
    Map<String, String> getAttachments();

    String getAttachment(String key);

    String getAttachment(String key, String defaultValue);

    Class<?> getInterface();


}
