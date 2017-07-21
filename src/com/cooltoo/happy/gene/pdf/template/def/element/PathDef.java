package com.cooltoo.happy.gene.pdf.template.def.element;

import com.cooltoo.happy.gene.pdf.template.*;
import com.cooltoo.happy.gene.pdf.template.def.PageDef;
import com.cooltoo.happy.gene.pdf.template.util.ColorUtil;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.colorspace.PdfDeviceCs;
import com.itextpdf.kernel.pdf.colorspace.PdfPattern;
import com.itextpdf.kernel.pdf.colorspace.PdfShading;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaolisong on 12/04/2017.
 */
public class PathDef extends AbstractDef implements IXml<PathDef>, IPosition<PathDef>, IBound {

    private String path;
    private List<Point> points;
    private float borderWidth;
    private String borderColor;
    private float opacity;
    private String fillColor;
    private String shading;
    private Shading shadingObj;

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
        this.points = new ArrayList<>();
        Point point = null;
        String[] ctrlPointsArray = null==getPath() ? new String[]{} : getPath().split(" ");
        for (int i = 0; i < ctrlPointsArray.length; i ++) {
            String cp = ctrlPointsArray[i];
            if ("m".equalsIgnoreCase(cp) || "l".equalsIgnoreCase(cp)) {
                point = new Point();
                point.type = cp;
                try { point.x1 = Float.parseFloat(ctrlPointsArray[i+1]); } catch (Exception ex) { point.x1 = null; }
                try { point.y1 = Float.parseFloat(ctrlPointsArray[i+2]); } catch (Exception ex) { point.y1 = null; }
                this.points.add(point);
                i += 2;
                continue;
            }
            if ("c".equalsIgnoreCase(cp)) {
                point = new Point();
                point.type = cp;
                try { point.x1 = Float.parseFloat(ctrlPointsArray[i+1]); } catch (Exception ex) { point.x1 = null; }
                try { point.y1 = Float.parseFloat(ctrlPointsArray[i+2]); } catch (Exception ex) { point.y1 = null; }
                try { point.x2 = Float.parseFloat(ctrlPointsArray[i+3]); } catch (Exception ex) { point.x2 = null; }
                try { point.y2 = Float.parseFloat(ctrlPointsArray[i+4]); } catch (Exception ex) { point.y2 = null; }
                try { point.x3 = Float.parseFloat(ctrlPointsArray[i+5]); } catch (Exception ex) { point.x3 = null; }
                try { point.y3 = Float.parseFloat(ctrlPointsArray[i+6]); } catch (Exception ex) { point.y3 = null; }
                this.points.add(point);
                i += 6;
                continue;
            }
            if ("v".equalsIgnoreCase(cp)) {
                point = new Point();
                point.type = cp;
                try { point.x1 = Float.parseFloat(ctrlPointsArray[i+1]); } catch (Exception ex) { point.x1 = null; }
                try { point.y1 = Float.parseFloat(ctrlPointsArray[i+2]); } catch (Exception ex) { point.y1 = null; }
                try { point.x2 = Float.parseFloat(ctrlPointsArray[i+3]); } catch (Exception ex) { point.x2 = null; }
                try { point.y2 = Float.parseFloat(ctrlPointsArray[i+4]); } catch (Exception ex) { point.y2 = null; }
                this.points.add(point);
                i += 4;
                continue;

            }
            if ("y".equalsIgnoreCase(cp)) {
                point = new Point();
                point.type = cp;
                try { point.x1 = Float.parseFloat(ctrlPointsArray[i+1]); } catch (Exception ex) { point.x1 = null; }
                try { point.y1 = Float.parseFloat(ctrlPointsArray[i+2]); } catch (Exception ex) { point.y1 = null; }
                try { point.x2 = Float.parseFloat(ctrlPointsArray[i+3]); } catch (Exception ex) { point.x2 = null; }
                try { point.y2 = Float.parseFloat(ctrlPointsArray[i+4]); } catch (Exception ex) { point.y2 = null; }
                this.points.add(point);
                i += 4;
                continue;

            }
            if ("h".equalsIgnoreCase(cp)) {
                point = new Point();
                point.type = cp;
                this.points.add(point);
            }
        }
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

