package com.happy.gene.pdfreport.pdf.def.element;

import com.happy.gene.pdfreport.pdf.IPosition;
import com.happy.gene.pdfreport.pdf.IVariable;
import com.happy.gene.pdfreport.pdf.IXml;
import com.happy.gene.pdfreport.pdf.def.FontDef;
import com.happy.gene.pdfreport.pdf.def.PageDef;
import com.happy.gene.pdfreport.pdf.util.ColorUtil;
import com.happy.gene.pdfreport.pdf.util.ParametersUtil;
import com.happy.gene.pdfreport.pdf.util.XmlDataCache;
import com.happy.gene.pdfreport.pdf.util.XmlDataParser;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.*;
import com.itextpdf.layout.renderer.LineRenderer;
import com.itextpdf.layout.renderer.ParagraphRenderer;
import com.itextpdf.layout.splitting.DefaultSplitCharacters;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaolisong on 12/04/2017.
 */
public class TextDef extends AbstractDef implements IXml<TextDef>, IPosition<TextDef>, IVariable {

    private String value;
    private String body;
    private String font;
    private float opacity;
    private float  fontSize;
    private String color;
    private boolean bold;
    private boolean italic;
    private boolean underline;
    private float x;
    private float y;
    private float width;
    private String hAlignment;
    private String vAlignment;
    private Float  lineLeading;
    private Float  charSpacing;
    private Float  wordSpacing;
    private Float  textRise;
    private boolean calculateWidthInTextBox;
    private boolean calculateHeightInTextBox;

    private boolean autoLayout;
    private float   lastY;
    private boolean crossPage;
    private float   topY;
    private float   bottomY;
    private float   marginTop;

    public String getValue() {
        return value;
    }
    public TextDef setValue(String value) {
        this.value = value;
        return this;
    }

    public PdfFont getPdfFont(String fontName, PdfFont defaultFont) throws Exception {
        PdfFont font = defaultFont;
        XmlDataCache xmlDataCache = XmlDataCache.getInstance();
        FontDef fontDef = xmlDataCache.getFont(fontName);
        if (fontDef instanceof FontDef) {
            font = fontDef.getFont();
        }
        return font;
    }
    private String getFont() {
        return font;
    }
    public TextDef setFont(String font) {
        this.font = font;
        return this;
    }

    public float getOpacity() {
        return opacity;
    }
    public TextDef setOpacity(float opacity) {
        this.opacity = opacity;
        return this;
    }

    public String getColor() {
        return color;
    }
    public TextDef setColor(String color) {
        this.color = color;
        return this;
    }

