package com.happy.gene.rawdata.parser.gene.site;

import com.happy.gene.utility.CsvUtil;
import com.happy.gene.utility.OfficeFileUtil;
import com.happy.gene.utility.StringUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.util.*;

/**
 * Created by zhaolisong on 21/09/2017.
 */
public class GSADataRiskCausalParser {

    private static final HashSet<String> columnNamesError   = new HashSet<>(Arrays.asList(new String[]{"Start", "END" }));
    private static final HashSet<String> columnValuesError  = new HashSet<>(Arrays.asList(new String[]{"NA",    "#N/A"}));
    private static final HashSet<String> numberDataColumns  = new HashSet<>(Arrays.asList(new String[]{
                                                                                "Kaviar_AF", "Kaviar_AC", "Kaviar_AN",
                                                                                "ExAC_ALL",  "ExAC_AFR",  "ExAC_AMR",
                                                                                "ExAC_EAS",  "ExAC_FIN",  "ExAC_NFE",
                                                                                "ExAC_OTH",  "ExAC_SAS",  "CADD13_RawScore",
                                                                                "CADD13_PHRED"}));

    private StringUtil      stringUtil      = StringUtil.newInstance();
    private OfficeFileUtil  officeFileUtil  = OfficeFileUtil.newInstance();

    private Map<String, List<Map<String, Object>>>  riskLociData        = null;
    private List<Map<String, Object>>               causalLociData      = null;
    private Map<String, Map<String, Integer>>       causalRefGeneCount  = null;

    private String causalFilePath       = null;
    private String riskFilePath         = null;
    private long   causalFileLastModify = 0l;
    private long   riskFileLastModify   = 0l;

    private String normalizingFieldName(String fieldName) {
        return fieldName.trim()
                .replace("  ", " ")
                .replace(" ", "_")
                .replace("(", "")
                .replace(")", "")
                .replace("@", "")
                .replace(".", "_");
    }
    private boolean isFileModified(String filePath, long lastModify) {
        File file = new File(filePath);
        return file.exists() && file.lastModified() != lastModify;
    }

    public void setCausalFilePath(String causalFilePath) { this.causalFilePath = causalFilePath; }
    public void setRiskFilePath(String riskFilePath) { this.riskFilePath = riskFilePath; }
    public void initData() {
        if (isFileModified(causalFilePath, causalFileLastModify)) {
            readCausalLociData(causalFilePath);
            initCausalLociList();
            causalFileLastModify = new File(causalFilePath).lastModified();
        }
        if (isFileModified(riskFilePath, riskFileLastModify)) {
            readRiskLociData(riskFilePath);
            riskFileLastModify = new File(riskFilePath).lastModified();
        }
    }

    private void readCausalLociData(String causalLociPath) {
        File    file    = new File(causalLociPath);
        try {
            List<Map<String, Object>> rows  = new ArrayList<>(2000);
            if (!file.getName().endsWith("txt")) {
                this.causalLociData = null;
                return;
            }

            CsvUtil csvUtil = new CsvUtil(file.getAbsolutePath(), '\t');
            if (!csvUtil.readHeaders()) {
                this.causalLociData = null;
                return;
            }

            String[] headers = csvUtil.getHeaders();
            int columnSize = headers.length;
            for (int i = 0; i < columnSize; i ++) {
                headers[i] = normalizingFieldName(headers[i]);
            }

            Map<String, Object> row     = null;
            String[]            rowData = null;
            String              header  = null;
            String              value   = null;
            while (csvUtil.readRecord()) {
                row     = new HashMap<>();
                rowData = csvUtil.getValues();

                for (int i = 0; i < columnSize; i++) {
                    header = headers[i];
                    value  = rowData[i].trim();
                    if (numberDataColumns.contains(header)) {
                        if (".".equals(value.trim())) {
                            value = null;
                        }
                    }
                    else {
                        value = value.replace("\'", "\\\'");
                    }

                    if (null!=value) {
                        if ("Cancer".equals(header)) {
                            row.put(header, normalizingFieldName(value));
                        }
                        else {
                            row.put(header, value);
                        }
                    }

                    if (columnNamesError.contains(header) && columnValuesError.contains(value)) {
                        row.put("ERROR", "Start");
                        row.clear();
                        break;
                    }
                }
                if (!row.isEmpty()) {
                    rows.add(row);
                }
            }

            this.causalLociData = rows;
            return;
        }
        catch (Exception ex){}

        this.causalLociData = null;
    }

