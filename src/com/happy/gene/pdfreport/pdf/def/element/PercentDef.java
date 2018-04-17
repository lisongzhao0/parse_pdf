package com.happy.gene.pdfreport.pdf.def.element;

import com.happy.gene.pdfreport.pdf.IVariable;
import com.happy.gene.pdfreport.pdf.IXml;
import com.happy.gene.pdfreport.pdf.def.FontDef;
import com.happy.gene.pdfreport.pdf.def.PageDef;
import com.happy.gene.pdfreport.pdf.util.ColorUtil;
import com.happy.gene.pdfreport.pdf.util.ParametersUtil;
import com.happy.gene.pdfreport.pdf.util.XmlDataCache;
import com.happy.gene.pdfreport.pdf.util.log.Logger;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.layout.MinMaxWidthLayoutResult;
import com.itextpdf.layout.minmaxwidth.MinMaxWidth;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.renderer.LineRenderer;
import com.itextpdf.layout.renderer.ParagraphRenderer;
import com.itextpdf.layout.splitting.DefaultSplitCharacters;
import org.dom4j.Element;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhaolisong on 26/04/2017.
 */
public class PercentDef extends AbstractDef implements IXml<PercentDef>, IVariable {

    private float  fullOpacity;
    private String fullColor;
    private float  fullX;
    private float  fullY;
    private float  fullWidth;
    private float  fullHeight;
    private Float  r;
    private Float[] padding; // left, top, right, bottom
    private boolean autoLayout;
    private float   lastY;
    private String  title;

    private boolean showScale;
    private boolean showScaleRegion;
    private boolean showScaleRegionDescription;
    private String  showScaleRegionDescriptionFont;
    private float   showScaleRegionDescriptionYOffset = 0.0f;
    private boolean showLasScale;
    private float   showScaleFontSize;
    private String  showScaleFontColor;
    private float   showPercentFontSize;
    private float   regionOrigin = 0f;
    private String[] regionDescription;
    private String[] regionColors;
    private Float[]  regionScale;

    private String percentNum;

    public float getFullOpacity() {
        return fullOpacity;
    }
    public void setFullOpacity(float fullOpacity) {
        this.fullOpacity = fullOpacity;
    }

    public String getFullColor() {
        if (null!=fullColor && fullColor.indexOf('$')>=0) {
            return ParametersUtil.getInstance().replaceParameter(fullColor);
        }
        return fullColor;
    }
    public void setFullColor(String fullColor) {
        this.fullColor = fullColor;
    }

    public float getFullX() {
        return fullX;
    }
    public void setFullX(float fullX) {
        this.fullX = fullX;
    }

    public float getFullY() {
        return fullY;
    }
    public void setFullY(float fullY) {
        this.fullY = fullY;
    }

    public float getFullWidth() {
        return fullWidth;
    }
    public void setFullWidth(float fullWidth) {
        this.fullWidth = fullWidth;
    }

    public float getFullHeight() {
        return fullHeight;
    }
    public void setFullHeight(float fullHeight) {
        this.fullHeight = fullHeight;
    }

