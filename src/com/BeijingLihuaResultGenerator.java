package com;

import com.happy.gene.pdfreport.pdf.IZOrder;
import com.happy.gene.pdfreport.pdf.data.DataTable;
import com.happy.gene.pdfreport.pdf.data.Group;
import com.happy.gene.pdfreport.pdf.data.Parameter;
import com.happy.gene.pdfreport.pdf.data.TreeNode;
import com.happy.gene.pdfreport.pdf.def.AbsolutePositionTemplateDef;
import com.happy.gene.pdfreport.pdf.def.FontDef;
import com.happy.gene.pdfreport.pdf.def.PageDef;
import com.happy.gene.pdfreport.pdf.def.PageGroupDef;
import com.happy.gene.pdfreport.pdf.def.element.AbstractDef;
import com.happy.gene.pdfreport.pdf.util.XmlDataCache;
import com.happy.gene.pdfreport.pdf.util.XmlDataParser;
import com.happy.gene.utility.SetUtil;
import com.happy.gene.utility.StringUtil;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.template.gene.site.data.XmlParam;
import org.dom4j.Element;
import org.dom4j.tree.DefaultDocument;
import org.dom4j.tree.DefaultElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.*;

public class BeijingLihuaResultGenerator {

    private static final Logger logger              = LoggerFactory.getLogger(BeijingLihuaResultGenerator.class);
    public  static final String templateFilePrefix  = "template_beijing_lihua_result_page";
    public  static final String fontsFileName       = "./fonts.xml";
    public  static final String getTemplateFilePath(int numOfGeneNotDuplicated) {
        if (numOfGeneNotDuplicated<=12) {
            return templateFilePrefix + "12.xml";
        }
        else {
            return templateFilePrefix + "X.xml";
        }
    }

    private SetUtil    setUtil    = SetUtil.newInstance();
    private StringUtil stringUtil = StringUtil.newInstance();


    public static void main(String[] args) throws Exception {
        BeijingLihuaResultGenerator resultGen = new BeijingLihuaResultGenerator();

        int geneCount = 105;
        Map<String, List<String[]>> sampleIdToGeneSite = new HashMap<>();
        for (int i = 0; i < 140; i ++) {
            List<String[]> GeneSite = new ArrayList<>();
            for (int j = 0; j < geneCount; j ++) {
                String a = j%2==0 ? "A" : "";
                String c = j%2==1 ? "C" : "";
                String t = j%2==0 ? "T" : "";
                String g = j%2==1 ? "G" : "";
                GeneSite.add(new String[]{"rs"+ (geneCount<=12 ? 0 : i)+"00000"+j, a+t+c+g});
            }
            sampleIdToGeneSite.put("20180050000"+i, GeneSite);
        }


        String templateDir = "/Users/zhaolisong/Desktop/projects/happygene/happy_gene_backend/pdf_report/templates/beijing_lihuan_center_sign_page";
        resultGen.genReport(templateDir,
                sampleIdToGeneSite,
                new int[]{0, 1},
                "20180131-YFS-801", "HD26-3", "20180117", "远盟康健科技有限公司", "口腔拭子", "/Users/zhaolisong/Desktop/LihuaResult_TEST.pdf");

    }

