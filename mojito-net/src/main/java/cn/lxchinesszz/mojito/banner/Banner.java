package cn.lxchinesszz.mojito.banner;


import com.hanframework.kit.text.Ansi;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSource;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

import static com.hanframework.kit.text.Ansi.ansi;

/**
 * @author liuxin
 * 2020-01-04 23:13
 */
public final class Banner {

    private static final String NAME = "Mojito";


    private static final String DEFAULT_BANNER = " ___      ___     ______      ___  __  ___________  ______    \n" +
            "|\"  \\    /\"  |   /    \" \\    |\"  ||\" \\(\"     _   \")/    \" \\   \n" +
            " \\   \\  //   |  // ____  \\   ||  |||  |)__/  \\\\__/// ____  \\  \n" +
            " /\\\\  \\/.    | /  /    ) :)  |:  ||:  |   \\\\_ /  /  /    ) :) \n" +
            "|: \\.        |(: (____/ //___|  / |.  |   |.  | (: (____/ //  \n" +
            "|.  \\    /:  | \\        //  :|_/ )/\\  |\\  \\:  |  \\        /   \n" +
            "|___|\\__/|___|  \\\"_____/(_______/(__\\_|_)  \\__|   \\\"_____/   ";

    public static void print() {
        printVersion();
    }


    public static String print(String text, Ansi.Color color) {
        return ansi().eraseScreen().fg(color).a(text).reset().toString();
    }

    private static void printVersion() {
        System.out.println();
        PrintStream printStream = System.out;
        String version = Banner.getVersion();
        version = version != null ? " (v" + version + ")" : "";
        StringBuilder padding = new StringBuilder();
        while (padding.length() < 42 - (version.length() + NAME.length())) {
            padding.append(" ");
        }
        printStream.println(print(DEFAULT_BANNER, Ansi.Color.GREEN));
        printStream.println();
        printStream.println(print(" :: Tomato :: " + padding, Ansi.Color.GREEN));
        printStream.println();
        printStream.println(print("麻烦给我的爱人来一杯Mojito,我喜欢阅读她微醺时的眼眸！", Ansi.Color.GREEN));
        printStream.println();
    }


    /**
     * 获取组件版本号
     *
     * @return String
     */
    private static String getVersion() {
        String implementationVersion = Banner.class.getPackage().getImplementationVersion();
        if (implementationVersion != null) {
            return implementationVersion;
        } else {
            CodeSource codeSource = Banner.class.getProtectionDomain().getCodeSource();
            if (codeSource == null) {
                return null;
            } else {
                URL codeSourceLocation = codeSource.getLocation();

                try {
                    URLConnection connection = codeSourceLocation.openConnection();
                    if (connection instanceof JarURLConnection) {
                        return getImplementationVersion(((JarURLConnection) connection).getJarFile());
                    } else {
                        JarFile jarFile = new JarFile(new File(codeSourceLocation.toURI()));
                        Throwable var5 = null;

                        String var6;
                        try {
                            var6 = getImplementationVersion(jarFile);
                        } catch (Throwable var16) {
                            var5 = var16;
                            throw var16;
                        } finally {
                            if (jarFile != null) {
                                if (var5 != null) {
                                    try {
                                        jarFile.close();
                                    } catch (Throwable var15) {
                                        var5.addSuppressed(var15);
                                    }
                                } else {
                                    jarFile.close();
                                }
                            }

                        }

                        return var6;
                    }
                } catch (Exception var18) {
                    return null;
                }
            }
        }
    }

    private static String getImplementationVersion(JarFile jarFile) throws IOException {
        return jarFile.getManifest().getMainAttributes().getValue(Attributes.Name.IMPLEMENTATION_VERSION);
    }

}
