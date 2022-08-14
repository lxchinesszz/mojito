package cn.lxchinesszz.mojito.fluent;

import cn.lxchinesszz.mojito.client.Client;
import cn.lxchinesszz.mojito.future.listener.MojitoListener;
import cn.lxchinesszz.mojito.protocol.mojito.model.RpcRequest;
import cn.lxchinesszz.mojito.protocol.mojito.model.RpcResponse;
import cn.lxchinesszz.mojito.task.AbstractHandlerTask;
import com.github.houbb.junitperf.core.annotation.JunitPerfConfig;
import com.github.houbb.junitperf.core.report.impl.ConsoleReporter;
import com.github.houbb.junitperf.core.report.impl.HtmlReporter;
import io.protostuff.Rpc;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author liuxin
 * 2022/8/14 20:00
 */
public class MojitoPerfTest {

    Mojito.ClientCreator<RpcRequest, RpcResponse> clientCreator;

    {
        try {
            clientCreator = Mojito.client(RpcRequest.class, RpcResponse.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("吞度量测试")
    @JunitPerfConfig(threads = 2, warmUp = 3000, duration = 5000, reporter = {HtmlReporter.class})
    public void testMojitoClient() throws Exception {
        // 创建连接，到执行。
        // 相同ip + 端口,不创建多次。
        // 服务端: 添加一个统计类
        Client<RpcRequest, RpcResponse> client = clientCreator.connect("127.0.0.1", 6666);
        RpcRequest rpcRequest = new RpcRequest();
        System.out.println(client.send(rpcRequest));
    }
}
