package com;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zhaolisong on 18/10/2017.
 */
public class ReducePdfSize {
    public static void main(String[] args) throws IOException {
        PdfReader           reader      = new PdfReader(new FileInputStream("./input.pdf"));

        WriterProperties    writerProp  = new WriterProperties();
        writerProp.setFullCompressionMode(true);
        PdfWriter           writer      = new PdfWriter(new FileOutputStream("./output.pdf"), writerProp);

        PdfDocument         outDoc      = new PdfDocument(reader, writer);
        outDoc.close();
    }
}
