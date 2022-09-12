package cn.lxchinesszz.mojito.rpc;

/**
 * @author liuxin
 * 2022/9/7 23:03
 */
public class User implements Person {
    private String name;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
