package cn.lxchinesszz.mojito.test.print;

/**
 * @author liuxin
 * 2022/7/30 11:37
 */
public enum AnsiStyle implements AnsiElement {
    NORMAL("0"),
    BOLD("1"),
    FAINT("2"),
    ITALIC("3"),
    UNDERLINE("4");

    private final String code;

    private AnsiStyle(String code) {
        this.code = code;
    }

    public String toString() {
        return this.code;
    }
}

