package com.test;

import com.itextpdf.kernel.pdf.*;
import org.dom4j.DocumentException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by zhaolisong on 11/04/2017.
 */
public class Example_61 {

    public static void main(String[] args) throws IOException, DocumentException, NoSuchFieldException {
//        java.util.Collections.UnmodifiableMap tmpObj = FilterHandlers.getDefaultFilterHandlers();
//        System.out.println(tmpObj.getClass());
//        Field map = tmpObj.getClass().getField("m");

//        PdfReader reader = new PdfReader("/Users/zhaolisong/Downloads/幸福基石报告_(170307).pdf");
//        PdfReader reader = new PdfReader("/Users/zhaolisong/Desktop/Example_TEST.pdf");
//        PdfReader reader = new PdfReader("/Users/zhaolisong/Downloads/page_one_tixing.pdf");
//        PdfReader reader = new PdfReader("/Users/zhaolisong/Downloads/page_one_just_1_image.pdf");
        PdfReader reader = new PdfReader("/Users/zhaolisong/Desktop/AABB.pdf");
        PdfDocument doc = new PdfDocument(reader);
        PdfPage page = doc.getPage(1);
        page.getPageSize();
        List<PdfIndirectReference> indirectRefs = doc.listIndirectReferences();



        File             file = new File("/Users/zhaolisong/Desktop/pdf_decode_data.txt");
        FileOutputStream out  = new FileOutputStream(file);
        byte[] decoded = null;
        for (PdfIndirectReference tmp : indirectRefs) {
            PdfObject obj = tmp.getRefersTo();
            if (null!=obj && obj.isStream()) {
                decoded = (tmp.getObjNumber() + " " + tmp.getGenNumber() + " R\r\n").getBytes();
                out.write(decoded);
                out.flush();
                if (!obj.toString().contains("DCTDecode")) {
                    decoded = ((PdfStream) obj).getBytes(true);
                    out.write(decoded);
                    out.flush();

                    decoded = ("--------------------------\r\n\r\n------------------------").getBytes();
                    out.write(decoded);
                    out.flush();
                }
                else {
                    decoded = ("need DCTDecode--------------------------\r\n\r\n------------------------").getBytes();
                    out.write(decoded);
                    out.flush();
                }
            }
        }

        out.close();
    }
}
