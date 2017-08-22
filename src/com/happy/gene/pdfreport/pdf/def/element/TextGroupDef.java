package com.happy.gene.pdfreport.pdf.def.element;

import com.happy.gene.pdfreport.pdf.IPosition;
import com.happy.gene.pdfreport.pdf.IVariable;
import com.happy.gene.pdfreport.pdf.IXml;
import com.happy.gene.pdfreport.pdf.def.PageDef;
import com.happy.gene.pdfreport.pdf.util.ColorUtil;
import com.happy.gene.pdfreport.pdf.util.DefFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.renderer.LineRenderer;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaolisong on 20/04/2017.
 */
public class TextGroupDef extends AbstractDef implements IXml<TextGroupDef>, IPosition<TextGroupDef>, IVariable {

    private float x;
    private float y;
    private float lastY;
    private float topY;
    private float bottomY;
    private float width;
    private float height;
    private String hAlign;
    private String vAlign;
    private String calWidth;
    private String calHeight;
    private boolean autoLayout;
    private boolean showBorder;
    private List<TextDef> components = new ArrayList<>();
    private RectDef prefixShape;

    public RectDef getPrefixShape() {
        return prefixShape;
    }
    public List<TextDef> getComponents() {
        return components;
    }

    public float getX() {
        return x;
    }
    public TextGroupDef setX(float x) {
        this.x = x;
        return this;
    }

    public float getY() {
        return y;
    }
    public TextGroupDef setY(float y) {
        this.y = y;
        return this;
    }

    public float getTopY() { return topY; }
    public TextGroupDef setTopY(float topY) {
        this.topY = topY;
        return this;
    }

    public float getBottomY() { return bottomY; }
    public TextGroupDef setBottomY(float bottomY) {
        this.bottomY = bottomY;
        return this;
    }

    public float getWidth() {
        return width;
    }
    public TextGroupDef setWidth(float width) {
        this.width = width;
        return this;
    }

    public float getHeight() {
        return height;
    }
    public TextGroupDef setHeight(float height) {
        this.height = height;
        return this;
    }

    public String getCalWidth() {
        return calWidth;
    }
    public String getCalHeight() {
        return calHeight;
    }

    public String getHAlign() {
        return null==hAlign ? "" : hAlign.toLowerCase();
    }
    public String getVAlign() {
        return null==vAlign ? "" : vAlign.toLowerCase();
    }

    public boolean isAutoLayout() {
        return autoLayout;
    }
    public TextGroupDef setAutoLayout(boolean autoLayout) {
        this.autoLayout = autoLayout;
        return this;
    }

    public boolean isShowBorder() { return showBorder; }
    public TextGroupDef setShowBorder(boolean showBorder) {
        this.showBorder = showBorder;
        return this;
    }

    public float lastY() {
        return lastY;
    }
    public void lastY(float lastY) {
        this.lastY = lastY;
    }

    public TextGroupDef translate(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public TextGroupDef parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"text_box".equalsIgnoreCase(element.getName())) {
            return null;
        }

