package cn.lxchinesszz.mojito.serialize;

import cn.lxchinesszz.mojito.exception.SerializeException;
import cn.lxchinesszz.mojito.serialize.impl.*;
import cn.lxchinesszz.mojito.test.print.ColorConsole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * @author liuxin
 * 2022/7/30 11:29
 */
class SerializerTest {

    @Data
    @ToString
    @AllArgsConstructor
    public static class UserNoSerializable {

        private String name;

        private Integer age;
    }

    @Data
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSerializable implements Serializable {

        private String name;

        private Integer age;
    }

    @Test
    @DisplayName("验证Netty序列化异常场景")
    public void test0() {
        Assertions.assertThrows(SerializeException.class, () -> {
            NettyObjectSerializer nettyObjectSerializer = new NettyObjectSerializer();
            byte[] jay = nettyObjectSerializer.serialize(new UserNoSerializable("周杰伦", 42));
        });
    }

    @Test
    @DisplayName("验证Netty序列化场景")
    public void test1() {
        NettyObjectSerializer nettyObjectSerializer = new NettyObjectSerializer();
        byte[] jay = nettyObjectSerializer.serialize(new UserSerializable("周杰伦", 42));
        ColorConsole.colorPrintln("数据长度:{}", jay.length);
        UserSerializable deserialize = nettyObjectSerializer.deserialize(jay, UserSerializable.class);
        Assertions.assertEquals(deserialize.getName(), "周杰伦");
        Assertions.assertEquals(deserialize.getAge(), 42);
    }

    @Test
    @DisplayName("验证NettyCompact序列化场景")
    public void test3() {
        NettyCompactObjectSerializer nettyObjectSerializer = new NettyCompactObjectSerializer();
        byte[] jay = nettyObjectSerializer.serialize(new UserSerializable("周杰伦", 42));
        ColorConsole.colorPrintln("数据长度:{}", jay.length);
        UserSerializable deserialize = nettyObjectSerializer.deserialize(jay, UserSerializable.class);
        Assertions.assertEquals(deserialize.getName(), "周杰伦");
        Assertions.assertEquals(deserialize.getAge(), 42);
    }

    @Test
    @DisplayName("验证HessionObjectSerializer序列化场景")
    public void test4() {
        HessionObjectSerializer nettyObjectSerializer = new HessionObjectSerializer();
        byte[] jay = nettyObjectSerializer.serialize(new UserSerializable("周杰伦", 42));
        ColorConsole.colorPrintln("数据长度:{}", jay.length);
        UserSerializable deserialize = nettyObjectSerializer.deserialize(jay, UserSerializable.class);
        Assertions.assertEquals(deserialize.getName(), "周杰伦");
        Assertions.assertEquals(deserialize.getAge(), 42);
    }

    @Test
    @DisplayName("验证Hession2ObjectSerializer序列化场景")
    public void test5() {
        Hession2ObjectSerializer nettyObjectSerializer = new Hession2ObjectSerializer();
        byte[] jay = nettyObjectSerializer.serialize(new UserSerializable("周杰伦", 42));
        ColorConsole.colorPrintln("数据长度:{}", jay.length);
        UserSerializable deserialize = nettyObjectSerializer.deserialize(jay, UserSerializable.class);
        Assertions.assertEquals(deserialize.getName(), "周杰伦");
        Assertions.assertEquals(deserialize.getAge(), 42);
    }

    @Test
    @DisplayName("验证ProtostuffObjectSerializer序列化场景")
    public void test6() {
        ProtostuffObjectSerializer nettyObjectSerializer = new ProtostuffObjectSerializer();
        byte[] jay = nettyObjectSerializer.serialize(new UserSerializable("周杰伦", 42));
        ColorConsole.colorPrintln("数据长度:{}", jay.length);
        UserSerializable deserialize = nettyObjectSerializer.deserialize(jay, UserSerializable.class);
        Assertions.assertEquals(deserialize.getName(), "周杰伦");
        Assertions.assertEquals(deserialize.getAge(), 42);
    }

    /**
     * Protostuff,序列化数据长度:13
     * Hession2,序列化数据长度:88
     * Hession,序列化数据长度:98
     * NettyCompact,序列化数据长度:132
     * NettyObject,序列化数据长度:136
     */
    @Test
    @DisplayName("序列化数据大小对比")
    public void test7() {
        ProtostuffObjectSerializer nettyObjectSerializer = new ProtostuffObjectSerializer();
        byte[] jay = nettyObjectSerializer.serialize(new UserSerializable("周杰伦", 42));
        ColorConsole.colorPrintln("{},序列化数据长度:{}", "Protostuff", jay.length);

        ColorConsole.colorPrintln("{},序列化数据长度:{}", "Hession2",
                new Hession2ObjectSerializer().serialize(new UserSerializable("周杰伦", 42)).length);

        ColorConsole.colorPrintln("{},序列化数据长度:{}", "Hession",
                new HessionObjectSerializer().serialize(new UserSerializable("周杰伦", 42)).length);

        ColorConsole.colorPrintln("{},序列化数据长度:{}", "NettyCompact",
                new NettyCompactObjectSerializer().serialize(new UserSerializable("周杰伦", 42)).length);

        ColorConsole.colorPrintln("{},序列化数据长度:{}", "NettyObject",
                new NettyObjectSerializer().serialize(new UserSerializable("周杰伦", 42)).length);
    }

    @Test
    @DisplayName("Java对象序列化 & 反序列化")
    public void test8() {
        ProtostuffObjectSerializer nettyObjectSerializer = new ProtostuffObjectSerializer();
        byte[] jay = nettyObjectSerializer.serialize(new UserSerializable("周杰伦", 32));
        // [10, 9, -27, -111, -88, -26, -99, -80, -28, -68, -90, 16, 32]
        System.out.println(Arrays.toString(jay));
        UserSerializable deserialize = nettyObjectSerializer.deserialize(jay, UserSerializable.class);
        // SerializerTest.UserSerializable(name=周杰伦, age=32)
        System.out.println(deserialize);
    }

    @Test
    public void print() throws Exception {
        ColorConsole.colorPrintln("{},序列化数据:{}", "NettyCompact",
                Arrays.toString(new NettyCompactObjectSerializer().serialize(new UserSerializable("周杰伦", 42))));

        ColorConsole.colorPrintln("{},序列化数据:{}", "NettyObject",
                Arrays.toString(new NettyObjectSerializer().serialize(new UserSerializable("周杰伦", 42))));

        Object j1 = new NettyCompactObjectSerializer().deserialize(new NettyCompactObjectSerializer().serialize(new UserSerializable("周杰伦", 42)));
        System.out.println(j1);
        // 前四个字节是数据大小
        byte[] bytes = {0, 0, 0, -124};
        System.out.println(my_bb_to_int_le(bytes));

    }

    public static int my_bb_to_int_le(byte[] byteBarray) {
        return ByteBuffer.wrap(byteBarray).order(ByteOrder.BIG_ENDIAN).getInt();
    }
}