    public float getWidth() {
        if (isShowScale()) {
            try {
                Float[] padding= getPadding();
                Float[] scales = getRegionScale();
                float   max    = scales[scales.length-1];
                float   origin = getRegionOrigin();
                float   percent= getFloatPercentNum();
                float   width  = (getFullWidth() -padding[0]-padding[2]);

                float minX = getFullX() + padding[0];
                float maxX = minX + width * Math.abs((percent-origin)/(max-origin));
                if (maxX>(minX + (getFullWidth() -padding[0]-padding[2]))) {
                    maxX = minX + (getFullWidth() -padding[0]-padding[2]);
                }

                String strScale = "" + getPercentNum();
                strScale = strScale.substring(0, strScale.indexOf(".") + 2);
                PdfFont font = PdfFontFactory.createRegisteredFont(FontConstants.TIMES.toLowerCase());
                float f_width = font.getWidth(strScale, getShowScaleFontSize());
                float f_gap = 4;
                float percentX = maxX + f_gap;

                float totalWidth = getFullWidth();
                if (((getFullX() + getFullWidth()) - (percentX + f_width)) < 0f) {
                    totalWidth = percentX + f_width - getFullX();
                }
                return totalWidth;
            }
            catch (Exception ex) {}
        }
        return getFullWidth();
    }
    public float getHeight() {
        if (isShowScale()) {
            String strScale = "1.0";
            try {
                PdfFont font = PdfFontFactory.createRegisteredFont(FontConstants.TIMES.toLowerCase());
                float f_ascent = font.getAscent(strScale, getShowScaleFontSize());
                float f_descent = font.getDescent(strScale, getShowScaleFontSize());
                float f_gap = 4;
                float totalHeight = getFullHeight() + f_ascent + f_descent + f_gap;
                return totalHeight;
            }
            catch (Exception ex) {}
        }
        return getFullHeight();
    }

    public Float getR() {
        return r;
    }
    public void setR(Float r) {
        this.r = r;
    }

    public Float[] getPadding() {
        return padding;
    }
    public void setPadding(Float[] padding) {
        this.padding = padding;
    }

    public boolean isAutoLayout() {
        return autoLayout;
    }
    public void setAutoLayout(boolean autoLayout) {
        this.autoLayout = autoLayout;
    }

    public boolean isShowScale() {
        return showScale;
    }
    public void setShowScale(boolean showScale) {
        this.showScale = showScale;
    }

    public boolean isShowScaleRegion() {
        return showScaleRegion;
    }
    public void setShowScaleRegion(boolean showScaleRegion) {
        this.showScaleRegion = showScaleRegion;
    }

    public boolean isShowScaleRegionDescription() {
        return showScaleRegionDescription;
    }
    public void setShowScaleRegionDescription(boolean showScaleRegionDescription) {
        this.showScaleRegionDescription = showScaleRegionDescription;
    }

    public String getShowScaleRegionDescriptionFont() {
        return showScaleRegionDescriptionFont;
    }
    public void setShowScaleRegionDescriptionFont(String showScaleRegionDescriptionFont) {
        this.showScaleRegionDescriptionFont = showScaleRegionDescriptionFont;
    }

    public Float getShowScaleRegionDescriptionYOffset() {
        return showScaleRegionDescriptionYOffset;
    }
    public void setShowScaleRegionDescriptionYOffset(Float showScaleRegionDescriptionYOffset) {
        this.showScaleRegionDescriptionYOffset = showScaleRegionDescriptionYOffset;
    }

    public boolean isShowLasScale() {
        return showLasScale;
    }
    public void setShowLasScale(boolean showLasScale) {
        this.showLasScale = showLasScale;
    }

    public String getShowScaleFontColor() {
        if (null!=showScaleFontColor && showScaleFontColor.indexOf('$')>=0) {
            return ParametersUtil.getInstance().replaceParameter(showScaleFontColor);
        }
        return showScaleFontColor;
    }
    public void setShowScaleFontColor(String showScaleFontColor) {
        this.showScaleFontColor = showScaleFontColor;
    }

    public float getShowScaleFontSize() {
        return showScaleFontSize;
    }
    public void setShowScaleFontSize(float showScaleFontSize) {
        this.showScaleFontSize = showScaleFontSize;
    }

    public float getShowPercentFontSize() {
        return showPercentFontSize;
    }
    public void setShowPercentFontSize(float showPercentFontSize) {
        this.showPercentFontSize = showPercentFontSize;
    }

    public float getRegionOrigin() {
        return regionOrigin;
    }
    public void setRegionOrigin(float regionOrigin) {
        this.regionOrigin = regionOrigin;
    }

    public String[] getRegionDescription() {
        return regionDescription;
    }
    public void setRegionDescription(String[] regionDescription) {
        this.regionDescription = regionDescription;
    }

