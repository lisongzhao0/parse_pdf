package com.test;
import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;


/**
 * @author zhangchaochao
 * @date 2015-12-4上午10:30:59
 */
public class MSWordPoi4
{

    /**
     * @param args
     */
    public static void main(String[] args) throws IOException
    {
        Map<String, String> map = new HashMap<>();
        map.put("${batchNumber}", "HD20-2");
        map.put("${sampleRecvDate}", "20180102");
        map.put("${sampleNumber}", "32");
        map.put("${reportNumber}", "2107118-YDU-93837");
//        map.put("${item1.numberStudent}", "编号002");
//        map.put("${item1.sex}", "男2");
//        map.put("${item1.age}", "19");
        String srcPath = "/Users/zhaolisong/Desktop/projects/cooltoo/parse_pdf/templates/template_office_word/检验报告.docx";
        readwriteWord(srcPath, map);
    }

    /**
     * 实现对word读取和修改操作
     *
     * @param filePath
     *            word模板路径和名称
     * @param map
     *            待填充的数据，从数据库读取
     */
    public static void readwriteWord(String filePath, Map<String, String> map) {

        // 读取word模板
        InputStream in = null;
        XWPFDocument hdt = null;

        try { in = new FileInputStream(new File(filePath)); }
        catch (FileNotFoundException e1) { e1.printStackTrace(); }
        try { hdt = new XWPFDocument(in); }
        catch (IOException e1) { e1.printStackTrace(); }


        //读取word页眉内容
        XWPFHeaderFooterPolicy harderRange= hdt.getHeaderFooterPolicy();
        //替换word页眉内容
        for(Map.Entry<String, String> entry : map.entrySet()){
            XWPFHeader header = harderRange.getDefaultHeader();
            replaceParagraph(header.getParagraphs(), map);
        }

        //读取word文本内容
        replaceParagraph(hdt.getParagraphs(), map);
        replaceTable(hdt.getTables(), map);

        Iterator<XWPFTable> tableIt = hdt.getTablesIterator();
        //迭代文档中的表格
        int ii = 0;
        while (tableIt.hasNext()) {
            XWPFTable tb = tableIt.next();
            ii++;
            System.out.println("第"+ii+"个表格数据...................");
            //迭代行，默认从0开始
            for (int i = 0; i < tb.getNumberOfRows(); i++) {
                XWPFTableRow tr = tb.getRow(i);
                //只读前8行，标题部分
                if(i >=8) break;
                //迭代列，默认从0开始
                for (int j = 0; j < tr.getTableCells().size(); j++) {
                    XWPFTableCell td = tr.getCell(j); //取得单元格
                    //取得单元格的内容
                    for(int k=0;k<td.getParagraphs().size();k++){
                        XWPFParagraph para =td.getParagraphs().get(k);
                        String s = para.getText();
                        System.out.println(s);
                    } //end for
                } //end for
            } //end for


            if (ii==2) {
                //获取表头
                for (int ir = 0; ir<36; ir++) {
                    CTRow tbRow = tb.getCTTbl().addNewTr();
                    XWPFTableRow tabRow = new XWPFTableRow(tbRow, tb);
                    tabRow.setHeight(380);
                    XWPFTableCell cell = tabRow.addNewTableCell();
                    if (ir % 6 == 0) {
                        cell.setText("样本编码："+"7654376");
                        cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
                        cell.getCTTc().getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
                    }
                    else {
                        cell.setText("rs33543276543767");
                    }
                    cell = tabRow.addNewTableCell();
                    if (ir % 6 == 0) {
                        cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
                    }
                    else {
                        cell.setText("SG");
                    }
                    cell = tabRow.addNewTableCell();
                    if (ir % 6 == 0) {
                        cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
                    }
                    else {
                        cell.setText("rs432543767");
                    }
                    cell = tabRow.addNewTableCell();
                    if (ir % 6 == 0) {
                        cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
                    }
                    else {
                        cell.setText("SG");
                    }
                    cell = tabRow.addNewTableCell();
                    if (ir % 6 == 0) {
                        cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
                    }
                    else {
                        cell.setText("rs335433767");
                    }
                    cell = tabRow.addNewTableCell();
                    if (ir % 6 == 0) {
                        cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
                    }
                    else {
                        cell.setText("SG");
                    }
                }
            }
        } //end while


        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        String fileName = "" + System.currentTimeMillis();
        fileName += ".docx";
        FileOutputStream out = null;
        try
        {
            out = new FileOutputStream("/Users/zhaolisong/Desktop/" + fileName, true);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        try
        {
            hdt.write(ostream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        // 输出字节流
        try
        {
            out.write(ostream.toByteArray());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            out.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            ostream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static void replaceParagraph(List<XWPFParagraph> paragraphs, Map<String, String> map) {
        if (null==paragraphs || paragraphs.isEmpty()) { return; }
        if (null==map || map.isEmpty()) { return; }
        // 替换文本内容
        for (Map.Entry<String, String> entry : map.entrySet()) {
            for (XWPFParagraph paragraph : paragraphs) {
                replaceParagraph(paragraph, entry.getKey(), entry.getValue());
            }
        }
    }

    private static void replaceTable(List<XWPFTable> tables, Map<String, String> map) {
        if (null==tables || tables.isEmpty()) { return; }
        if (null==map || map.isEmpty()) { return; }

        //表格内容替换添加
        for (XWPFTable table : tables) {
            int rowS = table.getNumberOfRows();
            for(int i = 0;i < rowS;i++){
                XWPFTableRow row = table.getRow(i);
                List<XWPFTableCell> cells =  row.getTableCells();
                for (XWPFTableCell cell : cells){
                    for(Map.Entry<String, String> e : map.entrySet()){
                        String text = cell.getText();
                        if (text.contains(e.getKey())){
                            List<XWPFParagraph> cellCnt = cell.getParagraphs();
                            for (XWPFParagraph cc : cellCnt) {
                                replaceParagraph(cc, e.getKey(), e.getValue());
                            }
                        }
                    }
                }
            }
        }
    }

    private static void replaceParagraph(XWPFParagraph paragraph, String key, String value) {
        if (null==paragraph) { return; }
        if (null==key || "".equals(key)) { return; }
        if (null==value) { return; }

        List<XWPFRun> runs = paragraph.getRuns();
        if (null==runs || runs.isEmpty()) { return; }

        String text = paragraph.getText();
        if (null==text || !text.contains(key)) { return; }


        text = text.replace(key, value);
        for (int i = runs.size()-1; i >= 1; i --) {
            paragraph.removeRun(i);
        }

        runs = paragraph.getRuns();
        runs.get(0).setText(text, 0);
    }
}