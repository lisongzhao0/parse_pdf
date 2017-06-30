package com.cooltoo.happy.gene.pdf.template.util.log;

import java.util.Date;

/**
 * Created by zhaolisong on 12/04/2017.
 */
public final class Logger {

    public static void error(String msg, String ... args) {
        System.err.println(replaceBrace("ERR ---- " + new Date() + " ---- " + msg, args));
    }

    public static void info(String msg, String ... args) {
        System.out.println(replaceBrace("INF ---- " + new Date() + " ---- " + msg, args));
    }

    private static String replaceBrace(String msg, String ... args) {
        if (null==msg || msg.length()==0) {
            return msg;
        }

        StringBuilder newMsg = new StringBuilder();
        char[] charArray   = msg.toCharArray();
        int index = 0;

        for (int i = 0; i < charArray.length; i ++) {
            char ch = charArray[i];
            if (ch == '{' && (i+1<charArray.length && charArray[i+1] == '}')) {
                newMsg.append(index<args.length ? args[index] : "{}");
                index ++;
                i ++;
                continue;
            }
            newMsg.append(ch);
        }
        return newMsg.toString();
    }
}
