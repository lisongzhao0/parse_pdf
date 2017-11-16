package com.test;


import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;

public class JavaScritpInvokeTest {


    public static void main(String[] args) throws ScriptException {
        StringBuilder row = new StringBuilder();
        List<String> result = new ArrayList<>();
        result.addAll(Arrays.asList(new String[]{
                "[rs2308327][AA]",
                "[rs763317][GG]",
                "[rs2274223][AA]",
                "[rs2240688][GT]",
                "[rs7574865][GT]",
                "[rs17401966][AA]",
                "[rs4939827][CC]",
                "[rs719725][AA]",
                "[rs1867277][GG]",
                "[rs2279115][GT]"
        }));

        String[]   column       = getColumn(detectionTitle1);
        int        columnSize   = null==column ? 0 : column.length;
        Object[][] lPercent     = parseDSP(detectionSitePercent1, columnSize);
        Object[][] lResult      = parseDSP(result, getColumn(result.get(0)).length);


        Object[][] lResPer      = getResultDSP(lPercent, lResult, new int[]{4,5}, new int[]{0, 1});

        for (int r = 0; r < lResPer.length; r ++) {
            for (int c = 0; c < lResPer[0].length; c++) {
                row.append("[").append(lResPer[r][c]).append("]");
            }
            System.out.println(row.toString());
            row.setLength(0);
        }


        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine javaScript = manager.getEngineByName("javascript");

        Formula formula1 = new Formula();
        formula1.setFormula(resultFormula1.get(0));
        formula1.setTitles(column);
        formula1.parse();
        formula1.calculateFormula(lResPer, javaScript);

        for (int r = 0; r < lResPer.length; r ++) {
            for (int c = 0; c < lResPer[0].length; c++) {
                row.append("[").append(lResPer[r][c]).append("]");
            }
            System.out.println(row.toString());
            row.setLength(0);
        }
        System.out.println("\n\n\n");

        Formula formula2 = new Formula();
        formula2.setFormula(resultFormula1.get(1));
        formula2.setTitles(column);
        formula2.parse();
        formula2.calculateFormula(lResPer, javaScript);

        for (int r = 0; r < lResPer.length; r ++) {
            for (int c = 0; c < lResPer[0].length; c++) {
                row.append("[").append(lResPer[r][c]).append("]");
            }
            System.out.println(row.toString());
            row.setLength(0);
        }



    }

    public static final String      GroupColumn = "@GROUP_COLUMN_";
    public static final String      ResultCell  = "@RESULT_CELL_";
    public static final String      Cell        = "@CELL_";
    public static final String      ResultType  = "@RESULT_TYPE_";

    public static String            detectionTitle1 = "[套餐][产品编号][检测内容][基因名称][基因位点][基因型][中国人群占比][风险/正常][相对风险值][中国人群平均遗传风险指数][检测者遗传风险指数][风险等级]";
    public static List<String>      resultFormula1   = new ArrayList<>();
    public static List<String>      detectionSitePercent1 = new ArrayList<>();

    public static List<String>      detectionSite1   = new ArrayList<>();


    public static String[]          getColumn(String title) {
        if (null==title || title.trim().isEmpty()) { return null; }

        StringBuilder regex         = new StringBuilder();
        regex.append('\\').append(']').append('\\').append('[');
        String[]      columns       = title.substring(1, title.length()-1).split(regex.toString());
        return columns;
    }

