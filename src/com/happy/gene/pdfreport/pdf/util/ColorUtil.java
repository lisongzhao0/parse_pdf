package com.happy.gene.pdfreport.pdf.util;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;

/**
 * Created by zhaolisong on 18/04/2017.
 */
public class ColorUtil {

    private ColorUtil() {}

    public static ColorUtil getInstance() {
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
                return new DeviceRgb(0, 0, 0);
            }
            return new DeviceRgb(
                    (intColor >> 16) & 0xFF,
                    (intColor >> 8)  & 0xFF,
                    (intColor >> 0)  & 0xFF
            );
        }
        if (null!=defaultColor) {
            return defaultColor;
        }
        return new DeviceRgb(0, 0, 0);
    }

    public String getRandomColor() {
        Double random = Math.random() * 0xffffff;
        String resColor = "#"+Integer.toHexString(random.intValue());
        return resColor;
    }

    public String parseColor(byte[] rgb) {
        if (null==rgb || rgb.length<3) { return "#000000"; }
        String resColor = "#"+Integer.toHexString(rgb[0])+Integer.toHexString(rgb[1])+Integer.toHexString(rgb[2]);
        return resColor;
    }
}
