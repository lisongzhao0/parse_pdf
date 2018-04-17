package com.test;

import com.happy.gene.utility.CsvUtil;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CsvTest {
    public static void main(String[] args) throws IOException {
        CsvUtil csvCN = new CsvUtil("/Users/zhaolisong/Downloads/远盟全基因组报告/中英对照表.txt", '\t', Charset.forName("UTF-8"));
        boolean begin = false;
        Map<String, String> encn = new HashMap<>();
        for (;csvCN.readRecord();) {
            String[] array = csvCN.getValues();
            if (array.length==2 && "Disease".equals(array[0])) { begin = true; continue; }
            if (!begin) {continue;}

            encn.put(array[0].trim(), array[1].trim());
            for (String arg : array) {
                System.out.print(arg + "\t");
            }
            System.out.println();
        }


        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("/Users/zhaolisong/Downloads/远盟全基因组报告/Casual_loci.new.txt"));
        StringBuilder line = new StringBuilder();
        CsvUtil csvC = new CsvUtil("/Users/zhaolisong/Downloads/远盟全基因组报告/Casual_loci.txt", '\t', Charset.forName("UTF-8"));
        Set<String> notExist = new HashSet<>();
        for (;csvC.readRecord();) {
            String[] array = csvC.getValues();
            if ("Cancer".equals(array[0])) { begin = true; }
            if (!begin) {continue;}
            if ("FALSE".equalsIgnoreCase(array[array.length-1])) { continue; }
            if ("SNP".equalsIgnoreCase(array[array.length-1])) { continue; }
            if (!"Cancer".equals(array[0]) && !array[11].toLowerCase().contains("pathogenic")) {continue;}

            line.setLength(0);
            for (int i=0; i<array.length; i ++) {
                String val = array[i];
                if (i==0) {
                    if ("Cancer".equals(val)) {
                        line.append("CN").append("\t").append("Cancer");
                    }
                    else {
                        if ("Kideny Cancer".equals(val)) { val = "Renal cell carcinoma"; }
                        String cn = encn.get(val);
                        if (null==cn || "".equals(cn)) { notExist.add(val); System.out.println(val + " not exist"); }
                        line.append(null==cn?"":cn).append("\t").append(val);
                    }
                    continue;
                }
                line.append("\t").append(array[i]);
            }
            line.append("\n");
            bos.write(line.toString().getBytes("UTF-8"));
            bos.flush();
        }
        bos.close();

        System.out.println(notExist);
    }
}
