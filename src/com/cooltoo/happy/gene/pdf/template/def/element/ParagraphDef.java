package com.cooltoo.happy.gene.pdf.template.def.element;

import com.cooltoo.happy.gene.pdf.template.def.PageDef;
import com.cooltoo.happy.gene.pdf.template.util.XmlDataCache;
import com.cooltoo.happy.gene.pdf.template.util.XmlDataParser;
import com.cooltoo.happy.gene.pdf.template.util.log.Logger;
import com.cooltoo.happy.gene.pdf.template.*;
import com.cooltoo.happy.gene.pdf.template.def.FontDef;
import com.cooltoo.happy.gene.pdf.template.util.ColorUtil;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.renderer.*;
import com.itextpdf.layout.splitting.DefaultSplitCharacters;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaolisong on 03/05/2017.
 */
public class ParagraphDef extends AbstractDef implements IXml<ParagraphDef>, IPosition<ParagraphDef>, IVariable {

    private String value;

    // content area and position
    private boolean autoLayout;
    private float x;
    private float y;
    private float topY;
    private float bottomY;
    private float width;
    private float lastY;
    private String hAlignment;
    private String vAlignment;

    // font relative parameter
    private String font;
    private float  fontOpacity;
    private Color  fontColor;
    private float  fontSize;
    private Float  lineLeading;
    private Float  charSpacing;
    private Float  wordSpacing;
    private boolean bold;
    private boolean italic;
    private boolean underline;

    public String getValue() {
        return value;
    }
    public ParagraphDef setValue(String value) {
        this.value = value;
        return this;
    }

    public float getX() {
        return x;
    }
    public ParagraphDef setX(float x) {
        this.x = x;
        return this;
    }

    public float getY() {
        return y;
    }
    public ParagraphDef setY(float y) {
        this.y = y;
        return this;
    }

    public float getTopY() {
        return topY;
    }
    public ParagraphDef setTopY(float topY) {
        this.topY = topY;
        return this;
    }

    public float getBottomY() {
        return bottomY;
    }
    public ParagraphDef setBottomY(float bottomY) {
        this.bottomY = bottomY;
        return this;
    }

    public float getWidth() {
        return width;
    }
    public ParagraphDef setWidth(float width) {
        this.width = width;
        return this;
    }

    public String getHAlignment() {
        return hAlignment;
    }
    public ParagraphDef setHAlignment(String hAlignment) {
        this.hAlignment = hAlignment;
        return this;
    }

    public String getVAlignment() {
        return vAlignment;
    }
    public ParagraphDef setVAlignment(String vAlignment) {
        this.vAlignment = vAlignment;
        return this;
    }

    private PdfFont getPdfFont(String fontName) throws Exception {
        PdfFont font = PdfFontFactory.createFont();
        XmlDataCache xmlDataCache = XmlDataCache.getInstance();
        FontDef fontDef = xmlDataCache.getFont(fontName);
        if (fontDef instanceof FontDef) {
            font = fontDef.getFont();
        }
        return font;
    }
    public String getFont() {
        return font;
    }
    public ParagraphDef setFont(String font) {
        this.font = font;
        return this;
    }

    public float getFontOpacity() {
        return fontOpacity;
    }
    public ParagraphDef setFontOpacity(float fontOpacity) {
        this.fontOpacity = fontOpacity;
        return this;
    }