    private void initCausalLociList() {
        if (null==this.causalLociData) {
            this.causalRefGeneCount = null;
        }

        Map<String, Map<String, Integer>>   causalGeneMap   = new HashMap<>();
        Map<String, Integer>                refGeneCountMap = null;

        for (Map<String, Object> l : this.causalLociData) {
            String cancerType   = (String) l.get("Cancer");
            String refGene      = (String) l.get("Gene_refGene");

            refGeneCountMap = causalGeneMap.get(cancerType);
            if (null==refGeneCountMap) {
                refGeneCountMap = new HashMap<>();
                causalGeneMap.put(cancerType, refGeneCountMap);
            }
            Integer refGeneCount = refGeneCountMap.get(refGene);
            if (null==refGeneCount) {
                refGeneCountMap.put(refGene, 0);
            }

            refGeneCountMap.put(refGene, refGeneCountMap.get(refGene)+1);

        }
        this.causalRefGeneCount = causalGeneMap;
    }

    private void readRiskLociData(String riskLociPath) {
        Workbook    workbook    = officeFileUtil.getExcel(riskLociPath);
        if (null==workbook) { System.err.println("workbook is null"); }
        int         sheetSize   = workbook.getNumberOfSheets();

        Map<String, List<Map<String, Object>>>  riskLociData    = new HashMap<>();
        List<Map<String, Object>>               rows            = null;
        Map<String, Object>                     row             = null;
        List<String>                            columnNames     = null;
        String                                  cancerType      = null;
        int[]                                   rowColStartEnd  = null;
        Cell[][]                                cells           = null;
        Integer[]                               headerRowIndex  = null;

        for (int i = 0; i < sheetSize; i ++) {
            Sheet   sheet       = workbook.getSheetAt(i);
            String  sheetName   = sheet.getSheetName();
            if (null==sheetName || "note".equalsIgnoreCase(sheetName)) {
                continue;
            }

            cancerType      = normalizingFieldName(sheetName);
            rowColStartEnd  = officeFileUtil.getSheetRowColumnStartEnd(sheet);
            cells           = officeFileUtil.getExcelCellArea(sheet, rowColStartEnd[0], rowColStartEnd[1], rowColStartEnd[2], rowColStartEnd[3]);
            headerRowIndex  = officeFileUtil.getExcelContentRowIndex(cells, "[header]", false, false);

            int columnHeadIndex = headerRowIndex[headerRowIndex.length-1] + 1;
            columnNames = new ArrayList<>();
            for (int colIdx = rowColStartEnd[2]; colIdx <= rowColStartEnd[3]; colIdx ++) {
                Cell columnName = cells[columnHeadIndex][colIdx];
                if (null==columnName || stringUtil.isEmpty((String) officeFileUtil.cellValue(columnName))) {
                    columnNames.add(null);
                }
                else {
                    columnNames.add(normalizingFieldName((String) officeFileUtil.cellValue(columnName)));
                }
            }

            rows = new ArrayList<>();
            for (int rowIdx = columnHeadIndex + 1; rowIdx <= rowColStartEnd[1]; rowIdx ++) {
                row = new HashMap<>();
                for (int colIdx = rowColStartEnd[2]; colIdx <= rowColStartEnd[3]; colIdx ++) {
                    String columnName = columnNames.get(colIdx - rowColStartEnd[2]);
                    if (null==columnName) { continue; }

                    Cell value = cells[rowIdx][colIdx];

                    row.put(columnName, officeFileUtil.cellValue(value));
                }

                rows.add(row);
            }

            riskLociData.put(cancerType, rows);
        }

        this.riskLociData = riskLociData;
    }

    public Map<String, Map<String, Integer>> getCausalRefGeneCount() {
        return causalRefGeneCount;
    }

