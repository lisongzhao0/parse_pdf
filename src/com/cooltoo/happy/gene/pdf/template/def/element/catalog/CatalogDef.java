package com.cooltoo.happy.gene.pdf.template.def.element.catalog;

import com.cooltoo.happy.gene.pdf.template.IDrawable;
import com.cooltoo.happy.gene.pdf.template.IXml;
import com.cooltoo.happy.gene.pdf.template.def.PageDef;
import com.cooltoo.happy.gene.pdf.template.def.element.AbstractDef;
import com.cooltoo.happy.gene.pdf.template.data.TreeNode;
import com.cooltoo.happy.gene.pdf.template.util.XmlDataCache;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import org.dom4j.Element;

import java.util.List;
import java.util.Map;

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
