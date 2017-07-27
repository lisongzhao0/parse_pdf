package com.happy.gene.pdfreport.pdf.def;

import com.happy.gene.pdfreport.pdf.IXml;
import com.itextpdf.kernel.font.PdfFont;
import org.dom4j.Element;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by zhaolisong on 12/04/2017.
 */
public class FontDef implements IXml<FontDef> {

    private String id;
    private String name;
    private String family;
    private String ttfPath;
    private String embedded;
    private PdfFont font;

    public String getId() {
        return id;
    }
    public FontDef setId(String id) {
        this.id = id;
        return this;
    }

    public String getTtfPath() {
        return ttfPath;
    }
    public FontDef setTtfPath(String ttfPath) {
        this.ttfPath = ttfPath;
        return this;
    }

    public String getName() {
        return name;
    }
    public FontDef setName(String name) {
        this.name = name;
        return this;
    }

    public String getFamily() {
        return family;
    }
    public FontDef setFamily(String family) {
        this.family = family;
        return this;
    }

    public String getEmbedded() {
        return embedded;
    }
    public FontDef setEmbedded(String embedded) {
        this.embedded = embedded;
        return this;
    }

    public InputStream getFontInputStream() throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(new File(ttfPath));
        return inputStream;
    }
    public PdfFont getFont() {
        return font;
    }
    public FontDef setFont(PdfFont font) {
        this.font = font;
        return this;
    }

    public FontDef parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"font".equalsIgnoreCase(element.getName())) {
            return null;
        }

        this.id      = element.attributeValue("id");
        this.name    = element.attributeValue("name");
        this.family  = element.attributeValue("family");
        this.ttfPath = element.attributeValue("ttf_path");
        this.embedded= element.attributeValue("embedded");
        return this;
    }

    public String toString() {
        StringBuilder msg = new StringBuilder();
        msg.append(getClass().getName()).append("@").append(hashCode()).append("[");
        msg.append("id=").append(id).append(",");
        msg.append("name=").append(name).append("]");
        return msg.toString();
    }
}