    public float getFontSize() {
        return fontSize;
    }
    public ParagraphDef setFontSize(float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public Color getFontColor() {
        return fontColor;
    }
    public ParagraphDef setFontColor(Color fontColor) {
        this.fontColor = fontColor;
        return this;
    }

    public Float getLineLeading() {
        return lineLeading;
    }
    public ParagraphDef setLineLeading(Float lineLeading) {
        this.lineLeading = lineLeading;
        return this;
    }

    public Float getCharSpacing() {
        return charSpacing;
    }
    public ParagraphDef setCharSpacing(Float charSpacing) {
        this.charSpacing = charSpacing;
        return this;
    }

    public Float getWordSpacing() {
        return wordSpacing;
    }
    public ParagraphDef setWordSpacing(Float wordSpacing) {
        this.wordSpacing = wordSpacing;
        return this;
    }

    public boolean isBold() {
        return bold;
    }
    public ParagraphDef setBold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public boolean isItalic() {
        return italic;
    }
    public ParagraphDef setItalic(boolean italic) {
        this.italic = italic;
        return this;
    }

    public boolean isUnderline() {
        return underline;
    }
    public ParagraphDef setUnderline(boolean underline) {
        this.underline = underline;
        return this;
    }

    public float lastY() {
        return lastY;
    }
    public void lastY(float lastY) {
        if (autoLayout) {
            this.lastY = lastY;
        }
    }

    public ParagraphDef translate(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public ParagraphDef parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"paragraph".equalsIgnoreCase(element.getName())) {
            return null;
        }

        try { this.setZOrder(Integer.parseInt(element.attributeValue("z_order")));   } catch (Exception ex) {this.setZOrder(0);}
        try { this.x         = Float.parseFloat(element.attributeValue("x"));        } catch (Exception ex) {this.x      = 0.0f;}
        try { this.y         = Float.parseFloat(element.attributeValue("y"));        } catch (Exception ex) {this.y      = 0.0f;}
        try { this.topY      = Float.parseFloat(element.attributeValue("top_y"));    } catch (Exception ex) {this.topY   = Float.MAX_VALUE;}
        try { this.bottomY   = Float.parseFloat(element.attributeValue("bottom_y")); } catch (Exception ex) {this.bottomY= 0.0f;}
        try { this.width     = Float.parseFloat(element.attributeValue("width"));    } catch (Exception ex) {this.width  = 0.0f;}
        try { this.autoLayout= Boolean.parseBoolean(element.attributeValue("auto_layout"));} catch (Exception ex) {this.autoLayout = false;}
        this.lastY = this.y;
        this.hAlignment = element.attributeValue("h_alignment");
        this.hAlignment = (null==this.hAlignment||"".equals(this.hAlignment)) ? "L" : this.hAlignment;
        this.vAlignment = element.attributeValue("v_alignment");
        this.vAlignment = (null==this.vAlignment||"".equals(this.vAlignment)) ? "C" : this.vAlignment;

        this.font      = element.attributeValue("font");
        this.fontColor = getColorUtil().parseColor(element.attributeValue("color"), Color.BLACK);
        try { this.fontOpacity= Float.parseFloat(element.attributeValue("font_opacity")); } catch (Exception ex) {this.fontOpacity= 1.0f;}
        try { this.fontSize   = Float.parseFloat(element.attributeValue("font_size"));    } catch (Exception ex) {this.fontSize   = 12f;}
        try { this.lineLeading= Float.parseFloat(element.attributeValue("line_leading")); } catch (Exception ex) {this.lineLeading=null;}
        try { this.charSpacing= Float.parseFloat(element.attributeValue("char_spacing")); } catch (Exception ex) {this.charSpacing=0.0f;}
        try { this.wordSpacing= Float.parseFloat(element.attributeValue("word_spacing")); } catch (Exception ex) {this.wordSpacing=0.0f;}
        try { this.bold       = Boolean.parseBoolean(element.attributeValue("bold"));     } catch (Exception ex) {this.bold       =false;}
        try { this.italic     = Boolean.parseBoolean(element.attributeValue("italic"));   } catch (Exception ex) {this.italic     =false;}
        try { this.underline  = Boolean.parseBoolean(element.attributeValue("underline"));} catch (Exception ex) {this.underline  =false;}

        this.value = element.attributeValue("value");
        if (null==this.value || "".equalsIgnoreCase(this.value)) {
            this.value = element.getTextTrim();
        }
        if (null!=this.value && this.value.contains("®")) {
            this.value.replace('®', '\u00ae');
        }
        return this;
    }

    private String[] replaceBlankTag(String[] values) {
        if (null==values || values.length==0) {
            return values;
        }
        StringBuilder finalStr = new StringBuilder();
        for (int i = 0; i < values.length; i ++) {
            String tmp = values[i];
            tmp = tmp.replace("<br/>", "\n");
            tmp = tmp.replace("&nbsp;", " ");
            tmp = tmp.replace("&tab;", "\t");
            if (tmp.contains("®")) {
                tmp = tmp.replace('®', '\u00ae');
            }
            values[i] = tmp;
        }

        return values;
    }

