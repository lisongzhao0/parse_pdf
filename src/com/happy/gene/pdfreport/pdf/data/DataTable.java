package com.happy.gene.pdfreport.pdf.data;

import com.happy.gene.pdfreport.pdf.IXml;
import org.dom4j.Element;

import java.util.*;

/**
 * Created by zhaolisong on 26/04/2017.
 */
public class DataTable implements IXml<DataTable> {

    String       id;
    String       group;
    List<DataTable.Header> headers;
    List<DataTable.Row>    rows;

    public String getId() {
        return id;
    }
    public String getGroup() {
        return group;
    }

    public List<DataTable.Header> getHeaders() {
        return headers;
    }
    public List<DataTable.Row> getRows() {
        return rows;
    }

    @Override
    public DataTable parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"table".equalsIgnoreCase(element.getName())) {
            return null;
        }

        DataTable def = new DataTable();
        def.id    = element.attributeValue("id");
        def.group = element.attributeValue("group");

        Header header = new Header();
        Row    row    = new Row();
        List<Element> children = element.elements();
        for (Element tmp : children) {
            if ("headers".equalsIgnoreCase(tmp.getName())) {
                def.headers = header.parseElement(tmp);
            }
            if ("rows".equalsIgnoreCase(tmp.getName())) {
                def.rows = row.parseElement(tmp);
            }
        }
        return def;
    }


    public static class Header implements IXml<List<Header>> {
        int column = 0;
        String name = "";

        public int getColumn() {
            return column;
        }
        public String getName() {
            return name;
        }

        @Override
        public List<Header> parseElement(Element element) {
            if (null==element) {
                return null;
            }
            if (!"headers".equalsIgnoreCase(element.getName())) {
                return null;
            }
            List<DataTable.Header> headers = new ArrayList<>();

            List<Element> elements = element.elements();
            for (Element tmp : elements) {
                DataTable.Header def = new DataTable.Header();
                def.name = tmp.attributeValue("value");
                try {def.column = Integer.parseInt(tmp.attributeValue("column")); } catch (Exception ex) { def.column = 0; }
                headers.add(def);
            }

            Collections.sort(headers, new Comparator<DataTable.Header>() {
                @Override
                public int compare(Header o1, Header o2) {
                    return o1.getColumn()-o2.getColumn();
                }
            });
            return headers;
        }
    }

    public static class Row implements IXml<List<Row>> {
        int row = 0;
        List<Cell> cells;

        public int getRow() {
            return row;
        }

        public int getCellSize() {
            return null==cells ? 0 : cells.size();
        }
        public Cell getCell(int colIndex) {
            if (null==cells || cells.isEmpty()) return null;
            if (colIndex<0 || colIndex>=cells.size()) return null;
            return cells.get(colIndex);
        }
        public List<Cell> getCells() {
            return cells;
        }
        public void setCells(List<Cell> cells) {
            this.cells = cells;
        }

        @Override
        public List<Row> parseElement(Element element) {
            if (null==element) {
                return null;
            }
            if (!"rows".equalsIgnoreCase(element.getName())) {
                return null;
            }
            int minRow = Integer.MAX_VALUE;
            int maxRow = Integer.MIN_VALUE;
            Map<Integer, List<Cell>> cellsInRow = new HashMap<>();

            List<Element> elements = element.elements();
            Cell def = new Cell();
            for (Element tmp : elements) {
                Cell cell = def.parseElement(tmp);
                minRow = minRow>cell.getRow() ? cell.getRow() : minRow;
                maxRow = maxRow<cell.getRow() ? cell.getRow() : maxRow;
                List<Cell> rowCells = cellsInRow.get(cell.getRow());
                if (null==rowCells) {
                    rowCells = new ArrayList<>();
                    cellsInRow.put(cell.getRow(), rowCells);
                }
                rowCells.add(cell);
                Collections.sort(rowCells, new Comparator<Cell>() {
                    @Override
                    public int compare(Cell o1, Cell o2) {
                        if (o1.getRow()==o2.getRow()) {
                            return o1.getColumn() - o2.getColumn();
                        }
                        return o1.getRow() - o2.getRow();
                    }
                });
            }

            List<Row> rows = new ArrayList<>();
            for (int i = minRow; i <= maxRow; i ++) {
                List<Cell> rowCells = cellsInRow.get(i);
                if (null==rowCells) {
                    continue;
                }
                Row row = new Row();
                row.row = i;
                row.setCells(rowCells);
                rows.add(row);
            }

            return rows;
        }
    }

    public static class Cell implements IXml<Cell> {
        int row = 0;
        int column = 0;
        String value = "";

        public int getRow() {
            return row;
        }
        public int getColumn() {
            return column;
        }
        public String getValue() {
            return value;
        }
        public String setValue(String value) {
            this.value = value;
            return value;
        }

        @Override
        public Cell parseElement(Element element) {
            if (null==element) {
                return null;
            }
            if (!"cell".equalsIgnoreCase(element.getName())) {
                return null;
            }

            Cell def = new Cell();
            def.value = element.attributeValue("value");
            if (null==def.value || "".equalsIgnoreCase(def.value)) { def.value = element.getTextTrim(); }
            try { def.column = Integer.parseInt(element.attributeValue("column"));} catch (Exception ex) {def.column = 0;}
            try { def.row    = Integer.parseInt(element.attributeValue("row"));   } catch (Exception ex) {def.row    = 0;}
            return def;
        }
    }
}