    public float getFontSize() {
        return fontSize;
    }
    public TextDef setFontSize(float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public boolean isBold() {
        return bold;
    }
    public TextDef setBold(boolean bold) {
        this.bold = bold;
        return this;
    }

    public boolean isItalic() {
        return italic;
    }
    public TextDef setItalic(boolean italic) {
        this.italic = italic;
        return this;
    }

    public boolean isUnderline() {
        return underline;
    }
    public TextDef setUnderline(boolean underline) {
        this.underline = underline;
        return this;
    }

    public float getX() {
        return x;
    }
    public TextDef setX(float x) {
        this.x = x;
        return this;
    }

    public float getY() {
        return y;
    }
    public TextDef setY(float y) {
        this.y = y;
        return this;
    }

    public float getWidth() {
        return width;
    }
    public TextDef setWidth(float width) {
        this.width = width;
        return this;
    }

    public String getHAlignment() {
        return hAlignment;
    }
    public TextDef setHAlignment(String hAlignment) {
        this.hAlignment = hAlignment;
        return this;
    }

    public String getVAlignment() {
        return vAlignment;
    }
    public TextDef setVAlignment(String vAlignment) {
        this.vAlignment = vAlignment;
        return this;
    }

    public Float getLineLeading() {
        return lineLeading;
    }
    public TextDef setLineLeading(Float lineLeading) {
        this.lineLeading = lineLeading;
        return this;
    }

    public Float getCharSpacing() {
        return charSpacing;
    }
    public TextDef setCharSpacing(Float charSpacing) {
        this.charSpacing = charSpacing;
        return this;
    }

    public Float getWordSpacing() {
        return wordSpacing;
    }
    public TextDef setWordSpacing(Float wordSpacing) {
        this.wordSpacing = wordSpacing;
        return this;
    }

    public Float getTextRise() {
        return textRise;
    }
    public void setTextRise(Float textRise) {
        this.textRise = textRise;
    }

    public boolean isCalculateWidthInTextBox() {
        return calculateWidthInTextBox;
    }
    public TextDef setCalculateWidthInTextBox(boolean calculateWidthInTextBox) {
        this.calculateWidthInTextBox = calculateWidthInTextBox;
        return this;
    }

    public boolean isCalculateHeightInTextBox() {
        return calculateHeightInTextBox;
    }
    public TextDef setCalculateHeightInTextBox(boolean calculateHeightInTextBox) {
        this.calculateHeightInTextBox = calculateHeightInTextBox;
        return this;
    }

    public boolean isAutoLayout() {
        return autoLayout;
    }
    public TextDef setAutoLayout(boolean autoLayout) {
        this.autoLayout = autoLayout;
        return this;
    }

    public boolean isCrossPage() {
        return crossPage;
    }
    public TextDef setCrossPage(boolean crossPage) {
        this.crossPage = crossPage;
        return this;
    }

    public float getTopY() {
        return topY;
    }
    public TextDef setTopY(float topY) {
        this.topY = topY;
        return this;
    }

    public float getBottomY() {
        return bottomY;
    }
    public TextDef setBottomY(float bottomY) {
        this.bottomY = bottomY;
        return this;
    }

    public float getMarginTop() {
        return marginTop;
    }
    public void setMarginTop(float marginTop) {
        this.marginTop = marginTop;
    }

    public float lastY() {
        return lastY;
    }
    public void lastY(float lastY) {
        this.lastY = lastY;
    }

    public TextDef translate(float x, float y) {
        setX(getX() + x);
        setY(getY() + y);
        return this;
    }

    public TextDef parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"text".equalsIgnoreCase(element.getName())) {
            return null;
        }

        this.catalog = element.attributeValue("catalog");
        this.visible = getStringUtil().isEmpty(element.attributeValue("visible")) ? "true" : element.attributeValue("visible");
        this.value = element.attributeValue("value");
        this.font  = element.attributeValue("font");
        this.color = element.attributeValue("color");
        this.color = (null==this.color||"".equals(this.color)) ? "#000000" : this.color;
        this.hAlignment = element.attributeValue("h_alignment");
        this.hAlignment = (null==this.hAlignment||"".equals(this.hAlignment)) ? "L" : this.hAlignment;
        this.vAlignment = element.attributeValue("v_alignment");
        this.vAlignment = (null==this.vAlignment||"".equals(this.vAlignment)) ? "C" : this.vAlignment;