    private String[] parseStyleLine(String value) {
        if (null==value) {
            return null;
        }
        if ("".equals(value)) {
            return new String[]{value};
        }
        value = getParametersUtil().replaceParameter(value);
        StringBuilder parseStyle = new StringBuilder();
        List<String> res = new ArrayList<>();
        for (int i = 0; i < value.length(); i ++) {
            parseStyle.append(value.charAt(i));
            if (parseStyle.lastIndexOf("<style")>0){
                res.add(parseStyle.substring(0, parseStyle.lastIndexOf("<style")));
                parseStyle.delete(0, parseStyle.lastIndexOf("<style"));
            }
            if (parseStyle.lastIndexOf("</style>")>0) {
                res.add(parseStyle.substring(0, parseStyle.lastIndexOf("</style>")+"</style>".length()));
                parseStyle.delete(0, parseStyle.lastIndexOf("</style>")+"</style>".length());
            }
        }
        if (parseStyle.length()>0) {
            if (res.size()<=0) {
                res.add(parseStyle.toString());
            }
            else {
                String lastOne = res.get(res.size() - 1);
                if (lastOne.endsWith("</style>")) {
                    res.add(parseStyle.toString());
                }
                else {
                    res.remove(res.size() - 1);
                    res.add(lastOne+parseStyle.toString());
                }
            }
        }
        return replaceBlankTag(res.toArray(new String[res.size()]));
    }

    private List<Style> parseStyle(String value) {
        String[] values = parseStyleLine(value);
        if (null==values || values.length==0) {
            return null;
        }
        List<Style> allParts = new ArrayList<>();
        for (int i = 0; i < values.length; i ++) {
            String val = values[i];
            Style style = Style.getStyle(val, getXmlDataParser());
            allParts.add(style);
        }

        return allParts;
    }

    public Object generate(Document pdf, PageDef pageDef) throws Exception {
        PdfPage page = pdf.getPdfDocument().getLastPage();
        List<Style> allParts = parseStyle(getValue());
        if (null==page || null==allParts || allParts.isEmpty()) {
            return null;
        }
        splitPartsOnPage(allParts);

        float startY = lastY();
        float height = lastY() - getBottomY();
        if (height<0) {
            startY = getTopY();
            pdf.getPdfDocument().addNewPage();
            pageDef.rendererPage(pdf, pageDef);
            page = pdf.getPdfDocument().getLastPage();
        }

        Paragraph paragraph = null;
        int           pageIndex           = Integer.MIN_VALUE;
        float         onPageContentHeight = 0.0f;
        for (int i=0; i < allParts.size(); i ++) {
            Style tmp = allParts.get(i);
            if (null==tmp.value || "".equals(tmp.value)) {
                continue;
            }
            if (pageIndex==Integer.MIN_VALUE) {
                pageIndex           = tmp.pageIndex;
                onPageContentHeight = tmp.pageContentHeight;
                paragraph           = getBaseParagraph();
            }
            else if (pageIndex != tmp.pageIndex) {
                paragraph.setFixedPosition(pdf.getPdfDocument().getPageNumber(page), getX(), startY-onPageContentHeight, getWidth());
                pdf.add(paragraph);

                pageIndex           = tmp.pageIndex;
                onPageContentHeight = tmp.pageContentHeight;
                startY              = getTopY();
                pdf.getPdfDocument().addNewPage();
                pageDef.rendererPage(pdf, pageDef);
                page                = pdf.getPdfDocument().getLastPage();
                paragraph           = getBaseParagraph();
            }
            addPartToParagraph(tmp, paragraph);
        }
        paragraph.setFixedPosition(pdf.getPdfDocument().getPageNumber(page), getX(), startY-onPageContentHeight, getWidth());
        lastY = startY-onPageContentHeight;
        pdf.add(paragraph);

        return paragraph;
    }

