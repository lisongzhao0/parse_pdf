package com;

import com.happy.gene.pdfreport.pdf.IDrawable;
import com.happy.gene.pdfreport.pdf.IVariable;
import com.happy.gene.pdfreport.pdf.IZOrder;
import com.happy.gene.pdfreport.pdf.data.*;
import com.happy.gene.pdfreport.pdf.def.AbsolutePositionTemplateDef;
import com.happy.gene.pdfreport.pdf.def.PageGroupDef;
import com.happy.gene.pdfreport.pdf.def.element.AbstractDef;
import com.happy.gene.pdfreport.pdf.util.XmlDataCache;
import com.happy.gene.pdfreport.pdf.util.XmlDataParser;
import com.happy.gene.pdfreport.pdf.def.FontDef;
import com.happy.gene.pdfreport.pdf.def.PageDef;
import com.template.MedicineSelectedReportTemplatePageRenderer;
import com.template.UserGeneCheckReportTemplatePageRenderer;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.*;

/**
 * Created by zhaolisong on 31/03/2017.
 */
public class MainMedicineSelectedDoctor {

    public static void main(String[] args) throws Exception {
//        String templateFilePath = "/Users/zhaolisong/Desktop/projects/cooltoo/parse_pdf/templates/template_gene_check_report_doctor/template_male.xml";
        String templateFilePath = "/Users/zhaolisong/Desktop/projects/cooltoo/parse_pdf/templates/template_gene_check_report_doctor/template_female.xml";
        String templateDataPath = "/Users/zhaolisong/Desktop/projects/cooltoo/parse_pdf/templates/template_gene_check_report_doctor/test_data.xml";

        XmlDataParser parser = XmlDataParser.getInstance();
        XmlDataCache cache  = XmlDataCache.getInstance();
        cache.clearCache();

        Map<String, FontDef>    fonts   = parser.getFonts(templateDataPath, "");
        Map<String, Parameter>  params  = parser.getParameters(templateDataPath);
        Map<String, TreeNode>   trees   = parser.getTrees(templateDataPath);
        Map<String, DataTable>  tables  = parser.getTables(templateDataPath);
        Map<String, Repeat>     repeats = parser.getRepeats(templateDataPath);
        List<Group>             groups  = parser.getGroups(templateDataPath);
        Map<String, AbsolutePositionTemplateDef> templates = parser.getTemplates(templateFilePath);

        cache.setParameters(XmlDataCache.TOP_KEY_FONT, fonts);
        cache.setParameters(XmlDataCache.TOP_KEY_PARAMETER, params);
        cache.setParameters(XmlDataCache.TOP_KEY_TREE, trees);
        cache.setParameters(XmlDataCache.TOP_KEY_TABLE, tables);
        cache.setParameters(XmlDataCache.TOP_KEY_REPEAT, repeats);
        cache.setParameters(XmlDataCache.TOP_KEY_IMAGE_URL, null);
        cache.setParameters(XmlDataCache.TOP_KEY_TEMPLATE, templates);
        cache.setParameter(XmlDataCache.SECOND_KEY_IMAGE_BASE_DIR, "templates/template_gene_check_report_doctor/");


        ByteArrayOutputStream baos      = new ByteArrayOutputStream(64280);
        PdfWriter             pdfWriter = new PdfWriter(baos);
        PdfDocument           pdfDoc    = new PdfDocument(pdfWriter);
        pdfDoc.setDefaultPageSize(PageSize.A4);
        Document doc = new com.itextpdf.layout.Document(pdfDoc, PageSize.A4);

        Collection<FontDef> fontCollection = fonts.values();
        for (FontDef tmp : fontCollection) {
            if ("true".equals(tmp.getEmbedded())) {
                if (null!=tmp.getTtfPath() && !"".equalsIgnoreCase(tmp.getTtfPath())) {
                    PdfFont embededFont = PdfFontFactory.createFont(tmp.getTtfPath(), PdfEncodings.IDENTITY_H, true);
                    tmp.setFont(embededFont);
                    fonts.put(tmp.getId(), tmp);
                }
            }
            else if ("system".equals(tmp.getEmbedded())) {
                PdfFont embededFont = PdfFontFactory.createFont(tmp.getName(), PdfEncodings.IDENTITY_H, true);
                tmp.setFont(embededFont);
                fonts.put(tmp.getId(), tmp);
            }
        }

        UserGeneCheckReportTemplatePageRenderer testRender = new UserGeneCheckReportTemplatePageRenderer();
        List<AbstractDef> pages = parser.getPages(templateFilePath, testRender);
        Collections.sort(pages, IZOrder.comparator);
        int nextPageIndex = 1;

        for (AbstractDef tmp : pages) {
            System.out.println(tmp.getZOrder());
            if (tmp instanceof PageDef) {
                ((PageDef)tmp).setPageStartNumber(nextPageIndex);
                ((PageDef)tmp).generate(doc, ((PageDef)tmp));
                nextPageIndex = ((PageDef)tmp).getPageEndNumber() + 1;
                continue;
            }
        }

        int pageSize = pdfDoc.getNumberOfPages();
        if (pageSize%4!=0) {
            int sizeToAdd = 4 - pageSize%4;
            for (int i = 0; i < sizeToAdd; i ++) {
                pdfDoc.addNewPage(pageSize);
            }
        }


        pdfDoc.close();
        baos.writeTo(new FileOutputStream("/Users/zhaolisong/Desktop/Example_TEST.pdf"));


        System.out.println();
    }
}
