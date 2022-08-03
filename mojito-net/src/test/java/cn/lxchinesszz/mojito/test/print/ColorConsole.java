package cn.lxchinesszz.mojito.test.print;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;


/**
 * @author liuxin
 * 2022/4/30 18:05
 */
@Slf4j
public class ColorConsole {

    static {
        log.info("ColorConsole init");
    }


    public static String color(String text, AnsiColor color) {
        AnsiOutput.setEnabled(AnsiOutput.Enabled.ALWAYS);
        return AnsiOutput.toString(color, text, AnsiColor.DEFAULT, AnsiStyle.BOLD);
    }

    public static String color(String text) {
        return color(text, AnsiColor.BRIGHT_GREEN);
    }

    public static void colorPrintln(String descFormat, Object... args) {
        System.out.println(color(MessageFormatter.arrayFormat(descFormat, args).getMessage()));
    }

    public static void colorPrintln(AnsiColor color, String descFormat, Object... args) {
        System.out.println(color(MessageFormatter.arrayFormat(descFormat, args).getMessage(), color));
    }

}