    private Paragraph getBaseParagraph() throws Exception {
        Paragraph paragraph = new Paragraph();
        paragraph.setFont(getPdfFont(getFont()));
        paragraph.setFontSize(getFontSize());
        paragraph.setCharacterSpacing(getCharSpacing());
        paragraph.setWordSpacing(getWordSpacing());
        paragraph.setOpacity(getFontOpacity());
        if (getWidth()>0) {
            paragraph.setWidth(getWidth());
        }


        String alignment = getHAlignment();
        if ("R".equalsIgnoreCase(alignment)) {
            paragraph.setTextAlignment(TextAlignment.RIGHT);
        } else if ("C".equalsIgnoreCase(alignment)) {
            paragraph.setTextAlignment(TextAlignment.CENTER);
        } else if ("L".equalsIgnoreCase(alignment)) {
            paragraph.setTextAlignment(TextAlignment.LEFT);
        } else if ("J".equalsIgnoreCase(alignment)) {
            paragraph.setTextAlignment(TextAlignment.JUSTIFIED);
        } else if ("JA".equalsIgnoreCase(alignment)) {
            paragraph.setTextAlignment(TextAlignment.JUSTIFIED_ALL);
        }

        alignment = getVAlignment();
        if ("T".equalsIgnoreCase(alignment)) {
            paragraph.setVerticalAlignment(VerticalAlignment.TOP);
        } else if ("M".equalsIgnoreCase(alignment)) {
            paragraph.setVerticalAlignment(VerticalAlignment.MIDDLE);
        } else if ("B".equalsIgnoreCase(alignment)) {
            paragraph.setVerticalAlignment(VerticalAlignment.BOTTOM);
        }

        Color fontColor = getFontColor();
        paragraph.setFontColor(fontColor);

        if (null!= getLineLeading()) {
            paragraph.setMultipliedLeading(getLineLeading());
        }

        return paragraph;
    }

    private Paragraph addPartToParagraph(Style part, Paragraph paragraph) throws Exception {
        if (null==part || null==paragraph || null==part.value) {
            return paragraph;
        }
        Text line = new Text(part.value);
        if (!part.fontStyleSameWithParagraph) {
            PdfFont tmpFont = null;
            if (null!=part.font) {
                tmpFont = getPdfFont(part.font);
            }
            if (null!=tmpFont         ) { line.setFont(tmpFont); }
            if (null!=part.fontColor  ) { line.setFontColor(part.fontColor); }
            if (null!=part.fontSize   ) { line.setFontSize(part.fontSize); }
            if (null!=part.fontOpacity) { line.setOpacity(part.fontOpacity); }
            if (null!=part.charSpacing) { line.setCharacterSpacing(part.charSpacing);}
            if (null!=part.wordSpacing) { line.setWordSpacing(part.wordSpacing);}
            if (part.bold     ) { line.setBold(); }
            if (part.italic   ) { line.setItalic(); }
            if (part.underline) { line.setUnderline(); }
        }
        paragraph.add(line);
        return paragraph;
    }

    private List<Style> splitPartsOnPage(List<Style> allStyleParts) throws Exception {
        Paragraph paragraph = getBaseParagraph();

        for (int i = 0; null!=allStyleParts && i < allStyleParts.size(); i ++) {
            Style section = allStyleParts.get(i);
            addPartToParagraph(section, paragraph);
        }

        ParagraphRenderer renderer = (ParagraphRenderer) paragraph.createRendererSubTree();
        paragraph.setProperty(Property.TEXT_RISE, 0.0f);
        paragraph.setProperty(Property.SPLIT_CHARACTERS, new DefaultSplitCharacters());
        LayoutResult layout = renderer.layout(new LayoutContext(new LayoutArea((int) 0, new Rectangle(0, 0, getWidth(), 10000f))));

        float startY = lastY();
        float height = startY - getBottomY();
        if (height<0) {
            startY = getTopY();
            height = startY - getBottomY();
        }


        StringBuilder onePageContent      = new StringBuilder();
        int           pageIndex           = 0;
        float         onPageContentHeight = 0.0f;
        if (null!=layout.getOccupiedArea()) {
            Rectangle allArea = layout.getOccupiedArea().getBBox();
            if (allArea.getHeight() < height) {
                for (Style tmp : allStyleParts) {
                    if (null==tmp) { continue; }
                    tmp.pageIndex         = pageIndex;
                    tmp.pageContentHeight = allArea.getHeight();
                }
                return allStyleParts;
            }

            float pageContentStartTopLeftY = (allArea.getY()+allArea.getHeight());
            List<LineRenderer> lines = renderer.getLines();
            if (null!=lines) {
                StringBuilder textInRenderer = new StringBuilder();
                for (int i = 0; i < lines.size(); i ++) {
                    LineRenderer tmp = lines.get(i);
                    // 如果没有行高或文本，判断下一行。
                    if (null==tmp.getChildRenderers() || null==tmp.getOccupiedArea()) {
                        continue;
                    }

                    // 计算到该行的高度
                    LayoutArea lineArea = tmp.getOccupiedArea();
                    float lineEndY   = lineArea.getBBox().getY();
                    float lineHeight = lineArea.getBBox().getHeight();
                    float heightHere = pageContentStartTopLeftY - lineEndY;

                    // 计算到该行的高度小于height
                    if (heightHere<=height) {
                        List<IRenderer> renderers = tmp.getChildRenderers();
                        if (null==renderers || renderers.isEmpty()) {
                            continue;
                        }
                        else {
                            for (IRenderer iRenderer : renderers) {
                                if (iRenderer instanceof TextRenderer) {
                                    textInRenderer.append(((TextRenderer) iRenderer).getText().toString());
                                }
                            }
                        }
                        onePageContent.append(textInRenderer.toString());
                        onPageContentHeight = heightHere;
                        textInRenderer.setLength(0);
                        continue;
                    }
                    // 计算有几个style在该页面
                    else {
                        getPartsInContent(onePageContent.toString(), allStyleParts, pageIndex, onPageContentHeight);

                        i                        --;
                        pageIndex                ++;
                        onPageContentHeight      = 0.0f;
                        startY                   = getTopY();
                        height                   = startY - getBottomY();
                        pageContentStartTopLeftY = lineEndY + lineHeight;
                        onePageContent.setLength(0);
                    }
                }
                getPartsInContent(onePageContent.toString(), allStyleParts, pageIndex, onPageContentHeight);

            }
        }
        return allStyleParts;
    }

