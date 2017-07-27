package com.happy.gene.pdfreport.pdf.def.element.table;

import com.happy.gene.pdfreport.pdf.IPosition;
import com.happy.gene.pdfreport.pdf.IVariable;
import com.happy.gene.pdfreport.pdf.IXml;
import com.happy.gene.pdfreport.pdf.data.DataTable;
import com.happy.gene.pdfreport.pdf.def.PageDef;
import com.happy.gene.pdfreport.pdf.def.element.AbstractDef;
import com.happy.gene.pdfreport.pdf.def.element.RectDef;
import com.happy.gene.pdfreport.pdf.util.DefFactory;
import com.happy.gene.pdfreport.pdf.util.ParametersUtil;
import com.happy.gene.pdfreport.pdf.util.XmlDataCache;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaolisong on 23/04/2017.
 */
public class VerticalTableDef extends AbstractDef implements IXml<VerticalTableDef>, IPosition<VerticalTableDef>, IVariable {

    private float     x;
    private float     y;
    private float     topY;
    private float     bottomY;
    private float     width;
    private Float[]   columnsWidth;
    private boolean   headerInEveryPage;
    private boolean   cellCenterV;
    private String    data;
    private float     gapH;
    private float     gapHeaderRow;
    private boolean   headerSplitLine;
    private float     headerSplitLineWidth;
    private String    headerSplitLineColor;

    private HeaderDef    tableHeader;
    private List<RowDef> tableRow = new ArrayList<>();
    private PdfCanvas tableCanvas;
    private float     lastY;
    private String    rowSplitTopBottom = "T";
    private RectDef rowSplit;
    private RectDef   colSplit;

    public float getX() {
        return x;
    }
    public VerticalTableDef setX(float x) {
        this.x = x;
        return this;
    }

    public float getY() {
        return y;
    }
    public VerticalTableDef setY(float y) {
        this.y = y;
        return this;
    }

    public float getTopY() {
        return topY;
    }
    public VerticalTableDef setTopY(float topY) {
        this.topY = topY;
        return this;
    }

    public float getBottomY() {
        return bottomY;
    }
    public VerticalTableDef setBottomY(float bottomY) {
        this.bottomY = bottomY;
        return this;
    }

    public float getWidth() {
        return width;
    }
    public VerticalTableDef setWidth(float width) {
        this.width = width;
        return this;
    }

    public Float[] getColumnsWidth() {
        return columnsWidth;
    }
    public VerticalTableDef setColumnsWidth(Float[] columnsWidth) {
        this.columnsWidth = columnsWidth;
        return this;
    }

    public boolean isHeaderInEveryPage() {
        return headerInEveryPage;
    }
    public VerticalTableDef setHeaderInEveryPage(boolean headerInEveryPage) {
        this.headerInEveryPage = headerInEveryPage;
        return this;
    }

    public boolean isCellCenterV() {
        return cellCenterV;
    }
    public VerticalTableDef setCellCenterV(boolean cellCenterV) {
        this.cellCenterV = cellCenterV;
        return this;
    }

    public String getData() {
        return null!=data&&data.startsWith("$") ? data.substring(1) : data;
    }
    public VerticalTableDef setData(String data) {
        this.data = data;
        return this;
    }

    public float getGapH() {
        return gapH;
    }
    public VerticalTableDef setGapH(float gapH) {
        this.gapH = gapH;
        return this;
    }

    public float getGapHeaderRow() {
        return gapHeaderRow;
    }
    public VerticalTableDef setGapHeaderRow(float gapHeaderRow) {
        this.gapHeaderRow = gapHeaderRow;
        return this;
    }

    public boolean isHeaderSplitLine() {
        return headerSplitLine;
    }
    public VerticalTableDef setHeaderSplitLine(boolean headerSplitLine) {
        this.headerSplitLine = headerSplitLine;
        return this;
    }