    public static Object[][]        parseDSP(List<String> dsp, int columnSize) {
        if (null==dsp || dsp.isEmpty()) { return null; }

        String[]      columns       = null;
        StringBuilder regex         = new StringBuilder();
        regex.append('\\').append(']').append('\\').append('[');

        int         rowSize     = 0;
        String      tmp         = null;
        Object[][]  arrayDSP    = new Object[dsp.size()][columnSize];

        for (int row = 0; row < arrayDSP.length; row ++) {
            tmp = dsp.get(row);

            tmp = tmp.substring(1, tmp.length()-1);
            columns = tmp.split(regex.toString());

            for (int col = 0; col < columnSize; col ++) {
                if (col < columns.length) {
                    tmp = columns[col];
                }
                else {
                    tmp = null;
                }
                Object val = null;
                if (null==val) {try { val = Integer.parseInt(tmp); } catch (Exception ex) {}}
                if (null==val) {try { val = Long.parseLong(tmp); } catch (Exception ex) {}}
                if (null==val) {try { val = Float.parseFloat(tmp); } catch (Exception ex) {}}
                if (null==val) {try { val = Double.parseDouble(tmp); } catch (Exception ex) {}}
                if (null==val && ("true".equalsIgnoreCase(tmp) || "false".equalsIgnoreCase(tmp))) {try { val = Boolean.parseBoolean(tmp); } catch (Exception ex) {}}
                if (null==val) { val = tmp; }
                if ("N/A".equalsIgnoreCase(tmp)) { val = null; }

                arrayDSP[rowSize][col] = val;
            }

            rowSize ++;
        }


        Object[][]  rArrayDSP   = new Object[rowSize][columnSize];
        System.arraycopy(arrayDSP, 0, rArrayDSP, 0, rowSize);
        arrayDSP = null;
        return rArrayDSP;
    }

    public static Object[][]        getResultDSP(Object[][] dsp, Object[][] result, int[] dspCompareColumn, int[] resultCompareColumn) {
        if (null==dsp || dsp.length == 0) { return null; }
        if (null==result || result.length == 0) { return null; }
        if (null==dspCompareColumn || dspCompareColumn.length == 0) { return null; }
        if (null==resultCompareColumn || resultCompareColumn.length == 0) { return null; }

        Map<String, Integer> resultKey = new HashMap<>();
        StringBuilder        key       = new StringBuilder();
        for (int row = 0; row < result.length; row ++) {
            for (int col = 0; col < resultCompareColumn.length; col ++) {
                key.append("[").append(result[row][resultCompareColumn[col]]).append("]");
            }
            resultKey.put(key.toString(), null);
            key.setLength(0);
        }


        int         rowSize     = 0;
        Object[][]  destDSP     = new Object[dsp.length][dsp[0].length];
        for (int row = 0; row < dsp.length; row ++) {
            key.setLength(0);

            for (int col = 0; col < dspCompareColumn.length; col ++) {
                key.append("[").append(dsp[row][dspCompareColumn[col]]).append("]");
            }

            if (!resultKey.containsKey(key.toString())) {
                continue;
            }

            System.arraycopy(dsp[row], 0, destDSP[rowSize], 0, dsp[row].length);
            rowSize ++;
        }

        Object[][]  resultDSP   = new Object[rowSize][dsp[0].length];

        System.arraycopy(destDSP, 0, resultDSP, 0, rowSize);
        return resultDSP;
    }

    public static class Formula {

        public static class GroupIndex {
            int start = 0;
            int end   = 0;
        }

        public static class LogicCell {
            Integer row = null;
            Integer col = null;
            Object  val = null;
        }

        public static final String      ResultType          = "@RESULT_TYPE_";
        public static final String      ResultTypeInt       = "@RESULT_TYPE_INT";
        public static final String      ResultTypeBool      = "@RESULT_TYPE_BOOL";
        public static final String      ResultTypeLong      = "@RESULT_TYPE_LONG";
        public static final String      ResultTypeFloat     = "@RESULT_TYPE_FLOAT";
        public static final String      ResultTypeDouble    = "@RESULT_TYPE_DOUBLE";
        public static final String      ResultTypeString    = "@RESULT_TYPE_STRING";

        public static final String      GroupColumn = "@GROUP_COLUMN_";
        public static final String      ResultCell  = "@RESULT_CELL_";
        public static final String      Formula     = "var result = ";
        public static final String      Cell        = "@CELL_";

