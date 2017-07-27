package com.happy.gene.pdfreport.pdf.def.element.table;

import com.happy.gene.pdfreport.pdf.IXml;
import com.happy.gene.pdfreport.pdf.data.DataTable;
import com.happy.gene.pdfreport.pdf.def.PageDef;
import com.happy.gene.pdfreport.pdf.def.element.AbstractDef;
import com.happy.gene.pdfreport.pdf.def.element.TextDef;
import com.happy.gene.pdfreport.pdf.util.DefFactory;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.layout.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaolisong on 20/04/2017.
 */
public class HeaderDef extends AbstractDef implements IXml<HeaderDef> {

    private VerticalTableDef    table;
    private HeaderAreaDef       headerArea;
    /** just support C(center), T(top) and B(bottom) */
    private String              vAlignment;
    private List<CellDef>       componentsArea = new ArrayList<>();
    private List<DataTable.Header> headers = null;

    public HeaderDef(VerticalTableDef table) {
        this.table = table;
    }

    public VerticalTableDef getTable() {
        return table;
    }
    public HeaderDef setTable(VerticalTableDef table) {
        this.table = table;
        return this;
    }

    public HeaderAreaDef getHeaderArea() {
        return headerArea;
    }
    public void setHeaderArea(HeaderAreaDef headerArea) {
        this.headerArea = headerArea;
    }

    public List<CellDef> getComponentsArea() {
        return componentsArea;
    }
    public HeaderDef setComponentsArea(List<CellDef> componentsArea) {
        this.componentsArea = componentsArea;
        return this;
    }

    public List<DataTable.Header> getHeaders() {
        return headers;
    }
    public HeaderDef setHeaders(List<DataTable.Header> headers) {
        this.headers = headers;
        return this;
    }

    public float getHeight() {
        float maxHeight = 0.0f;
        if (null==getTable() || null==getComponentsArea()) {
            return maxHeight;
        }

        float     width          = getTable().getWidth();
        Float[]   columnsWidth   = getTable().getColumnsWidth();
        width = width - (columnsWidth.length-1) * getTable().getGapH();

        float[] headerAreaRect = null;
        List<CellDef> cells = getComponentsArea();
        for (int i = 0; i < cells.size(); i++) {
            CellDef tmp = cells.get(i);
            if (null!=tmp.getText() && null!=headers) {
                tmp.getText().setValue(headers.get(i).getName());
            }
            try { headerAreaRect = tmp.getArea(width * columnsWidth[i]); } catch (Exception ex) { headerAreaRect = null;}
            if (null!=headerAreaRect) {
                maxHeight = (headerAreaRect[1])<maxHeight
                        ? maxHeight
                        : (headerAreaRect[1]);
            }
        }
        if (null!=this.headerArea) {
            maxHeight += this.headerArea.getHeight();
        }
        return maxHeight;
    }

    public HeaderDef parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"header".equalsIgnoreCase(element.getName())) {
            return null;
        }

        this.vAlignment = element.attributeValue("v_alignment");
        if (null==this.vAlignment || this.vAlignment.trim().isEmpty()) {
            this.vAlignment = "B";
        }
        this.vAlignment = this.vAlignment.trim();

        List<Element> subEle = element.elements();
        DefFactory defFactory = DefFactory.getIntance();
        for (Element ele : subEle) {
            AbstractDef node = defFactory.getDef(ele);
            if (null==node) { continue; }

            if (node instanceof TextDef) {
                this.componentsArea.add(new CellDef((TextDef) node, null, null));
                continue;
            }
            if (node instanceof HeaderAreaDef) {
                this.headerArea = (HeaderAreaDef) node;
                continue;
            }

        }
        return this;
    }

    public Object generate(Document pdf, PageDef pageDef) throws Exception {
        PdfPage page = pdf.getPdfDocument().getLastPage();
        if (null!=getTable()) {
            float   width        = getTable().getWidth();
            Float[] columnsWidth = getTable().getColumnsWidth();
            width = width - (columnsWidth.length-1) * getTable().getGapH();

            float headerHeight = getHeight();
            float x = getTable().getX();
            float y = getTable().lastY();

            if (null!=this.headerArea) {
                this.headerArea.setX(getTable().getX());
                this.headerArea.setY(getTable().lastY());
                this.headerArea.generate(pdf, pageDef);
                y            -= this.headerArea.getHeight();
                headerHeight -= this.headerArea.getHeight();
            }

            List<CellDef> cells = getComponentsArea();
            float deltaY = 0f;
            for (int i = 0; i < cells.size(); i ++) {
                CellDef tmp = cells.get(i);
                float[] bound = tmp.getText().getAreaLine(width * columnsWidth[i], null);
                if ("T".equalsIgnoreCase(vAlignment)) {
                    deltaY = bound[1];
                }
                else if ("C".equalsIgnoreCase(vAlignment)) {
                    deltaY = headerHeight/2 + bound[1]/2;
                }
                else {
                    deltaY = headerHeight;
                }
                tmp.getText().setX(x);
                tmp.getText().setY(y - deltaY);
                tmp.getText().setWidth(width * columnsWidth[i]);
                tmp.getText().generate(pdf, pageDef);
                x += width*columnsWidth[i]+getTable().getGapH();
            }
        }

        return page;
    }

    public HeaderDef clone() {
        HeaderDef newOne = new HeaderDef(this.table);
        newOne.setZOrder(this.getZOrder());
        if (!this.componentsArea.isEmpty()) {
            for (CellDef tmp : this.componentsArea) {
                newOne.componentsArea.add(tmp.clone());
            }
        }
        if (null!=headerArea) {
            newOne.headerArea = (HeaderAreaDef) this.headerArea.clone();
        }
        return newOne;
    }
}
