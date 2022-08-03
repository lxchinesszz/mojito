package cn.lxchinesszz.mojito.test.print;

import java.util.Locale;

/**
 * @author liuxin
 * 2022/7/30 11:36
 */

public abstract class AnsiOutput {
    private static final String ENCODE_JOIN = ";";
    private static Enabled enabled;
    private static Boolean consoleAvailable;
    private static Boolean ansiCapable;
    private static final String OPERATING_SYSTEM_NAME;
    private static final String ENCODE_START = "\u001b[";
    private static final String ENCODE_END = "m";
    private static final String RESET;

    public AnsiOutput() {
    }

    public static void setEnabled(Enabled enabled) {
        AnsiOutput.enabled = enabled;
    }

    public static Enabled getEnabled() {
        return enabled;
    }

    public static void setConsoleAvailable(Boolean consoleAvailable) {
        AnsiOutput.consoleAvailable = consoleAvailable;
    }

    public static String encode(AnsiElement element) {
        return isEnabled() ? "\u001b[" + element + "m" : "";
    }

    public static String toString(Object... elements) {
        StringBuilder sb = new StringBuilder();
        if (isEnabled()) {
            buildEnabled(sb, elements);
        } else {
            buildDisabled(sb, elements);
        }

        return sb.toString();
    }

    private static void buildEnabled(StringBuilder sb, Object[] elements) {
        boolean writingAnsi = false;
        boolean containsEncoding = false;
        Object[] var4 = elements;
        int var5 = elements.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Object element = var4[var6];
            if (element instanceof AnsiElement) {
                containsEncoding = true;
                if (!writingAnsi) {
                    sb.append("\u001b[");
                    writingAnsi = true;
                } else {
                    sb.append(";");
                }
            } else if (writingAnsi) {
                sb.append("m");
                writingAnsi = false;
            }

            sb.append(element);
        }

        if (containsEncoding) {
            sb.append(writingAnsi ? ";" : "\u001b[");
            sb.append(RESET);
            sb.append("m");
        }

    }

    private static void buildDisabled(StringBuilder sb, Object[] elements) {
        Object[] var2 = elements;
        int var3 = elements.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Object element = var2[var4];
            if (!(element instanceof AnsiElement) && element != null) {
                sb.append(element);
            }
        }

    }

    private static boolean isEnabled() {
        if (enabled == AnsiOutput.Enabled.DETECT) {
            if (ansiCapable == null) {
                ansiCapable = detectIfAnsiCapable();
            }

            return ansiCapable;
        } else {
            return enabled == AnsiOutput.Enabled.ALWAYS;
        }
    }

    private static boolean detectIfAnsiCapable() {
        try {
            if (Boolean.FALSE.equals(consoleAvailable)) {
                return false;
            } else if (consoleAvailable == null && System.console() == null) {
                return false;
            } else {
                return !OPERATING_SYSTEM_NAME.contains("win");
            }
        } catch (Throwable var1) {
            return false;
        }
    }

    static {
        enabled = AnsiOutput.Enabled.DETECT;
        OPERATING_SYSTEM_NAME = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        RESET = "0;" + AnsiColor.DEFAULT;
    }

    public static enum Enabled {
        DETECT,
        ALWAYS,
        NEVER;

        private Enabled() {
        }
    }
}