        String[]    titles          = null;
        String      formula         = null;

        LogicCell   resultCell      = null;
        String      resultType      = null;
        int[]       groupColumnIdx  = null;
        List<LogicCell> logicCells  = null;
        String      logicFormula    = null;

        public void setFormula(String formula) { this.formula = formula; }
        public void setTitles(String[] titles) { this.titles = titles; }


        private String       getResultType(String formula, String[] titles) {
            String[]    columns = getColumn(formula);
            String      column  = null;
            if (null==columns || columns.length==0) { return null; }

            for (int i = 0, count = columns.length; i < count; i ++) {
                column = columns[i];
                if (column.startsWith(ResultType)) { return column; }
            }

            return null;
        }

        private LogicCell    getResultCell(String formula, String[] titles) {
            String[] columns = getColumn(formula);
            if (null==columns || columns.length==0) { return null; }

            String      column  = null;
            String[]    splite  = null;
            for (int i = 0, count = columns.length; i < count; i ++) {
                column = columns[i];

                if (column.startsWith(ResultCell)) {
                    column = column.replace(ResultCell, "");
                    splite = column.split("_");
                    if (null==splite || splite.length==0 && splite.length!=2) {
                        continue;
                    }

                    LogicCell resCell = new LogicCell();
                    try { resCell.row = Integer.parseInt(splite[0]); } catch (Exception ex){} // row
                    try { resCell.col = Integer.parseInt(splite[1]); } catch (Exception ex){} // col

                    if (null==resCell.row) { resCell.row = getTitleIndex(splite[0], titles); }
                    if (null==resCell.col) { resCell.col = getTitleIndex(splite[1], titles); }

                    return resCell;
                }
            }

            return null;
        }

        private int[]        getGroupColumn(String formula, String[] titles) {
            String[] columns = getColumn(formula);
            if (null==columns || columns.length==0) { return null; }

            List<Integer> result = new ArrayList<>();

            String      column = null;
            String[]    split = null;
            for (int i = 0, count = columns.length; i < count; i ++) {
                column = columns[i];

                if (column.startsWith(GroupColumn)) {
                    column = column.replace(GroupColumn, "");
                    split = column.split("_");
                    if (null==split || split.length==0) { continue; }

                    for (String integer : split) {
                        Integer intg = null;
                        try { intg = Integer.parseInt(integer); } catch (Exception ex){}

                        if (null==intg) { intg = getTitleIndex(integer, titles); }

                        if (null!=intg) { result.add(intg); }
                    }
                }
            }

            if (result.isEmpty()) { return null; }

            int[] aInt = new int[result.size()];
            for (int idx = 0, count = result.size(); idx < count; idx ++) {
                Integer tmp = result.get(idx);
                aInt[idx] = tmp;
            }

            return aInt;
        }

