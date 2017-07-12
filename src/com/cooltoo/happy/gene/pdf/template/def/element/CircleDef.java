package com.cooltoo.happy.gene.pdf.template.def.element;

import com.cooltoo.happy.gene.pdf.template.*;
import com.cooltoo.happy.gene.pdf.template.def.PageDef;
import com.cooltoo.happy.gene.pdf.template.util.ColorUtil;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import org.dom4j.Element;

/**
 * Created by zhaolisong on 02/05/2017.
 */
public class CircleDef extends AbstractDef implements IXml<CircleDef>, IPosition<CircleDef>, IVariable, IBound {

    private float borderWidth;
    private Float dashOn;
    private Float dashOff;
    private String borderColor;
    private float opacity;
    private String fillColor;
    private float cx = 0.0f, cy = 0.0f, r = 0.0f;
    private boolean autoLayout;
    private float lastY;

    public float getBorderWidth() {
        return borderWidth;
    }
    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }

    public Float getDashOn() {
        return dashOn;
    }
    public void setDashOn(Float dashOn) {
        this.dashOn = dashOn;
    }

    public Float getDashOff() {
        return dashOff;
    }
    public void setDashOff(Float dashOff) {
        this.dashOff = dashOff;
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

    public float getCx() {
        return cx;
    }
    public void setCx(float cx) {
        this.cx = cx;
    }

    public float getCy() {
        return cy;
    }
    public void setCy(float cy) {
        this.cy = cy;
    }

    public float getR() {
        return r;
    }
    public void setR(float r) {
        this.r = r;
    }

    public float[] minMaxXY() {
        return new float[]{getCx()-getR(), getCy()-getR(), getCx()+getR(), getCy()+getR()};
    }

    public boolean isAutoLayout() {
        return autoLayout;
    }
    public void setAutoLayout(boolean autoLayout) {
        this.autoLayout = autoLayout;
    }

    public float lastY() {
        return lastY;
    }
    public void lastY(float lastY) {
        this.lastY = lastY;
    }

    public CircleDef translate(float x, float y) {
        this.cx += x;
        this.cy += y;
        return this;
    }

    public CircleDef parseElement(Element element) {
        if (null == element) {
            return null;
        }
        if (!"circle".equalsIgnoreCase(element.getName())) {
            return null;
        }

        this.borderColor = element.attributeValue("border_color");
        this.fillColor = element.attributeValue("fill_color");
        try { this.setZOrder(Integer.parseInt(element.attributeValue("z_order")));            } catch (Exception ex) { this.setZOrder(0); }
        try { this.borderWidth = Float.parseFloat(element.attributeValue("border_width"));    } catch (Exception ex) { this.borderWidth = 0.0f; }
        try { this.opacity     = Float.parseFloat(element.attributeValue("opacity"));         } catch (Exception ex) { this.opacity     = 1.0f; }
        try { this.autoLayout  = Boolean.parseBoolean(element.attributeValue("auto_layout")); } catch (Exception ex) { this.autoLayout  = false; }
        try { this.dashOn      = Float.parseFloat(element.attributeValue("dash_on"));         } catch (Exception ex) { this.dashOn      = null; }
        try { this.dashOff     = Float.parseFloat(element.attributeValue("dash_off"));        } catch (Exception ex) { this.dashOff     = null; }
        try { this.cx = Float.parseFloat(element.attributeValue("cx")); } catch (Exception ex) { this.cx = 1.0f; }
        try { this.cy = Float.parseFloat(element.attributeValue("cy")); } catch (Exception ex) { this.cy = 1.0f; }
        try { this.r  = Float.parseFloat(element.attributeValue("r"));  } catch (Exception ex) { this.r = 1.0f; }
        this.lastY = this.cy - this.r;

        return this;
    }

    public PdfCanvas generate(Document pdf, PageDef pageDef) throws Exception {
        PdfPage page = pdf.getPdfDocument().getLastPage();
        PdfCanvas path = new PdfCanvas(page);
        path.saveState();

        PdfExtGState state = new PdfExtGState();
        state.setFillOpacity(getOpacity());
        path.setExtGState(state);

        if (isAutoLayout()) {
            lastY(lastY() - 2*getR());
            setCy(lastY() + getR());
        }
        if (!(null==dashOff && null==dashOff)) {
            dashOff = null==dashOff ? dashOn  : dashOff;
            dashOn  = null==dashOn  ? dashOff : dashOn;
            path.setLineDash(dashOn, dashOff, dashOn+dashOff);
        }
        path.circle(getCx(), getCy(), getR());
        path.closePath();

        ColorUtil colorUtil = getColorUtil();
        if (null != getFillColor() && !"".equalsIgnoreCase(getFillColor())) {
            path.setFillColor(colorUtil.parseColor(getFillColor(), Color.BLACK));
            path.fill();
        } else {
            path.setColor(colorUtil.parseColor(getBorderColor(), Color.BLACK), false);
            path.setLineWidth(getBorderWidth());
            path.stroke();
        }

        path.restoreState();
        return path;
    }

    public CircleDef clone() {
        CircleDef newOne = new CircleDef();
        newOne.setZOrder(this.getZOrder());
        newOne.borderWidth= this.borderWidth;
        newOne.borderColor= this.borderColor;
        newOne.opacity    = this.opacity;
        newOne.fillColor  = this.fillColor;
        newOne.cx         = this.cx;
        newOne.cy         = this.cy;
        newOne.r          = this.r;
        newOne.autoLayout = this.autoLayout;
        newOne.lastY      =  this.cy - this.r;

        return newOne;
    }
}
