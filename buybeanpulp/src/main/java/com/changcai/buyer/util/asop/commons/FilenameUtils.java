package com.changcai.buyer.util.asop.commons;

import java.io.File;

/**
 * Created by liuxingwei on 2017/1/24.
 */

public class FilenameUtils {
    public static final char EXTENSION_SEPARATOR = '.';
    public static final String EXTENSION_SEPARATOR_STR = (new Character('.')).toString();
    private static final char UNIX_SEPARATOR = '/';
    private static final char WINDOWS_SEPARATOR = '\\';
    private static final char SYSTEM_SEPARATOR;

    static {
        SYSTEM_SEPARATOR = File.separatorChar;
    }

    public FilenameUtils() {
    }

    static boolean isSystemWindows() {
        return SYSTEM_SEPARATOR == 92;
    }

    private static boolean isSeparator(char ch) {
        return ch == 47 || ch == 92;
    }

    public static int getPrefixLength(String filename) {
        if(filename == null) {
            return -1;
        } else {
            int len = filename.length();
            if(len == 0) {
                return 0;
            } else {
                char ch0 = filename.charAt(0);
                if(ch0 == 58) {
                    return -1;
                } else if(len == 1) {
                    return ch0 == 126?2:(isSeparator(ch0)?1:0);
                } else {
                    int posUnix;
                    if(ch0 == 126) {
                        int ch11 = filename.indexOf(47, 1);
                        posUnix = filename.indexOf(92, 1);
                        if(ch11 == -1 && posUnix == -1) {
                            return len + 1;
                        } else {
                            ch11 = ch11 == -1?posUnix:ch11;
                            posUnix = posUnix == -1?ch11:posUnix;
                            return Math.min(ch11, posUnix) + 1;
                        }
                    } else {
                        char ch1 = filename.charAt(1);
                        if(ch1 == 58) {
                            ch0 = Character.toUpperCase(ch0);
                            return ch0 >= 65 && ch0 <= 90?(len != 2 && isSeparator(filename.charAt(2))?3:2):-1;
                        } else if(isSeparator(ch0) && isSeparator(ch1)) {
                            posUnix = filename.indexOf(47, 2);
                            int posWin = filename.indexOf(92, 2);
                            if((posUnix != -1 || posWin != -1) && posUnix != 2 && posWin != 2) {
                                posUnix = posUnix == -1?posWin:posUnix;
                                posWin = posWin == -1?posUnix:posWin;
                                return Math.min(posUnix, posWin) + 1;
                            } else {
                                return -1;
                            }
                        } else {
                            return isSeparator(ch0)?1:0;
                        }
                    }
                }
            }
        }
    }
}
