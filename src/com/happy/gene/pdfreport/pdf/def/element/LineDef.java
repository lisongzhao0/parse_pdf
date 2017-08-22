package com.happy.gene.pdfreport.pdf.def.element;

import com.happy.gene.pdfreport.pdf.IBound;
import com.happy.gene.pdfreport.pdf.IPosition;
import com.happy.gene.pdfreport.pdf.IVariable;
import com.happy.gene.pdfreport.pdf.IXml;
import com.happy.gene.pdfreport.pdf.def.PageDef;
import com.happy.gene.pdfreport.pdf.util.ColorUtil;
import com.happy.gene.pdfreport.pdf.util.ParametersUtil;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import org.dom4j.Element;

/**
 * Created by zhaolisong on 16/08/2017.
 */
public class LineDef extends AbstractDef implements IXml<LineDef>, IPosition<LineDef>, IVariable, IBound {

    private String path;
    private float  borderWidth;
    private String borderColor;
    private float  opacity;
    private Float  maxX = null, maxY = null, minX = null, minY = null;
    private boolean autoLayout;
    private float lastY;
    private Float[] borderMargin;//[left, top, right, bottom]

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public float getBorderWidth() {
        return borderWidth;
    }
    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }

    public String getBorderColor() {
        return borderColor;
    }
    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public float getOpacity() {
        return opacity;
    }
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    public float[] minMaxXY() {
        return new float[]{minX, minY, maxX, maxY};
    }

    public float[] getBound() {
        return new float[] {
                null==minX ? 0f : minX,
                null==minY ? 0f : minY,
                null==maxX ? 0f : maxX-minX,
                null==maxY ? 0f : maxY-minY,
                borderMargin[0], borderMargin[1], borderMargin[2], borderMargin[3]};
    }
    public void setBound(float minX, float minY, float width, float height) {
        this.minX = minX;
        this.minY = minY;
        maxX = minX + width;
        maxY = minY + height;
    }
    public void setBound(float minX, float minY, float width, float height,
                         float paddingLeft, float paddingTop, float paddingRight, float paddingBottom) {
        this.minX = minX;
        this.minY = minY;
        maxX = minX + width;
        maxY = minY + height;
        this.borderMargin[0] = paddingLeft;
        this.borderMargin[1] = paddingTop;
        this.borderMargin[2] = paddingRight;
        this.borderMargin[3] = paddingBottom;
    }

    public boolean isAutoLayout() {
        return autoLayout;
    }
    public LineDef setAutoLayout(boolean autoLayout) {
        this.autoLayout = autoLayout;
        return this;
    }

    public float lastY() {
        return lastY;
    }
    public void lastY(float lastY) {
        this.lastY = lastY;
    }

    public LineDef translate(float x, float y) {
        this.maxX += x;
        this.maxY += y;
        this.minX += x;
        this.minY += y;
        return this;
    }

    public LineDef parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"line".equalsIgnoreCase(element.getName())) {
            return null;
        }

        this.catalog = element.attributeValue("catalog");
        this.visible = getStringUtil().isEmpty(element.attributeValue("visible")) ? "true" : element.attributeValue("visible");
        this.path        = element.attributeValue("path");
        this.borderColor = element.attributeValue("border_color");
        try { this.setZOrder(Integer.parseInt(element.attributeValue("z_order")));           } catch (Exception ex) {this.setZOrder(0);}
        try { this.borderWidth = Float.parseFloat(element.attributeValue("border_width"));   } catch (Exception ex) {this.borderWidth= 0.0f;}
        try { this.opacity     = Float.parseFloat(element.attributeValue("opacity"));        } catch (Exception ex) {this.opacity    = 1.0f;}
        try { this.autoLayout  = Boolean.parseBoolean(element.attributeValue("auto_layout"));} catch (Exception ex) {this.autoLayout= false;}
        try { this.borderMargin = ParametersUtil.getInstance().parseFloatArray(element.attributeValue("border_margin")); } catch (Exception ex) { this.borderMargin = new Float[]{0f, 0f, 0f, 0f}; }
        if (null==this.borderMargin || this.borderMargin.length!=4) {
            this.borderMargin = new Float[]{0f, 0f, 0f, 0f};
        }

        this.maxX = null; this.maxY = null; this.minX = null; this.minY = null;
        String[] ctrlPointsArray = null==this.getPath() ? new String[]{} : this.getPath().split(" ");
        for (int i = 0; i < ctrlPointsArray.length; i ++) {
            String cp = ctrlPointsArray[i];
            if ("maxX".equalsIgnoreCase(cp)) {
                try { this.maxX = Float.parseFloat(ctrlPointsArray[i+1]); } catch (Exception ex) {}
                i += 1;
                continue;
            }
            if ("maxY".equalsIgnoreCase(cp)) {
                try { this.maxY = Float.parseFloat(ctrlPointsArray[i+1]); } catch (Exception ex) {}
                i += 1;
                continue;
            }
            if ("minX".equalsIgnoreCase(cp)) {
                try { this.minX = Float.parseFloat(ctrlPointsArray[i+1]); } catch (Exception ex) {}
                i += 1;
                continue;
            }
            if ("minY".equalsIgnoreCase(cp)) {
                try { this.minY = Float.parseFloat(ctrlPointsArray[i+1]); } catch (Exception ex) {}
                i += 1;
                continue;
            }
        }
        this.lastY = null==this.minY ? 0 : this.minY;

        return this;
    }

    public PdfCanvas generate(Document pdf, PageDef pageDef) throws Exception {
        if (!isVisible()) { return null; }
        PdfPage page = pdf.getPdfDocument().getLastPage();
        setPageStartNumberInPdf(pdf.getPdfDocument().getPageNumber(page));
        PdfCanvas path = new PdfCanvas(page);
        path.saveState();

        if (maxX == null || maxY == null || minX == null || minY == null) {
            return null;
        }

        PdfExtGState state = new PdfExtGState();
        state.setStrokeOpacity(getOpacity());
        path.setExtGState(state);
        ColorUtil colorUtil = ColorUtil.getInstance();
        path.setColor(colorUtil.parseColor(getBorderColor(), Color.BLACK), false);
        path.setLineWidth(getBorderWidth());


        if (isAutoLayout()) {
            float height = (maxY-minY/*height*/);
            lastY(lastY() - height - borderMargin[1]);
            minY = lastY();
            maxY = lastY()+height;
            lastY(lastY()- borderMargin[1]);
        }


        path.moveTo(maxX, maxY);
        path.lineTo(minX, minY);
        path.closePath();


        path.stroke();

        path.restoreState();
        return path;
    }


    public LineDef clone() {
        LineDef newOne = new LineDef();
        newOne.setZOrder(this.getZOrder());
        newOne.path        = this.path;
        newOne.borderWidth = this.borderWidth;
        newOne.borderColor = this.borderColor;
        newOne.opacity   = this.opacity;
        newOne.maxX = this.maxX;
        newOne.maxY = this.maxY;
        newOne.minX = this.minX;
        newOne.minY = this.minY;
        if (null!=this.borderMargin) {
            newOne.borderMargin = new Float[this.borderMargin.length];
            for (int i = 0; i < this.borderMargin.length; i ++) {
                newOne.borderMargin[i] = this.borderMargin[i];
            }
        }

        newOne.autoLayout = this.autoLayout;
        newOne.lastY      = null==this.minY ? 0 : this.minY;

        return newOne;
    }
}
