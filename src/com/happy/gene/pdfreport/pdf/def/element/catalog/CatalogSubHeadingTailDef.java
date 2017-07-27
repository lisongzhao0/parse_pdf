package com.happy.gene.pdfreport.pdf.def.element.catalog;

import com.happy.gene.pdfreport.pdf.IDrawable;
import com.happy.gene.pdfreport.pdf.IPosition;
import com.happy.gene.pdfreport.pdf.IXml;
import com.happy.gene.pdfreport.pdf.IZOrder;
import com.happy.gene.pdfreport.pdf.def.PageDef;
import com.happy.gene.pdfreport.pdf.def.element.*;
import com.happy.gene.pdfreport.pdf.util.DefFactory;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.layout.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