    public String[] getRegionColors() {
        String[] tmpColors = null;
        for (int i=0,size=null==regionColors ? 0 : regionColors.length; i<size; i++) {
            if (null==tmpColors) { tmpColors = new String[size]; }
            tmpColors[i] = regionColors[i];
            if (null!=tmpColors[i] && tmpColors[i].indexOf('$')>=0) {
                tmpColors[i] = ParametersUtil.getInstance().replaceParameter(tmpColors[i]);
            }
        }
        if (null!=tmpColors) { return tmpColors; }
        return regionColors;
    }
    public void setRegionColors(String[] regionColors) {
        this.regionColors = regionColors;
    }

    public Float[] getRegionScale() {
        return regionScale;
    }
    public void setRegionScale(Float[] regionScale) {
        this.regionScale = regionScale;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getPercentNum() {
        return percentNum;
    }
    public void setPercentNum(String percentNum) {
        this.percentNum = percentNum;
    }
    public Float getFloatPercentNum() {
        try {
            return Float.parseFloat(percentNum);
        } catch (Exception ex) {
            return 0.0f;
        }
    }

    public float lastY() {
        return lastY;
    }
    public void lastY(float lastY) {
        this.lastY = lastY;
    }

    public PercentDef parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"percent".equalsIgnoreCase(element.getName())) {
            return null;
        }
        ParametersUtil paramUtil = getParametersUtil();
        ColorUtil      colorUtil = getColorUtil();

        this.catalog = element.attributeValue("catalog");
        this.visible = getStringUtil().isEmpty(element.attributeValue("visible")) ? "true" : element.attributeValue("visible");
        try { this.setZOrder(Integer.parseInt(element.attributeValue("z_order")));   } catch (Exception ex) {this.setZOrder(0);}
        try { this.fullOpacity = Float.parseFloat(element.attributeValue("opacity"));} catch (Exception ex) {this.fullOpacity= 1.0f;}
        this.fullColor = element.attributeValue("color");
        this.showScaleFontColor = element.attributeValue("show_scale_font_color");
        if (null==this.showScaleFontColor || "".equalsIgnoreCase(this.showScaleFontColor)) {
            this.showScaleFontColor  = this.fullColor;
        }
        try { this.fullX     = Float.parseFloat(element.attributeValue("x"));        } catch (Exception ex) {this.fullX= 0.0f;}
        try { this.fullY     = Float.parseFloat(element.attributeValue("y"));        } catch (Exception ex) {this.fullY= 0.0f;}
        try { this.fullWidth = Float.parseFloat(element.attributeValue("width"));    } catch (Exception ex) {this.fullWidth= 0.0f;}
        try { this.fullHeight= Float.parseFloat(element.attributeValue("height"));   } catch (Exception ex) {this.fullHeight= 0.0f;}
        try { this.r         = Float.parseFloat(element.attributeValue("r"));        } catch (Exception ex) {this.r= 0.0f;}
        try { this.padding = paramUtil.parseFloatArray(element.attributeValue("padding"));} catch (Exception ex) {}
        if (null==this.padding || this.padding.length!=4) {
            this.padding = new Float[]{0f, 0f, 0f, 0f};
        }
        this.lastY = this.fullY;

