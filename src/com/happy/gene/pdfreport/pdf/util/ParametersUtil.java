package com.happy.gene.pdfreport.pdf.util;

import com.happy.gene.pdfreport.pdf.data.Parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaolisong on 20/04/2017.
 */
public class ParametersUtil {

    private ParametersUtil() {}

    public static ParametersUtil getInstance() {
        return new ParametersUtil();
    }

    public String replaceParameter(String value, Map<String, String> parameters) {
        if (null==value || null==parameters || parameters.isEmpty()) {
            return value;
        }
        if (value.indexOf('$') < 0) {
            return value;
        }
        List<String> paramKey = getParameterKey(value);
        if (null==paramKey) {
            return value;
        }

        for (int i = 0; i < paramKey.size(); i ++) {
            String tmpParamKey = paramKey.get(i);
            Object tmpParamValue = parameters.get(tmpParamKey.substring(1));
            if (null!=tmpParamValue) {
                if (tmpParamValue instanceof Parameter) {
                    value = value.replace(tmpParamKey, ((Parameter) tmpParamValue).getValue());
                }
                if (tmpParamValue instanceof String) {
                    value = value.replace(tmpParamKey, (String)tmpParamValue);
                }
            }
        }

        return value;
    }

    public List<String> getParameterName(String value) {
        List<String> paramKey = getParameterKey(value);
        if (null==paramKey) {
            return new ArrayList<>();
        }

        List<String> paramName = new ArrayList<>();
        for (int i = 0; i < paramKey.size(); i ++) {
            String tmpParamKey = paramKey.get(i);
            paramName.add(tmpParamKey.substring(1));
        }

        return paramName;
    }

    private List<String> getParameterKey(String value) {
        if (null==value || value.isEmpty()) {
            return null;
        }
        if (value.indexOf('$') < 0) {
            return null;
        }

        List<String> paramKey = new ArrayList<>();
        StringBuilder paramBuff  = new StringBuilder();

        char[] charArr = value.toCharArray();
        for (int i = 0; i < charArr.length; i ++) {
            char tmpChar = charArr[i];
            if (tmpChar=='$') {
                if (paramBuff.length()==0) {
                    paramBuff.append(tmpChar);
                }
                else {
                    paramKey.add(paramBuff.toString());
                    paramBuff.setLength(0);
                    paramBuff.append(tmpChar);
                }
            }
            else {
                if (('0' <= tmpChar && tmpChar <= '9') ||
                        ('a' <= tmpChar && tmpChar <= 'z') ||
                        ('A' <= tmpChar && tmpChar <= 'Z')) {
                    paramBuff.append(tmpChar);
                } else {
                    if (paramBuff.length()<=1) {
                        paramBuff.setLength(0);
                        continue;
                    }
                    else {
                        if ('$'==paramBuff.charAt(0)) {
                            paramKey.add(paramBuff.toString());
                        }
                        paramBuff.setLength(0);
                    }
                }
            }
        }
        if (0!=paramBuff.length()) {
            paramKey.add(paramBuff.toString());
            paramBuff.setLength(0);
        }

        return paramKey;
    }

    public String replaceParameter(String value) {
        XmlDataCache dataCache = XmlDataCache.getInstance();
        Map<String, String> parameters = (Map<String, String>)dataCache.getParameters(XmlDataCache.TOP_KEY_PARAMETER);
        return replaceParameter(value, parameters);
    }

    public Float[] parseFloatArray(String columnWidth) {
        String[] split = (null==columnWidth || "".equalsIgnoreCase(columnWidth)) ? null : columnWidth.split(" ");
        if (null==split) {
            return null;
        }
        Float[] columns = new Float[split.length];
        for (int i = 0; i < split.length; i ++) {
            try { columns[i] = Float.parseFloat(split[i]); } catch (Exception ex) {columns[i] = null;}
        }
        return columns;
    }

    public Float parseFloat(String columnWidth, Float defaultVal) {
        columnWidth = (null==columnWidth || "".equalsIgnoreCase(columnWidth)) ? null : columnWidth.trim();
        if (null==columnWidth) {
            return defaultVal;
        }
        try { return Float.parseFloat(columnWidth); }
        catch (Exception ex) { return defaultVal; }
    }

    public String[] parseStringArray(String columnWidth) {
        String[] split = (null==columnWidth || "".equalsIgnoreCase(columnWidth)) ? null : columnWidth.split(" ");
        if (null==split) {
            return null;
        }
        return split;
    }
}