    public float[] minMaxXY() {
        if (null==points || points.isEmpty()) {
            return new float[]{0, 0, 0, 0};
        }
        float minX=Float.MAX_VALUE, minY=Float.MAX_VALUE, maxX=Float.MIN_VALUE, maxY=Float.MIN_VALUE;
        for (Point tmp : points) {
            if ("h".equalsIgnoreCase(tmp.type)) {
                continue;
            }
            if (null!=tmp.x1 && minX>tmp.x1) {
                minX = tmp.x1;
            }
            if (null!=tmp.x2 && minX>tmp.x2) {
                minX = tmp.x2;
            }
            if (null!=tmp.x3 && minX>tmp.x3) {
                minX = tmp.x3;
            }

            if (null!=tmp.x1 && maxX<tmp.x1) {
                maxX = tmp.x1;
            }
            if (null!=tmp.x2 && maxX<tmp.x2) {
                maxX = tmp.x2;
            }
            if (null!=tmp.x3 && maxX<tmp.x3) {
                maxX = tmp.x3;
            }

            if (null!=tmp.y1 && minY>tmp.y1) {
                minY = tmp.y1;
            }
            if (null!=tmp.y2 && minY>tmp.y2) {
                minY = tmp.y2;
            }
            if (null!=tmp.y3 && minY>tmp.y3) {
                minY = tmp.y3;
            }

            if (null!=tmp.y1 && maxY<tmp.y1) {
                maxY = tmp.y1;
            }
            if (null!=tmp.y2 && maxY<tmp.y2) {
                maxY = tmp.y2;
            }
            if (null!=tmp.y3 && maxY<tmp.y3) {
                maxY = tmp.y3;
            }
        }
        return new float[]{minX, minY, maxX, maxY};
    }

    public String getShading() {
        return shading;
    }
    public void setShading(String shading) {
        this.shading = shading;
        String[] shadingArray = null==getShading() ? new String[]{} : getShading().split(" ");
        if (shadingArray.length>0) {
            this.shadingObj = new Shading();
            ColorUtil colorUtil = ColorUtil.getInstance();
            try { this.shadingObj.x1 = Float.parseFloat(shadingArray[0]); } catch (Exception ex) { this.shadingObj.x1 = null; }
            try { this.shadingObj.y1 = Float.parseFloat(shadingArray[1]); } catch (Exception ex) { this.shadingObj.y1 = null; }
            try { this.shadingObj.x2 = Float.parseFloat(shadingArray[3]); } catch (Exception ex) { this.shadingObj.x2 = null; }
            try { this.shadingObj.y2 = Float.parseFloat(shadingArray[4]); } catch (Exception ex) { this.shadingObj.y2 = null; }
            try { this.shadingObj.c1 = colorUtil.parseColor(shadingArray[2], null); } catch (Exception ex) { this.shadingObj.c1 = null; }
            try { this.shadingObj.c2 = colorUtil.parseColor(shadingArray[5], null); } catch (Exception ex) { this.shadingObj.c2 = null; }
        }
        else {
            this.shadingObj = null;
        }
    }

    public PathDef translate(float x, float y) {
        if (null!=this.points) {
            for (Point pnt : this.points) {
                pnt.translate(x, y);
            }
        }
        if (null!=this.shadingObj) {
            this.shadingObj.translate(x, y);
        }
        return this;
    }