    public float getHeaderSplitLineWidth() {
        return headerSplitLineWidth;
    }
    public VerticalTableDef setHeaderSplitLineWidth(float headerSplitLineWidth) {
        this.headerSplitLineWidth = headerSplitLineWidth;
        return this;
    }

    public String getHeaderSplitLineColor() {
        return headerSplitLineColor;
    }
    public VerticalTableDef setHeaderSplitLineColor(String headerSplitLineColor) {
        this.headerSplitLineColor = headerSplitLineColor;
        return this;
    }

    public PdfCanvas getTableCanvas() {
        return tableCanvas;
    }
    public VerticalTableDef setTableCanvas(PdfCanvas tableCanvas) {
        this.tableCanvas = tableCanvas;
        return this;
    }

    public HeaderDef getTableHeader() {
        return tableHeader;
    }
    public void setTableHeader(HeaderDef tableHeader) {
        this.tableHeader = tableHeader;
    }

    public List<RowDef> getTableRow() {
        return tableRow;
    }
    public void setTableRow(List<RowDef> tableRow) {
        this.tableRow = tableRow;
    }

    public String getRowSplitTopBottom() {
        return rowSplitTopBottom;
    }
    public void setRowSplitTopBottom(String rowSplitTopBottom) {
        this.rowSplitTopBottom = rowSplitTopBottom;
    }

    public RectDef getRowSplit() {
        return rowSplit;
    }
    public void setRowSplit(RectDef rowSplit) {
        this.rowSplit = rowSplit;
    }

    public RectDef getColSplit() {
        return colSplit;
    }
    public void setColSplit(RectDef colSplit) {
        this.colSplit = colSplit;
    }

    public float lastY() {
        return lastY;
    }
    public void lastY(float lastY) {
        this.lastY = lastY;
    }

    public VerticalTableDef translate(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public VerticalTableDef parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"vertical_table".equalsIgnoreCase(element.getName())) {
            return null;
        }
        ParametersUtil paramUtil = ParametersUtil.getInstance();


        this.data = element.attributeValue("data");
        this.setCatalog(element.attributeValue("catalog"));
        this.setVisible(getStringUtil().isEmpty(element.attributeValue("visible")) ? "true" : element.attributeValue("visible"));
        try { this.setZOrder(Integer.parseInt(element.attributeValue("z_order")));            } catch (Exception ex) {this.setZOrder(0);}
        try { this.x          = Float.parseFloat(element.attributeValue("x"));                } catch (Exception ex) {this.x        = 0.0f;}
        try { this.y          = Float.parseFloat(element.attributeValue("y"));                } catch (Exception ex) {this.y        = 0.0f;}
        try { this.topY       = Float.parseFloat(element.attributeValue("top_y"));            } catch (Exception ex) {this.topY     = Float.MAX_VALUE;}
        try { this.bottomY    = Float.parseFloat(element.attributeValue("bottom_y"));         } catch (Exception ex) {this.bottomY  = 0.0f;}
        try { this.width      = Float.parseFloat(element.attributeValue("width"));            } catch (Exception ex) {this.width    = 0.0f;}
        try { this.gapH       = Float.parseFloat(element.attributeValue("gap_h"));            } catch (Exception ex) {this.gapH     =0.0f;}
        try { this.gapHeaderRow= Float.parseFloat(element.attributeValue("gap_header_row"));  } catch (Exception ex) {this.gapHeaderRow=0.0f;}
        try { this.headerSplitLine= Boolean.parseBoolean(element.attributeValue("header_split_line"));     } catch (Exception ex) {this.headerSplitLine=false;}
        try { this.columnsWidth= paramUtil.parseFloatArray(element.attributeValue("column_width")); } catch (Exception ex) {this.columnsWidth=null;}
        try { this.headerInEveryPage= Boolean.parseBoolean(element.attributeValue("header_in_every_page"));} catch (Exception ex) {this.headerInEveryPage=false;}
        try { this.cellCenterV= Boolean.parseBoolean(element.attributeValue("cell_center_v"));} catch (Exception ex) {this.cellCenterV=false;}
        try { this.headerSplitLineWidth= Float.parseFloat(element.attributeValue("header_split_line_width"));} catch (Exception ex) {this.headerSplitLineWidth=0.01f;}
        this.headerSplitLineColor = element.attributeValue("header_split_line_color");
        if (null==this.headerSplitLineColor || "".equalsIgnoreCase(this.headerSplitLineColor)) {
            this.headerSplitLineColor = "#ffffff";
        }
        this.lastY(this.getY());