        this.percentNum     = element.attributeValue("percent");
        this.autoLayout     = Boolean.parseBoolean(element.attributeValue("auto_layout"));
        this.showScale      = Boolean.parseBoolean(element.attributeValue("show_scale"));
        this.showScaleRegion= Boolean.parseBoolean(element.attributeValue("show_scale_region"));
        this.showScaleRegionDescription = Boolean.parseBoolean(element.attributeValue("show_scale_region_description"));
        this.showScaleRegionDescriptionFont = element.attributeValue("show_scale_region_description_font");
        this.showScaleRegionDescriptionYOffset = paramUtil.parseFloat(element.attributeValue("show_scale_region_description_y_offset"), 0.0f);
        try { this.showLasScale   = Boolean.parseBoolean(element.attributeValue("show_last_scale"));  } catch (Exception ex) {this.showLasScale   = false;}
        try { this.showScaleFontSize  = Float.parseFloat(element.attributeValue("show_scale_font_size"));  } catch (Exception ex) {this.showScaleFontSize   = 9.0f;}
        try { this.showPercentFontSize= Float.parseFloat(element.attributeValue("show_percent_font_size"));} catch (Exception ex) {this.showPercentFontSize = 9.0f;}
        try { this.regionOrigin = Float.parseFloat(element.attributeValue("origin"));             } catch (Exception ex) {this.regionOrigin = 0.0f;}
        try { this.regionColors = paramUtil.parseStringArray(element.attributeValue("region_color"));  } catch (Exception ex) {this.regionColors = null;}
        try { this.regionScale  = paramUtil.parseFloatArray(element.attributeValue("region_scale"));   } catch (Exception ex) {this.regionScale  = null;}
        try { this.regionDescription = paramUtil.parseStringArray(element.attributeValue("region_description"));  } catch (Exception ex) {this.regionDescription = null;}
        if (null==this.regionScale) {
            this.regionScale = new Float[]{1f};
        }
        if (null==this.regionColors || this.regionColors.length != this.regionScale.length) {
            this.regionColors = new String[this.regionScale.length];
            for (int i = 0; i < this.regionColors.length; i ++) {
                this.regionColors[i] = colorUtil.getRandomColor();
            }
        }
        if (null==this.regionDescription || this.regionDescription.length != this.regionScale.length) {
            this.regionDescription = new String[this.regionScale.length];
            for (int i = 0; i < this.regionDescription.length; i ++) {
                this.regionDescription[i] = "";
            }
        }