    private void getPartsInContent(String content, List<Style> allStyleParts, int onPageIndex, float onPageContentHeight) {
        int contentLength= content.length();
        int contentIndex = 0;
        for (; contentIndex < contentLength; contentIndex++) {
            int     styleValueIndex = 0;
            int     styleValueLength= 0;
            boolean styleEndWithMatched = true;
            for (int i = 0; i < allStyleParts.size(); i ++) {
                Style style = allStyleParts.get(i);
                if (style.pageIndex!=Integer.MIN_VALUE) {
                    continue;
                }
                String styleValue      = style.value;
                styleValueIndex = 0;
                styleValueLength= styleValue.length();

                for (; styleValueIndex<styleValueLength && contentIndex<contentLength; styleValueIndex++, contentIndex++) {
                    char contentChar   = content.charAt(contentIndex);
                    char styleValChar  = styleValue.charAt(styleValueIndex);
                    if (styleValChar==contentChar) {
                        styleEndWithMatched = true;
                        continue;
                    }
                    else {
                        styleEndWithMatched = false;
                        if (styleValChar==' ' || styleValChar=='\t' || styleValChar=='\u2028') {
                            String tmp = styleValue.substring(0, styleValueIndex);
                            if (styleValueIndex+1>=styleValueLength) {
                            }
                            else {
                                tmp += styleValue.substring(styleValueIndex + 1);
                            }
                            styleValue  = tmp;
                            style.value = styleValue;
                            styleValueIndex --;
                            styleValueLength--;
                            contentIndex    --;
                            continue;
                        }
                    }
                }
                if (!styleEndWithMatched) {
                    style.pageIndex         = onPageIndex;
                    style.pageContentHeight = onPageContentHeight;
                    continue;
                }
                if (styleValueIndex>=styleValueLength) {
                    style.pageIndex         = onPageIndex;
                    style.pageContentHeight = onPageContentHeight;
                    if (contentIndex>=contentLength) {
                        break;
                    }
                }
                else {
                    if (contentIndex >= contentLength) {
                        Style nextSplit = Style.copy(style);
                        style.value = styleValue.substring(0, styleValueIndex);
                        nextSplit.value = styleValue.substring(styleValueIndex);

                        style.pageIndex         = onPageIndex;
                        style.pageContentHeight = onPageContentHeight;
                        if (i+1>=allStyleParts.size()) {
                            allStyleParts.add(nextSplit);
                        }
                        else {
                            allStyleParts.add(i+1, nextSplit);
                        }
                        break;
                    }
                    else {
                        Logger.error("parse paragraph has error: {}<-->{}", content, style.value);
                    }
                }

            }
        }

        return;
    }

    private static class Style {

        private int     index = 0;
        private int     pageIndex = Integer.MIN_VALUE;
        private float   pageContentHeight = 0.0f;
        // font relative parameter
        private boolean fontStyleSameWithParagraph;
        private Float   width;
        private String  font;
        private Float   fontOpacity;
        private Color   fontColor;
        private Float   fontSize;
        private Float   lineLeading;
        private Float   charSpacing;
        private Float   wordSpacing;
        private Boolean bold;
        private Boolean italic;
        private Boolean underline;