    public Map<String, Map<String, String>> readGSAFile(String filePath) {

        try {
            BufferedReader  reader          = new BufferedReader(new FileReader(filePath));;
            boolean         geneDataLine    = false;
            String[]        columnNames     = null;
            String          line            = null;
            int             snpNameIdx      = -1;
            Map<String, Map<String, String>>    snpAlleleDict   = new HashMap<>();
            while (null!=(line = reader.readLine())) {
                if (line.contains("[Data]")) { geneDataLine = true; continue; }
                if (!geneDataLine) { continue; }

                if (null==columnNames) {
                    if (line.contains("SNP Name")) {
                        line = line.replace("-", "");
                        line = normalizingFieldName(line);
                        columnNames = line.split("\t");
                        for (int i = 0; i < columnNames.length; i ++) {
                            if (columnNames[i].equals("SNP_Name")) {
                                snpNameIdx = i;
                                break;
                            }
                        }
                    }
                    continue;
                }
                else {
                    String[]                            alleleArray     = line.split("\t");
                    Map<String, String>                 alleleDict      = new HashMap<>(50);
                    for (int i = 0; i < alleleArray.length; i ++) {
                        String colN = columnNames[i];
                        String allV = alleleArray[i];
                        alleleDict.put(colN, allV);// TODO -- map 的 put 方法效率上有待优化
                    }
                    snpAlleleDict.put(alleleArray[snpNameIdx], alleleDict);
                }
            }

            return snpAlleleDict;
        }
        catch (Exception ex){}

        return null;
    }

