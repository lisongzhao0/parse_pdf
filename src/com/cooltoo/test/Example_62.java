package com.cooltoo.test;

//import org.apache.pdfbox.tools.PDFSplit;

import java.io.File;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;

/**
 * Created by zhaolisong on 13/04/2017.
 */
public class Example_62 {

    public static final String DEST = "/Users/zhaolisong/Desktop/projects/cooltoo/parse_pdf/rowspan_absolute_position.pdf";
    public static final String IMG = "/Users/zhaolisong/Desktop/projects/cooltoo/parse_pdf/templates/template001/info.png";

//    public static void main(String[] args) throws IOException {
//        PDFSplit.main(new String[]{"-startPage", "1", "-endPage", "1", "-outputPrefix", "/Users/zhaolisong/Downloads/page_one", "/Users/zhaolisong/Downloads/幸福基石报告_(170307).pdf"});
//}



    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new Example_62().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);

        Table table1 = new Table(new float[]{15, 20, 20});
        table1.setWidth(555);
        Cell header = new Cell(1, 3).add(new Paragraph("header"));
        table1.addHeaderCell(header);
        Cell cell = new Cell(1, 2).add(new Paragraph("{Month}"));
        cell.setHorizontalAlignment(HorizontalAlignment.LEFT);
        Image img = new Image(ImageDataFactory.create(IMG));
        img.scaleToFit(555f * 20f / 55f, 10000);
        Cell cell2 = new Cell(2, 1).add(img.setAutoScale(true));
        Cell cell3 = new Cell(1, 2).add(new Paragraph("Mr Fname Lname"));
        cell3.setHorizontalAlignment(HorizontalAlignment.LEFT);
        table1.addCell(cell);
        table1.addCell(cell2);
        table1.addCell(cell3);
        Cell cell4 = new Cell(1, 2).add(new Paragraph("{Month}"));
        Cell cell5 = new Cell(2, 1).add(img.setAutoScale(true));
        Cell cell6 = new Cell(1, 2).add(new Paragraph("Mr Fname Lname"));
        table1.addCell(cell4);
        table1.addCell(cell5);
        table1.addCell(cell6);
        cell4 = new Cell(1, 2).add(new Paragraph("{Month}"));
        cell5 = new Cell(2, 1).add(img.setAutoScale(true));
        cell6 = new Cell(1, 2).add(new Paragraph("Mr Fname Lname"));
        table1.addCell(cell4);
        table1.addCell(cell5);
        table1.addCell(cell6);
        cell4 = new Cell(1, 2).add(new Paragraph("{Month}"));
        cell5 = new Cell(2, 1).add(img.setAutoScale(true));
        cell6 = new Cell(1, 2).add(new Paragraph("Mr Fname Lname"));
        cell4.setBorder(Border.NO_BORDER);
        cell5.setBorder(Border.NO_BORDER);
        cell6.setBorder(Border.NO_BORDER);
        table1.addCell(cell4);
        table1.addCell(cell5);
        table1.addCell(cell6);
        doc.add(table1);


        doc.close();
    }

    class EmptyBorder extends Border {
        public EmptyBorder() {
            super(0f);
        }
        @Override
        public void draw(PdfCanvas canvas, float x1, float y1, float x2, float y2, float borderWidthBefore, float borderWidthAfter) {

        }

        @Override
        public void drawCellBorder(PdfCanvas canvas, float x1, float y1, float x2, float y2) {

        }

        @Override
        public int getType() {
            return 0;
        }
    }
}
