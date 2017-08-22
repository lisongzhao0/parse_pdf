package com.happy.gene.pdfreport.pdf.util;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceCmyk;
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
        else if(color.startsWith("CMYK")) {
            color = color.replace("CMYK", "").trim();
            String[] cmyk = color.split("[ ,]+");
            Integer[] iCMYK = new Integer[4];
            for (int i = 0; i < cmyk.length; i ++) {
                try { iCMYK[i] = Integer.parseInt(cmyk[i]); } catch (Exception ex) { iCMYK[i] = 0; }
            }
            return new DeviceCmyk(
                    null==iCMYK[0] ? 0 : iCMYK[0],
                    null==iCMYK[1] ? 0 : iCMYK[1],
                    null==iCMYK[2] ? 0 : iCMYK[2],
                    null==iCMYK[3] ? 0 : iCMYK[3]
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
        int color = (
                ((rgb[0] & 0xFF) << 16) |
                ((rgb[1] & 0xFF) << 8)  |
                (rgb[2]  & 0xFF)
        );
        String resColor = "#"+Integer.toHexString(color);
        return resColor;
    }

    public static void main(String[] args) {
        ColorUtil.getInstance().parseColor("cmyk", null);
    }
}