    public List<CancerRisk> calculateCancerRisk(Map<String, Map<String, String>> gsaFileInfo, String gender) {
        if (null==riskLociData) { throw new NullPointerException("risk loci data is empty!"); }
        if (null==causalLociData) { throw new NullPointerException("causal loci data is empty!"); }
        /*
        * 肿瘤风险的计算 V1.0
        * 1 步：上表 Allele 1–Plus 和 Allele 2–Plus 合并产生新变量 Allele1+2： 如 A, A ==> AA
        * 2 步：以下表（SNPs）调取上表（SNP Name）位点的Allele1+2，在下表进行检索：
        *     . 若该位点 Allele1+2 与其 Risk Homo 的值一致，返回 Risk homo OR@（F/M）给该位点新变量 iOR@;
        *     . 若该位点 Allele1+2 与其 Hetero    的值一致，返回 Hetero OR@（F/M）   给该位点      iOR@;
        *     . 若该位点 Allele1+2 与其 WT Homo   的值一致，返回 WT homo OR@（F/M）  给该位点      iOR@。
        * 3 步：将该个体的下表全部位点的 iOR@ 相乘，得出该肿瘤的全部风险值 fOR@
        * 4 步：根据全部肿瘤的风险值 fOR@ 排序，可视化展示
        * 注意，其中 F/M 代表 女性/男性，需要根据患者的临床资料判断；在单一性别肿瘤中，仅有 OR@，调用不分 F/M
        */

        gender = "F".equalsIgnoreCase(gender.trim()) ? "F" : "M".equalsIgnoreCase(gender) ? "M" : "F";
        if (!("F".equalsIgnoreCase(gender) || "M".equalsIgnoreCase(gender))) {
            gender = "F";
        }

        // 1 步
        Map<String, Map<String, String>>    snpAllele12Dict     = gsaFileInfo;
        Map<String, CancerRisk>             cancerRiskIorDict   = new HashMap<>();

        // 2 步
        /* Colorectal Cancer	结直肠癌
        *  Pancreatic Cancer	胰腺癌
        *  Lung Cancer	    肺癌
        *  Breast Cancer	    乳腺癌	    仅限女性
        *  Gastric Cancer	胃癌
        *  Liver Cancer	    肝癌
        *  Postate Cancer	前列腺癌	    仅限男性
        *  Bladder Cancer	膀胱癌
        *  Esophageal Cancer	食管癌
        *  Lymphoma	        淋巴瘤
        *  Thyroid Cancer	甲状腺癌
        *  Cervical Cancer	宫颈癌	    仅限女性
        *  Ovarian Cancer	卵巢癌	    仅限女性
        *  Glioblastoma	    胶质瘤
        *
        *  注意，其中 F/M 代表 女性/男性，需要根据患者的临床资料判断；在单一性别肿瘤中，仅有OR@，调用不分F/M
        */
        List<Map<String, Object>>   riskPercent             = null;
        Set<String>                 riskKeys                = this.riskLociData.keySet();
        List<RiskGeneDetail>        cancerRiskLociValueList = null;
        Map<String, Object>         riskPerc                = null;
        for (String cancerType : riskKeys) {
            riskPercent = this.riskLociData.get(cancerType);

            cancerRiskLociValueList = new ArrayList<>();
            for (int riskIdx = 0; riskIdx < riskPercent.size(); riskIdx++) {
                riskPerc = riskPercent.get(riskIdx);
                String snps = (String) riskPerc.get("SNPS");

                // 2步：以下表（SNPs）调取上表（SNP Name）位点的Allele1+2，在下表进行检索：
                Double ior = 1.0;

                // 1步：上表 Allele1 – Plus 和 Allele 2 – Plus 合并产生新变量 Allele1+2： 如 A, A ==> AA
                if (!snpAllele12Dict.containsKey(snps)) continue;

                String allele12 = snpAllele12Dict.get(snps).get("Allele1_Plus")+snpAllele12Dict.get(snps).get("Allele2_Plus");
                if (!allele12.equals(riskPerc.get("Risk_homo")) && !allele12.equals(riskPerc.get("Hetero")) && !allele12.equals(riskPerc.get("WT_homo"))) {
                    allele12 = allele12.charAt(1) +""+ allele12.charAt(0);
                }

                if (riskPerc.get("Risk_homo").equals(riskPerc.get("Hetero")) && riskPerc.get("Hetero").equals(riskPerc.get("WT_homo"))) {
                    System.out.println("#################");
                }

                String riskHomoOr = null;
                if (allele12.equals(riskPerc.get("Risk_homo"))) {
                    // 若该位点 Allele1+2 与其 Risk Homo 的值一致，返回 Risk homo OR@（F/M）给该位点新变量 iOR@;
                    if (gender.equalsIgnoreCase("F")) {
                        riskHomoOr = "Risk_homo_OR_F";
                    }
                    else if (gender.equalsIgnoreCase("M")) {
                        riskHomoOr = "Risk_homo_OR_M";
                    }
                    ior = (Double) riskPerc.get(riskHomoOr);
                }

                String heteroOr = null;
                if (allele12.equals(riskPerc.get("Hetero"))) {
                    // 若该位点 Allele1+2 与其 Hetero 的值一致，返回 Hetero OR@（F/M）给该位点 iOR@;
                    if (gender.equalsIgnoreCase("F")) {
                        heteroOr = "Hetero_OR_F";
                    }
                    else if (gender.equalsIgnoreCase("M")) {
                        heteroOr = "Hetero_OR_M";
                    }
                    ior = (Double) riskPerc.get(heteroOr);
                }

                String wtHomoOr = null;
                if (allele12.equals(riskPerc.get("WT_homo"))) {
                    // 若该位点 Allele1+2 与其 WT Homo 的值一致，返回 WT homo OR@（F/M）给该位点 iOR@。
                    if (gender.equalsIgnoreCase("F")) {
                        wtHomoOr = "WT_OR_F";
                    }
                    else if (gender.equalsIgnoreCase("M")) {
                        wtHomoOr = "WT_OR_M";
                    }
                    ior = (Double) riskPerc.get(wtHomoOr);
                }

                String refAllele  = (String) riskPerc.get("Ref_Alelle");
                String riskAllele = (String) riskPerc.get("Risk_Allele");

                cancerRiskLociValueList.add(new RiskGeneDetail(snps, refAllele, riskAllele, allele12, ior));

                // 3 步
                // 将该个体的下表全部位点的 iOR@ 相乘，得出该肿瘤的全部风险值 fOR@
                CancerRisk cancerRisk = cancerRiskIorDict.get(cancerType);
                if (null==cancerRisk) {
                    cancerRisk = new CancerRisk(cancerType);
                    cancerRiskIorDict.put(cancerType, cancerRisk);
                }
                cancerRisk.setRisk(cancerRisk.getRisk()* ior);
            }
            cancerRiskIorDict.get(cancerType).setRiskLociValueList(cancerRiskLociValueList);
        }

        // 4 步
        // 根据全部肿瘤的风险值fOR@排序，可视化展示
        List<CancerRisk>        cancerRisksList = new ArrayList<>();
        Collection<CancerRisk>  cancerRisks     = cancerRiskIorDict.values();

        cancerRisksList.addAll(cancerRisks);
        Collections.sort(cancerRisksList, new Comparator<CancerRisk>() {
            @Override
            public int compare(CancerRisk o1, CancerRisk o2) {
                if (null==o1 && null==o2) { return 0; }
                if (null!=o1 && null==o2) { return 1; }
                if (null==o1 && null!=o2) { return -1;}

                double delta = o1.getRisk() - o2.getRisk();
                final double contant = 0.000000000001f;
                if (delta < contant && delta > -contant) { return 0; }
                if (delta > 0.0f) { return -1; }
                if (delta < 0.0f) { return 1;  }

                return 0;
            }
        });

        return  cancerRisksList;
    }

}