        this.catalog = element.attributeValue("catalog");
        this.visible = getStringUtil().isEmpty(element.attributeValue("visible")) ? "true" : element.attributeValue("visible");
        this.hAlign = element.attributeValue("h_alignment");
        this.hAlign = null==this.hAlign ? null : (!"LCR".contains(this.hAlign.toUpperCase()) ? null : this.hAlign);
        this.vAlign = element.attributeValue("v_alignment");
        this.vAlign = null==this.vAlign ? null : (!"TCB".contains(this.vAlign.toUpperCase()) ? null : this.vAlign);
        this.calWidth = element.attributeValue("cal_width");
        this.calWidth = null==this.calWidth ? null : (!"MS".contains(this.calWidth.toUpperCase()) ? null : this.calWidth);
        this.calHeight = element.attributeValue("cal_height");
        this.calHeight = null==this.calHeight ? null : (!"MS".contains(this.calHeight.toUpperCase()) ? null : this.calHeight);
        try { this.setZOrder(Integer.parseInt(element.attributeValue("z_order")));} catch (Exception ex) {this.setZOrder(0);}
        try { this.topY   = Float.parseFloat(element.attributeValue("top_y"));    } catch (Exception ex) {this.topY   = Float.MAX_VALUE;}
        try { this.bottomY= Float.parseFloat(element.attributeValue("bottom_y")); } catch (Exception ex) {this.bottomY= 0.0f;}
        try { this.showBorder= Boolean.parseBoolean(element.attributeValue("show_border")); } catch (Exception ex) {this.showBorder= false;}
        try { this.x      = Float.parseFloat(element.attributeValue("x"));        } catch (Exception ex) {this.x      = 0.0f;}
        try { this.y      = Float.parseFloat(element.attributeValue("y"));        } catch (Exception ex) {this.y      = 0.0f;}
        try { this.width  = Float.parseFloat(element.attributeValue("width"));    } catch (Exception ex) {this.width  = 0.0f;}
        try { this.height = Float.parseFloat(element.attributeValue("height"));   } catch (Exception ex) {this.height = 0.0f;}
        try { this.autoLayout  = Boolean.parseBoolean(element.attributeValue("auto_layout"));} catch (Exception ex) {this.autoLayout= false;}

        this.lastY(this.getY());

        DefFactory defFactory = DefFactory.getIntance();
        List<Element> subEle = element.elements();
        for (Element ele : subEle) {
            AbstractDef newText = defFactory.getDef(ele);
            if (newText instanceof TextDef) {
                this.components.add((TextDef) newText);
            }
            AbstractDef newPrefixShape = defFactory.getDef(ele);
            if (newPrefixShape instanceof RectDef) {
                this.prefixShape = (RectDef) newPrefixShape;
            }
        }

