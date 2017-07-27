package com.happy.gene.pdfreport.pdf.def.element;

import com.happy.gene.pdfreport.pdf.IDrawable;
import com.happy.gene.pdfreport.pdf.IZOrder;
import com.happy.gene.pdfreport.pdf.def.PageDef;
import com.happy.gene.pdfreport.pdf.util.StringUtil;
import com.happy.gene.pdfreport.pdf.util.XmlDataParser;
import com.happy.gene.pdfreport.pdf.util.ColorUtil;
import com.happy.gene.pdfreport.pdf.util.ParametersUtil;
import com.itextpdf.layout.Document;

/**
 * Created by zhaolisong on 25/04/2017.
 */
public abstract class AbstractDef implements IZOrder, Cloneable, IDrawable {

    int                     zOrder          = 0;
    String                  catalog         = "";
    int                     pageStartNumberInPdf = 0;
    String                  visible         = "true";

    private ColorUtil       colorUtil       = ColorUtil.getInstance();
    private ParametersUtil  parametersUtil  = ParametersUtil.getInstance();
    private XmlDataParser   xmlDataParser   = XmlDataParser.getInstance();
	private StringUtil      stringUtil      = StringUtil.newInstance();


    public AbstractDef() {}

    public ColorUtil getColorUtil() {
        return colorUtil;
    }
    public ParametersUtil getParametersUtil() {
        return parametersUtil;
    }
    public XmlDataParser getXmlDataParser() {
        return xmlDataParser;
    }
    public StringUtil getStringUtil() {
        return stringUtil;
    }

    public Object generate(Document pdf, PageDef pageDef) throws Exception {
        return null;
    }

    public int getZOrder() {
        return zOrder;
    }
    public void setZOrder(int zOrder) {
        this.zOrder = zOrder;
    }

    public String getCatalog() { return catalog; }
    public void setCatalog(String catalog) { this.catalog = catalog; }

    public boolean isVisible() {
        boolean not = false;
        if (null!=visible && visible.toLowerCase().contains("not")) {
            not = true;
        }
        if (not) {
            return !Boolean.parseBoolean(getParametersUtil().replaceParameter(this.visible));
        }
        else {
            return Boolean.parseBoolean(getParametersUtil().replaceParameter(this.visible));
        }
    }
    public void setVisible(String visible) { this.visible = visible; }

    public int getPageStartNumberInPdf() { return pageStartNumberInPdf; }
    public void setPageStartNumberInPdf(int pageStartNumberInPdf) { this.pageStartNumberInPdf = pageStartNumberInPdf; }

    public abstract AbstractDef clone();
}