        private List<LogicCell>  getLogicCell(String formula, String[] titles) {
            String[] columns = getColumn(formula);
            if (null==columns || columns.length==0) { return null; }

            // get formula
            String  strFormula  = null;
            String  column      = null;
            for (int i = 0, count = columns.length; i < count; i ++) {
                column = columns[i];
                if (null!=column && column.trim().startsWith(Formula)) {
                    strFormula = column.trim();
                    break;
                }
            }

            // get cell size
            int     cellSize    = 0;
            for (int idx = 0; idx < strFormula.length(); idx ++) {
                idx = strFormula.indexOf(Cell, idx);
                if (idx < 0) { break; }
                cellSize ++;
            }
            if (cellSize<=0) { return null; }

            // get cell
            List<LogicCell> cells   = new ArrayList<>();
            String[]        split   = null;
            cellSize = 0;
            for (int idx = 0, count = strFormula.length(); idx < count; idx ++) {
                idx     = strFormula.indexOf(Cell, idx);
                if (idx < 0) { break; }

                StringBuilder rowOrCol = new StringBuilder();
                int rcIdx = 0;
                for (rcIdx = idx+Cell.length(); rcIdx < count; rcIdx ++) {
                    char ch = strFormula.charAt(rcIdx);
                    if ("0123456789_x".indexOf(ch)<0) { break; }
                    rowOrCol.append(ch);
                }
                idx = rcIdx-1;

                split   = rowOrCol.toString().split("_");
                if (null==split || split.length==0 || split.length!=2) {
                    continue;
                }

                LogicCell logicCell = new LogicCell();
                try { logicCell.row = Integer.parseInt(split[0]); } catch (Exception ex){} // row
                try { logicCell.col = Integer.parseInt(split[1]); } catch (Exception ex){} // col

                if (null==logicCell.row) { logicCell.row = getTitleIndex(split[0], titles); }
                if (null==logicCell.col) { logicCell.col = getTitleIndex(split[1], titles); }

                cells.add(logicCell);

                if (idx < 0) { break; }
                cellSize ++;
            }

            return cells;
        }

        private String       getLogicFormula(String formula) {
            String[] columns = getColumn(formula);
            if (null==columns || columns.length==0) { return null; }

            String  strFormula  = null;
            String  column      = null;
            for (int i = 0, count = columns.length; i < count; i ++) {
                column = columns[i];
                if (null!=column && column.trim().startsWith(Formula)) {
                    strFormula = column.trim();
                    break;
                }
            }

            return strFormula;
        }

        private Integer      getTitleIndex(String t, String[] titles) {
            if (null==t || t.isEmpty()) { return null; }
            if (null==titles || titles.length==0) { return null; }

            for (int titleIdx = 0; titleIdx < titles.length; titleIdx ++) {
                if (!t.equals(titles[titleIdx])) { continue; }
                return titleIdx;
            }

            return null;
        }

        public void parse() {
            resultCell      = getResultCell(formula, titles);
            resultType      = getResultType(formula, titles);
            groupColumnIdx  = getGroupColumn(formula, titles);
            logicCells      = getLogicCell(formula, titles);
            logicFormula    = getLogicFormula(formula);
        }

        public void calculateFormula(Object[][] dsp, ScriptEngine scriptEngine) {
            if (null==dsp || dsp.length==0) { return; }

            List<GroupIndex>    groupIndexs = groupColumnIndex(dsp);

            if (null==groupIndexs || groupIndexs.isEmpty()) {
                if (null==logicCells || logicCells.isEmpty()) {
                    resultCell.val = resultValue(logicFormula, scriptEngine);
                    setResultValue(resultCell, null, dsp);
                }
                else {
                    fillLogicCells(logicCells, null, dsp);
                    String tmpLogicFormula = replceLogicCellsInFormula(logicFormula, logicCells);
                    resultCell.val = resultValue(tmpLogicFormula, scriptEngine);
                    setResultValue(resultCell, null, dsp);
                }
            }
            else {
                for (GroupIndex gI : groupIndexs) {
                    if (null==logicCells || logicCells.isEmpty()) {
                        resultCell.val = resultValue(logicFormula, scriptEngine);
                        setResultValue(resultCell, gI, dsp);
                    }
                    else {
                        fillLogicCells(logicCells, gI, dsp);
                        String tmpLogicFormula = replceLogicCellsInFormula(logicFormula, logicCells);
                        resultCell.val = resultValue(tmpLogicFormula, scriptEngine);
                        setResultValue(resultCell, gI, dsp);
                    }
                }
            }
        }