        return this;
    }

    public Object generate(Document pdf, PageDef pageDef) throws Exception {
        if (!isVisible()) { return null; }
        PdfPage page = pdf.getPdfDocument().getLastPage();
        if (getComponents().isEmpty()) {
            return null;
        }
        if (isAutoLayout()) {
            if (lastY() - getHeight() < getBottomY()) {
                lastY(getTopY());
                pdf.getPdfDocument().addNewPage();
                pageDef.rendererPage(pdf, pageDef);
                page = pdf.getPdfDocument().getLastPage();
            }
        }

        int pageNumber = pdf.getPdfDocument().getPageNumber(page);
        setPageStartNumberInPdf(pageNumber);

        Rectangle whfl = null;
        Float tmpWidth  = null;
        Float tmpHeight = null;
        List<LineRenderer> lineRenderers = new ArrayList<>();
        for (TextDef tmp : components) {
            tmp.getAreaLine(getWidth(), lineRenderers);
            whfl = (lineRenderers.isEmpty()) ? null : lineRenderers.get(0).getOccupiedArea().getBBox();
            if (null==whfl) {
                continue;
            }
            if (tmp.isCalculateWidthInTextBox()) {
                if ("M".equalsIgnoreCase(getCalWidth())) { // max width
                    tmpWidth = (null==tmpWidth || tmpWidth<whfl.getWidth()) ? whfl.getWidth() : tmpWidth;
                }
                else if ("S".equalsIgnoreCase(getCalWidth())) { // sum width
                    tmpWidth = null==tmpWidth ? whfl.getWidth() : (tmpWidth + whfl.getWidth());
                }
            }
            if (tmp.isCalculateHeightInTextBox()) {
                if ("M".equalsIgnoreCase(getCalHeight())) { // max height
                    tmpHeight = (null==tmpHeight || tmpHeight<whfl.getHeight()) ? whfl.getHeight() : tmpHeight;
                }
                else if ("S".equalsIgnoreCase(getCalHeight())) { // sum height
                    tmpHeight = null==tmpHeight ? whfl.getHeight() : (tmpHeight + whfl.getHeight());
                }
            }

            lineRenderers.clear();
        }

        // fit width
        if (null!=tmpWidth) { setWidth(tmpWidth); }
        // fit height
        if (null!=tmpHeight) { setHeight(tmpHeight); }

        Rectangle pageSize = page.getPageSize();
        if ("L".equalsIgnoreCase(getHAlign())) {// left of page
            setX(0);
        }
        else if ("C".equalsIgnoreCase(getHAlign())) {// center of page
            setX((pageSize.getWidth()-getWidth())/2f);
        }
        else if ("R".equalsIgnoreCase(getHAlign())) {// right of page
            setX(pageSize.getWidth() - getWidth());
        }

        if ("T".equalsIgnoreCase(getVAlign())) {// top of page
            setY(pageSize.getHeight() - getHeight());
        }
        else if ("C".equalsIgnoreCase(getVAlign())) {// center of page
            setX((pageSize.getHeight()-getHeight())/2f);
        }
        else if ("B".equalsIgnoreCase(getVAlign())) {// bottom of page
            setY(0);
        }

        PdfCanvas canvas = new PdfCanvas(page);
        canvas.saveState();


        if (isAutoLayout()) {
            setY(lastY()-getHeight());
        }

        float tlx = getX();
        float tly = getY() + getHeight();
        ColorUtil colorUtil = ColorUtil.getInstance();
        if (null!=getPrefixShape()) {
            //minX, minY, width, height, r, paddingLeft, paddingTop, paddingRight, paddingBottom
            float[] bound = getPrefixShape().getBound();
            bound[0] = tlx-bound[7]-bound[2];
            bound[1] = getY()+bound[8];
            canvas.setFillColor(colorUtil.parseColor(getPrefixShape().getFillColor(), Color.BLACK));
            canvas.moveTo(bound[0], bound[1]);
            canvas.lineTo(bound[0], bound[1] + bound[3]);
            canvas.lineTo(bound[0]+bound[2], bound[1] + bound[3]);
            canvas.lineTo(bound[0]+bound[2], bound[1]);
            canvas.closePath();
            canvas.fill();
        }
        for (TextDef tmp : components) {
            tmp = tmp.clone();
            tmp.getAreaLine(getWidth(), lineRenderers);
            whfl = (lineRenderers.isEmpty()) ? null : lineRenderers.get(0).getOccupiedArea().getBBox();
            if (null==whfl) {
                continue;
            }
            tmp.setX(tlx+tmp.getX());
            tmp.setY(tly-tmp.getY()-whfl.getHeight());
            tmp.generate(pdf, pageDef);
            lineRenderers.clear();
        }
        canvas.restoreState();

        if (isShowBorder()) {
            canvas.saveState();
            canvas.setStrokeColor(colorUtil.parseColor("#000000", Color.BLACK));
            canvas.moveTo(getX(), getY());
            canvas.lineTo(getX(), getY() + getHeight());
            canvas.lineTo(getX()+getWidth(), getY() + getHeight());
            canvas.lineTo(getX()+getWidth(), getY());
            canvas.closePath();
            canvas.stroke();
            canvas.restoreState();
        }

        lastY(getY());
        return canvas;
    }

    public TextGroupDef clone() {
        TextGroupDef newOne = new TextGroupDef();
        newOne.setZOrder(this.getZOrder());
        newOne.x          = this.x;
        newOne.y          = this.y;
        newOne.topY       = this.topY;
        newOne.bottomY    = this.bottomY;
        newOne.width      = this.width;
        newOne.height     = this.height;
        newOne.hAlign     = this.hAlign;
        newOne.vAlign     = this.vAlign;
        newOne.calWidth   = this.calWidth;
        newOne.calHeight  = this.calHeight;
		newOne.autoLayout = this.autoLayout;
        newOne.showBorder = this.showBorder;
        newOne.prefixShape= null!=this.prefixShape ? this.prefixShape.clone() : null;
        if (this.components instanceof List) {
            for (TextDef tmp : this.components) {
                newOne.components.add(tmp.clone());
            }
        }

        return newOne;
    }
}
