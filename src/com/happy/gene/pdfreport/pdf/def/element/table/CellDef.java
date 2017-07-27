package com.happy.gene.pdfreport.pdf.def.element.table;

import com.happy.gene.pdfreport.pdf.def.element.AbstractDef;
import com.happy.gene.pdfreport.pdf.def.element.ImageDef;
import com.happy.gene.pdfreport.pdf.def.element.PercentDef;
import com.happy.gene.pdfreport.pdf.def.element.TextDef;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.layout.renderer.LineRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaolisong on 25/04/2017.
 */
public class CellDef extends AbstractDef {

    private static final Rectangle ZERO = new Rectangle(0f, 0f, 0f, 0f);

    private TextDef             text;
    private ImageDef            image;
    private PercentDef          percent;
    private List<LineRenderer>  everyLineArea;

    public CellDef(TextDef text, ImageDef image, PercentDef percent) {
        this.text = text;
        this.image = image;
        this.percent = percent;
    }

    public TextDef getText() {
        return text;
    }
    public ImageDef getImage() {
        return image;
    }
    public PercentDef getPercent() {
        return percent;
    }

    public float[] getArea(float maxWidth) throws Exception {
        if (null!=image) {
            return new float[]{image.getWidth(), image.getHeight(), 0.0f, 0.0f};
        }
        if (null!=percent) {
            return new float[]{percent.getWidth(), percent.getHeight(), 0.0f, 0.0f};
        }
        if (null!=text) {
            everyLineArea = new ArrayList<>();
            float[] area = text.getAreaLine(maxWidth, everyLineArea);
            if (null != area) {
                return area;
            }
        }
        return new float[]{0.0f, 0.0f, 0.0f};
    }

    public CellDef clone() {
        CellDef newOne = new CellDef(
                null!=this.text    ? this.text.clone()    : null,
                null!=this.image   ? this.image.clone()   : null,
                null!=this.percent ? this.percent.clone() : null
        );
        newOne.setZOrder(this.getZOrder());
        return newOne;
    }
}
