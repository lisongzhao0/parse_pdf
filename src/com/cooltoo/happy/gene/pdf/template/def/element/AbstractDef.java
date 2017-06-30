package com.cooltoo.happy.gene.pdf.template.def.element;

import com.cooltoo.happy.gene.pdf.template.IDrawable;
import com.cooltoo.happy.gene.pdf.template.IZOrder;
import com.cooltoo.happy.gene.pdf.template.def.PageDef;
import com.cooltoo.happy.gene.pdf.template.util.XmlDataParser;
import com.cooltoo.happy.gene.pdf.template.util.ColorUtil;
import com.cooltoo.happy.gene.pdf.template.util.ParametersUtil;
import com.itextpdf.layout.Document;

/**
 * Created by zhaolisong on 25/04/2017.
 */
public abstract class AbstractDef implements IZOrder, Cloneable, IDrawable {

    private int                 zOrder         = 0;
    private ColorUtil           colorUtil      = ColorUtil.getInstance();
    private ParametersUtil      parametersUtil = ParametersUtil.getInstance();
    private XmlDataParser       xmlDataParser  = XmlDataParser.getInstance();


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

    public Object generate(Document pdf, PageDef pageDef) throws Exception {
        return null;
    }

    public int getZOrder() {
        return zOrder;
    }
    public void setZOrder(int zOrder) {
        this.zOrder = zOrder;
    }

    public abstract AbstractDef clone();
}
