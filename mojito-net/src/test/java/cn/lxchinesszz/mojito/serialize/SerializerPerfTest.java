package cn.lxchinesszz.mojito.serialize;

import cn.lxchinesszz.mojito.serialize.impl.*;
import com.github.houbb.junitperf.core.annotation.JunitPerfConfig;
import com.github.houbb.junitperf.core.report.impl.HtmlReporter;
import org.junit.jupiter.api.DisplayName;

/**
 * 性能测试
 *
 * @author liuxin
 * 2022/7/30 13:39
 */
public class SerializerPerfTest {

    static ProtostuffObjectSerializer proto = new ProtostuffObjectSerializer();

    static Hession2ObjectSerializer hession2 = new Hession2ObjectSerializer();

    static HessionObjectSerializer hession = new HessionObjectSerializer();

    static NettyObjectSerializer nettyObj = new NettyObjectSerializer();

    static NettyCompactObjectSerializer compact = new NettyCompactObjectSerializer();

    @DisplayName("Protostuff性能测试")
    @JunitPerfConfig(threads = 20, warmUp = 3000, duration = 5000, reporter = {HtmlReporter.class})
    public void testProtostuff() {
        proto.serialize(new SerializerTest.UserSerializable("周杰伦", 42));
    }

    @DisplayName("Hession2性能测试")
    @JunitPerfConfig(threads = 20, warmUp = 3000, duration = 5000, reporter = {HtmlReporter.class})
    public void testHession2() {
        hession2.serialize(new SerializerTest.UserSerializable("周杰伦", 42));
    }

    @DisplayName("Hession性能测试")
    @JunitPerfConfig(threads = 20, warmUp = 3000, duration = 5000, reporter = {HtmlReporter.class})
    public void testHession() {
        hession.serialize(new SerializerTest.UserSerializable("周杰伦", 42));
    }

    @DisplayName("NettyObject性能测试")
    @JunitPerfConfig(threads = 20, warmUp = 3000, duration = 5000, reporter = {HtmlReporter.class})
    public void testNettyObject() {
        nettyObj.serialize(new SerializerTest.UserSerializable("周杰伦", 42));
    }

    @DisplayName("NettyCompact性能测试")
    @JunitPerfConfig(threads = 20, warmUp = 3000, duration = 5000, reporter = {HtmlReporter.class})
    public void testNettyCompact性能测试() {
        compact.serialize(new SerializerTest.UserSerializable("周杰伦", 42));
    }
}
