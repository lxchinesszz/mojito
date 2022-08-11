package cn.lxchinesszz.mojito.utils;

/**
 * @author liuxin
 * 2022/8/6 13:11
 */
public enum EPlatform {

    Linux("Linux"),

    Mac_OS("Mac OS"),

    Mac_OS_X("Mac OS X"),

    Windows("windows"),

    Others("Others");

    private String systemName;

    EPlatform(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
}
