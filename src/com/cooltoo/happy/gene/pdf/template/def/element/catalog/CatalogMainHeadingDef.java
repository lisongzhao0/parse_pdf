package com.cooltoo.happy.gene.pdf.template.def.element.catalog;

import com.cooltoo.happy.gene.pdf.template.IDrawable;
import com.cooltoo.happy.gene.pdf.template.IPosition;
import com.cooltoo.happy.gene.pdf.template.IXml;
import com.cooltoo.happy.gene.pdf.template.IZOrder;
import com.cooltoo.happy.gene.pdf.template.def.PageDef;
import com.cooltoo.happy.gene.pdf.template.def.element.*;
import com.cooltoo.happy.gene.pdf.template.util.DefFactory;
import com.itextpdf.layout.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhaolisong on 20/04/2017.
 */
public class CatalogMainHeadingDef extends AbstractDef implements IXml<CatalogMainHeadingDef>, IPosition<CatalogMainHeadingDef> {

    private List<Object> components = new ArrayList<>();


    public List<Object> getComponents() {
        return components;
    }
    public void setComponents(List<Object> component) {
        this.components = component;
    }

    public CatalogMainHeadingDef translate(float x, float y) {
        if (!components.isEmpty()) {
            for (Object obj : components) {
                if (obj instanceof IPosition) {
                    ((IPosition) obj).translate(x, y);
                }
            }
        }
        return this;
    }

    public CatalogMainHeadingDef parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"main_heading".equalsIgnoreCase(element.getName())) {
            return null;
        }

        try { this.setZOrder(Integer.parseInt(element.attributeValue("z_order"))); } catch (Exception ex) {this.setZOrder(0);}

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

    public CatalogMainHeadingDef clone() {
        return null;
    }
}