        DefFactory defFactory = DefFactory.getIntance();
        List<Element> subEle = element.elements();
        for (Element ele : subEle) {
            AbstractDef newOne = defFactory.getDef(ele);
            if (newOne instanceof HeaderDef) {
                this.tableHeader = (HeaderDef) newOne;
                this.tableHeader.setTable(this);
            }
            newOne = defFactory.getDef(ele);
            if (newOne instanceof RowDef) {
                this.tableRow.add(((RowDef) newOne));
                ((RowDef) newOne).setTable(this);
            }
            if (newOne instanceof RectDef) {
                RectDef rect = (RectDef) newOne;
                if ("row".equalsIgnoreCase(rect.getTableSplit())) {
                    this.rowSplitTopBottom = rect.getTableSplitTopBottom();
                    this.rowSplit = rect;
                }
                if ("column".equalsIgnoreCase(rect.getTableSplit())) {
                    this.colSplit = rect;
                }
            }

        }

        return this;
    }

    public Object generate(Document pdf, PageDef pageDef) throws Exception {
        if (!isVisible()) { return null; }
        PdfPage page = pdf.getPdfDocument().getLastPage();
        tableCanvas = new PdfCanvas(page);
        tableCanvas.saveState();

        boolean isDrawHeader = false;
        boolean isNewPage    = false;
        float   rowHeight    = 0.0f;
        float   lastY        = lastY();
        RowDef  rowDef       = null;

        XmlDataCache xmlDataCache = XmlDataCache.getInstance();
        DataTable table = xmlDataCache.getTable(getData());

        int rows = 1;
        if (null!=table) {
            if (null!=getTableHeader()) { getTableHeader().setHeaders(table.getHeaders()); }
            rows = table.getRows().isEmpty() ? 1 : table.getRows().size();
        }

        for (int i = 0; i < rows; i ++) {
            // new table header
            if (null != getTableHeader() && (!isDrawHeader || (isHeaderInEveryPage() && isNewPage))) {
                rowHeight = getTableHeader().getHeight();
                if (lastY - rowHeight < getBottomY()) {
                    getTableCanvas().restoreState();
                    page = pdf.getPdfDocument().addNewPage();
                    pageDef.rendererPage(pdf, pageDef);
                    lastY(getTopY());
                    lastY = lastY();
                    setTableCanvas(new PdfCanvas(page));
                    getTableCanvas().saveState();
                    isNewPage = true;
                    i--;
                    continue;
                }
                getTableHeader().generate(pdf, pageDef);
                lastY(lastY - rowHeight - getGapHeaderRow());
                lastY = lastY();
                isDrawHeader = true;
                isNewPage = false;
                if (isHeaderSplitLine()) {
                    getTableCanvas()
                            .saveState()
                            .setColor(getColorUtil().parseColor(getHeaderSplitLineColor(), Color.BLACK), false)
                            .setLineWidth(getHeaderSplitLineWidth())
                            .moveTo(getX(), lastY)
                            .lineTo(getX() + width, lastY)
                            .closePath()
                            .stroke()
                            .restoreState();
                }
            }

            // new table row
            if (null != getTableRow() && (null==table || !table.getRows().isEmpty())) {
                if (null!= getRowSplit() && "T".equalsIgnoreCase(getRowSplitTopBottom())) {
                    //{minX, minY, maxX-minX, maxY-minY, r, paddingLeft, paddingTop, paddingRight, paddingBottom};
                    float[] bound = getRowSplit().getBound();
                    float splitY = lastY - bound[3] - bound[6];
                    getRowSplit().setBound(getX(), splitY, getWidth(), bound[3], bound[4]);
                    getRowSplit().generate(pdf, pageDef);
                    lastY(lastY - bound[3] - bound[6] - bound[8]);
                    lastY = lastY();

                }

                List<RowDef> tableRows = getTableRow();
                for (int rowIdx = 0; rowIdx <tableRows.size(); rowIdx ++) {
                    rowDef = tableRows.get(rowIdx);
                    if (null!=table) {
                        if (rowDef.fitCondition(table.getRows().get(i))) {
                            break;
                        }
                    }
                    else {
                        break;
                    }
                }
                if (null!=table) {
                    rowDef.setRow(table.getRows().get(i));
                }

                if (null==rowDef) {
                    continue;
                }
                rowHeight = rowDef.getHeight();
                if (lastY - rowHeight < getBottomY()) {
                    getTableCanvas().restoreState();
                    page = pdf.getPdfDocument().addNewPage();
                    pageDef.rendererPage(pdf, pageDef);
                    lastY(getTopY());
                    lastY = lastY();
                    setTableCanvas(new PdfCanvas(page));
                    getTableCanvas().saveState();
                    isNewPage = true;
                    i--;
                    continue;
                }
                rowDef.generate(pdf, pageDef);
                lastY(lastY - rowHeight);
                lastY = lastY();

                if (null!= getRowSplit() && "B".equalsIgnoreCase(getRowSplitTopBottom())) {
                    //{minX, minY, maxX-minX, maxY-minY, r, paddingLeft, paddingTop, paddingRight, paddingBottom};
                    float[] bound = getRowSplit().getBound();
                    float splitY = lastY - bound[3] - bound[6];
                    getRowSplit().setBound(getX(), splitY, getWidth(), bound[3], bound[4]);
                    getRowSplit().generate(pdf, pageDef);
                    lastY(lastY - bound[3] - bound[6] - bound[8]);
                    lastY = lastY();

                }
            }
        }

        getTableCanvas().restoreState();
        return null;
    }

    public VerticalTableDef clone() {
        VerticalTableDef newOne = new VerticalTableDef();

        newOne.setZOrder(this.getZOrder());
        newOne.x       = this.x;
        newOne.y       = this.y;
        newOne.topY    = this.topY;
        newOne.bottomY = this.bottomY;
        newOne.width   = this.width;
        newOne.cellCenterV  = this.cellCenterV;
        newOne.data         = this.data;
        newOne.gapH         = this.gapH;
        newOne.gapHeaderRow = this.gapHeaderRow;
        newOne.headerSplitLine      = this.headerSplitLine;
        newOne.headerSplitLineColor = this.headerSplitLineColor;
        newOne.headerSplitLineWidth = this.headerSplitLineWidth;
        newOne.headerInEveryPage    = this.headerInEveryPage;

        newOne.tableHeader = null!=this.tableHeader ? this.tableHeader.clone() : null;
        if (null!=newOne.tableHeader) {
            newOne.tableHeader.setTable(newOne);
        }
        newOne.lastY       = this.y;
        newOne.rowSplit    = null!=this.rowSplit ? this.rowSplit.clone() : null;
        newOne.colSplit    = null!=this.colSplit ? this.colSplit.clone() : null;
        if (null!=this.columnsWidth) {
            newOne.columnsWidth = new Float[this.columnsWidth.length];
            for (int i = 0; i<this.columnsWidth.length; i ++) {
                newOne.columnsWidth[i] = this.columnsWidth[i];
            }
        }
        if (!this.tableRow.isEmpty()) {
            for (RowDef tmp : this.tableRow) {
                tmp = tmp.clone();
                tmp.setTable(newOne);
                newOne.tableRow.add(tmp);
            }
        }
        return newOne;
    }
}
