package com.happy.gene.pdfreport.pdf.def.element.table;

import com.happy.gene.pdfreport.pdf.IBound;
import com.happy.gene.pdfreport.pdf.IXml;
import com.happy.gene.pdfreport.pdf.IZOrder;
import com.happy.gene.pdfreport.pdf.data.DataTable;
import com.happy.gene.pdfreport.pdf.def.AreaDef;
import com.happy.gene.pdfreport.pdf.def.PageDef;
import com.happy.gene.pdfreport.pdf.def.element.*;
import com.happy.gene.pdfreport.pdf.util.DefFactory;
import com.happy.gene.pdfreport.pdf.util.NumberUtil;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.layout.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaolisong on 20/04/2017.
 */
public class RowDef extends AbstractDef implements IXml<RowDef> {

    private VerticalTableDef   table;
    private List<AbstractDef>  renderBefore  = new ArrayList<>();
    private List<CellDef>      componentsArea= new ArrayList<>();
    private List<AbstractDef>  renderAfter   = new ArrayList<>();
    private DataTable.Row      row = null;
    private String             condition = null;

    public RowDef(VerticalTableDef table) {
        this.table = table;
    }

    public int getZOrder() {
        return 0;
    }
    public void setZOrder(int order) {}

    public VerticalTableDef getTable() {
        return table;
    }
    public RowDef setTable(VerticalTableDef table) {
        this.table = table;
        return this;
    }

    public List<CellDef> getComponentsArea() {
        return componentsArea;
    }
    public RowDef setComponentsArea(List<CellDef> componentsArea) {
        this.componentsArea = componentsArea;
        return this;
    }

    public DataTable.Row getRow() {
        return row;
    }
    public RowDef setRow(DataTable.Row row) {
        this.row = row;
        return this;
    }

    public boolean fitCondition(DataTable.Row row) {
        if (null==condition || "".equalsIgnoreCase(condition)) {
            return true;
        }
        if (condition.contains("==")) {
            String split[] = condition.split("==");
            if (split.length!=2) {
                split = new String[]{split[0], ""};
            }
            split[0] = split[0].replace("col", "");
            Integer colIndex = NumberUtil.newInstance().parseInteger(split[0]);
            if (null==colIndex) {
                return false;
            }
            DataTable.Cell cell = row.getCell(colIndex);
            return split[1].equalsIgnoreCase(cell.getValue());
        }
        else if (condition.contains("!=")) {
            String split[] = condition.split("!=");
            if (split.length!=2) {
                split = new String[]{split[0], ""};
            }
            split[0] = split[0].replace("col", "");
            Integer colIndex = NumberUtil.newInstance().parseInteger(split[0]);
            if (null==colIndex) {
                return false;
            }

            DataTable.Cell cell = row.getCell(colIndex);
            return !split[1].equalsIgnoreCase(cell.getValue());
        }
        return false;
    }

    public float getHeight() {
        float maxHeight = 0.0f;
        if (null==getTable() || null==getComponentsArea()) {
            return maxHeight;
        }

        float     width        = getTable().getWidth();
        Float[]   columnsWidth = getTable().getColumnsWidth();
        width = width - (columnsWidth.length-1) * getTable().getGapH();

        float[] areaRect     = null;
        List<CellDef> cells = getComponentsArea();
        for (int i = 0; i < cells.size(); i++) {
            CellDef tmp = cells.get(i);
            if (null!=tmp.getImage()) {
                if (null!=row) {
                    tmp.getImage().setUrl(row.getCells().get(i).getValue());
                }
                maxHeight = tmp.getImage().getHeight()<maxHeight ? maxHeight : tmp.getImage().getHeight();
                continue;
            }
            if (null!=tmp.getPercent()) {
                if (null!=row) {
                    tmp.getPercent().setPercentNum(row.getCells().get(i).getValue());
                }
                maxHeight = tmp.getPercent().getHeight()<maxHeight ? maxHeight : tmp.getPercent().getHeight();
                continue;
            }
            if (null!=tmp.getText() && null!=row) {
                tmp.getText().setValue(row.getCells().get(i).getValue());
            }
            try { areaRect = tmp.getArea(width * columnsWidth[i]); } catch (Exception ex) { areaRect = null;}
            if (null!=areaRect) {
                maxHeight = (areaRect[1]-areaRect[2])<maxHeight ? maxHeight : (areaRect[1]-areaRect[2]);
            }
        }

        return maxHeight;
    }

