package com.cooltoo.happy.gene.pdf.template.def.element.catalog;

import com.cooltoo.happy.gene.pdf.template.IDrawable;
import com.cooltoo.happy.gene.pdf.template.IXml;
import com.cooltoo.happy.gene.pdf.template.IZOrder;
import com.cooltoo.happy.gene.pdf.template.def.PageDef;
import com.cooltoo.happy.gene.pdf.template.def.element.*;
import com.cooltoo.happy.gene.pdf.template.util.DefFactory;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaolisong on 20/04/2017.
 */
public class CatalogHeaderDef extends AbstractDef implements IXml<CatalogHeaderDef> {

    private List<Object> components = new ArrayList<>();

    public List<Object> getComponents() {
        return components;
    }
    public void setComponents(List<Object> component) {
        this.components = component;
    }

    public CatalogHeaderDef parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"header".equalsIgnoreCase(element.getName())) {
            return null;
        }


        DefFactory defFactory = DefFactory.getIntance();
        List<Element> subEle = element.elements();
        for (Element ele : subEle) {
            AbstractDef subComp = defFactory.getDef(ele);
            if (null!=subComp) {
                this.components.add(subComp);
            }
        }

        return this;
    }

    public Object generate(Document pdf, PageDef pageDef) throws Exception {
        List<Object> components = getComponents();
        Collections.sort(components, IZOrder.comparator);

        for (Object com : components) {
            if (com instanceof IDrawable) {
                Object draw = ((IDrawable) com).generate(pdf, pageDef);
            }
        }

        return null;
    }

    public CatalogHeaderDef clone() {
        return null;
    }
}
