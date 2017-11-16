package com.happy.gene.utility;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhaolisong on 31/05/2017.
 */
public class NumberUtil {

    public static final String MongoID = "[[0-9|a-z|A-Z]+,]*[0-9|a-z|A-Z]+";
    public static final String IDS = "[[-]{0,1}[0-9]+,]*[-]{0,1}[0-9]+";
    public static final String[] CN = new String[]{"零","一","二","三","四","五","六","七","八","九","十"};

    public static NumberUtil newInstance() {
        return new NumberUtil();
    }

    public  String numList2String(List ids) {
        if (null==ids||ids.isEmpty()) {
            return "";
        }
        else if (ids.size()==1) {
            return ""+ids.get(0);
        }
        else {
            StringBuilder strIds = new StringBuilder("");
            strIds.append(ids.get(0));
            for (int i = 1; i < ids.size(); i++) {
                strIds.append(",").append(ids.get(i));
            }
            return strIds.toString();
        }
    }

    public boolean isIds(String ids) {
        if (ids instanceof String) {
            if (ids.contains(".")) {
                ids = ids.replace(".", "");
            }
            return ids.matches(IDS);
        }
        return false;
    }

    public List<String> parseMongoStringIds(String ids) {
        if (!ids.matches(MongoID)) {
            return new ArrayList();
        }

        String[] strArray  = ids.split(",");
        return Arrays.asList(strArray);
    }

    public List<Integer> parseIntIds(String ids) {
        if (!isIds(ids)) {
            return new ArrayList();
        }

        String[]      strArray  = ids.split(",");
        List<Integer> recordIds = new ArrayList<>();
        try {
            for (String tmp : strArray) {
                Integer id = Integer.parseInt(tmp);
                recordIds.add(id);
            }
        }
        catch (Exception ex) {
            recordIds.clear();
        }
        return recordIds;
    }

    public List<Long> parseLongIds(String ids) {
        if (!isIds(ids)) {
            return new ArrayList();
        }

        String[]   strArray  = ids.split(",");
        List<Long> recordIds = new ArrayList<>();
        try {
            for (String tmp : strArray) {
                Long id = Long.parseLong(tmp);
                recordIds.add(id);
            }
        }
        catch (Exception ex) {
            recordIds.clear();
        }
        return recordIds;
    }

    public Integer parseInt(String strInt) {
        if (null==strInt) { return null; }
        if (strInt.indexOf('.') >= 0) { strInt = strInt.substring(0, strInt.indexOf('.')); }
        if (!isIds(strInt)) {
            return null;
        }
        try {
            Integer id = Integer.parseInt(strInt);
            return id;
        }
        catch (Exception ex) {
        }
        return null;
    }

    public Long parseLong(String strLong) {
        if (!isIds(strLong)) {
            return null;
        }
        try {
            Long id = Long.parseLong(strLong);
            return id;
        }
        catch (Exception ex) {
        }
        return null;
    }

    public String parsePrice(int price) {
        boolean negative = price<0;
        price = negative ? -price : price;

        int prefix = (price/100);
        int suffix = (price%100);
        String strPrice = prefix +".";
        if (suffix<10) {
            strPrice = strPrice+"0"+suffix;
        }
        else {
            strPrice = strPrice+suffix;
        }

        strPrice = negative ? ("-"+strPrice) : strPrice;
        return strPrice;
    }

    public Double parseDouble(String strDouble) {
        try {
            return Double.parseDouble(strDouble);
        }
        catch (Exception ex) {
            return null;
        }
    }
    public Double parseScientificDouble(String strDouble) {
        try {
            BigDecimal decimal = new BigDecimal(strDouble);
            return decimal.doubleValue();
        }
        catch (Exception ex) {
            return  null;
        }

    }

    public String parseNumber(double src, int dot) {
        String res = new BigDecimal(src+"").toString();
        int dotIndex = res.indexOf('.');
        if (dotIndex >= 0 && dotIndex + dot + 1 <res.length() && dotIndex + dot >= 0) {
            if (dot <= 0) {
                res = res.substring(0, dotIndex + dot);
                for (int i = 0; i < -dot; i ++) {
                    res += "0";
                }
            }
            else {
                res = res.substring(0, dotIndex + dot + 1);
            }
        }
        return res;
    }

    public String getCnInt(int number, int start) {
        int delta = number - start + 1;
        String cn = delta < 0 ? "负" : "";
        if (delta > 100) { return "太大"; }
        if (delta < -100) { return "太小"; }

        delta = delta < 0 ? -delta : delta;

        int shiWei = (delta / 10) % 10;
        int geWei  = delta % 10;
        if (shiWei>0) {
            if (shiWei>1) {
                cn += CN[shiWei];
            }
            cn += CN[10];
        }
        if (geWei>0) {
            cn += CN[geWei];
        }
        else if (shiWei<=0) {
            cn = CN[0];
        }

        return cn;
    }

    public static void main(String[] args) {

        System.out.println("223365754334abcdefggi7ewff,fdsarjei3432r".matches(MongoID));
    }
}
