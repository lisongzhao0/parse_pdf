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
 * Created by zhaolisong on 12/04/2017.
 */
public class RectDef extends AbstractDef implements IXml<RectDef>, IPosition<RectDef>, IVariable, IBound {

    private String path;
    private float borderWidth;
    private String borderColor;
    private float opacity;
    private String fillColor;
    private Float maxX = null, maxY = null, minX = null, minY = null, r = null;
    private boolean autoLayout;
    private float lastY;
    private Float[] borderMargin;//[left, top, right, bottom]

    // for table usage
    private String tableSplit = "none";
    private String tableSplitTopBottom = null;

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

    public String getFillColor() {
        return fillColor;
    }
    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    public String getTableSplit() {
        return tableSplit;
    }
    public void setTableSplit(String tableSplit) {
        this.tableSplit = tableSplit;
    }

    public String getTableSplitTopBottom() {
        return tableSplitTopBottom;
    }
    public void setTableSplitTopBottom(String tableSplitTopBottom) {
        this.tableSplitTopBottom = tableSplitTopBottom;
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
                null==r    ? 0f : r,
                borderMargin[0], borderMargin[1], borderMargin[2], borderMargin[3]};
    }
    public void setBound(float minX, float minY, float width, float height, float r) {
        this.minX = minX;
        this.minY = minY;
        maxX = minX + width;
        maxY = minY + height;
        this.r = r;
    }
    public void setBound(float minX, float minY, float width, float height, float r,
                         float paddingLeft, float paddingTop, float paddingRight, float paddingBottom) {
        this.minX = minX;
        this.minY = minY;
        maxX = minX + width;
        maxY = minY + height;
        this.r = r;
        this.borderMargin[0] = paddingLeft;
        this.borderMargin[1] = paddingTop;
        this.borderMargin[2] = paddingRight;
        this.borderMargin[3] = paddingBottom;
    }

    public boolean isAutoLayout() {
        return autoLayout;
    }
    public RectDef setAutoLayout(boolean autoLayout) {
        this.autoLayout = autoLayout;
        return this;
    }

    public float lastY() {
        return lastY;
    }
    public void lastY(float lastY) {
        this.lastY = lastY;
    }

    public RectDef translate(float x, float y) {
        this.maxX += x;
        this.maxY += y;
        this.minX += x;
        this.minY += y;
        return this;
    }

    public RectDef parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"rect".equalsIgnoreCase(element.getName())) {
            return null;
        }

        this.catalog = element.attributeValue("catalog");
        this.visible = getStringUtil().isEmpty(element.attributeValue("visible")) ? "true" : element.attributeValue("visible");
        this.path        = element.attributeValue("path");
        this.borderColor = element.attributeValue("border_color");
        this.fillColor   = element.attributeValue("fill_color");
        this.tableSplit  = element.attributeValue("table_split");
        this.tableSplit  = null==this.tableSplit || "".equals(this.tableSplit) ? "none" : this.tableSplit;
        this.tableSplitTopBottom = element.attributeValue("table_split_top_bottom");
        this.tableSplitTopBottom = null==this.tableSplitTopBottom || "".equals(this.tableSplitTopBottom) ? "T" : this.tableSplitTopBottom;
        try { this.setZOrder(Integer.parseInt(element.attributeValue("z_order")));           } catch (Exception ex) {this.setZOrder(0);}
        try { this.borderWidth = Float.parseFloat(element.attributeValue("border_width"));   } catch (Exception ex) {this.borderWidth= 0.0f;}
        try { this.opacity     = Float.parseFloat(element.attributeValue("opacity"));        } catch (Exception ex) {this.opacity    = 1.0f;}
        try { this.autoLayout  = Boolean.parseBoolean(element.attributeValue("auto_layout"));} catch (Exception ex) {this.autoLayout= false;}
        try { this.borderMargin = ParametersUtil.getInstance().parseFloatArray(element.attributeValue("border_margin")); } catch (Exception ex) { this.borderMargin = new Float[]{0f, 0f, 0f, 0f}; }
        if (null==this.borderMargin || this.borderMargin.length!=4) {
            this.borderMargin = new Float[]{0f, 0f, 0f, 0f};
        }

        this.maxX = null; this.maxY = null; this.minX = null; this.minY = null; this.r = null;
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
            if ("r".equalsIgnoreCase(cp)) {
                try { this.r = Float.parseFloat(ctrlPointsArray[i+1]); } catch (Exception ex) { }
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
        state.setFillOpacity(getOpacity());
        path.setExtGState(state);


        if (isAutoLayout()) {
            float height = (maxY-minY/*height*/);
            lastY(lastY() - height - borderMargin[1]);
            minY = lastY();
            maxY = lastY()+height;
            lastY(lastY()- borderMargin[1]);
        }
        else {
        }

        if (null!=r) {
            path.moveTo(maxX-r, maxY);
            path.curveTo(maxX, maxY, maxX, maxY, maxX, maxY-r);
            path.lineTo(maxX, minY+r);
            path.curveTo(maxX, minY, maxX, minY, maxX-r, minY);
            path.lineTo(minX+r, minY);
            path.curveTo(minX, minY, minX, minY, minX, minY+r);
            path.lineTo(minX, maxY-r);
            path.curveTo(minX, maxY, minX, maxY, minX+r, maxY);
            path.closePath();
        }
        else {
            path.moveTo(maxX, maxY);
            path.lineTo(maxX, minY);
            path.lineTo(minX, minY);
            path.lineTo(minX, maxY);
            path.closePath();
        }

        ColorUtil colorUtil = ColorUtil.getInstance();
        if (null!=getFillColor() && !"".equalsIgnoreCase(getFillColor())) {
            path.setFillColor(colorUtil.parseColor(getFillColor(), Color.BLACK));
            path.fill();
        }
        else {
            path.setColor(colorUtil.parseColor(getBorderColor(), Color.BLACK), false);
            path.setLineWidth(getBorderWidth());
            path.stroke();
        }

        path.restoreState();
        return path;
    }


    public RectDef clone() {
        RectDef newOne = new RectDef();
        newOne.setZOrder(this.getZOrder());
        newOne.path        = this.path;
        newOne.borderWidth = this.borderWidth;
        newOne.borderColor = this.borderColor;
        newOne.opacity   = this.opacity;
        newOne.fillColor = this.fillColor;
        newOne.maxX = this.maxX;
        newOne.maxY = this.maxY;
        newOne.minX = this.minX;
        newOne.minY = this.minY;
        newOne.r = this.r;
        if (null!=this.borderMargin) {
            newOne.borderMargin = new Float[this.borderMargin.length];
            for (int i = 0; i < this.borderMargin.length; i ++) {
                newOne.borderMargin[i] = this.borderMargin[i];
            }
        }

        newOne.autoLayout = this.autoLayout;
        newOne.lastY      = null==this.minY ? 0 : this.minY;
        newOne.tableSplit = this.tableSplit;

        return newOne;
    }
}
