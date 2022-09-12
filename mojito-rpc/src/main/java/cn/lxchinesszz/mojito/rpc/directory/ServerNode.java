package cn.lxchinesszz.mojito.rpc.directory;

/**
 * @author liuxin
 * 2022/9/7 22:17
 */
public class ServerNode {

    private final String host;

    private final Integer port;

    private final Integer weight;

    public ServerNode(String host, Integer port) {
        this(host, port, 0);
    }

    public ServerNode(String host, Integer port, Integer weight) {
        this.host = host;
        this.port = port;
        this.weight = weight;
    }

    public String getHost() {
        return host;
    }

    public Integer getWeight() {
        return weight;
    }

    public Integer getPort() {
        return port;
    }

    public static ServerNode serverNode(String host, Integer port) {
        return new ServerNode(host, port);
    }

    public static ServerNode serverNode(String host, Integer port, Integer weight) {
        return new ServerNode(host, port, weight);
    }
}
