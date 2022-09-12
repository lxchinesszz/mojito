package cn.lxchinesszz.mojito.rpc.proxy;

import cn.lxchinesszz.mojito.rpc.User;
import cn.lxchinesszz.mojito.rpc.invoker.Invoker;
import cn.lxchinesszz.mojito.rpc.invoker.Result;
import cn.lxchinesszz.mojito.rpc.invoker.RpcInvocation;
import cn.lxchinesszz.mojito.rpc.utils.PropertiesReflector;
import cn.lxchinesszz.mojito.rpc.utils.reflect.JvmInvoker;
import org.junit.jupiter.api.Test;

/**
 * @author liuxin
 * 2022/9/7 20:10
 */
class ProxyFactoryTest {


    @Test
    void getInvoker() throws Throwable {
        JdkProxyFactory jdkProxyFactory = new JdkProxyFactory();
        Invoker<User> hello = jdkProxyFactory.getLocalInvoker(new User("hello"));
        RpcInvocation invocation = RpcInvocation.builder().interfaceType(User.class).methodName("getName").arguments(null).build();
        Result invoke = hello.invoke(invocation);
        if (invoke.hasException()) {
            throw invoke.getException();
        } else {
            System.out.println(invoke.getValue());
            System.out.println(hello.getInterface());
        }
    }

    @Test
    void test() throws Throwable {
        // 对象
        PropertiesReflector reflectorCache = new PropertiesReflector(User.class);
        User empUser = new User();
        JvmInvoker setNameInvoker = reflectorCache.getSetInvoker("name");
        setNameInvoker.invoke(empUser, new Object[]{"Jay"});
        JvmInvoker getNameInvoker = reflectorCache.getGetInvoker("name");
        System.out.println(getNameInvoker.invoke(empUser, null));
    }
}