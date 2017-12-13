package com.test;

import com.happy.gene.utility.DateUtil;
import com.itextpdf.forms.PdfPageFormCopier;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PdfPageMergeTest {

    public static void main(String[] args) throws IOException {

        String[] pdfs = {
                "/Users/zhaolisong/Desktop/projects/cooltoo/parse_pdf/templates/HD2_TX00011561_5a17b8674b28afb749ab8afb.pdf",
                "/Users/zhaolisong/Desktop/projects/cooltoo/parse_pdf/templates/HD2_TX00011561_5a17b8674b28afb749ab8afb.pdf",
                "/Users/zhaolisong/Desktop/projects/cooltoo/parse_pdf/templates/HD2_TX00011561_5a17b8674b28afb749ab8afb.pdf",
                "/Users/zhaolisong/Downloads/远盟/理化盖章页.pdf",
                "/Users/zhaolisong/Desktop/projects/cooltoo/parse_pdf/templates/HD2_TX00011561_5a17b8674b28afb749ab8afb.pdf",
        };
        getBatchPdfs(
                "报告编号：batchDate-YFS-batchNum", 801, new Date(),
                "第 pageNum 页，共 totalNum 页", pdfs.length,
                Arrays.asList(pdfs), 1, 1,
                "/Users/zhaolisong/Desktop/projects/cooltoo/parse_pdf/templates/fzltxh_gbk.ttf",
                "/Users/zhaolisong/Desktop/projects/cooltoo/parse_pdf/Example_TEST.pdf"
                );
        System.out.println(new Date());
    }

    private static String getBatchPdfs(String batchNumString, int batchNum, Date batchDate,
                                       String pageNumFormat, int totalPageSize,
                                       List<String> batchPdfPaths, int toPage, int fromPage,
                                       String fontPath, String batchPdfSavePath) {
        DateUtil dateUtil       = DateUtil.newInstance();
        String   datePrefix     = dateUtil.timeToString(batchDate, DateUtil.DATE_YYMMDD);
        if (null==batchNumString || "".equals(batchNumString.trim())) { batchNumString = ""; }
        if (null==datePrefix || "".equals(datePrefix.trim()))
        { batchNumString = batchNumString.replace("batchDate", ""); }
        else
        { batchNumString = batchNumString.replace("batchDate", datePrefix); }
        { batchNumString = batchNumString.replace("batchNum", ""+batchNum); }

        PdfFont  font           = null;
        try { font = PdfFontFactory.createFont(fontPath, PdfEncodings.IDENTITY_H, true); } catch (Exception ex) {}

        ByteArrayOutputStream   baos      = new ByteArrayOutputStream(64280);
        PdfWriter               pdfWriter = new PdfWriter(baos);
        PdfDocument             pdfDoc    = new PdfDocument(pdfWriter);
        pdfDoc.setDefaultPageSize(PageSize.A4);
        AddPageNumberCopier pageNumCopier = new AddPageNumberCopier(pdfDoc);
        pageNumCopier.setPageNumFormat(pageNumFormat);
        pageNumCopier.setTotalNum(totalPageSize+2);
        pageNumCopier.setFont(font);
        pageNumCopier.setBatchNum(batchNumString);


        List<Integer> pages = new ArrayList<>(toPage - fromPage);
        for (int pageNum = fromPage; pageNum <= toPage; pageNum++){
            pages.add(pageNum);
        }
        for (int i=0; i<totalPageSize; i++) {
            String      pdfPath = batchPdfPaths.get(i);
            PdfReader   reader  = null;
            try { reader = new PdfReader(pdfPath); } catch (Exception ex) {}

            PdfDocument from = null;
            if (null==reader) {
                pdfDoc.addNewPage();
                PdfPage lastPage = pdfDoc.getLastPage();
                pageNumCopier.setNumbers(lastPage);
            }
            else {
                from = new PdfDocument(reader);
                if (!pdfDoc.isTagged() && from.isTagged()) { pdfDoc.setTagged(); }
                if (!pdfDoc.hasOutlines() && from.hasOutlines()) { pdfDoc.initializeOutlines(); }

                pageNumCopier.setPageNum(i + 2);
                from.copyPagesTo(pages, pdfDoc, pageNumCopier);

                from.close();
                reader.setCloseStream(true);
                try { reader.close(); } catch (Exception ex) {}
            }
        }

        pdfDoc.close();
        try { baos.writeTo(new FileOutputStream(batchPdfSavePath)); }
        catch (Exception ex){}
        finally {
            try { if (null!=baos) { baos.close(); } } catch (Exception ex){}
        }

        return batchPdfSavePath;
    }

    public static class AddPageNumberCopier extends PdfPageFormCopier {
        private PdfDocument destPdf;
        private String      pageNumFormat = "pageNum/totalNum";
        private int         pageNum     = 0;
        private int         totalNum    = 0;
        private String      batchNum    = null;
        private PdfFont     font        = null;


        public AddPageNumberCopier(){}
        public AddPageNumberCopier(PdfDocument destPdf) { this.destPdf = destPdf; }

        public void setPageNumFormat(String pageNumFormat) {
            if (null==pageNumFormat || "".equals(pageNumFormat)) { return; }
            this.pageNumFormat = pageNumFormat;
        }
        public void setPageNum(int pageNum)   { this.pageNum = pageNum;   }
        public void setTotalNum(int totalNum) { this.totalNum = totalNum; }
        public void setBatchNum(String batchNum) { this.batchNum = batchNum; }
        public void setFont(PdfFont font) { this.font = font; }

        public void copy(PdfPage fromPage, PdfPage toPage) {
            super.copy(fromPage, toPage);
            setNumbers(toPage);
        }

        public void setNumbers(PdfPage toPage) {
            PdfCanvas under = new PdfCanvas(toPage);
            Canvas canvas = new Canvas(under, destPdf, destPdf.getDefaultPageSize());

            String pageFormat = null;
            pageFormat = this.pageNumFormat.replace("pageNum", ""+pageNum);
            pageFormat = pageFormat.replace("totalNum", ""+totalNum);

            Paragraph p = new Paragraph(pageFormat).setFontSize(9).setFontColor(Color.BLACK);
            if (null!=font) { p.setFont(font); }
            float x = toPage.getPageSize().getWidth() / 2;
            float y = 50;
            canvas.showTextAligned(p, x, y, 1, TextAlignment.CENTER, VerticalAlignment.MIDDLE, 0);

            p = new Paragraph(batchNum).setFontSize(13).setFontColor(Color.BLACK);
            if (null!=font) { p.setFont(font); }
            x = toPage.getPageSize().getWidth() * 0.6f;
            y = toPage.getPageSize().getHeight() - 50;
            canvas.showTextAligned(p, x, y, 1, TextAlignment.LEFT, VerticalAlignment.MIDDLE, 0);
        }
    }
}
