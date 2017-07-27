package com.happy.gene.pdfreport.pdf.def.element.catalog;

import com.happy.gene.pdfreport.pdf.IDrawable;
import com.happy.gene.pdfreport.pdf.IXml;
import com.happy.gene.pdfreport.pdf.IZOrder;
import com.happy.gene.pdfreport.pdf.def.PageDef;
import com.happy.gene.pdfreport.pdf.def.element.*;
import com.happy.gene.pdfreport.pdf.util.DefFactory;
import com.itextpdf.layout.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