        return this;
    }

    public PdfCanvas generate(Document pdf, PageDef pageDef) throws Exception {
        if (!isVisible()) { return null; }
        PdfPage page = pdf.getPdfDocument().getLastPage();
        PdfCanvas path = new PdfCanvas(page);
        int pageNumber = pdf.getPdfDocument().getPageNumber(page);
        setPageStartNumberInPdf(pageNumber);

        ParametersUtil parametersUtil = ParametersUtil.getInstance();
        String strPercentNum = getPercentNum();
        strPercentNum = parametersUtil.replaceParameter(strPercentNum);
        setPercentNum(strPercentNum);

        if (!isShowScaleRegion() && !isShowScaleRegionDescription()) {
            generateBase(path);
        }
        float totalHeight = getFullHeight();
        if (isShowScale()) {
            totalHeight = generateScale(path);
        }
        if (!isShowScaleRegion() && isShowScaleRegionDescription()) {
            generateScaleRegionDescription(path, pageNumber, pdf);
        }
        if (isShowScaleRegion() && !isShowScaleRegionDescription()) {
            generateScaleRegion(path);
        }
        if (!isShowScaleRegion() && !isShowScaleRegionDescription()) {
            generatePercent(path);
        }

        lastY(lastY() - totalHeight);
        return path;
    }

    private void generateBase(PdfCanvas path) {
        path.saveState();

        PdfExtGState state = new PdfExtGState();
        state.setFillOpacity(getFullOpacity());
        path.setExtGState(state);

        float minX = getFullX();
        float maxY = getFullY();
        if (isAutoLayout()) {
            maxY = lastY();
        }
        float maxX = minX + getFullWidth();
        float minY = maxY - getFullHeight();
        if (null!=r && r>0.0000001f) {
            path.moveTo(maxX-r, minY);
            path.curveTo(maxX, minY, maxX, minY, maxX, minY+r);
            path.lineTo(maxX, maxY-r);
            path.curveTo(maxX, maxY, maxX, maxY, maxX-r, maxY);
            path.lineTo(minX+r, maxY);
            path.curveTo(minX, maxY, minX, maxY, minX, maxY-r);
            path.lineTo(minX, minY+r);
            path.curveTo(minX, minY, minX, minY, minX+r, minY);
            path.closePath();
        }
        else {
            path.moveTo(maxX, minY);
            path.lineTo(maxX, maxY);
            path.lineTo(minX, maxY);
            path.lineTo(minX, minY);
            path.closePath();
        }

        ColorUtil colorUtil = ColorUtil.getInstance();
        path.setFillColor(colorUtil.parseColor(getFullColor(), Color.BLACK));
        path.fill();
        path.restoreState();
    }

    private float generatePercent(PdfCanvas path) {
        path.saveState();
        if (null==getRegionScale()) {
            return 0f;
        }
        if (null==percentNum || "".equalsIgnoreCase(percentNum)) {
            return 0f;
        }

        String[]colors = getRegionColors();
        Float[] padding= getPadding();
        Float[] scales = getRegionScale();
        float   max    = scales[scales.length-1];
        float   origin = getRegionOrigin();
        float   percent= getFloatPercentNum();

        float   width  = (getFullWidth() -padding[0]-padding[2]);
        float   height = (getFullHeight()-padding[1]-padding[3]);

        float minX = getFullX() + padding[0];
        float maxY = getFullY() - padding[1];
        if (isAutoLayout()) {
            maxY = lastY() - padding[1];
        }
        float maxX = minX + width * Math.abs((percent-origin)/(max-origin));
        if (maxX>(minX + (getFullWidth() -padding[0]-padding[2]))) {
            maxX = minX + (getFullWidth() -padding[0]-padding[2]);
        }
        float minY = maxY - height;

        PdfExtGState state = new PdfExtGState();
        state.setFillOpacity(getFullOpacity());
        path.setExtGState(state);

        ColorUtil colorUtil = ColorUtil.getInstance();
        Color color = null;
        float preScale = origin;
        for (int i = 0; i < scales.length; i ++) {
            if (percent>=preScale && percent<scales[i]) {
                color = colorUtil.parseColor(colors[i], Color.BLACK);
                break;
            }
            if (i+1>=scales.length) {
                if (percent>=scales[i]) {
                    color = colorUtil.parseColor(colors[i], Color.BLACK);
                    break;
                }
                if (percent<=origin) {
                    color = colorUtil.parseColor(colors[0], Color.BLACK);
                    break;
                }

            }
        }

        path.moveTo(maxX, minY);
        path.lineTo(maxX, maxY);
        path.lineTo(minX, maxY);
        path.lineTo(minX, minY);
        path.closePath();

        path.setFillColor(color);
        path.fill();

        float totalWidth = getFullWidth();
        if (isShowScale()) {
            // show percent
            try {
                String strScale = ""+getPercentNum();
                PdfFont font = PdfFontFactory.createRegisteredFont(FontConstants.TIMES.toLowerCase());
                FontDef fontDef = XmlDataCache.getInstance().getFont(getShowScaleRegionDescriptionFont());
                if (null!=fontDef) {
                    font = fontDef.getFont();
                }

                Paragraph paragraph = new Paragraph(strScale+"\n ");
                paragraph.setFixedPosition(0, 0, 0, 1000f);
                paragraph.setFont(font).setFontSize(getShowScaleFontSize());
                paragraph.setProperty(Property.TEXT_RISE, 0.0f);
                paragraph.setProperty(Property.SPLIT_CHARACTERS, new DefaultSplitCharacters());
                ParagraphRenderer renderer = (ParagraphRenderer) paragraph.createRendererSubTree();
                LayoutResult layout = renderer.layout(new LayoutContext(new LayoutArea((int) 0, new Rectangle(0, 0, 10000f, 10000f))));
                List<LineRenderer> lineRenderers = renderer.getLines();
                LineRenderer lineRenderer = lineRenderers.get(0);
                Rectangle allTextRect   = layout.getOccupiedArea().getBBox();
                Rectangle firstLineRect = lineRenderer.getOccupiedArea().getBBox();
                float firstLineGap = allTextRect.getY()+allTextRect.getHeight()-(firstLineRect.getY()+firstLineRect.getHeight());
                float lineHeight = firstLineRect.getHeight();
                float f_width  = lineRenderer.getOccupiedArea().getBBox().getWidth();
                float f_gap    = 4;

                float percentX = maxX + f_gap;
                float percentY = maxY - (getFullHeight() - lineHeight)/2 - firstLineGap;
                color = ((getFullX()+getFullWidth())-(percentX+f_width))<1f ? color : Color.WHITE;
                path.saveState()
                    .beginText()
                    .setLeading(1)
                    .setColor(color, false)
                    .setColor(color, true)
                    .setFontAndSize(font, getShowPercentFontSize())
                    .setTextMatrix(1.1f, 0, 0, 1.1f, percentX, percentY)
                    //.moveText(percentX, percentY)
                    .showText(strScale)
                    .endText()
                    .restoreState()
                ;
                totalWidth = Color.WHITE.equals(color) ? getFullWidth() : (percentX+f_width-getFullX());
            } catch (IOException e) {
                Logger.error("show scale has error; {}", e.getMessage());
            }
        }

        path.restoreState();
        return totalWidth;
    }

    private float generateScale(PdfCanvas path) {
        path.saveState();
        if (null==getRegionScale()) {
            return 0;
        }

        Float[] padding= getPadding();
        Float[] scales = getRegionScale();
        float   max    = scales[scales.length-1];
        float   origin = getRegionOrigin();

        float   width  = (getFullWidth() -padding[0]-padding[2]);
        float   height = getFullHeight();

        float minX = getFullX() + padding[0];
        float maxX = 0.0f;
        float maxY = getFullY() - height;
        if (isAutoLayout()) {
            maxY = lastY() - height;
        }
        float minY = maxY + height;

        String scaleColor = getShowScaleFontColor();
        Color scaleFontColor = getColorUtil().parseColor(scaleColor, Color.BLACK);

        PdfExtGState state = new PdfExtGState();
        state.setFillOpacity(getFullOpacity());
        path.setExtGState(state);

        float totalHeight = getFullHeight();
        for (int i = -1; i < scales.length; i ++) {
            float scale = origin;
            if (i>=0) {
                scale = scales[i];
            }
            maxX = minX + (scale-origin)/(max-origin)*width;
            if (i>=0 && i+1<scales.length) {
                path.saveState()
                    .setColor(Color.WHITE, false)
                    .setLineWidth(0.3f)
                    .moveTo(maxX, minY)
                    .lineTo(maxX, maxY)
                    .closePath()
                    .stroke()
                    .restoreState()
                ;
            }

            String strScale = ""+scale;
            strScale = strScale.substring(0, strScale.indexOf(".") + 2);
            try {
                PdfFont font = PdfFontFactory.createRegisteredFont(FontConstants.TIMES.toLowerCase());
                FontDef fontDef = XmlDataCache.getInstance().getFont(getShowScaleRegionDescriptionFont());
                if (null!=fontDef) {
                    font = fontDef.getFont();
                }
                float f_ascent = font.getAscent(strScale, getShowScaleFontSize());
                float f_descent= font.getDescent(strScale, getShowScaleFontSize());
                float f_width  = font.getWidth(strScale, getShowScaleFontSize());
                float f_gap    = 4;
                if (i+1<scales.length) {
                    path.saveState()
                        .beginText()
                        .setColor(scaleFontColor, false)
                        .setColor(scaleFontColor, true)
                        .setFontAndSize(font, getShowScaleFontSize())
                        .moveText(maxX - f_width/2, maxY - f_ascent - f_descent - f_gap)
                        .showText(strScale)
                        .endText()
                        .restoreState()
                    ;
                }
                else if (isShowLasScale()) { // show last scale)
                    path.saveState()
                        .beginText()
                        .setColor(scaleFontColor, false)
                        .setColor(scaleFontColor, true)
                        .setFontAndSize(font, getShowScaleFontSize())
                        .moveText(maxX - f_width/2, maxY - f_ascent - f_descent - f_gap)
                        .showText(strScale)
                        .endText()
                        .restoreState()
                    ;
                }
                totalHeight = getFullHeight() + f_ascent + f_descent + f_gap;
            } catch (IOException e) {
                Logger.error("show scale has error; {}", e.getMessage());
            }
        }

        path.restoreState();
        return totalHeight;
    }

    private float generateScaleRegion(PdfCanvas path) {
        path.saveState();
        if (null==getRegionScale()) {
            return 0;
        }

        Float[]  padding= getPadding();
        Float[]  scales = getRegionScale();
        String[] colors = getRegionColors();
        float    max    = scales[scales.length-1];
        float    origin = getRegionOrigin();

        float   width  = (getFullWidth() -padding[0]-padding[2]);
        float   height = getFullHeight();

        float minX = getFullX() + padding[0];
        float maxX = 0.0f;
        float maxY = getFullY() - height;
        if (isAutoLayout()) {
            maxY = lastY() - height;
        }
        float minY = maxY + height;
        if (minY > maxY) {
            float tmp = minY;
            minY = maxY;
            maxY = tmp;
        }

        PdfExtGState state = new PdfExtGState();
        state.setFillOpacity(getFullOpacity());
        path.setExtGState(state);

        float totalHeight = getFullHeight();
        float totalRegion = max-origin;
        for (int i = -1; i < scales.length; i ++) {
            float scale = origin;
            if (i>=0) {
                scale = scales[i];
            }
            maxX = minX + (scale-origin)/totalRegion*width;
            if (i>=0 && i<scales.length) {
                path.saveState()
                        .setColor(getColorUtil().parseColor(colors[i], Color.WHITE), true)
                        .setLineWidth(0.3f)
                        .moveTo(maxX, minY)
                        .lineTo(maxX, maxY)
                        .lineTo(minX, maxY)
                        .lineTo(minX, minY)
                        .closePath()
                        .fill()
                        .restoreState()
                ;
                minX  = maxX;
                origin= scale;
            }
        }

        path.restoreState();
        return totalHeight;
    }

    private float generateScaleRegionDescription(PdfCanvas path, int pageNumber, Document pdf) throws IOException {
        path.saveState();
        if (null==getRegionScale()) {
            return 0;
        }

        Float[]  padding= getPadding();
        Float[]  scales = getRegionScale();
        String[] colors = getRegionColors();
        String[] descrs = getRegionDescription();
        float    max    = scales[scales.length-1];
        float    origin = getRegionOrigin();

        float   width  = (getFullWidth() -padding[0]-padding[2]);
        float   height = getFullHeight();

        float minX = getFullX() + padding[0];
        float maxX = 0.0f;
        float maxY = getFullY() - height;
        if (isAutoLayout()) {
            maxY = lastY() - height;
        }
        float minY = maxY + height;
        if (minY > maxY) {
            float tmp = minY;
            minY = maxY;
            maxY = tmp;
        }



        PdfExtGState state = new PdfExtGState();
        state.setFillOpacity(getFullOpacity());
        path.setExtGState(state);

        float totalHeight = getFullHeight();
        float totalRegion = max-origin;
        for (int i = -1; i < scales.length; i ++) {
            float scale = origin;
            if (i>=0) {
                scale = scales[i];
            }
            maxX = minX + (scale-origin)/totalRegion*width;
            if (i>=0 && i<scales.length) {
                path.saveState()
                        .setStrokeColor(getColorUtil().parseColor(colors[i], Color.WHITE))
                        .setLineWidth(1.f)
                        .moveTo(maxX, minY)
                        .lineTo(maxX, maxY)
                        .lineTo(minX, maxY)
                        .lineTo(minX, minY)
                        .closePath()
                        .stroke()
                        .restoreState()
                ;
                String descr = descrs[i];
                if (null!=descr && !"".equalsIgnoreCase(descr)) {
                    Paragraph text = new Paragraph(descr);
                    text.setFixedPosition(0, 0, 0, 1000f);
                    FontDef fontDef = XmlDataCache.getInstance().getFont(getShowScaleRegionDescriptionFont());
                    if (null!=fontDef) {
                        text.setFont(fontDef.getFont());
                    }
                    text.setFontSize(getShowScaleFontSize());
                    text.setFontColor(getColorUtil().parseColor(colors[i], Color.RED));
                    text.setProperty(Property.TEXT_RISE, 0.0f);
                    text.setProperty(Property.SPLIT_CHARACTERS, new DefaultSplitCharacters());
                    ParagraphRenderer renderer = (ParagraphRenderer) text.createRendererSubTree();
                    LayoutResult layout = renderer.layout(new LayoutContext(new LayoutArea((int) 0, new Rectangle(0, 0, 10000f, 10000f))));
                    if (layout instanceof MinMaxWidthLayoutResult && null!=layout.getOccupiedArea()) {
                        MinMaxWidthLayoutResult minMaxWidthLayout = (MinMaxWidthLayoutResult) layout;
                        MinMaxWidth maxWidth = minMaxWidthLayout.getMinMaxWidth();
                        float textW = maxWidth.getChildrenMaxWidth();
                        float textX = ((maxX - minX) - textW) / 2 + minX;
                        float textY = minY + getShowScaleRegionDescriptionYOffset();
                        text.setFixedPosition(pageNumber, textX, textY, textW);
                        pdf.add(text);
                    }
                }
                minX  = maxX;
                origin= scale;
            }
        }

        path.restoreState();
        return totalHeight;
    }

    public PercentDef clone() {
        PercentDef newOne = new PercentDef();
        newOne.setZOrder(this.getZOrder());
        newOne.fullOpacity = this.fullOpacity;
        newOne.fullColor   = this.fullColor;
        newOne.fullX       = this.fullX;
        newOne.fullY       = this.fullY;
        newOne.fullWidth   = this.fullWidth;
        newOne.fullHeight  = this.fullHeight;
        newOne.r           = this.r;
        newOne.showScale   = this.showScale;
        newOne.showLasScale= this.showLasScale;
        newOne.showScaleRegion = this.showScaleRegion;
        newOne.showScaleRegionDescription = this.showScaleRegionDescription;
        newOne.showScaleRegionDescriptionFont = this.showScaleRegionDescriptionFont;
        newOne.showScaleRegionDescriptionYOffset = 0.0f;
        newOne.showScaleFontColor  = this.showScaleFontColor;
        newOne.showScaleFontSize   = this.showScaleFontSize;
        newOne.showPercentFontSize = this.showPercentFontSize;
        newOne.regionOrigin= this.regionOrigin;
        newOne.percentNum  = this.percentNum;
        if (null!=this.padding) {
            newOne.padding = new Float[this.padding.length];
            for (int i = 0; i < this.padding.length; i ++) {
                newOne.padding[i] = this.padding[i];
            }
        }
        if (null!=this.regionColors) {
            newOne.regionColors = new String[this.regionColors.length];
            for (int i = 0; i < this.regionColors.length; i ++) {
                newOne.regionColors[i] = this.regionColors[i];
            }
        }
        if (null!=this.regionScale) {
            newOne.regionScale = new Float[this.regionScale.length];
            for (int i = 0; i < this.regionScale.length; i ++) {
                newOne.regionScale[i] = this.regionScale[i];
            }
        }
        if (null!=this.regionDescription) {
            newOne.regionDescription = new String[this.regionDescription.length];
            for (int i = 0; i < this.regionDescription.length; i ++) {
                newOne.regionDescription[i] = this.regionDescription[i];
            }
        }

        newOne.autoLayout = this.autoLayout;
        newOne.lastY      = this.lastY;
        newOne.title = this.title;

        return newOne;

    }
}
