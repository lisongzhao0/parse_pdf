package com.cooltoo.happy.gene.pdf.template.def;

import com.cooltoo.happy.gene.pdf.template.IVariable;
import com.cooltoo.happy.gene.pdf.template.IXml;
import com.cooltoo.happy.gene.pdf.template.def.element.AbstractDef;
import com.cooltoo.happy.gene.pdf.template.def.element.ImageDef;
import com.cooltoo.happy.gene.pdf.template.def.element.RectDef;
import com.cooltoo.happy.gene.pdf.template.def.element.TextDef;
import com.cooltoo.happy.gene.pdf.template.util.DefFactory;
import com.cooltoo.happy.gene.pdf.template.util.ParametersUtil;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaolisong on 14/07/2017.
 */
public class AreaDef extends AbstractDef implements IXml<AreaDef>, IVariable {

    private float x = 0, y = 0, w = 0, h = 0;
    private float marginL=0,marginT=0,marginR=0,marginB=0;
    private List<AbstractDef> components = new ArrayList<>();

    // Cross page
    private boolean autoLayout;
    private float topY, bottomY;
    private float lastY;

    // debug
    private boolean borderShown;

    public float getX() {
        return x;
    }
    public AreaDef setX(float x) {
        this.x = x;
        return this;
    }

    public float getY() {
        return y;
    }
    public AreaDef setY(float y) {
        this.y = y;
        return this;
    }

    public float getW() {
        return w;
    }
    public AreaDef setW(float w) {
        this.w = w;
        return this;
    }

    public float getH() {
        return h;
    }
    public AreaDef setH(float h) {
        this.h = h;
        return this;
    }

    public float getMarginL() {
        return marginL;
    }
    public AreaDef setMarginL(float marginL) {
        this.marginL = marginL;
        return this;
    }

    public float getMarginT() {
        return marginT;
    }
    public AreaDef setMarginT(float marginT) {
        this.marginT = marginT;
        return this;
    }

    public float getMarginR() {
        return marginR;
    }
    public AreaDef setMarginR(float marginR) {
        this.marginR = marginR;
        return this;
    }

    public float getMarginB() {
        return marginB;
    }
    public AreaDef setMarginB(float marginB) {
        this.marginB = marginB;
        return this;
    }

    public float[] getMargin() {
        return new float[]{this.marginL, this.marginT, this.marginR, this.marginB};
    }
    public AreaDef setMargin(float[] margin) {
        if (null==margin && margin.length>=4) {
            this.marginL = margin[0];
            this.marginT = margin[1];
            this.marginR = margin[2];
            this.marginB = margin[3];
        }
        return this;
    }

    public boolean isAutoLayout() {
        return autoLayout;
    }
    public AreaDef setAutoLayout(boolean autoLayout) {
        this.autoLayout = autoLayout;
        return this;
    }

    public boolean isBorderShown() {
        return borderShown;
    }
    public AreaDef setBorderShown(boolean borderShown) {
        this.borderShown = borderShown;
        return this;
    }

    public float lastY() {
        return lastY;
    }
    public void lastY(float lastY) {
        this.lastY = lastY;
    }

    public float getTopY() {
        return topY;
    }
    public AreaDef setTopY(float topY) {
        this.topY = topY;
        return this;
    }

    public float getBottomY() {
        return bottomY;
    }
    public AreaDef setBottomY(float bottomY) {
        this.bottomY = bottomY;
        return this;
    }