    public int genReport(String templateDir /* pdf 模板路径 */,
                         Map<String, List<String[]>> sampleIdToGeneSite /* 结果 */ ,
                         int[] snpIdAndCallIdx /* snpId 和 call 在结果中的下标 */,
                         String resultNumber /* 结果编号 */,
                         String batchNumber /* 批次号 */,
                         String receiveDate /* 收样日期 */,
                         String client /* 委托单位 */,
                         String sampleType /* 样本类型 */,
                         String reportPath /* pdf 存储路径 */
    ) throws Exception {
        int sampleNumber = null==sampleIdToGeneSite ? 0 : sampleIdToGeneSite.size() /* 样本数量 */;
        logger.debug("gen pdf by templateDir={} sampleIdToGeneSiteSize={} resultNumber={} batchNumber={} receiveDate={} client={} sampleNumber={} reportPath={}",
                templateDir, sampleIdToGeneSite, resultNumber, batchNumber, receiveDate, client, sampleNumber, reportPath);
        if (setUtil.isMapEmpty(sampleIdToGeneSite) ||
                stringUtil.isEmpty(templateDir) ||
                stringUtil.isEmpty(resultNumber) ||
                0==sampleNumber ||
                stringUtil.isEmpty(client) ||
                stringUtil.isEmpty(reportPath)) {
            logger.debug("parameter is empty");
            return -1;
        }
        receiveDate = stringUtil.isEmpty(receiveDate) ? "——" : receiveDate;

        templateDir = templateDir.replace('\\','/');
        templateDir = templateDir.endsWith("/") ? templateDir : templateDir + "/";

        List<String> SNP01_12 = new ArrayList<>();
        org.dom4j.Document  data    = getReportXmlData(
                sampleIdToGeneSite, snpIdAndCallIdx, resultNumber, batchNumber,
                receiveDate, client, sampleType, sampleNumber, SNP01_12
        );
        if (null==data) {
            logger.debug("construct data xml failed!");
            return -2;
        }


        String templateFilePath = templateDir + getTemplateFilePath(SNP01_12.size());
        if (stringUtil.isEmpty(templateFilePath)) {
            logger.debug("template file({}) is empty is empty", getTemplateFilePath(SNP01_12.size()));
            return -3;
        }

        XmlDataCache cache       = getDataCache(templateDir, templateFilePath, data);
        XmlDataParser parser      = XmlDataParser.getInstance();
        List<AbstractDef>       pages       = parser.getPages(templateFilePath, null);
        List<Group>             groups      = parser.getGroups(data);
        // create font cache
        Map<String, FontDef>   fonts  = parser.getFonts(templateDir, fontsFileName);
        Collection<FontDef> values = fonts.values();
        for (FontDef tmp : values) { tmp.setTtfPath(templateDir + tmp.getTtfPath()); }
        Collection<FontDef> fontCollection = fonts.values();
        for (FontDef tmp : fontCollection) {
            if (null!=tmp.getTtfPath() && !"".equalsIgnoreCase(tmp.getTtfPath())) {
                PdfFont embededFont = PdfFontFactory.createFont(tmp.getTtfPath(), PdfEncodings.IDENTITY_H, true);
                tmp.setFont(embededFont);
                fonts.put(tmp.getName(), tmp);
            }
        }
        Collections.sort(pages, IZOrder.comparator);

        ByteArrayOutputStream   baos        = new ByteArrayOutputStream(64280);
        PdfWriter               pdfWriter   = new PdfWriter(baos);
        PdfDocument             pdfDoc      = new PdfDocument(pdfWriter);
        pdfDoc.setDefaultPageSize(new PageSize(612.24f, 824.88f));
        Document                doc         = new Document(pdfDoc, new PageSize(612.24f, 824.88f));

        int nextPageIndex = 1;
        for (AbstractDef tmp : pages) {
            System.out.println(tmp.getZOrder());
            if (tmp instanceof PageDef) {
                ((PageDef)tmp).setPageStartNumber(nextPageIndex);
                if (((PageDef) tmp).getBooleanProperty("repaint_after_all")) {
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
                    String       catalog   = groupData.getProperty("catalog");
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
                    }
                }
                continue;
            }
        }

        // paint page number
        for (int pageNum = 1, totalNum = pdfDoc.getNumberOfPages(); pageNum <= totalNum; pageNum ++) {
            PdfPage toPage = pdfDoc.getPage(pageNum);
            String pageFormat = null;
            pageFormat = "第 pageNum 页 / 共 totalNum 页".replace("pageNum", ""+pageNum);
            pageFormat = pageFormat.replace("totalNum", ""+totalNum);

            PdfCanvas under = new PdfCanvas(toPage);
            Canvas canvas = new Canvas(under, pdfDoc, pdfDoc.getDefaultPageSize());
            Paragraph p = new Paragraph(pageFormat).setFontSize(9).setFontColor(com.itextpdf.kernel.color.Color.BLACK);
            if (null!=fonts.get("fzltxh_gbk")) { p.setFont((fonts.get("fzltxh_gbk").getFont())); }
            float x = toPage.getPageSize().getWidth() / 2;
            float y = 50;
            canvas.showTextAligned(p, x, y, 1, TextAlignment.CENTER, com.itextpdf.layout.property.VerticalAlignment.MIDDLE, 0);
        }