        private boolean groupColumnEqual(Object[] column, String[] groupColumn, int[] groupColumnIdx) {
            if (null==groupColumnIdx) { return true; }
            if (null==column && null==groupColumn) { return true; }
            if (null==column && null!=groupColumn) { return false; }
            if (null!=column && null==groupColumn) { return false; }

            boolean equal = true;
            for (int gcIdx = 0, count = groupColumnIdx.length; gcIdx < count; gcIdx ++) {
                if (equal && null==groupColumn[gcIdx] && null==column[groupColumnIdx[gcIdx]]) { equal = true; }
                if (equal && null!=groupColumn[gcIdx] && null==column[groupColumnIdx[gcIdx]]) { equal = false; }
                if (equal && null==groupColumn[gcIdx] && null!=column[groupColumnIdx[gcIdx]]) { equal = false; }
                if (equal && !groupColumn[gcIdx].equals(column[groupColumnIdx[gcIdx]])) { equal = false; }

                if (!equal) { break; }
            }

            return equal;
        }

        private void groupColumnCopy(Object[] column, String[] groupColumn, int[] groupColumnIdx) {
            if (null==groupColumnIdx) { return; }
            if (null==column && null==groupColumn) { return; }
            if (null==column && null!=groupColumn) { return; }
            if (null!=column && null==groupColumn) { return; }

            boolean equal = true;
            for (int gcIdx = 0, count = groupColumnIdx.length; gcIdx < count; gcIdx ++) {
                groupColumn[gcIdx] = (String)column[groupColumnIdx[gcIdx]];
            }

            return;
        }

        private List<GroupIndex> groupColumnIndex(Object[][] dsp) {
            if (null==dsp || dsp.length==0) { return null; }

            List<GroupIndex>    groupIndexes = new ArrayList<>();
            GroupIndex          groupIndex  = null;
            if (null==groupColumnIdx) {
                groupIndex          = new GroupIndex();
                groupIndex.start    = 0;
                groupIndex.end      = dsp.length-1;
                groupIndexes.add(groupIndex);
                return groupIndexes;
            }

            String[]            group       = new String[groupColumnIdx.length];
            for (int rowIdx = 0; rowIdx < dsp.length; rowIdx ++) {
                if (!groupColumnEqual(dsp[rowIdx], group, groupColumnIdx)) {
                    if (groupIndexes.isEmpty()) {
                        groupColumnCopy(dsp[rowIdx], group, groupColumnIdx);
                        groupIndex = new GroupIndex();
                        groupIndex.start = rowIdx;
                        groupIndexes.add(groupIndex);
                    }
                    else {
                        groupColumnCopy(dsp[rowIdx], group, groupColumnIdx);
                        groupIndex.end = rowIdx - 1;
                        groupIndex = new GroupIndex();
                        groupIndex.start = rowIdx;
                        groupIndexes.add(groupIndex);
                    }
                }
            }
            if (null!=groupIndex) {
                groupIndex.end = dsp.length - 1;
            }

            return groupIndexes;
        }

        private void fillLogicCells(List<LogicCell> logicCells, GroupIndex groupIndex, Object[][]dsp) {
            if (null==logicCells || logicCells.isEmpty()) { return; }
            if (null==dsp || dsp.length==0) { return; }

            for (LogicCell lc : logicCells) {
                int colIdx = null!=lc.col ? lc.col : 0;
                int rowIdx = 0;
                if (null!=lc.row) {
                    if (null==groupIndex) { rowIdx = lc.row; }
                    else { rowIdx = groupIndex.start+lc.row; }
                }
                else {
                    if (null==groupIndex) { rowIdx = 0; }
                    else { rowIdx = groupIndex.start; }
                }

                lc.val = dsp[rowIdx][colIdx];
            }
        }

        private String replceLogicCellsInFormula(String logicFormula, List<LogicCell> logicCells) {
            if (null==logicFormula || logicFormula.isEmpty()) { return null; }
            if (null==logicCells   || logicCells.isEmpty()) { return logicFormula; }

            String result = logicFormula;
            String cell   = null;
            for (LogicCell lc : logicCells) {
                cell = Cell + (null==lc.row ? "x" : lc.row) + "_" + (null==lc.col ? "x" : lc.col);
                result = result.replace(cell, (null==lc.val ? "null" : lc.val.toString()));
            }

            return result;
        }

