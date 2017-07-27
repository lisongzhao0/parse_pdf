package com.happy.gene.pdfreport.pdf.util;

/**
 * Created by zhaolisong on 26/07/2017.
 */
public class StringUtil {

    public static StringUtil newInstance() {
        return new StringUtil();
    }

    private StringUtil() {}

    public boolean isEmpty(String str) {
        return (null==str) || "".equals(str.trim());
    }
}