        try { this.setZOrder(Integer.parseInt(element.attributeValue("z_order")));      } catch (Exception ex) {this.setZOrder(0);}
        try { this.opacity   = Float.parseFloat(element.attributeValue("opacity"));      } catch (Exception ex) {this.opacity = 1.0f;}
        try { this.fontSize  = Float.parseFloat(element.attributeValue("font_size"));    } catch (Exception ex) {this.fontSize = 12f;}
        try { this.x         = Float.parseFloat(element.attributeValue("x"));            } catch (Exception ex) {this.x        = 0.0f;}
        try { this.y         = Float.parseFloat(element.attributeValue("y"));            } catch (Exception ex) {this.y        = 0.0f;}
        try { this.width     = Float.parseFloat(element.attributeValue("width"));        } catch (Exception ex) {this.width    = 0.0f;}
        try { this.lineLeading= Float.parseFloat(element.attributeValue("line_leading"));} catch (Exception ex) {this.lineLeading=null;}
        try { this.charSpacing= Float.parseFloat(element.attributeValue("char_spacing"));} catch (Exception ex) {this.charSpacing=0.0f;}
        try { this.wordSpacing= Float.parseFloat(element.attributeValue("word_spacing"));} catch (Exception ex) {this.wordSpacing=0.0f;}
        try { this.textRise   = Float.parseFloat(element.attributeValue("text_rise"));   } catch (Exception ex) {this.textRise = null; }
        try { this.bold      = Boolean.parseBoolean(element.attributeValue("bold"));     } catch (Exception ex) {this.bold     = false;}
        try { this.italic    = Boolean.parseBoolean(element.attributeValue("italic"));   } catch (Exception ex) {this.italic   = false;}
        try { this.underline = Boolean.parseBoolean(element.attributeValue("underline"));} catch (Exception ex) {this.underline= false;}
        try { this.autoLayout= Boolean.parseBoolean(element.attributeValue("auto_layout"));} catch (Exception ex) {this.autoLayout= false;}
        try { this.crossPage = Boolean.parseBoolean(element.attributeValue("cross_page")); } catch (Exception ex) {this.crossPage = false;}
        try { this.topY      = Float.parseFloat(element.attributeValue("top_y"));          } catch (Exception ex) {this.topY      = Float.MAX_VALUE;}
        try { this.bottomY   = Float.parseFloat(element.attributeValue("bottom_y"));       } catch (Exception ex) {this.bottomY   = 0.0f;}
        try { this.marginTop = Float.parseFloat(element.attributeValue("margin_top"));     } catch (Exception ex) {this.marginTop = 0.0f;}
        try { this.calculateWidthInTextBox = Boolean.parseBoolean(element.attributeValue("calculate_width_in_box")); } catch (Exception ex) {this.calculateWidthInTextBox = false;}
        try { this.calculateHeightInTextBox = Boolean.parseBoolean(element.attributeValue("calculate_height_in_box")); } catch (Exception ex) {this.calculateHeightInTextBox = false;}
        this.lastY(this.y);

