package com.happy.gene.pdfreport.pdf.def.element.table;

import com.happy.gene.pdfreport.pdf.IXml;
import com.happy.gene.pdfreport.pdf.def.PageDef;
import com.happy.gene.pdfreport.pdf.def.element.AbstractDef;
import com.happy.gene.pdfreport.pdf.def.element.RectDef;
import com.happy.gene.pdfreport.pdf.def.element.TextDef;
import com.happy.gene.pdfreport.pdf.util.DefFactory;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.layout.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaolisong on 28/06/2017.
 */
public class HeaderAreaDef extends AbstractDef implements IXml<HeaderAreaDef> {

    private float x;
    private float y;
    private float width;
    private float height;
    private List<AbstractDef> components = new ArrayList<>();

    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }
    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }
    public void setHeight(float height) {
        this.height = height;
    }

    @Override
    public HeaderAreaDef parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"header_area".equalsIgnoreCase(element.getName())) {
            return null;
        }

        HeaderAreaDef newDef = new HeaderAreaDef();

        try { newDef.x      = Float.parseFloat(element.attributeValue("x"));      } catch (Exception ex) {newDef.x      = 0.0f;}
        try { newDef.y      = Float.parseFloat(element.attributeValue("y"));      } catch (Exception ex) {newDef.y      = 0.0f;}
        try { newDef.width  = Float.parseFloat(element.attributeValue("width"));  } catch (Exception ex) {newDef.width  = 0.0f;}
        try { newDef.height = Float.parseFloat(element.attributeValue("height")); } catch (Exception ex) {newDef.height = 0.0f;}

        List<Element> subEle = element.elements();
        DefFactory defFactory = DefFactory.getIntance();
        for (Element ele : subEle) {
            AbstractDef node = defFactory.getDef(ele);
            if (null!=node) {
                newDef.components.add(node);
            }

        }
        return newDef;
    }

    @Override
    public Object generate(Document pdf, PageDef pageDef) throws Exception {
        PdfPage page = pdf.getPdfDocument().getLastPage();

        float startY = getY() - getHeight();
        float startX = getX();

        for (int i = 0; i < components.size(); i ++) {
            AbstractDef tmp     = components.get(i);
            AbstractDef clone   = tmp.clone();

            if (clone instanceof TextDef) {
                TextDef txt = ((TextDef) clone);
                txt.setX(txt.getX() + startX);
                txt.setY(startY + txt.getY());
                txt.generate(pdf, pageDef);
            }
            if (clone instanceof RectDef) {
                RectDef rec = ((RectDef) clone);
                rec.translate(startX, startY);
                rec.generate(pdf, pageDef);
            }
        }

        return page;
    }

    @Override
    public AbstractDef clone() {
        HeaderAreaDef newOne = new HeaderAreaDef();
        newOne.x        = this.x;
        newOne.y        = this.y;
        newOne.width    = this.width;
        newOne.height   = this.height;

        for (AbstractDef obj : this.components) {
            if (obj instanceof AbstractDef) {
                AbstractDef clone = obj.clone();
                if (null!=clone) {
                    newOne.components.add(clone);
                }
            }
        }
        return newOne;
    }
}
