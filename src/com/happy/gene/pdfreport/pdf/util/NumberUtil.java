package com.happy.gene.pdfreport.pdf.util;

/**
 * Created by zhaolisong on 02/05/2017.
 */
public class NumberUtil {

    public static final String IDS = "[[-]{0,1}[0-9]+,]*[-]{0,1}[0-9]+";

    public static NumberUtil newInstance() {
        return new NumberUtil();
    }

    private NumberUtil() {}

    public boolean isInteger(String strInt) {
        return strInt instanceof String ? strInt.matches(IDS) : false;
    }

    public Integer parseInteger(String strInt) {
        if (isInteger(strInt)) {
            return Integer.parseInt(strInt);
        }
        return null;
    }
}