        private String value;
        private boolean used;

        public static Style copy(Style src) {
            Style one = new Style();

            one.index       = src.index;
            one.pageIndex   = src.pageIndex;
            // font relative parameter
            one.fontStyleSameWithParagraph = src.fontStyleSameWithParagraph;
            one.width       = src.width;
            one.font        = src.font;
            one.fontColor   = src.fontColor;
            one.fontSize    = src.fontSize;
            one.lineLeading = src.lineLeading;
            one.charSpacing = src.charSpacing;
            one.wordSpacing = src.wordSpacing;
            one.bold        = src.bold;
            one.italic      = src.italic;
            one.underline   = src.underline;
            one.value       = src.value;
            one.used        = src.used;

            return one;
        }

        public static Style getStyle(String value, XmlDataParser xmlDataParser) {
            Style style = new Style();

            String eleFormatValue = value;
            if (value.startsWith("<style") && value.endsWith("</style>")) {
            }
            else {
                eleFormatValue = "<style>" + value + "</style>";
            }
            Element element = xmlDataParser.getRootElement(eleFormatValue.getBytes());
            if (null==element || !"style".equalsIgnoreCase(element.getName())) {
                style.value = value;
                return style;
            }

            ColorUtil colorUtil = ColorUtil.getInstance();
            List attributes = element.attributes();
            if (null==attributes || attributes.isEmpty()) {
                style.fontStyleSameWithParagraph = true;
            }
            else {
                style.font      = element.attributeValue("font");
                style.fontColor = colorUtil.parseColor(element.attributeValue("font_color"), null);
                style.bold      = Boolean.parseBoolean(element.attributeValue("bold"));
                style.italic    = Boolean.parseBoolean(element.attributeValue("italic"));
                style.underline = Boolean.parseBoolean(element.attributeValue("underline"));
                try {style.fontOpacity = Float.parseFloat(element.attributeValue("font_opacity"));} catch (Exception ex) {style.fontOpacity = null;}
                try {style.fontSize    = Float.parseFloat(element.attributeValue("font_size"));   } catch (Exception ex) {style.fontSize    = null;}
                try {style.width       = Float.parseFloat(element.attributeValue("width"));       } catch (Exception ex) {style.width       = null;}
                try {style.lineLeading = Float.parseFloat(element.attributeValue("line_leading"));} catch (Exception ex) {style.lineLeading = null;}
                try {style.charSpacing = Float.parseFloat(element.attributeValue("char_spacing"));} catch (Exception ex) {style.charSpacing = null;}
                try {style.wordSpacing = Float.parseFloat(element.attributeValue("word_spacing"));} catch (Exception ex) {style.wordSpacing = null;}
                if (null == style.font      || "".equals(style.font)     ) { style.font      = null; }
                if (null == style.fontColor || "".equals(style.fontColor)) { style.fontColor = null; }
            }

            style.value = element.attributeValue("value");
            if (null==style.value || "".equalsIgnoreCase(style.value)) {
                style.value = element.getText();
            }
            if (null!=style.value && style.value.contains("®")) {
                style.value.replace('®', '\u00ae');
            }
            return style;
        }

        public String toString() {
            StringBuilder msg = new StringBuilder();
            msg.append(getClass()).append("@").append(hashCode()).append("[");
            msg.append("font=").append(font).append(",");
            msg.append("font_size=").append(fontSize).append(",");
            msg.append("value=").append(value).append("]");
            return value ;
        }
    }



    public ParagraphDef clone() {
        ParagraphDef newOne = new ParagraphDef();
        newOne.setZOrder(this.getZOrder());
        newOne.value   = this.value;
        newOne.x       = this.x;
        newOne.y       = this.y;
        newOne.topY    = this.topY;
        newOne.bottomY = this.bottomY;
        newOne.width   = this.width;
        newOne.lastY   = this.y;
        newOne.hAlignment= this.hAlignment;
        newOne.vAlignment= this.vAlignment;
        newOne.font      = this.font;
        newOne.fontSize  = this.fontSize;
        newOne.fontColor = this.fontColor;
        newOne.fontOpacity = this.fontOpacity;
        newOne.lineLeading = this.lineLeading;
        newOne.charSpacing = this.charSpacing;
        newOne.wordSpacing = this.wordSpacing;
        newOne.bold      = this.bold;
        newOne.italic    = this.italic;
        newOne.underline = this.underline;

        return newOne;
    }
}
