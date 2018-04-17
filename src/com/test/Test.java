package com.test;

/**
 * Created by zhaolisong on 24/04/2017.
 */
public class Test {
    public static void main(String[] args) {

//        String[] styles = parseStyleLines("<style >本服务的目标，是通过对您的全方位评估及后续跟踪</style>，帮助您达成对于肿瘤的精准预防。line_end" +
//                "精准预防 * 是一个前沿新兴的概念框架，其目标是通过将某种肿瘤发生的多种影响因素综合计算，定制肿瘤预防的个体化 方案。在精准预防中包含如下因素:line_end line_end" +
//                "<style font_size=\"\" font_color=\"\">(1)</style>个人基因组line_end" +
//                "<style font_size=\"\" font_color=\"\">(2)</style>年龄line_end" +
//                "<style font_size=\"\" font_color=\"\">(3)</style>性别line_end" +
//                "<style font_size=\"\" font_color=\"\">(4)</style>家族史，包括肿瘤的遗传倾向line_end" +
//                "<style font_size=\"\" font_color=\"\">(5)</style>生活方式，包括吸烟、饮酒、肥胖、运动等line_end" +
//                "<style font_size=\"\" font_color=\"\">(6)</style>生殖和医疗因素line_end" +
//                "<style font_size=\"\" font_color=\"\">(7)</style>致癌性的病原体感染line_end" +
//                "<style font_size=\"\" font_color=\"\">(8)</style>社会经济状况line_end" +
//                "<style font_size=\"\" font_color=\"\">(9)</style>地理因素line_end" +
//                "<style font_size=\"\" font_color=\"\">(10)</style>其他尚未知<style>因素");
//        for (String tmp : styles) {
//            System.out.println(tmp);
//        }
//        System.out.println(System.getenv());
//
//        System.out.println(Integer.MAX_VALUE);
//
        System.out.println("12345678901".matches("^[\\d]{11,11}$"));




        Object[][] dest  = new Object[3][3];
        Object[][] src   = new Object[][]{
            {"A", "B", "C"},
            {1, 2, 3},
            {null, null, true},
        };
        int destIdx = 0;
        for (int i=0; i<src.length; i ++) {
            if (null==src[i][0]) { continue; }
            System.arraycopy(src[i], 0, dest[destIdx], 0, 3);
            destIdx ++;
        }
        Object[][] dest2  = new Object[destIdx][3];
        System.arraycopy(dest, 0, dest2, 0, destIdx);
        System.out.println(destIdx);
        System.out.println(dest);
        src[0][0] = "aaa";
        System.out.println(dest);



        double start = 734.258;
        double step  = -17.822;
        double delta = 1.575;

        for (int i=0; i < 90; i++) {
            if (i < 10) {
                System.out.println("==0" + i + "==>" + (start + step * i + delta));
            }
            else {
                System.out.println("==" + i + "==>" + (start + step * i + delta));
            }
        }
    }
}