    public RowDef parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"row".equalsIgnoreCase(element.getName())) {
            return null;
        }


        this.condition = element.attributeValue("condition");

        DefFactory defFactory = DefFactory.getIntance();
        List<Element> subEle = element.elements();
        for (Element ele : subEle) {
            AbstractDef subComp = defFactory.getDef(ele);
            if (subComp instanceof ImageDef) {
                this.componentsArea.add(new CellDef(null, (ImageDef)subComp, null));
            }
            if (subComp instanceof TextDef) {
                this.componentsArea.add(new CellDef((TextDef)subComp, null, null));
            }
            if (subComp instanceof PercentDef) {
                this.componentsArea.add(new CellDef(null, null, (PercentDef) subComp));
            }

            AbstractDef beforeAfter = defFactory.getDef(ele);
            if (beforeAfter instanceof IZOrder) {
                if (beforeAfter.getZOrder()<0) {
                    this.renderBefore.add(beforeAfter);
                }
                else {
                    this.renderAfter.add(beforeAfter);
                }
            }
        }

        return this;
    }

    public Object generate(Document pdf, PageDef pageDef) throws Exception {
        PdfPage page = pdf.getPdfDocument().getLastPage();
        if (null!=getTable() && null!=getComponentsArea()) {
            float   width        = getTable().getWidth();
            Float[] columnsWidth = getTable().getColumnsWidth();
            width = width - (columnsWidth.length-1) * getTable().getGapH();

            float rowHeight = getHeight();

            float x = getTable().getX();
            float y = getTable().lastY();
            float[] minYMaxY = getMinMaxY(renderBefore);

            if (null!=minYMaxY) {
                float deltaY = y - (rowHeight - (minYMaxY[1]-minYMaxY[0]))/2f;
                deltaY = deltaY - minYMaxY[1];
                for (int i = 0; !renderBefore.isEmpty() && i < renderBefore.size(); i ++) {
                    AbstractDef tmp = renderBefore.get(i);
                    if (tmp instanceof RectDef) {
                        ((RectDef) tmp).translate(0, deltaY);
                        ((RectDef) tmp).generate(pdf, pageDef);
                    }
                    if (tmp instanceof CircleDef) {
                        ((CircleDef) tmp).translate(0, deltaY);
                        ((CircleDef) tmp).generate(pdf, pageDef);
                    }
                    if (tmp instanceof PathDef) {
                        ((PathDef) tmp).translate(0, deltaY);
                        ((PathDef) tmp).generate(pdf, pageDef);
                    }
                    if (tmp instanceof AreaDef) {
                        ((AreaDef) tmp).translate(0, deltaY);
                        ((AreaDef) tmp).generate(pdf, pageDef);
                    }
                }
            }

            for (int i = 0; i < componentsArea.size(); i ++) {
                CellDef tmp = componentsArea.get(i);
                if (null!=tmp.getText()) {
                    float[] areaRect = null;
                    areaRect = tmp.getArea(width * columnsWidth[i]);
                    tmp.getText().setX(x);
                    if (getTable().isCellCenterV()) {
                        tmp.getText().setY(y - (rowHeight - (areaRect[1] - areaRect[2] - areaRect[3])) * 0.5f - (areaRect[1] - areaRect[2] - areaRect[3]));
                    }
                    else {
                        tmp.getText().setY(y - areaRect[1]);
                    }
                    tmp.getText().setWidth(width * columnsWidth[i]);
                    tmp.getText().generate(pdf, pageDef);
                }
                else if (null!=tmp.getImage()) {
                    tmp.getImage().setX(x);
                    if (getTable().isCellCenterV()) {
                        tmp.getImage().setY(y - (rowHeight - tmp.getImage().getHeight()) * 0.5f - tmp.getImage().getHeight());
                    }
                    else {
                        tmp.getImage().setY(y - tmp.getImage().getHeight());
                    }
                    tmp.getImage().setWidth(width * columnsWidth[i]);
                    tmp.getImage().generate(pdf, pageDef);
                }
                else if (null!=tmp.getPercent()) {
                    float height = tmp.getPercent().getHeight();
                    tmp.getPercent().setFullX(x);
                    if (getTable().isCellCenterV()) {
                        tmp.getPercent().setFullY(y - (rowHeight - height) * 0.5f);
                    }
                    else {
                        tmp.getPercent().setFullY(y - height);
                    }
                    tmp.getPercent().setFullWidth(width * columnsWidth[i]);
                    tmp.getPercent().generate(pdf, pageDef);
                }
                x += width*columnsWidth[i] + getTable().getGapH();
            }
        }

        for (int i = 0; !renderAfter.isEmpty() && i < renderAfter.size(); i ++) {
            AbstractDef tmp = renderAfter.get(i);
            if (tmp instanceof RectDef) {
                ((RectDef) tmp).generate(pdf, pageDef);
            }
            if (tmp instanceof CircleDef) {
                ((CircleDef) tmp).generate(pdf, pageDef);
            }
            if (tmp instanceof PathDef) {
                ((PathDef) tmp).generate(pdf, pageDef);
            }
        }
        return page;
    }

    public float[] getMinMaxY(List<AbstractDef> renderBeforeAfter) {
        if (null==renderBeforeAfter || renderBeforeAfter.isEmpty()) {
            return null;
        }
        float maxY = Float.MIN_VALUE;
        float minY = Float.MAX_VALUE;
        for (AbstractDef tmp : renderBeforeAfter) {
            if (!(tmp instanceof IBound)) {
                continue;
            }

            float[] minMaxXy = ((IBound) tmp).minMaxXY();
            if (null!=minMaxXy) {
                if (minMaxXy[1]<minY) {
                    minY = minMaxXy[1];
                }
                if (minMaxXy[3]>maxY) {
                    maxY = minMaxXy[3];
                }
            }
        }
        return new float[]{minY, maxY};
    }

    public RowDef clone() {
        RowDef newOne = new RowDef(this.table);
        newOne.setZOrder(this.getZOrder());
        newOne.condition = this.condition;
        if (!this.renderBefore.isEmpty()) {
            for (AbstractDef tmp : this.renderBefore) {
                newOne.renderBefore.add(tmp.clone());
            }
        }
        if (!this.renderAfter.isEmpty()) {
            for (AbstractDef tmp : this.renderAfter) {
                newOne.renderAfter.add(tmp.clone());
            }
        }
        if (!this.componentsArea.isEmpty()) {
            for (CellDef tmp : this.componentsArea) {
                newOne.componentsArea.add(tmp.clone());
            }
        }
        return newOne;
    }
}
