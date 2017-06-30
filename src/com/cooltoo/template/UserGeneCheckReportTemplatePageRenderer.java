package com.cooltoo.template;

import com.cooltoo.happy.gene.pdf.template.IDrawable;
import com.cooltoo.happy.gene.pdf.template.IPageDefRenderer;
import com.cooltoo.happy.gene.pdf.template.IVariable;
import com.cooltoo.happy.gene.pdf.template.IZOrder;
import com.cooltoo.happy.gene.pdf.template.data.TreeNode;
import com.cooltoo.happy.gene.pdf.template.def.AbsolutePositionTemplateDef;
import com.cooltoo.happy.gene.pdf.template.def.PageDef;
import com.cooltoo.happy.gene.pdf.template.def.element.AbstractDef;
import com.cooltoo.happy.gene.pdf.template.util.XmlDataCache;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.layout.Document;

import java.util.*;

/**
 * Created by zhaolisong on 10/05/2017.
 */
public class UserGeneCheckReportTemplatePageRenderer implements IPageDefRenderer {

    private Map<String, Integer> catalogs = new HashMap<>();

    public Map<String, Integer> getCatalogs() {
        Map<String, Integer> res = new HashMap<>();
        Set<String> keySet = catalogs.keySet();
        for (String key : keySet) {
            res.put(key, catalogs.get(key));
        }
        return res;
    }

    public void beforeRenderer(Document pdf, PageDef pageDef) throws Exception {
        Integer pageRestartNumber = pageDef.getIntProperty("page_number_start");
        if (null!=pageRestartNumber) {
            pageDef.setPageStartNumber(pageRestartNumber);
        }
        PdfPage page = pdf.getPdfDocument().getLastPage();
        int currentPageNumberInDoc= pdf.getPdfDocument().getPageNumber(page);
        int pageStartNumberInDoc  = pageDef.getPageStartNumberInDoc();
        int pageStartNumber       = pageDef.getPageStartNumber();
        int pageNumber            = (currentPageNumberInDoc - pageStartNumberInDoc) + pageStartNumber;

        String catalog = pageDef.getProperty("catalog");
        Integer catalogPage = catalogs.get(catalog);
        if (null==catalogPage) {
            catalogs.put(catalog, pageNumber);
        }
    }
    public void afterRenderer(Document pdf, PageDef pageDef) throws Exception {

    }

    public void rendererHeader(Document pdf, PageDef pageDef) throws Exception {
        PdfPage page = pdf.getPdfDocument().getLastPage();
        int currentPageNumberInDoc= pdf.getPdfDocument().getPageNumber(page);
        int pageStartNumberInDoc  = pageDef.getPageStartNumberInDoc();
        if (currentPageNumberInDoc!=pageStartNumberInDoc && pageDef.getBooleanProperty("page_header")) {
            XmlDataCache xmlDataCache = XmlDataCache.getInstance();
            AbsolutePositionTemplateDef pageNumberTemplate = xmlDataCache.getTemplate("page_header");
            List<AbstractDef> components = pageNumberTemplate.getComponents(((currentPageNumberInDoc+1)%2) + "");
            for (AbstractDef tmp : components) {
                if (tmp instanceof IDrawable) {
                    ((IDrawable) tmp).generate(pdf, pageDef);
                }
            }
        }
    }

    public void rendererTail(Document pdf, PageDef pageDef) throws Exception {
        Integer pageRestartNumber = pageDef.getIntProperty("page_number_start");
        if (null!=pageRestartNumber) {
            pageDef.setPageStartNumber(pageRestartNumber);
        }
        PdfPage page = pdf.getPdfDocument().getLastPage();
        int currentPageNumberInDoc= pdf.getPdfDocument().getPageNumber(page);
        int pageStartNumberInDoc  = pageDef.getPageStartNumberInDoc();
        int pageStartNumber       = pageDef.getPageStartNumber();
        int pageNumber            = (currentPageNumberInDoc - pageStartNumberInDoc) + pageStartNumber;

        String catalog = pageDef.getProperty("catalog");
        Integer catalogPage = catalogs.get(catalog);
        if (null==catalogPage) {
            catalogs.put(catalog, pageNumber);
        }

        if (pageDef.getBooleanProperty("page_number")) {
            XmlDataCache xmlDataCache = XmlDataCache.getInstance();
            String strPageNumber = pageNumber>99 ? pageNumber+"" : (pageNumber>9 ? "0"+pageNumber : (pageNumber>=0 ? "00"+pageNumber : pageNumber+""));
            xmlDataCache.setParameter("pageNumber", strPageNumber);
            AbsolutePositionTemplateDef pageNumberTemplate = xmlDataCache.getTemplate("page_tail");
            pageNumberTemplate = pageNumberTemplate.clone();
            List<AbstractDef> components = pageNumberTemplate.getComponents(((currentPageNumberInDoc+1)%2) + "");
            generateContent(components, pdf, pageDef);
        }
    }