    public PathDef parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"path".equalsIgnoreCase(element.getName())) {
            return null;
        }

        this.setPath(element.attributeValue("path"));
        this.borderColor = element.attributeValue("border_color");
        this.fillColor   = element.attributeValue("fill_color");
        this.setShading(element.attributeValue("shading"));
        try { this.setZOrder(Integer.parseInt(element.attributeValue("z_order")));        } catch (Exception ex) {this.setZOrder(0);}
        try { this.borderWidth = Float.parseFloat(element.attributeValue("border_width"));} catch (Exception ex) {this.borderWidth= 0.0f;}
        try { this.opacity     = Float.parseFloat(element.attributeValue("opacity"));     } catch (Exception ex) {this.opacity    = 1.0f;}

        return this;
    }

    public PdfCanvas generate(Document pdf, PageDef pageDef) throws Exception {
        PdfPage page = pdf.getPdfDocument().getLastPage();
        PdfCanvas path = new PdfCanvas(page);
        path.saveState();

        for (int i = 0; i < points.size(); i ++) {
            Point tmpPnt = points.get(i);
            tmpPnt.addToPath(path);
        }

        PdfExtGState state = new PdfExtGState();
        state.setFillOpacity(getOpacity());
        path.setExtGState(state);

        ColorUtil colorUtil = ColorUtil.getInstance();
        if (null!=getFillColor() && !"".equalsIgnoreCase(getFillColor())) {
            if (this.shadingObj != null) {
                this.shadingObj.shadingPath(path);
            }
            else {
                path.setFillColor(colorUtil.parseColor(getFillColor(), Color.BLACK));
            }
            path.fill();
        }
        else {
            path.setStrokeColor(colorUtil.parseColor(getBorderColor(), Color.BLACK));
            path.setLineWidth(getBorderWidth());
            path.stroke();
        }

        path.restoreState();
        return path;
    }

    class Point implements IPosition<Point> {
        String type = "";
        Float x1 = null;
        Float y1 = null;
        Float x2 = null;
        Float y2 = null;
        Float x3 = null;
        Float y3 = null;

        public Point translate(float x, float y) {
            if ("m".equalsIgnoreCase(type) || "l".equalsIgnoreCase(type)) {
                x1 = null!=x1 ? x1 + x : null;
                y1 = null!=y1 ? y1 + y : null;
            }
            else if ("c".equalsIgnoreCase(type)) {
                x1 = null!=x1 ? x1 + x : null;
                y1 = null!=y1 ? y1 + y : null;
                x2 = null!=x2 ? x2 + x : null;
                y2 = null!=y2 ? y2 + y : null;
                x3 = null!=x3 ? x3 + x : null;
                y3 = null!=y3 ? y3 + y : null;
            }
            else if ("v".equalsIgnoreCase(type) || "y".equalsIgnoreCase(type)) {
                x1 = null!=x1 ? x1 + x : null;
                y1 = null!=y1 ? y1 + y : null;
                x2 = null!=x2 ? x2 + x : null;
                y2 = null!=y2 ? y2 + y : null;
            }
            else if ("h".equalsIgnoreCase(type)) {
                // do nothing
            }

            return this;
        }

        public PdfCanvas addToPath(PdfCanvas path) {
            if ("m".equalsIgnoreCase(type) || "l".equalsIgnoreCase(type)) {
                if (x1!=null && y1!=null) {
                    if ("m".equalsIgnoreCase(type)) {
                        path.moveTo(x1, y1);
                    } else {
                        path.lineTo(x1, y1);
                    }
                }
            }
            if ("c".equalsIgnoreCase(type)) {
                if (x1!=null && y1!=null && x2!=null && y2!=null && x3!=null && y3!=null) {
                    path.curveTo(x1, y1, x2, y2, x3, y3);
                }
            }
            if ("v".equalsIgnoreCase(type)) {
                if (x1!=null && y1!=null && x2!=null && y2!=null) {
                    path.curveTo(x1, y1, x2, y2);
                }
            }
            if ("y".equalsIgnoreCase(type)) {
                if (x1!=null && y1!=null && x2!=null && y2!=null) {
                    path.curveFromTo(x1, y1, x2, y2);
                }
            }
            if ("h".equalsIgnoreCase(type)) {
                path.closePath();
            }
            return path;
        }

        public Point clone() {
            Point newOne = new Point();
            newOne.type = this.type;
            newOne.x1 = null!=this.x1 ? x1.floatValue() : null;
            newOne.x2 = null!=this.x2 ? x2.floatValue() : null;
            newOne.x3 = null!=this.x3 ? x3.floatValue() : null;
            newOne.y1 = null!=this.y1 ? y1.floatValue() : null;
            newOne.y2 = null!=this.y2 ? y2.floatValue() : null;
            newOne.y3 = null!=this.y3 ? y3.floatValue() : null;
            return newOne;
        }
    }

    class Shading implements IPosition<Shading> {

        Float x1 = 0.0f;
        Float y1 = 0.0f;
        Color c1 = null;
        Float x2 = 0.0f;
        Float y2 = 0.0f;
        Color c2 = null;

        @Override
        public Shading translate(float x, float y) {
            x1 = null!=x1 ? x1 + x : null;
            y1 = null!=y1 ? y1 + y : null;
            x2 = null!=x2 ? x2 + x : null;
            y2 = null!=y2 ? y2 + y : null;
            return this;
        }

        public PdfCanvas shadingPath(PdfCanvas path) {
            if (x1!=null && y1!=null && x2!=null && y2!=null && c1!=null && c2!=null) {
                PdfShading.Axial axial = new PdfShading.Axial(
                        new PdfDeviceCs.Rgb(),
                        x1, y1, c1.getColorValue(),
                        x2, y2, c2.getColorValue());
                path.setFillColorShading(new PdfPattern.Shading(axial));

            }
            return path;
        }

        public Shading clone() {
            Shading newOne = new Shading();
            newOne.x1 = null!=this.x1 ? x1.floatValue() : null;
            newOne.y1 = null!=this.y1 ? y1.floatValue() : null;
            newOne.c1 = null;
            newOne.x2 = null!=this.x2 ? x2.floatValue() : null;
            newOne.y2 = null!=this.y2 ? y2.floatValue() : null;
            newOne.c2 = null;
            if (this.c1 instanceof DeviceRgb) {
                float[] rgb = this.c1.getColorValue();
                newOne.c1 = new DeviceRgb(rgb[0], rgb[1], rgb[2]);
            }
            if (this.c2 instanceof DeviceRgb) {
                float[] rgb = this.c2.getColorValue();
                newOne.c2 = new DeviceRgb(rgb[0], rgb[1], rgb[2]);
            }

            return newOne;
        }
    }

    public PathDef clone() {
        PathDef newOne = new PathDef();
        newOne.setZOrder(this.getZOrder());
        newOne.path = this.path;
        newOne.borderWidth = this.borderWidth;
        newOne.borderColor = this.borderColor;
        newOne.opacity     = this.opacity;
        newOne.fillColor   = this.fillColor;
        newOne.shading     = this.shading;
        newOne.shadingObj  = null!=this.shadingObj ? this.shadingObj.clone() : null;

        if (null!=this.points) {
            newOne.points = new ArrayList<>();
            for (int i = 0; i < this.points.size(); i ++) {
                newOne.points.add(this.points.get(i).clone());
            }
        }

        return newOne;
    }
}
