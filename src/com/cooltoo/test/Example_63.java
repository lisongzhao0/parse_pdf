package com.cooltoo.test;

/**
 * Created by zhaolisong on 20/04/2017.
 */

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.TextRenderer;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Example_63 {
//    public static final String DEST = "/Users/zhaolisong/Desktop/projects/cooltoo/parse_pdf/every25words.pdf";
    public static final String DEST = "/Users/zhaolisong/Desktop/projects/cooltoo/parse_pdf/StarWars_3.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
//        new Example_63().manipulatePdf(DEST);
        new Example_63().createPdf(DEST);
    }

    public String readFile() throws IOException {
        String msg =  "1 Gallia est omnis divisa in partes tres, quarum unam incolunt Belgae, aliam Aquitani, tertiam qui ipsorum lingua Celtae, nostra Galli appellantur. 2 Hi omnes lingua, institutis, legibus inter se differunt. Gallos ab Aquitanis Garumna flumen, a Belgis Matrona et Sequana dividit. 3 Horum omnium fortissimi sunt Belgae, propterea quod a cultu atque humanitate provinciae longissime absunt, minimeque ad eos mercatores saepe commeant atque ea quae ad effeminandos animos pertinent important, 4 proximique sunt Germanis, qui trans Rhenum incolunt, quibuscum continenter bellum gerunt. Qua de causa Helvetii quoque reliquos Gallos virtute praecedunt, quod fere cotidianis proeliis cum Germanis contendunt, cum aut suis finibus eos prohibent aut ipsi in eorum finibus bellum gerunt. 5 [Eorum una, pars, quam Gallos obtinere dictum est, initium capit a flumine Rhodano, continetur Garumna flumine, Oceano, finibus Belgarum, attingit etiam ab Sequanis et Helvetiis flumen Rhenum, vergit ad septentriones. 6 Belgae ab extremis Galliae finibus oriuntur, pertinent ad inferiorem partem fluminis Rheni, spectant in septentrionem et orientem solem. 7 Aquitania a Garumna flumine ad Pyrenaeos montes et eam partem Oceani quae est ad Hispaniam pertinet; spectat inter occasum solis et septentriones.] ";
        msg += "\r\n11111\r\n" + msg;
        msg += "\r\n22222\r\n" + msg;
        msg += "\r\n33333\r\n" + msg;
        msg += "\r\n44444\r\n" + msg;
        msg += "\r\n55555\r\n" + msg;
        return "\r\n66666\r\n" + msg;
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        String[] words = readFile().split("\\s+");
        Paragraph paragraph = new Paragraph();
        Text text = null;
        int i = 0;
        for (String word : words) {
            if (text != null) {
                paragraph.add(" ");
            }
            if (i%200==0) {
                doc.add(paragraph);
                paragraph = new Paragraph();
                doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            }
            text = new Text(word);
            text.setNextRenderer(new Word25TextRenderer(text, ++i));
            paragraph.add(text);
        }
        doc.add(paragraph);
        doc.close();
    }


    private class Word25TextRenderer extends TextRenderer {
        private int count = 0;
        public Word25TextRenderer(Text textElement, int count) {
            super(textElement);
            this.count = count;
        }
        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            if (0 == count % 25) {
                Rectangle rect = getOccupiedAreaBBox();
                int pageNumber = getOccupiedArea().getPageNumber();
                PdfCanvas canvas = drawContext.getCanvas();
                canvas.saveState()
                        .setLineDash(5, 5)
                        .moveTo(drawContext.getDocument().getPage(pageNumber).getPageSize().getLeft(), rect.getBottom())
                        .lineTo(rect.getRight(), rect.getBottom())
                        .lineTo(rect.getRight(), rect.getTop())
                        .lineTo(drawContext.getDocument().getDefaultPageSize().getRight(), rect.getTop())
                        .stroke()
                        .restoreState();
            }
        }
    }

    public void createPdf(String dest) throws IOException {

        //Initialize PDF document
        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));

        //Add new page
        PageSize ps = PageSize.A4;
        PdfPage page = pdf.addNewPage(ps);

        PdfCanvas canvas = new PdfCanvas(page);

        List<String> text = new ArrayList();
        text.add("         Episode V         ");
        text.add("  THE EMPIRE STRIKES BACK  ");
        text.add("It is a dark time for the");
        text.add("Rebellion. Although the Death");
        text.add("Star has been destroyed,");
        text.add("Imperial troops have driven the");
        text.add("Rebel forces from their hidden");
        text.add("base and pursued them across");
        text.add("the galaxy.");
        text.add("Evading the dreaded Imperial");
        text.add("Starfleet, a group of freedom");
        text.add("fighters led by Luke Skywalker");
        text.add("has established a new secret");
        text.add("base on the remote ice world");
        text.add("of Hoth...");

        text.add("Evading the dreaded Imperial");
        text.add("Starfleet, a group of freedom");
        text.add("fighters led by Luke Skywalker");
        text.add("has established a new secret");
        text.add("base on the remote ice world");
        text.add("of Hoth...");

        //Replace the origin of the coordinate system to the top left corner
        canvas.concatMatrix(1, 0, 0, -1, 0, 0);
        canvas.beginText()
                .setFontAndSize(PdfFontFactory.createFont(FontConstants.COURIER_BOLD), 14)
                .setLeading(14 * 1.2f)
                .moveText(70, -40);
        for (String s : text) {
            //Add text and move to the next line
            canvas.newlineShowText(s);
        }
        canvas.endText();

        //Close document
        pdf.close();

    }
}
