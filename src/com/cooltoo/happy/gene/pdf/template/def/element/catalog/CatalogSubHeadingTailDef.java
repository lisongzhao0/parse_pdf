package com.cooltoo.happy.gene.pdf.template.def.element.catalog;

import com.cooltoo.happy.gene.pdf.template.IDrawable;
import com.cooltoo.happy.gene.pdf.template.IPosition;
import com.cooltoo.happy.gene.pdf.template.IXml;
import com.cooltoo.happy.gene.pdf.template.IZOrder;
import com.cooltoo.happy.gene.pdf.template.def.PageDef;
import com.cooltoo.happy.gene.pdf.template.def.element.*;
import com.cooltoo.happy.gene.pdf.template.util.DefFactory;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
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
public class CatalogSubHeadingTailDef extends AbstractDef implements IXml<CatalogSubHeadingTailDef>, IPosition<CatalogSubHeadingTailDef> {

    private float unitYSize;
    private List<Object> components = new ArrayList<>();

    public float getUnitYSize() {
        return unitYSize;
    }
    public CatalogSubHeadingTailDef setUnitYSize(float unitYSize) {
        this.unitYSize = unitYSize;
        return this;
    }

    public List<Object> getComponents() {
        return components;
    }
    public void setComponents(List<Object> component) {
        this.components = component;
    }

    public CatalogSubHeadingTailDef translate(float x, float y) {
        if (!components.isEmpty()) {
            for (Object obj : components) {
                if (obj instanceof IPosition) {
                    ((IPosition) obj).translate(x, y);
                }
            }
        }
        return this;
    }

    public CatalogSubHeadingTailDef parseElement(Element element) {
        if (null == element) {
            return null;
        }
        if (!"sub_heading_tail".equalsIgnoreCase(element.getName())) {
            return null;
        }

        try { this.setZOrder(Integer.parseInt(element.attributeValue("z_order")));      } catch (Exception ex) {this.setZOrder(0);}
        try { this.unitYSize = Integer.parseInt(element.attributeValue("unit_y_size")); } catch (Exception ex) {this.unitYSize = 1;}

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

    public PdfPage generate(Document pdf, PageDef pageDef) throws Exception {
        List<Object> components = getComponents();
        Collections.sort(components, IZOrder.comparator);

        for (Object com : components) {
            if (com instanceof IDrawable) {
                Object draw = ((IDrawable) com).generate(pdf, pageDef);
            }
        }

        return null;
    }

    public CatalogSubHeadingTailDef clone() {
        return null;
    }
}