    public void rendererCatalog(Document pdf, PageDef pageDef) throws Exception {
        setPageNumberForCatalog(pdf, pageDef);
    }

    private void setPageNumberForCatalog(Document pdf, PageDef pageDef) throws Exception {
        String  catalog         = pageDef.getProperty("type");
        Integer fixedPageNumber = pageDef.getIntProperty("fixed_page_number");
        if (null==catalog || !"catalog".equalsIgnoreCase(catalog) || null==fixedPageNumber) {
            return;
        }

        PdfPage lastPage = pdf.getPdfDocument().getLastPage();
        Integer lastPageNumber= pdf.getPdfDocument().getPageNumber(lastPage);
        pageDef.setStopGenerate(true);

        Set<String> keys = catalogs.keySet();
        TreeNode catalogRoot = XmlDataCache.getInstance().getTree("catalog");
        setPageNumber(catalogRoot, keys);

        List<Object> components = pageDef.getComponents();
        Collections.sort(components, IZOrder.comparator);

        Object preCom = null;
        for (Object com : components) {
            if (com instanceof IDrawable) {
                if (preCom instanceof IVariable && com instanceof IVariable) {
                    float lastY = ((IVariable) preCom).lastY();
                    ((IVariable) com).lastY(lastY);
                }
                ((IDrawable) com).generate(pdf, pageDef);
                preCom = com;
            }
        }

        lastPage = pdf.getPdfDocument().removePage(lastPageNumber);
        pdf.getPdfDocument().addPage(fixedPageNumber, lastPage);
    }

    private void setPageNumber(TreeNode node, Set<String> keys) {
        if (null==node || null==keys || keys.isEmpty()) {
            return;
        }
        String property = node.getProperty("catalog");
        if (null==property || "".equalsIgnoreCase(property)) {
            return;
        }
        for (String key : keys) {
            if (null==key || "".equalsIgnoreCase(key)) {
                continue;
            }
            Integer pageNumber = catalogs.get(key);
            String strPageNumber = pageNumber>99 ? pageNumber+"" : (pageNumber>9 ? "0"+pageNumber : (pageNumber>=0 ? "00"+pageNumber : pageNumber+""));
            if (key.equalsIgnoreCase(property)) {
                node.setProperties("page", strPageNumber);
                break;
            }
            if (key.contains("x")) {
                String[] splitKey = key.split(" ");
                String[] splitPro = property.split(" ");
                if (splitKey.length == splitPro.length) {
                    boolean equal = true;
                    for (int i = 0, length = splitPro.length; i < length; i++) {
                        String ck = splitKey[i].trim();
                        String cp = splitPro[i].trim();
                        if (ck.equalsIgnoreCase(cp)) {
                            continue;
                        }
                        if (ck.equalsIgnoreCase("x") && !"".equalsIgnoreCase(cp)) {
                            continue;
                        }
                        equal = false;
                        break;
                    }
                    if (equal) {
                        System.out.println(strPageNumber);
                        node.setProperties("page", strPageNumber);
                    }
                }
            }
        }

        List<TreeNode> children = node.getChilds();
        if (null==children || children.isEmpty()) {
            return;
        }
        for (TreeNode tmp : children) {
            setPageNumber(tmp, keys);
        }

    }

    private void generateContent(List<AbstractDef> components, Document pdf, PageDef pageDef) throws Exception {
        for (AbstractDef tmp : components) {
            if (tmp instanceof IDrawable) {
                ((IDrawable) tmp).generate(pdf, pageDef);
            }
        }
    }
}
