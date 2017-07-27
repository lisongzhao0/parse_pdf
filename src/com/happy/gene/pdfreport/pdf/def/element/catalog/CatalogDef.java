package com.happy.gene.pdfreport.pdf.def.element.catalog;

import com.happy.gene.pdfreport.pdf.IXml;
import com.happy.gene.pdfreport.pdf.data.TreeNode;
import com.happy.gene.pdfreport.pdf.def.PageDef;
import com.happy.gene.pdfreport.pdf.def.element.AbstractDef;
import com.happy.gene.pdfreport.pdf.util.XmlDataCache;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import org.dom4j.Element;

import java.util.List;

/**
 * Created by zhaolisong on 20/04/2017.
 */
public class CatalogDef extends AbstractDef implements IXml<CatalogDef> {

    private String catalogId;
    private CatalogHeaderDef header;
    private CatalogRepeatDef repeat;

    public String getCatalogId() {
        return catalogId;
    }
    public CatalogHeaderDef getHeader() {
        return header;
    }
    public CatalogRepeatDef getRepeat() {
        return repeat;
    }

    public CatalogDef parseElement(Element element) {
        if (null == element) {
            return null;
        }
        if (!"catalog".equalsIgnoreCase(element.getName())) {
            return null;
        }

        this.catalogId = element.attributeValue("data");


        // TODO -- parse other component
        List<Element> subEle = element.elements();
        for (Element ele : subEle) {
            CatalogHeaderDef newHeader = (new CatalogHeaderDef()).parseElement(ele);
            if (null != newHeader) {
                this.header = newHeader;
            }
            CatalogRepeatDef newRepeat = (new CatalogRepeatDef()).parseElement(ele);
            if (null != newRepeat) {
                this.repeat = newRepeat;
            }
        }
        return this;
    }

    public PdfCanvas generate(Document pdf, PageDef pageDef) throws Exception {
        XmlDataCache xmlDataCache = XmlDataCache.getInstance();
        TreeNode catalogData = xmlDataCache.getTree(getCatalogId());
        if (catalogData instanceof TreeNode) {
        }
        else {
            return null;
        }

        CatalogHeaderDef header = getHeader();
        CatalogRepeatDef repeat = getRepeat();
        if (null!=header) {
            header.generate(pdf, pageDef);
        }

        if (null!=repeat) {
            repeat.setCatalog(catalogData);
            repeat.generate(pdf, pageDef);
        }

        return null;
    }

    public CatalogDef clone() {
        return null;
    }
}