        if (null==this.value || "".equals(this.value.trim())){
            this.value = element.getTextTrim();
        }
        if (null!=this.value && this.value.contains("®")) {
            this.value.replace('®', '\u00ae');
        }
        return this;
    }

    public Object generate(Document pdf, PageDef pageDef) throws Exception {
        if (!isVisible()) { return null; }
        PdfPage page = pdf.getPdfDocument().getLastPage();
        int pageNumber = 0;
        if (null!=page) {
            Paragraph paragraph = getText();
            List<LineRenderer> lineAreas = isCrossPage() ? new ArrayList<>() : null;
            float[] area      = getAreaLine(getWidth(), lineAreas);
            float   marginTop = getMarginTop();
            if (isAutoLayout()) {
                if (!isCrossPage()) {
                    if (null!=area) {
                        lastY(lastY() - area[1] - marginTop);
                    }
                    pageNumber = pdf.getPdfDocument().getPageNumber(page);
                    setPageStartNumberInPdf(pageNumber);
                    paragraph.setFixedPosition(pageNumber, getX(), lastY(), getWidth());

                    if (null!=pdf) {
                        pdf.add(paragraph);
                    }
                    return paragraph;
                }
                else {
                    if (null!=area) {
                        if (lastY() - area[1] - marginTop < bottomY) {
                            page = pdf.getPdfDocument().addNewPage();
                            pageDef.rendererPage(pdf, pageDef);
                            page = pdf.getPdfDocument().getLastPage();
                            lastY(topY);
                        }
                        lastY(lastY() - area[1] - marginTop);
                    }
                    pageNumber = pdf.getPdfDocument().getPageNumber(page);
                    setPageStartNumberInPdf(pageNumber);
                    paragraph.setFixedPosition(pageNumber, getX(), lastY(), getWidth());

                    if (null!=pdf) {
                        pdf.add(paragraph);
                    }
                    return paragraph;
                }
            }
            else {
                if (null!=area) {
                    lastY(getY() - marginTop);
                }
                page = pdf.getPdfDocument().getLastPage();
                pageNumber = pdf.getPdfDocument().getPageNumber(page);
                setPageStartNumberInPdf(pageNumber);
                paragraph.setFixedPosition(pageNumber, getX(), getY()-marginTop, getWidth());
                if (null!=pdf) {
                    pdf.add(paragraph);
                }
                return paragraph;
            }
        }

        return null;
    }

    public Paragraph getText() throws Exception {
        XmlDataParser xmlParser = getXmlDataParser();
        String[] sections = getParagraph();

        PdfFont font = getPdfFont(getFont(), null);
        Paragraph paragraph = new Paragraph();
        paragraph.setFont(font);
        paragraph.setFontSize(getFontSize());
        paragraph.setCharacterSpacing(getCharSpacing());
        paragraph.setWordSpacing(getWordSpacing());
        paragraph.setOpacity(getOpacity());
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

        ColorUtil colorUtil = ColorUtil.getInstance();
        Color fontColor = colorUtil.parseColor(getColor(), Color.BLACK);
        paragraph.setFontColor(fontColor);

        if (null!= getLineLeading()) {
            paragraph.setFixedLeading(getFontSize()*getLineLeading());
        }

        for (int i = 0; null!=sections && i < sections.length; i ++) {
            String section = sections[i];

            String[] styleLines = parseStyleLines(section);
            for (int index = 0; null!=styleLines && index < styleLines.length; index ++) {
                PdfFont tmpFont      = font;
                Color   tmpFontColor = fontColor;
                Float   tmpFontSize  = getFontSize();
                Float   tmpTextRise  = getTextRise();
                boolean tmpBold      = isBold();
                boolean tmpItalic    = isItalic();
                boolean tmpUnderline = isUnderline();
                String subLine = styleLines[index];
                if (subLine.trim().startsWith("<style") && subLine.trim().endsWith("</style>")) {
                    Element styleText = xmlParser.getRootElement(subLine.getBytes());
                    subLine = styleText.getText();
                    try { tmpFont      = getPdfFont(styleText.attributeValue("font"), tmpFont);       } catch (Exception ex) {}
                    try { tmpFontSize  = Float.parseFloat(styleText.attributeValue("font_size"));     } catch (Exception ex) {}
                    try { tmpBold      = Boolean.parseBoolean(styleText.attributeValue("bold"));      } catch (Exception ex) {}
                    try { tmpItalic    = Boolean.parseBoolean(styleText.attributeValue("italic"));    } catch (Exception ex) {}
                    try { tmpUnderline = Boolean.parseBoolean(styleText.attributeValue("underline")); } catch (Exception ex) {}
                    try { tmpFontColor = colorUtil.parseColor(styleText.attributeValue("font_color"), tmpFontColor);} catch (Exception ex) {}
                    try { tmpTextRise  = Float.parseFloat(styleText.attributeValue("text_rise"));     } catch (Exception ex) {}
                }
                Text line = new Text(subLine);
                if (null!=tmpFont) { line.setFont(tmpFont); }
                if (null!=tmpFontColor) { line.setFontColor(tmpFontColor);}
                if (null!=tmpFontSize)  { line.setFontSize(tmpFontSize);  }
                if (null!=tmpTextRise)  { line.setTextRise(tmpTextRise);  }
                if (isItalic() || tmpItalic)       { line.setItalic();    }
                if (isBold()   || tmpBold)         { line.setBold();      }
                if (isUnderline() || tmpUnderline) { line.setUnderline(); }
                paragraph.add(line);
            }
        }

        return paragraph;
    }

    private String[] parseStyleLines(String line) {
        if (null==line) {
            return null;
        }
        if ("".equals(line.trim())) {
            return new String[]{line};
        }
        StringBuilder parseStyle = new StringBuilder();
        List<String> res = new ArrayList<>();
        for (int i = 0; i < line.length(); i ++) {
            parseStyle.append(line.charAt(i));
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
        return res.toArray(new String[res.size()]);
    }

    public String[] getParagraph() {
        ParametersUtil parametersUtil = ParametersUtil.getInstance();
        String value = null;
        if (null!=getValue() && !"".equals(getValue().trim())) {
            value = getValue();
        }
        if (null!=value) {
            String tmpBody = parametersUtil.replaceParameter(value);
            tmpBody = tmpBody.replace("<br/>", "\n<br/>");
            tmpBody = tmpBody.replace("&nbsp;", " ");
            tmpBody = tmpBody.replace("&tab;", "\t");
            if (tmpBody.contains("®")) {
                tmpBody = tmpBody.replace('®', '\u00ae');
            }
            return tmpBody.split("<br/>");
        }
        return null;
    }

    /**
     * get paragraph occupied area and first line left top corner Y
     * @param maxWidth the specific width
     * @param everyLineArea every line area
     * @return [totalWidth, totalHeight, firstLineOffset, lastLineOffset]
     * @throws Exception
     */
    public float[] getAreaLine(float maxWidth, List<LineRenderer> everyLineArea) throws Exception {
        Paragraph paragraph = getText();
        ParagraphRenderer renderer = (ParagraphRenderer) paragraph.createRendererSubTree();
        paragraph.setProperty(Property.TEXT_RISE, 0.0f);
        paragraph.setProperty(Property.SPLIT_CHARACTERS, new DefaultSplitCharacters());
        LayoutResult layout = renderer.layout(new LayoutContext(new LayoutArea((int) 0, new Rectangle(0, 0, maxWidth, 10000f))));
        Rectangle allArea = null;
        if (null!=layout.getOccupiedArea()) {
            allArea = layout.getOccupiedArea().getBBox();
        }
        else if (null!=layout.getOverflowRenderer() && null!=layout.getOverflowRenderer().getOccupiedArea()) {
            allArea = layout.getOverflowRenderer().getOccupiedArea().getBBox();
        }
        float firstLineTopLeftYOffset = 0.0f;
        float lastLineTopLeftYOffset = 0.0f;
        List<LineRenderer> lines = renderer.getLines();
        if (null!=lines) {
            for (int i = 0; i < lines.size(); i ++) {
                LineRenderer tmp      = lines.get(i);
                Rectangle    lineArea = null;
                if (i==0 && null!=tmp.getOccupiedArea()) {
                    lineArea = tmp.getOccupiedArea().getBBox();
                    firstLineTopLeftYOffset = lineArea.getY()+lineArea.getHeight();
                }
                if (i==lines.size()-1 && null!=tmp.getOccupiedArea()) {
                    lineArea = tmp.getOccupiedArea().getBBox();
                    lastLineTopLeftYOffset = lineArea.getY();
                }
                if (null!=everyLineArea) {
                    everyLineArea.add(tmp);
                }
            }
        }
        if (null==allArea) {
            return null;
        }
        if (firstLineTopLeftYOffset!=0.0f) {
            firstLineTopLeftYOffset = allArea.getY()+allArea.getHeight()-firstLineTopLeftYOffset;
        }
        if (lastLineTopLeftYOffset!=0.0f) {
            lastLineTopLeftYOffset = lastLineTopLeftYOffset-allArea.getY();
        }
        return new float[]{allArea.getWidth(), allArea.getHeight(), firstLineTopLeftYOffset, lastLineTopLeftYOffset};
    }

    public TextDef clone() {
        TextDef newOne = new TextDef();
        newOne.setZOrder(this.getZOrder());
        newOne.value = this.value;
        newOne.body = this.body;
        newOne.font = this.font;
        newOne.opacity = this.opacity;
        newOne.fontSize = this.fontSize;
        newOne.color = this.color;
        newOne.bold = this.bold;
        newOne.italic = this.italic;
        newOne.underline = this.underline;
        newOne.x = this.x;
        newOne.y = this.y;
        newOne.width = this.width;
        newOne.hAlignment = this.hAlignment;
        newOne.vAlignment = this.vAlignment;
        newOne.lineLeading = this.lineLeading;
        newOne.charSpacing = this.charSpacing;
        newOne.wordSpacing = this.wordSpacing;
        newOne.textRise    = this.textRise;
        newOne.calculateWidthInTextBox  = this.calculateWidthInTextBox;
        newOne.calculateHeightInTextBox = this.calculateHeightInTextBox;

        newOne.autoLayout = this.autoLayout;
        newOne.lastY      = this.y;
        newOne.crossPage  = this.crossPage;
        newOne.topY       = this.topY;
        newOne.bottomY    = this.bottomY;
        newOne.marginTop  = this.marginTop;

        return newOne;
    }
}