    @Override
    public AreaDef parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"area".equalsIgnoreCase(element.getName())) {
            return null;
        }

        AreaDef newDef = new AreaDef();

        try { newDef.setZOrder(Integer.parseInt(element.attributeValue("z_order"))); } catch (Exception ex) {newDef.setZOrder(0);}
        try { newDef.x = Float.parseFloat(element.attributeValue("x"));     } catch (Exception ex) {newDef.x = 0.0f;}
        try { newDef.y = Float.parseFloat(element.attributeValue("y"));     } catch (Exception ex) {newDef.y = 0.0f;}
        try { newDef.w = Float.parseFloat(element.attributeValue("width")); } catch (Exception ex) {newDef.w = 0.0f;}
        try { newDef.h = Float.parseFloat(element.attributeValue("height"));} catch (Exception ex) {newDef.h = 0.0f;}
        try { newDef.autoLayout = Boolean.parseBoolean(element.attributeValue("auto_layout"));} catch (Exception ex) {newDef.autoLayout= false;}
        try { newDef.topY       = Float.parseFloat(element.attributeValue("top_y"));    } catch (Exception ex) {newDef.topY     = Float.MAX_VALUE;}
        try { newDef.bottomY    = Float.parseFloat(element.attributeValue("bottom_y")); } catch (Exception ex) {newDef.bottomY  = 0.0f;}
        try { newDef.borderShown= Boolean.parseBoolean(element.attributeValue("show_border"));} catch (Exception ex) {newDef.borderShown= false;}

        Float[] margin = null;
        try { margin = ParametersUtil.getInstance().parseFloatArray(element.attributeValue("border_margin")); } catch (Exception ex) { margin = new Float[]{0f, 0f, 0f, 0f}; }
        if (null==margin || margin.length!=4) {
            margin = new Float[]{0f, 0f, 0f, 0f};
        }
        newDef.marginL = margin[0];
        newDef.marginT = margin[1];
        newDef.marginR = margin[2];
        newDef.marginB = margin[3];

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

        float startY = (isAutoLayout() ? lastY() : getY());
        float startX = getX();
        if (isAutoLayout() && (startY-getH())<getBottomY()) {
            setY(getTopY());
            startY = getTopY();
            pdf.getPdfDocument().addNewPage();
            page = pdf.getPdfDocument().getLastPage();
        }
        lastY(startY-getH());

        for (int i = 0; i < components.size(); i ++) {
            AbstractDef tmp     = components.get(i);
            AbstractDef clone   = tmp.clone();

            if (clone instanceof TextDef) {
                TextDef txt = ((TextDef) clone);
                txt.setX(txt.getX() + startX);
                txt.setY(startY - txt.getY());
                txt.generate(pdf, pageDef);
            }
            if (clone instanceof RectDef) {
                RectDef rec = ((RectDef) clone);
                float[] bound = rec.getBound();
                rec.setBound(bound[0]+startX, startY-bound[1], bound[2]+startX, startY-bound[3], bound[4]);
                rec.generate(pdf, pageDef);
            }
            if (clone instanceof ImageDef) {
                ImageDef img = ((ImageDef) clone);
                img.setX(img.getX() + startX);
                img.setY(startY - img.getY() - img.getHeight());
                img.generate(pdf, pageDef);
            }
        }

        if (isBorderShown()) {
            PdfCanvas rect = new PdfCanvas(page);
            rect.saveState();
            rect.setColor(Color.BLACK, false);
            rect.moveTo(x, y);
            rect.lineTo(x + getW(), y);
            rect.lineTo(x + getW(), y - getH());
            rect.lineTo(x, y - getH());
            rect.closePath();
            rect.setLineWidth(1.0f);
            rect.stroke();
            rect.restoreState();
        }

        return page;
    }

    @Override
    public AbstractDef clone() {
        AreaDef newOne = new AreaDef();
        newOne.x    = this.x;
        newOne.y    = this.y;
        newOne.w    = this.w;
        newOne.h    = this.h;

        newOne.marginL=this.marginL;
        newOne.marginT=this.marginT;
        newOne.marginR=this.marginR;
        newOne.marginB=this.marginB;

        // Cross page
        newOne.autoLayout = this.autoLayout;
        newOne.topY     = this.topY;
        newOne.bottomY  = this.bottomY;
        newOne.lastY    = this.lastY();

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
