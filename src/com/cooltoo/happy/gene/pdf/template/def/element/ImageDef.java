package com.cooltoo.happy.gene.pdf.template.def.element;

import com.cooltoo.happy.gene.pdf.template.def.PageDef;
import com.cooltoo.happy.gene.pdf.template.util.log.Logger;
import com.cooltoo.happy.gene.pdf.template.IDrawable;
import com.cooltoo.happy.gene.pdf.template.IPosition;
import com.cooltoo.happy.gene.pdf.template.IXml;
import com.cooltoo.happy.gene.pdf.template.util.ParametersUtil;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import org.dom4j.Element;

/**
 * Created by zhaolisong on 12/04/2017.
 */
public class ImageDef extends AbstractDef implements IXml<ImageDef>, IPosition<ImageDef> {

    private String id;
    private String url;
    private String imageType;
    private String fit;
    private float x;
    private float y;
    private float width;
    private float height;

    public String getId() {
        return id;
    }
    public ImageDef setId(String id) {
        this.id = id;
        return this;
    }

    public String getUrl() {
        return url;
    }
    public ImageDef setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getImageType() {
        return imageType;
    }
    public ImageDef setImageType(String imageType) {
        this.imageType = imageType;
        return this;
    }

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
    public ImageDef setWidth(float width) {
        this.width = width;
        return this;
    }

    public float getHeight() {
        return height;
    }
    public ImageDef setHeight(float height) {
        this.height = height;
        return this;
    }

    public ImageDef translate(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public ImageDef parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"image".equalsIgnoreCase(element.getName())) {
            return null;
        }

        this.id        = element.attributeValue("id");
        this.url       = element.attributeValue("url");
        this.imageType = element.attributeValue("type");
        this.fit       = element.attributeValue("fit");
        this.fit       = (!"a".equalsIgnoreCase(this.fit) && !"f".equalsIgnoreCase(this.fit)) ? "F" : this.fit;
        try { this.setZOrder(Integer.parseInt(element.attributeValue("z_order")));} catch (Exception ex) {this.setZOrder(0);}
        try { this.x      = Float.parseFloat(element.attributeValue("x"));        } catch (Exception ex) {this.x = 0.0f;}
        try { this.y      = Float.parseFloat(element.attributeValue("y"));        } catch (Exception ex) {this.y = 0.0f;}
        try { this.width  = Float.parseFloat(element.attributeValue("width"));    } catch (Exception ex) {this.width  = 0.0f;}
        try { this.height = Float.parseFloat(element.attributeValue("height"));   } catch (Exception ex) {this.height = 0.0f;}
        return this;
    }

    public Image generate(Document pdf, PageDef pageDef) throws Exception {
        PdfPage page = pdf.getPdfDocument().getLastPage();
        try {
            ParametersUtil parametersUtil = ParametersUtil.getInstance();
            String tmpValue = null;
            if (null!=url && !"".equals(url.trim())) {
                tmpValue = parametersUtil.replaceParameter(url);
            }
            else {
                return null;
            }
            Image image = new Image(ImageDataFactory.create(tmpValue, false));
            image.setFixedPosition(pdf.getPdfDocument().getPageNumber(page), x, y);
            if ("a".equalsIgnoreCase(fit)) {
                image.scaleAbsolute(width, height);
            }
            else {
                image.scaleToFit(width, height);
            }
            pdf.add(image);
            return image;
        }
        catch (Exception ex) {
            Logger.error("parse image has exception: {}", ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }

    public ImageDef clone() {
        ImageDef newOne = new ImageDef();
        newOne.setZOrder(this.getZOrder());
        newOne.id    = this.id;
        newOne.url   = this.url;
        newOne.imageType = this.imageType;
        newOne.fit   = this.fit;
        newOne.x     = this.x;
        newOne.y     = this.y;
        newOne.width = this.width;
        newOne.height= this.height;

        return newOne;
    }
}
