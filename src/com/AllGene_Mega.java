package com;

import com.happy.gene.pdfreport.pdf.IDrawable;
import com.happy.gene.pdfreport.pdf.IVariable;
import com.happy.gene.pdfreport.pdf.IZOrder;
import com.happy.gene.pdfreport.pdf.data.*;
import com.happy.gene.pdfreport.pdf.def.AbsolutePositionTemplateDef;
import com.happy.gene.pdfreport.pdf.def.FontDef;
import com.happy.gene.pdfreport.pdf.def.PageDef;
import com.happy.gene.pdfreport.pdf.def.PageGroupDef;
import com.happy.gene.pdfreport.pdf.def.element.AbstractDef;
import com.happy.gene.pdfreport.pdf.util.XmlDataCache;
import com.happy.gene.pdfreport.pdf.util.XmlDataParser;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.template.UserGeneCheckReportTemplatePageRenderer;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.*;

/**
 * Created by zhaolisong on 21/08/2017.
 */
public class AllGene_Mega {

    public static void main(String[] args) throws Exception {
        String templateFilePath = "/Users/zhaolisong/Desktop/projects/cooltoo/parse_pdf/templates/mega_genome_report/happy_gene_template.xml";
        String templateDataPath = "/Users/zhaolisong/Desktop/projects/cooltoo/parse_pdf/templates/mega_genome_report/test_data.xml";

        XmlDataParser parser = XmlDataParser.getInstance();
        XmlDataCache  cache  = XmlDataCache.getInstance();
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


        ByteArrayOutputStream baos      = new ByteArrayOutputStream(64280);
        PdfWriter             pdfWriter = new PdfWriter(baos);
        PdfDocument           pdfDoc    = new PdfDocument(pdfWriter);
        pdfDoc.setDefaultPageSize(PageSize.A4);
        Document doc = new Document(pdfDoc, PageSize.A4);

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

        List<PageDef> needRepaintPages = new ArrayList<>();
        for (AbstractDef tmp : pages) {
            System.out.println(tmp.getZOrder());
            if (tmp instanceof PageDef) {
                ((PageDef)tmp).setPageStartNumber(nextPageIndex);
                if (((PageDef) tmp).getBooleanProperty("repaint_after_all")) {
                    needRepaintPages.add(((PageDef) tmp));
                    ((PageDef) tmp).setStopGenerate(true);
                    ((PageDef) tmp).setStopRenderer(true);
                }
                ((PageDef)tmp).generate(doc, ((PageDef)tmp));
                nextPageIndex = ((PageDef)tmp).getPageEndNumber() + 1;
                continue;
            }
            if (tmp instanceof PageGroupDef) {
                for (Group groupData : groups) {
                    PageGroupDef pageGroup = ((PageGroupDef) tmp).clone();
                    String catalog = groupData.getProperty("catalog");
                    String needReplaceCatalog = pageGroup.getProperty("catalog");
                    List<AbstractDef> components = pageGroup.getComponents();
                    for (AbstractDef def : components) {
                        if (!(def instanceof PageDef)) {
                            continue;
                        }
                        PageDef page = (PageDef) def;
                        String pageCatalog = page.getProperty("catalog");
                        if (pageCatalog.contains(needReplaceCatalog)) {
                            pageCatalog = pageCatalog.replace(needReplaceCatalog, catalog);
                            page.setProperty("catalog", pageCatalog);
                        }
                    }

                    Map<String, Parameter> groupParams = groupData.getParams();
                    Map<String, DataTable> groupTables = groupData.getTables();
                    Map<String, TreeNode>  groupTrees  = groupData.getTrees();
                    Set<String> keys = null;
                    keys = groupParams.keySet();
                    for (String key : keys) {
                        cache.setParameter(key, groupParams.get(key));
                    }
                    keys = groupTables.keySet();
                    for (String key : keys) {
                        cache.setTable(key, groupTables.get(key));
                    }

                    for (AbstractDef def : components) {
                        if (!(def instanceof PageDef)) {
                            def.generate(doc, null);
                            continue;
                        }

                        PageDef page = (PageDef) def;
                        page.setPageStartNumber(nextPageIndex);
                        page.generate(doc, page);
                        nextPageIndex = page.getPageEndNumber() + 1;
                        if (page.getBooleanProperty("repaint_after_all")) {
                            needRepaintPages.add(((PageDef) tmp));
                        }
                    }
                }
                continue;
            }
        }


        for (PageDef pageDef : needRepaintPages) {
            int pageNumberInDoc = pageDef.getPageStartNumberInPdf();
            pageDef.setStopGenerate(true);
            pageDef.setStopRenderer(true);
            pageDef.generate(doc, pageDef);
            PdfPage lastPage = doc.getPdfDocument().getLastPage();
            Integer lastPageNumber= doc.getPdfDocument().getPageNumber(lastPage);

            List<Object> components = pageDef.getComponents();
            Collections.sort(components, IZOrder.comparator);

            Object preCom = null;
            for (Object com : components) {
                if (com instanceof IDrawable) {
                    if (preCom instanceof IVariable && com instanceof IVariable) {
                        float lastY = ((IVariable) preCom).lastY();
                        ((IVariable) com).lastY(lastY);
                    }
                    ((IDrawable) com).generate(doc, pageDef);
                    preCom = com;
                }
            }

            testRender.rendererTail(doc, pageDef);
            lastPage = doc.getPdfDocument().removePage(lastPageNumber);
            doc.getPdfDocument().removePage(pageNumberInDoc+1);
            doc.getPdfDocument().addPage(pageNumberInDoc+1, lastPage);
        }


        pdfDoc.close();
        baos.writeTo(new FileOutputStream("/Users/zhaolisong/Desktop/Example_TEST.pdf"));


        System.out.println();
    }
}
