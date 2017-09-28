package com.happy.gene.utility;


import java.awt.*;

/**
 * Created by zhaolisong on 18/04/2017.
 */
public class ColorUtil {

    private ColorUtil() {}

    public static ColorUtil newInstance() {
        return new ColorUtil();
    }

    public Color parseColor(String color, Color defaultColor) {
        if (null==color || "".equalsIgnoreCase(color.trim())) {
            return defaultColor;
        }
        color = color.trim().toUpperCase();
        if (color.startsWith("#")) {
            int intColor = 0xffffff;
            try { intColor = Integer.parseInt(color.substring(1), 16); }
            catch (Exception ex) {
                return new Color(0, 0, 0);
            }
            return new Color(
                    (intColor >> 16) & 0xFF,
                    (intColor >> 8)  & 0xFF,
                    (intColor >> 0)  & 0xFF
            );
        }
        if (null!=defaultColor) {
            return defaultColor;
        }
        return new Color(0, 0, 0);
    }

    public String getRandomColor() {
        Double random = Math.random() * 0xffffff;
        String resColor = "#"+Integer.toHexString(random.intValue());
        return resColor;
    }

    public String parseColor(byte[] rgb) {
        if (null==rgb || rgb.length<3) { return "#000000"; }
        int color = (
                ((rgb[0] & 0xFF) << 16) |
                ((rgb[1] & 0xFF) << 8) |
                (rgb[2] & 0xFF)
        );
        String resColor = Integer.toHexString(color);
        if (resColor.length()<6) {
            for (int i = 6-resColor.length(); i > 0; i --) {
                resColor = "0" + resColor;
            }
        }
        resColor = "#"+resColor;
        return resColor;
    }
}
