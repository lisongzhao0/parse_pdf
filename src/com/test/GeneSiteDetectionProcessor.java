package com.test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneSiteDetectionProcessor {

    //=============================================
    //           Test -- start
    //=============================================
    public static void main(String[] args) {
        Integer[] resultParamIn = new Integer[]{0,1};
        String[]  result = new String[]{
                "[rs2308327][GG]\n" +
                "[rs763317][GG]\n" +
                "[rs2274223][AA]\n" +
                "[rs2240688][GT]\n" +
                "[rs7574865][GT]\n" +
                "[rs17401966][AA]\n" +
                "[rs4939827][CC]\n" +
                "[rs719725][AA]\n" +
                "[rs1867277][GG]\n" +
                "[rs2279115][GT]"
        };
        String[] arrayDSP = new String[]{
                "[套餐][产品编号][检测内容][基因名称][基因位点][基因型][中国人群占比][风险/正常][相对风险值][中国人群平均遗传风险指数][检测者遗传风险指数][风险等级]\n" +
                "[肿瘤类疾病][yg002][肺癌][MGMT][rs2308327][AA][0.972][风险][1.0113478283075248][19.3]\n" +
                "[肿瘤类疾病][yg002][肺癌][MGMT][rs2308327][AG][0.028][正常][0.6068036647300529][19.3]\n" +
                "[肿瘤类疾病][yg002][肺癌][MGMT][rs2308327][GG][0.0][正常][0.1011328957548911][19.3]\n" +
                "[肿瘤类疾病][yg002][肺癌][EGFR][rs763317][AA][0.036][风险][2.550063395155563][19.3]\n" +
                "[肿瘤类疾病][yg002][肺癌][EGFR][rs763317][AG][0.333][风险][1.3546879827038074][19.3]\n" +
                "[肿瘤类疾病][yg002][肺癌][EGFR][rs763317][GG][0.631][正常][0.7244227165924314][19.3]\n" +
                "[肿瘤类疾病][yg002][胃癌][PLCE1][rs2274223][AA][0.585][正常][0.8122292484564599][18.4]\n" +
                "[肿瘤类疾病][yg002][胃癌][PLCE1][rs2274223][AG][0.351][风险][1.2183540159080484][18.4]\n" +
                "[肿瘤类疾病][yg002][胃癌][PLCE1][rs2274223][GG][0.063][风险][1.543258698770563][18.4]\n" +
                "[肿瘤类疾病][yg002][胃癌][PROM1][rs2240688][TT][0.532][正常][0.772099713589611][18.4]\n" +
                "[肿瘤类疾病][yg002][胃癌][PROM1][rs2240688][GT][0.397][风险][1.1736012240784304][18.4]\n" +
                "[肿瘤类疾病][yg002][胃癌][PROM1][rs2240688][GG][0.071][风险][1.7372587273363742][18.4]\n" +
                "[肿瘤类疾病][yg002][肝癌][STAT4][rs7574865][TT][0.113][正常][0.5770808308463555][22.2]\n" +
                "[肿瘤类疾病][yg002][肝癌][STAT4][rs7574865][GT][0.468][正常][0.8194588514191201][22.2]\n" +
                "[肿瘤类疾病][yg002][肝癌][STAT4][rs7574865][GG][0.419][风险][1.315764218422262][22.2]\n" +
                "[肿瘤类疾病][yg002][肝癌][KIF1B][rs17401966][AA][0.53][风险][1.26640344941696][22.2]\n" +
                "[肿瘤类疾病][yg002][肝癌][KIF1B][rs17401966][AG][0.365][正常][0.7698561310400454][22.2]\n" +
                "[肿瘤类疾病][yg002][肝癌][KIF1B][rs17401966][GG][0.105][正常][0.4555332369576848][22.2]\n" +
                "[肿瘤类疾病][yg002][大肠癌][SMAD7][rs4939827][TT][0.125][风险][1.275272446017186][4.1]\n" +
                "[肿瘤类疾病][yg002][大肠癌][SMAD7][rs4939827][CT][0.371][风险][1.0627224078739228][4.1]\n" +
                "[肿瘤类疾病][yg002][大肠癌][SMAD7][rs4939827][CC][0.504][正常][0.8855987909805305][4.1]\n" +
                "[肿瘤类疾病][yg002][大肠癌][TPD52L3||UHRF2][rs719725][AA][0.544][风险][1.2009903366315864][4.1]\n" +
                "[肿瘤类疾病][yg002][大肠癌][TPD52L3||UHRF2][rs719725][AC][0.387][正常][0.8006536536428339][4.1]\n" +
                "[肿瘤类疾病][yg002][大肠癌][TPD52L3||UHRF2][rs719725][CC][0.069][正常][0.5337661821226226][4.1]\n" +
                "[肿瘤类疾病][yg002][甲状腺癌][FOX1E][rs1867277][AA][0.018][风险][1.7857796580053376][4.7]\n" +
                "[肿瘤类疾病][yg002][甲状腺癌][FOX1E][rs1867277][AG][0.204][风险][1.339322485862893][4.7]\n" +
                "[肿瘤类疾病][yg002][甲状腺癌][FOX1E][rs1867277][GG][0.778][正常][0.8928734856307639][4.7]\n" +
                "[肿瘤类疾病][yg002][甲状腺癌][BCL2][rs2279115][GG][0.323][风险][1.372381174328115][4.7]\n" +
                "[肿瘤类疾病][yg002][甲状腺癌][BCL2][rs2279115][GT][0.518][正常][0.893222076785753][4.7]\n" +
                "[肿瘤类疾病][yg002][甲状腺癌][BCL2][rs2279115][TT][0.159][正常][0.5915341408365891][4.7]\n" +
                "[@FORMULA][@RESULT_CELL_x_10][@GROUP_COLUMN_2][@RESULT_TYPE_DOUBLE][var result = @CELL_0_8 * @CELL_1_8 * @CELL_x_9;]\n" +
                "[@FORMULA][@RESULT_CELL_x_11][@GROUP_COLUMN_2][@RESULT_TYPE_INT][var result = 1; if (@CELL_0_10 < 0.8) { result = 1; } else if (@CELL_0_10 < 1.25) { result = 2; } else if (@CELL_0_10 < 1.5) { result = 3; } else { result = 4;}]\n" +
                "[@RESULT_DSP_PARAM_4_5]",

                "[套餐名称][产品编号][检测内容][基因名称][基因位点][基因型][中国人群占比][风险/正常][相对风险值][风险等级]\n" +
                "[笑傲江湖][yc001][酒精代谢能力][ALDH2][rs671][GG][0.692][正常]\n" + //[高风险：rs671-AA较高风险：rs671-AG+rs1229984-TT/CT正常：rs671-AG+rs1229984-CC或rs671-GG+rs1229984-TT/CT低风险：rs671-GG+rs1229984-CC][N/A]
                "[笑傲江湖][yc001][酒精代谢能力][ALDH2][rs671][AG][0.268][风险]\n" +
                "[笑傲江湖][yc001][酒精代谢能力][ALDH2][rs671][AA][0.04][风险]\n" +
                "[笑傲江湖][yc001][酒精代谢能力][ADH1B][rs1229984][TT][0.484][风险]\n" +
                "[笑傲江湖][yc001][酒精代谢能力][ADH1B][rs1229984][CT][0.427][风险]\n" +
                "[笑傲江湖][yc001][酒精代谢能力][ADH1B][rs1229984][CC][0.089][正常]\n" +
                "[笑傲江湖][yc001][酒精代谢能力][CYP2E1][rs3813867][GG][0.651][正常]\n" +
                "[笑傲江湖][yc001][酒精代谢能力][CYP2E1][rs3813867][CG][0.294][风险]\n" +
                "[笑傲江湖][yc001][酒精代谢能力][CYP2E1][rs3813867][CC][0.056][风险]\n" +
                "[笑傲江湖][yc001][香烟代谢能力][CHRNA4][rs1044396][GG][0.536][风险]\n" +  //[N/A][高风险：3个风险结果较高风险：2个风险结果正常：1个风险结果低风险：无风险结果][N/A]
                "[笑傲江湖][yc001][香烟代谢能力][CHRNA4][rs1044396][AG][0.393][正常]\n" +
                "[笑傲江湖][yc001][香烟代谢能力][CHRNA4][rs1044396][AA][0.071][正常]\n" +
                "[笑傲江湖][yc001][香烟代谢能力][CHRNA3][rs1051730][GG][0.946][正常]\n" +
                "[笑傲江湖][yc001][香烟代谢能力][CHRNA3][rs1051730][AG][0.054][风险]\n" +
                "[笑傲江湖][yc001][香烟代谢能力][CHRNA3][rs1051730][AA][0.0][风险]\n" +
                "[笑傲江湖][yc001][香烟代谢能力][CNR1][rs806368][TT][0.276][正常]\n" +
                "[笑傲江湖][yc001][香烟代谢能力][CNR1][rs806368][CT][0.462][正常]\n" +
                "[笑傲江湖][yc001][香烟代谢能力][CNR1][rs806368][CC][0.262][风险]\n" +
                "[笑傲江湖][yc001][肥胖][APOA2][rs5082][GG][0.016][风险][1.6027660393506609]\n" + //[等级标准：0-0.8；0.8-1.25；1.25-1.5；1.5以上][N/A]
                "[笑傲江湖][yc001][肥胖][APOA2][rs5082][AG][0.165][风险][1.2256032466941622]\n" +
                "[笑傲江湖][yc001][肥胖][APOA2][rs5082][AA][0.819][正常][0.94274786138647]\n" +
                "[笑傲江湖][yc001][肥胖][APOA5 ][rs662799][GG][0.073][正常][0.595848841667976]\n" +
                "[笑傲江湖][yc001][肥胖][APOA5 ][rs662799][AG][0.429][正常][0.8461243087427018]\n" +
                "[笑傲江湖][yc001][肥胖][APOA5 ][rs662799][AA][0.498][风险][1.191761245242446]\n" +
                "[笑傲江湖][yc001][肥胖][PPARG][rs1801282][CC][0.95][正常][0.9916865371336706]\n" +
                "[笑傲江湖][yc001][肥胖][PPARG][rs1801282][CG][0.048][风险][1.150372721287243]\n" +
                "[笑傲江湖][yc001][肥胖][PPARG][rs1801282][GG][0.002][风险][1.3289000664854618]\n" +
                "[笑傲江湖][yc001][肥胖][FTO][rs9939609][TT][0.7][正常][0.9050970174527042]\n" +
                "[笑傲江湖][yc001][肥胖][FTO][rs9939609][AT][0.262][风险][1.1766547207757891]\n" +
                "[笑傲江湖][yc001][肥胖][FTO][rs9939609][AA][0.038][风险][1.5296994704778841]\n" +
                "[@FORMULA][@RESULT_CELL_x_9][@GROUP_COLUMN_2][@RESULT_TYPE_STRING][var result; if ('@CELL_0_4'=='rs671' && '@CELL_1_4'=='rs1229984' && '@CELL_2_4'=='rs3813867') { if ('@CELL_0_5'=='AA') { result='高风险'; } else if ('@CELL_0_5'=='AG' && ('@CELL_1_5'=='TT' || '@CELL_1_5'=='CT')) { result='较高风险'; } else if (('@CELL_0_5'=='AG' && '@CELL_1_5'=='CC') || ('@CELL_0_5'=='GG' && ('@CELL_1_5'=='TT' || '@CELL_1_5'=='CT'))) { result='正常'; } else if ('@CELL_0_5'=='GG' && '@CELL_1_5'=='CC') { result='低风险'; }} else { result='nochanged'; } ]\n" +
                "[@FORMULA][@RESULT_CELL_x_9][@GROUP_COLUMN_2][@RESULT_TYPE_STRING][var result; if ('@CELL_0_4'=='rs1044396' && '@CELL_1_4'=='rs1051730' && '@CELL_2_4'=='rs806368') { var riskSize = 0; riskSize += '@CELL_0_5'=='GG' ? 1 : 0; riskSize += '@CELL_1_5'!='GG' ? 1 : 0; riskSize += '@CELL_0_5'=='CC' ? 1 : 0; if (riskSize==3) { result='高风险'; } else if (riskSize==2) { result='较高风险'; } else if (riskSize==1) { result='正常'; } else { result='低风险'; }} else { result='nochanged'; }  ]\n" +
                "[@FORMULA][@RESULT_CELL_x_9][@GROUP_COLUMN_2][@RESULT_TYPE_INT][var result; if (@CELL_0_8!=null) { result = @CELL_0_8 * @CELL_1_8 * @CELL_2_8 * @CELL_3_8; if (result<0.8) { result = 1; } else if (result<1.25) { result = 2; } else if (result<1.5) { result = 3; } else { result = 4;} } else { result='nochanged' }]\n" +
                "[@RESULT_DSP_PARAM_4_5]",

                "[套餐][产品编号][检测内容][基因名称][基因位点][基因型][中国人群占比][风险/正常][相对风险值][中国人群平均遗传风险指数][检测者遗传风险指数][风险等级]\n" +
                "[肿瘤基础0新增][zljc0-xz][肺癌][MGMT][rs2308327][AA][0.972][风险][1.01][19.3]\n" + //[等级标准：0-0.8；0.8-1.25；1.25-1.5；1.5以上][19.3][N/A]
                "[肿瘤基础0新增][zljc0-xz][肺癌][MGMT][rs2308327][AG][0.028][正常][0.61][19.3]\n" +
                "[肿瘤基础0新增][zljc0-xz][肺癌][MGMT][rs2308327][GG][0.0][正常][0.1][19.3]\n" +
                "[肿瘤基础0新增][zljc0-xz][肺癌][EGFR][rs763317][AA][0.036][风险][2.55][19.3]\n" +
                "[肿瘤基础0新增][zljc0-xz][肺癌][EGFR][rs763317][AG][0.333][风险][1.35][19.3]\n" +
                "[肿瘤基础0新增][zljc0-xz][肺癌][EGFR][rs763317][GG][0.631][正常][0.72][19.3]\n" +
                "[肿瘤基础0新增][zljc0-xz][胃癌][PLCE1][rs2274223][AA][0.585][正常][0.81][18.4]\n" +
                "[肿瘤基础0新增][zljc0-xz][胃癌][PLCE1][rs2274223][AG][0.351][风险][1.22][18.4]\n" +
                "[肿瘤基础0新增][zljc0-xz][胃癌][PLCE1][rs2274223][GG][0.063][风险][1.54][18.4]\n" +
                "[肿瘤基础0新增][zljc0-xz][胃癌][PROM1][rs2240688][TT][0.532][正常][0.77][18.4]\n" +
                "[肿瘤基础0新增][zljc0-xz][胃癌][PROM1][rs2240688][GT][0.397][风险][1.17][18.4]\n" +
                "[肿瘤基础0新增][zljc0-xz][胃癌][PROM1][rs2240688][GG][0.071][风险][1.74][18.4]\n" +
                "[肿瘤基础0新增][zljc0-xz][肝癌][STAT4][rs7574865][TT][0.113][正常][0.58][22.2]\n" +
                "[肿瘤基础0新增][zljc0-xz][肝癌][STAT4][rs7574865][GT][0.468][正常][0.82][22.2]\n" +
                "[肿瘤基础0新增][zljc0-xz][肝癌][STAT4][rs7574865][GG][0.419][风险][1.32][22.2]\n" +
                "[肿瘤基础0新增][zljc0-xz][肝癌][KIF1B][rs17401966][AA][0.53][风险][1.27][22.2]\n" +
                "[肿瘤基础0新增][zljc0-xz][肝癌][KIF1B][rs17401966][AG][0.365][正常][0.77][22.2]\n" +
                "[肿瘤基础0新增][zljc0-xz][肝癌][KIF1B][rs17401966][GG][0.105][正常][0.46][22.2]\n" +
                "[肿瘤基础0新增][zljc0-xz][大肠癌][SMAD7][rs4939827][TT][0.125][风险][1.28][4.1]\n" +
                "[肿瘤基础0新增][zljc0-xz][大肠癌][SMAD7][rs4939827][CT][0.371][风险][1.06][4.1]\n" +
                "[肿瘤基础0新增][zljc0-xz][大肠癌][SMAD7][rs4939827][CC][0.504][正常][0.89][4.1]\n" +
                "[肿瘤基础0新增][zljc0-xz][大肠癌][TPD52L3||UHRF2][rs719725][AA][0.544][风险][1.2][4.1]\n" +
                "[肿瘤基础0新增][zljc0-xz][大肠癌][TPD52L3||UHRF2][rs719725][AC][0.387][正常][0.8][4.1]\n" +
                "[肿瘤基础0新增][zljc0-xz][大肠癌][TPD52L3||UHRF2][rs719725][CC][0.069][正常][0.53][4.1]\n" +
                "[肿瘤基础0新增][zljc0-xz][乳腺癌（女）][BRCA2][rs144848][AA][0.52][正常][0.8455948104155294][17.2]\n" +
                "[肿瘤基础0新增][zljc0-xz][乳腺癌（女）][BRCA2][rs144848][AC][0.391][风险][1.1077351543416902][17.2]\n" +
                "[肿瘤基础0新增][zljc0-xz][乳腺癌（女）][BRCA2][rs144848][CC][0.089][风险][1.4290723226485413][17.2]\n" +
                "[肿瘤基础0新增][zljc0-xz][乳腺癌（女）][TAB2][rs9485372][GG][0.335][风险][1.0994187922554741][17.2]\n" +
                "[肿瘤基础0新增][zljc0-xz][乳腺癌（女）][TAB2][rs9485372][AG][0.484][正常][0.9762814231293004][17.2]\n" +
                "[肿瘤基础0新增][zljc0-xz][乳腺癌（女）][TAB2][rs9485372][AA][0.181][正常][0.8795310692151371][17.2]\n" +
                "[肿瘤基础0新增][zljc0-xz][前列腺癌（男）][HNF1B][rs4430796][GG][0.087][正常][0.7843694003378281][11.7]\n" +
                "[肿瘤基础0新增][zljc0-xz][前列腺癌（男）][HNF1B][rs4430796][AG][0.379][正常][0.933402438057365][11.7]\n" +
                "[肿瘤基础0新增][zljc0-xz][前列腺癌（男）][HNF1B][rs4430796][AA][0.534][风险][1.0824363864097413][11.7]\n" +
                "[肿瘤基础0新增][zljc0-xz][前列腺癌（男）][CCAT2][rs6983267][GG][0.171][风险][1.2965258293875732][11.7]\n" +
                "[肿瘤基础0新增][zljc0-xz][前列腺癌（男）][CCAT2][rs6983267][GT][0.435][风险][1.0534219866266459][11.7]\n" +
                "[肿瘤基础0新增][zljc0-xz][前列腺癌（男）][CCAT2][rs6983267][TT][0.395][正常][0.8103205668678559][11.7]\n" +
                "[@FORMULA][@RESULT_CELL_x_10][@GROUP_COLUMN_2][@RESULT_TYPE_DOUBLE][var result = @CELL_0_8 * @CELL_1_8;]\n" +
                "[@FORMULA][@RESULT_CELL_x_11][@GROUP_COLUMN_2][@RESULT_TYPE_INT][var result = 1; if (@CELL_0_10 < 0.8) { result = 1; } else if (@CELL_0_10 < 1.25) { result = 2; } else if (@CELL_0_10 < 1.5) { result = 3; } else { result = 4;}]\n" +
                "[@RESULT_DSP_PARAM_4_5]",
        };

        GeneSiteDetectionProcessor processor = GeneSiteDetectionProcessor.newInstance();
        processor.setDetection(arrayDSP[0]);

        String[] titles    = processor.getTitles();
        int      titleSize = processor.getTitlesSize();

        StringBuilder cell = new StringBuilder();
        for (int r = 0; r < processor.lPercent.length; r ++) {
            for (int c = 0; c < processor.lPercent[0].length; c++) {
                cell.append("[").append(processor.lPercent[r][c]).append("]");
            }
            System.out.println(cell.toString());
            cell.setLength(0);
        }
        System.out.println("\n\n");

        Object[][] lResPer = processor.calculateResult(result[0], resultParamIn);

        StringBuilder row = new StringBuilder();
        for (int r = 0; r < lResPer.length; r ++) {
            for (int c = 0; c < lResPer[0].length; c++) {
                row.append("[").append(lResPer[r][c]).append("]");
            }
            System.out.println(row.toString());
            row.setLength(0);
        }
        System.out.println("\n\n\n");
    }
    //=============================================
    //           Test -- start
    //=============================================















    public static final String      ResultType          = "@RESULT_TYPE_";
    public static final String      ResultTypeInt       = "@RESULT_TYPE_INT";
    public static final String      ResultTypeBool      = "@RESULT_TYPE_BOOL";
    public static final String      ResultTypeLong      = "@RESULT_TYPE_LONG";
    public static final String      ResultTypeFloat     = "@RESULT_TYPE_FLOAT";
    public static final String      ResultTypeDouble    = "@RESULT_TYPE_DOUBLE";
    public static final String      ResultTypeString    = "@RESULT_TYPE_STRING";


    public static final String      FormulaMark     = "[@FORMULA]";
    public static final String      GroupColumn     = "@GROUP_COLUMN_";
    public static final String      ResultCell      = "@RESULT_CELL_";
    public static final String      Formula         = "var result";
    public static final String      Cell            = "@CELL_";
    public static final String      ResultDSPParam  = "@RESULT_DSP_PARAM_";
    public static final String      ResultNoChanged = "nochanged";


    private String          detection;
    private String[]        titles;
    private int             titlesSize;
    private Integer[]       dspParamIdxs;
    private Object[][]      lPercent;
    private List<Formula>   lFormula;
    private ScriptEngine    javascriptEngine;
    private final Formula   utility = new Formula();


    public static final GeneSiteDetectionProcessor newInstance() { return new GeneSiteDetectionProcessor(); }
    public static final GeneSiteDetectionProcessor newInstance(String detection) { return new GeneSiteDetectionProcessor(detection); }
    private GeneSiteDetectionProcessor(){ ScriptEngineManager manager = new ScriptEngineManager(); javascriptEngine = manager.getEngineByName("javascript"); }
    private GeneSiteDetectionProcessor(String detection) { this(); setDetection(detection); }


    public String[] getTitles() {
        String[] resTitles = null==titles ? null : new String[titlesSize];
		if (null==resTitles) { return resTitles; }
        System.arraycopy(titles, 0, resTitles, 0, titlesSize);
        return resTitles;
    }

    public int getTitlesSize() { return titlesSize; }

    public void setDetection(String detection) {
        this.detection = detection;
        parse();
    }

    private void parse() {
        titles      = null;
        titlesSize  = 0;
        lPercent    = null;
        lFormula    = null;
        dspParamIdxs= null;
        if (null==detection || detection.trim().isEmpty()) {
            return;
        }
        else {
            List<String>   lines            = readTextByLine(detection);
            if (null==lines || lines.isEmpty()) {
                return;
            }

            String         strResDspParam   = null;
            String         strTitle         = null;
            List<String>   detectionPercent = new ArrayList<>();
            List<String>   detectionFormula = new ArrayList<>();

            int     rowIdx = 0;
            for (String line : lines) {
                if (0==rowIdx) {
                    strTitle = line;
                }
                else {
                    if (line.startsWith(FormulaMark)) {
                        detectionFormula.add(line);
                    }
                    else if (line.contains(ResultDSPParam)) {
                        strResDspParam = line;
                    }
                    else {
                        detectionPercent.add(line);
                    }
                }
                rowIdx ++;
            }

            dspParamIdxs = utility.getResultDSPParamIdx(strResDspParam, titles);
            titles       = utility.getColumn(strTitle);
            titlesSize   = null==titles ? 0 : titles.length;
            lPercent     = utility.parseDSP(detectionPercent, titlesSize);
            if (null!=detectionFormula && !detectionFormula.isEmpty()) {
                lFormula = new ArrayList<>();
                for (String df : detectionFormula) {
                    Formula f = new Formula();
                    f.setFormula(df);
                    f.setTitles(titles);
                    f.parse();
                    lFormula.add(f);
                }
            }
        }
    }

    private List<String> readTextByLine(String text) {
        if (null==text || text.trim().isEmpty()) { return null; }
        try {
            BufferedReader reader   = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(text.getBytes("UTF-8"))));
            List<String>   lines    = new ArrayList<>();

            String  strRow = null;
            while (null!=(strRow=reader.readLine())) {
                lines.add(strRow);
            }
            return lines;
        }
        catch (Exception ex){}
        return null;
    }

    public Object[][] calculateResult(String result, Integer[] resultParamIdx) {
        List<String> resultLines = readTextByLine(result);
        if (null==resultLines || resultLines.isEmpty()) { return null; }

        Object[][] lResult       = utility.parseDSP(resultLines, titlesSize);
        Object[][] lResultPercent= utility.getResultDSP(lPercent, lResult, dspParamIdxs, resultParamIdx);

        if (null!=lFormula && !lFormula.isEmpty()) {
            for (Formula f : lFormula) {
                f.calculateFormula(lResultPercent, javascriptEngine);
            }
        }

        Object[][] newResPer = new Object[lResultPercent.length+1][lResultPercent[0].length];
        System.arraycopy(lResultPercent, 0, newResPer,    1, lResultPercent.length);
        System.arraycopy(titles,  0, newResPer[0], 0, titlesSize);

        return newResPer;
    }



    //=====================================================================
    //             Formula
    //=====================================================================
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

        String[]    titles          = null;
        String      formula         = null;

        LogicCell   resultCell      = null;
        String      resultType      = null;
        int[]       groupColumnIdx  = null;
        List<LogicCell> logicCells  = null;
        String      logicFormula    = null;

        public void setFormula(String formula) {
            this.formula = formula;
            if (null!=this.formula && !this.formula.isEmpty()) {
                this.formula = this.formula.replace(FormulaMark, "");
            }
        }
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

        public void     parse() {
            resultCell      = getResultCell(formula, titles);
            resultType      = getResultType(formula, titles);
            groupColumnIdx  = getGroupColumn(formula, titles);
            logicCells      = getLogicCell(formula, titles);
            logicFormula    = getLogicFormula(formula);
        }

        public void     calculateFormula(Object[][] dsp, ScriptEngine scriptEngine) {
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

        private void    groupColumnCopy(Object[] column, String[] groupColumn, int[] groupColumnIdx) {
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

        private void    fillLogicCells(List<LogicCell> logicCells, GroupIndex groupIndex, Object[][]dsp) {
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

        private String  replceLogicCellsInFormula(String logicFormula, List<LogicCell> logicCells) {
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

        private Object  resultValue(String logicF, ScriptEngine scriptEngine) {
            Object val = null;
            try {
                scriptEngine.eval(logicF);
                val = scriptEngine.get("result");
                if (null!=val) {
                    if (null==val) {
                        val = null;
                    }
                    else if (ResultTypeInt.equalsIgnoreCase(resultType)) {
                        val = ((Integer) val).intValue();
                    }
                    else if (ResultTypeLong.equalsIgnoreCase(resultType)) {
                        val = ((Double) val).longValue();
                    }
                    else if (ResultTypeFloat.equalsIgnoreCase(resultType)) {
                        val = ((Double) val).floatValue();
                    }
                    else if (ResultTypeDouble.equalsIgnoreCase(resultType)) {
                        val = ((Double) val).doubleValue();
                    }
                    else if (ResultTypeBool.equalsIgnoreCase(resultType)) {
                        val = (Boolean) val;
                    }
                    else if (ResultTypeString.equalsIgnoreCase(resultType)) {
                        val = (String) val;
                    }
                }
            }
            catch (Exception ex) {}
            return val;
        }

        private void    setResultValue(LogicCell resultCell, GroupIndex groupIndex, Object[][] dsp) {
            if (null==resultCell) { return; }
            if (null==dsp || dsp.length==0) { return; }
            if ((resultCell.val instanceof String) && ResultNoChanged.equalsIgnoreCase((String)resultCell.val)) { return; }

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

        //============================================
        //    utility
        //============================================
        public Integer[]getResultDSPParamIdx(String resultDspParam, String[] titles) {
            if (null==resultDspParam || resultDspParam.trim().isEmpty()) { return null; }

            resultDspParam  = resultDspParam.substring(1, resultDspParam.length()-1);
            resultDspParam  = resultDspParam.replace(ResultDSPParam, "");
            String[] strIdxs= resultDspParam.split("_");

            List<Integer> idxs = new ArrayList<>();
            Integer i = null;
            for (String si : strIdxs) {
                i = null;
                try { i = Integer.parseInt(si); } catch (Exception ex){} // row
                if (null==i) { i = getTitleIndex(si, titles); }

                if (null!=i) { idxs.add(i); }
            }

            if (idxs.isEmpty()) { return null; }
            return idxs.toArray(new Integer[idxs.size()]);
        }

        public String[]     getColumn(String title) {
            if (null==title || title.trim().isEmpty()) { return null; }

            StringBuilder regex         = new StringBuilder();
            regex.append('\\').append(']').append('\\').append('[');
            String[]      columns       = title.substring(1, title.length()-1).split(regex.toString());
            return columns;
        }

        public Object[][]   parseDSP(List<String> dsp, int columnSize) {
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

        public  Object[][]  getResultDSP(Object[][] dsp, Object[][] result, Integer[] dspCompareColumn, Integer[] resultCompareColumn) {
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
    }
}