        private Object resultValue(String logicF, ScriptEngine scriptEngine) {
            Object val = null;
            try {
                scriptEngine.eval(logicF);
                val = scriptEngine.get("result");
                if (null!=val) {
                    if (ResultTypeInt.equalsIgnoreCase(resultType)) {
                        val = ((Integer) val).intValue();
                    }
                    if (ResultTypeLong.equalsIgnoreCase(resultType)) {
                        val = ((Double) val).longValue();
                    }
                    if (ResultTypeFloat.equalsIgnoreCase(resultType)) {
                        val = ((Double) val).floatValue();
                    }
                    if (ResultTypeDouble.equalsIgnoreCase(resultType)) {
                        val = ((Double) val).doubleValue();
                    }
                    if (ResultTypeBool.equalsIgnoreCase(resultType)) {
                        val = (Boolean) val;
                    }
                    if (ResultTypeString.equalsIgnoreCase(resultType)) {
                        val = (String) val;
                    }
                }
            }
            catch (Exception ex) {}
            return val;
        }

        private void setResultValue(LogicCell resultCell, GroupIndex groupIndex, Object[][] dsp) {
            if (null==resultCell) { return; }
            if (null==dsp || dsp.length==0) { return; }

            Integer row = resultCell.row;
            Integer col = resultCell.col;

            if (null==row && null==col) { return; }
            if (null!=row && null!=col) {
                if (null==groupIndex) {
                    dsp[row][col] = resultCell.val;
                }
                else {
                    dsp[row+groupIndex.start][col] = resultCell.val;
                }
                return;
            }
            if (null!=row && null==col) {
                if (null==groupIndex) {
                    for (int i = 0; i < dsp[0].length; i++) {
                        dsp[row][i] = resultCell.val;
                    }
                }
                else {
                    for (int i = 0; i < dsp[0].length; i++) {
                        dsp[row+groupIndex.start][i] = resultCell.val;
                    }
                }
                return;
            }
            if (null==row && null!=col) {
                if (null==groupIndex) {
                    for (int i = 0; i < dsp.length; i++) {
                        dsp[i][col] = resultCell.val;
                    }
                }
                else {
                    for (int r = groupIndex.start; r <= groupIndex.end; r ++) {
                        dsp[r][col] = resultCell.val;
                    }
                }
                return;
            }
        }
    }