        doc.flush();
        pdfDoc.close();
        baos.writeTo(new FileOutputStream(reportPath));
        baos.flush();
        pdfWriter.flush();
        pdfWriter.close();
        baos.close();

        cache.clearCache();
        pages.clear();

        System.gc();
        System.gc();
        Runtime.getRuntime().gc();
        Runtime.getRuntime().gc();

        return 0;
    }

    private org.dom4j.Document getReportXmlData(Map<String, List<String[]>> sampleIdToGeneSite /* 结果 */,
                                                int[] snpIdAndCallIdx /* snpId 和 call 在结果中的下标 */,
                                                String resultNumber /* 结果编号 */,
                                                String batchNumber /* 批次号 */,
                                                String receiveDate /* 收样日期 */,
                                                String client /* 委托单位 */,
                                                String sampleType /* 样本类型 */,
                                                int sampleNumber /* 样本数量 */,
                                                List<String> SNP01_12
    ) {
        org.dom4j.Document document = new DefaultDocument();
        document.setXMLEncoding("UTF-8");
        if (null==sampleIdToGeneSite) { return document; }

        XmlParam xmlParam = new XmlParam();

        // root element
        Element     elePdf                      = new DefaultElement("pdf");
        // data element
        Element     eleData                     = new DefaultElement("data");

        // reportNumber
        xmlParam.isCDATA(true).setId("reportNumber").setValue(resultNumber);
        eleData.add(xmlParam.toXml());

        // batchNumber
        xmlParam.isCDATA(true).setId("batchNumber").setValue(batchNumber);
        eleData.add(xmlParam.toXml());

        // receiveDate
        xmlParam.isCDATA(true).setId("receiveDate").setValue(receiveDate);
        eleData.add(xmlParam.toXml());

        // client
        xmlParam.isCDATA(true).setId("client").setValue(client);
        eleData.add(xmlParam.toXml());

        // sampleType
        xmlParam.isCDATA(true).setId("sampleType").setValue(""+sampleType);
        eleData.add(xmlParam.toXml());

        // sampleNumber
        xmlParam.isCDATA(true).setId("sampleNumber").setValue(""+sampleNumber);
        eleData.add(xmlParam.toXml());

        // transferNum
        xmlParam.isCDATA(true).setId("transferNum").setValue(" ");
        eleData.add(xmlParam.toXml());

        List<String> keys = new ArrayList<>(sampleIdToGeneSite.keySet());
        Collections.sort(keys, new Comparator<String>() {
            @Override public int compare(String o1, String o2) {
                if (null!=o1 && null!=o2) { return o1.compareTo(o2); }
                else if (null==o1 && null!=o2) { return -1; }
                else if (null!=o1 && null==o2) { return 1; }
                else { return 0; }
            }
        });

        int groupIdx = 1;
        int rowIdx   = 1;
        int colIdx   = 1;
        final int minRowIdx = 0;
        final int maxRowIdx = 34;
        final int minColIdx = 0;
        final int maxColIdx = 5;
        final int colSize   = 6;

        xmlParam.isCDATA(true);
        for (int i=0,size=keys.size(); i<size; i++) {
            Element eGroup = new DefaultElement("group");
            eGroup.addAttribute("id", "catalog_1_"+groupIdx);
            eGroup.addAttribute("value", "1 "+groupIdx);
            eGroup.addAttribute("catalog", "1 "+groupIdx);
            eleData.add(eGroup);

            // 添加样本ID
            String key = keys.get(i);
            xmlParam.setId("id").setValue(key); eGroup.add(xmlParam.toXml());

            groupIdx++;
            rowIdx = minRowIdx;
            colIdx = minColIdx;

            int currGroupIdx = groupIdx;
            int startRowIdx  = rowIdx;

            List<String[]> gene = sampleIdToGeneSite.get(key);
            colIdx = minColIdx;
            for (int geneIdx=0, geneSize=gene.size(); geneIdx<geneSize; geneIdx++) {
                String[] tmpGS = gene.get(geneIdx);
                String snpId = tmpGS[snpIdAndCallIdx[0]];
                String call  = tmpGS[snpIdAndCallIdx[1]];
                if (!SNP01_12.contains(snpId)) { SNP01_12.add(snpId); }
                snpId = null==snpId || "".equals(snpId) ? "--" : snpId+" SNP检测";

                xmlParam.setId(getRowColIdx(rowIdx)+getRowColIdx(colIdx)).setValue(snpId);  eGroup.add(xmlParam.toXml());
                xmlParam.setId(getRowColIdx(rowIdx)+getRowColIdx(colIdx+1)).setValue(call); eGroup.add(xmlParam.toXml()); rowIdx ++;

                if (rowIdx>maxRowIdx) { rowIdx=startRowIdx; colIdx=colIdx+2; }
                if (colIdx>maxColIdx && (geneIdx+1<geneSize)) { // 翻页
                    groupIdx++;
                    rowIdx = minRowIdx;
                    colIdx = minColIdx;

                    eGroup = new DefaultElement("group");
                    eGroup.addAttribute("id", "catalog_1_"+groupIdx);
                    eGroup.addAttribute("value", "1 "+groupIdx);
                    eGroup.addAttribute("catalog", "1 "+groupIdx);
                    eleData.add(eGroup);
                }
            }

            for (; (colIdx<maxColIdx);) {
                xmlParam.setId(getRowColIdx(rowIdx)+getRowColIdx(colIdx)).setValue("--");   eGroup.add(xmlParam.toXml());
                xmlParam.setId(getRowColIdx(rowIdx)+getRowColIdx(colIdx+1)).setValue("--"); eGroup.add(xmlParam.toXml()); rowIdx ++;
                if (rowIdx>maxRowIdx) { rowIdx = groupIdx==currGroupIdx ? startRowIdx : minRowIdx; colIdx=colIdx+2; }
            }
        }
        // SNP01 - 12
        for (int i=0, size=SNP01_12.size(); size<13 && i < 12; i ++) {
            if (i<size && !stringUtil.isEmpty(SNP01_12.get(i))) {
                xmlParam.isCDATA(true).setId("SNP" + (i + 1)).setValue(SNP01_12.get(i) + " SNP检测");
            }
            else {
                xmlParam.isCDATA(true).setId("SNP" + (i + 1)).setValue("——");
            }
            eleData.add(xmlParam.toXml());
        }


        elePdf.add(eleData);
        document.add(elePdf);

        System.gc();
        System.gc();
        Runtime.getRuntime().gc();
        Runtime.getRuntime().gc();
        return document;
    }

    private String getRowColIdx(int rowColIdx) {
        if (rowColIdx<10) { return "0"+rowColIdx; }
        return ""+rowColIdx;
    }

    private XmlDataCache getDataCache(String templateDir, String templateFilePath, org.dom4j.Document data) throws Exception {
        XmlDataParser       parser = XmlDataParser.getInstance();
        XmlDataCache        cache  = XmlDataCache.getInstance();
        cache.clearCache();

        // create font cache
        Map<String, FontDef>   fonts  = parser.getFonts(templateDir, fontsFileName);
        Collection<FontDef> values = fonts.values();
        for (FontDef tmp : values) { tmp.setTtfPath(templateDir + tmp.getTtfPath()); }
        Collection<FontDef> fontCollection = fonts.values();
        for (FontDef tmp : fontCollection) {
            if (null!=tmp.getTtfPath() && !"".equalsIgnoreCase(tmp.getTtfPath())) {
                PdfFont embededFont = PdfFontFactory.createFont(tmp.getTtfPath(), PdfEncodings.IDENTITY_H, true);
                tmp.setFont(embededFont);
                fonts.put(tmp.getName(), tmp);
            }
        }

        Map<String, Parameter> params = parser.getParameters(data);
        Map<String, TreeNode>  trees  = parser.getTrees(data);
        Map<String, DataTable> tables = parser.getTables(data);
        Map<String, AbsolutePositionTemplateDef> templates = parser.getTemplates(templateFilePath);

        cache.setParameters(XmlDataCache.TOP_KEY_FONT, fonts);
        cache.setParameters(XmlDataCache.TOP_KEY_PARAMETER, params);
        cache.setParameters(XmlDataCache.TOP_KEY_TREE, trees);
        cache.setParameters(XmlDataCache.TOP_KEY_TABLE, tables);
        cache.setParameters(XmlDataCache.TOP_KEY_IMAGE_URL, null);
        cache.setParameters(XmlDataCache.TOP_KEY_TEMPLATE, templates);
        cache.setParameter(XmlDataCache.SECOND_KEY_IMAGE_BASE_DIR, templateDir);

        return cache;
    }
}