    static {
        String[] arrayDSP = new String[]{
                "[肿瘤类疾病][yg002][肺癌][MGMT][rs2308327][AA][0.972][风险][1.0113478283075248][19.3]",
                "[肿瘤类疾病][yg002][肺癌][MGMT][rs2308327][AG][0.028][正常][0.6068036647300529][19.3]",
                "[肿瘤类疾病][yg002][肺癌][MGMT][rs2308327][GG][0.0][正常][0.1011328957548911][19.3]",
                "[肿瘤类疾病][yg002][肺癌][EGFR][rs763317][AA][0.036][风险][2.550063395155563][19.3]",
                "[肿瘤类疾病][yg002][肺癌][EGFR][rs763317][AG][0.333][风险][1.3546879827038074][19.3]",
                "[肿瘤类疾病][yg002][肺癌][EGFR][rs763317][GG][0.631][正常][0.7244227165924314][19.3]",
                "[肿瘤类疾病][yg002][胃癌][PLCE1][rs2274223][AA][0.585][正常][0.8122292484564599][18.4]",
                "[肿瘤类疾病][yg002][胃癌][PLCE1][rs2274223][AG][0.351][风险][1.2183540159080484][18.4]",
                "[肿瘤类疾病][yg002][胃癌][PLCE1][rs2274223][GG][0.063][风险][1.543258698770563][18.4]",
                "[肿瘤类疾病][yg002][胃癌][PROM1][rs2240688][TT][0.532][正常][0.772099713589611][18.4]",
                "[肿瘤类疾病][yg002][胃癌][PROM1][rs2240688][GT][0.397][风险][1.1736012240784304][18.4]",
                "[肿瘤类疾病][yg002][胃癌][PROM1][rs2240688][GG][0.071][风险][1.7372587273363742][18.4]",
                "[肿瘤类疾病][yg002][肝癌][STAT4][rs7574865][TT][0.113][正常][0.5770808308463555][22.2]",
                "[肿瘤类疾病][yg002][肝癌][STAT4][rs7574865][GT][0.468][正常][0.8194588514191201][22.2]",
                "[肿瘤类疾病][yg002][肝癌][STAT4][rs7574865][GG][0.419][风险][1.315764218422262][22.2]",
                "[肿瘤类疾病][yg002][肝癌][KIF1B][rs17401966][AA][0.53][风险][1.26640344941696][22.2]",
                "[肿瘤类疾病][yg002][肝癌][KIF1B][rs17401966][AG][0.365][正常][0.7698561310400454][22.2]",
                "[肿瘤类疾病][yg002][肝癌][KIF1B][rs17401966][GG][0.105][正常][0.4555332369576848][22.2]",
                "[肿瘤类疾病][yg002][大肠癌][SMAD7][rs4939827][TT][0.125][风险][1.275272446017186][4.1]",
                "[肿瘤类疾病][yg002][大肠癌][SMAD7][rs4939827][CT][0.371][风险][1.0627224078739228][4.1]",
                "[肿瘤类疾病][yg002][大肠癌][SMAD7][rs4939827][CC][0.504][正常][0.8855987909805305][4.1]",
                "[肿瘤类疾病][yg002][大肠癌][TPD52L3||UHRF2][rs719725][AA][0.544][风险][1.2009903366315864][4.1]",
                "[肿瘤类疾病][yg002][大肠癌][TPD52L3||UHRF2][rs719725][AC][0.387][正常][0.8006536536428339][4.1]",
                "[肿瘤类疾病][yg002][大肠癌][TPD52L3||UHRF2][rs719725][CC][0.069][正常][0.5337661821226226][4.1]",
                "[肿瘤类疾病][yg002][甲状腺癌][FOX1E][rs1867277][AA][0.018][风险][1.7857796580053376][4.7]",
                "[肿瘤类疾病][yg002][甲状腺癌][FOX1E][rs1867277][AG][0.204][风险][1.339322485862893][4.7]",
                "[肿瘤类疾病][yg002][甲状腺癌][FOX1E][rs1867277][GG][0.778][正常][0.8928734856307639][4.7]",
                "[肿瘤类疾病][yg002][甲状腺癌][BCL2][rs2279115][GG][0.323][风险][1.372381174328115][4.7]",
                "[肿瘤类疾病][yg002][甲状腺癌][BCL2][rs2279115][GT][0.518][正常][0.893222076785753][4.7]",
                "[肿瘤类疾病][yg002][甲状腺癌][BCL2][rs2279115][TT][0.159][正常][0.5915341408365891][4.7]"
        };
        detectionSitePercent1.addAll(Arrays.asList(arrayDSP));
        String[] arrayRF = new String[]{
                "[@RESULT_CELL_x_10][@GROUP_COLUMN_2][@RESULT_TYPE_DOUBLE][var result = @CELL_0_8 * @CELL_1_8 * @CELL_x_9;]",
                "[@RESULT_CELL_x_11][@GROUP_COLUMN_2][@RESULT_TYPE_INT][var result = 1; if (@CELL_0_10 < 0.8) { result = 1; } else if (@CELL_0_10 < 1.25) { result = 2; } else if (@CELL_0_10 < 1.5) { result = 3; } else { result = 4;}]"
        };
        resultFormula1.addAll(Arrays.asList(arrayRF));
    }
}
