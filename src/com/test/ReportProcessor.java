package com.test;

import com.happy.gene.rawdata.parser.gene.site.GSADataRiskCausalParser;
import com.happy.gene.rawdata.parser.gene.site.CancerRisk;
import com.happy.gene.rawdata.parser.gene.site.GeneResult;
import com.happy.gene.rawdata.parser.gene.site.RiskGeneDetail;

import java.util.*;

/**
 * Created by zhaolisong on 21/09/2017.
 */
public class ReportProcessor {

    public static void main(String[] args) {

        System.out.println("Start init ........................... "+System.currentTimeMillis());
        GSADataRiskCausalParser reportProcessor = new GSADataRiskCausalParser();
        reportProcessor.setCausalFilePath("/Users/zhaolisong/Desktop/gene_report_server/data/gene/causal/Causal 20170619.txt");
        reportProcessor.setRiskFilePath("/Users/zhaolisong/Desktop/gene_report_server/data/gene/gene_detection/Cancer risk loci V1.3.xlsx");
        reportProcessor.initData();
        System.out.println("End init - start parse rawdata 1...... "+System.currentTimeMillis());

        Map<String, Map<String, String>>    gsaData                     = reportProcessor.readGSAFile("/Users/zhaolisong/Desktop/baby_check_happygene/源点报告/数据/客户王舒涵-rawdata.txt");
        System.out.println("End init --- read GSA file end........ "+System.currentTimeMillis());
        List<CancerRisk>                    resultCancerRisk            = reportProcessor.calculateCancerRisk(gsaData, "M");
        System.out.println("End init --- calculate end............ "+System.currentTimeMillis());
        Map<String, Map<String, Integer>>   resultCausalRefGeneCount    = reportProcessor.getCausalRefGeneCount();
        System.out.println("End init --- parse rawdata end........ "+System.currentTimeMillis());

        GeneResult geneResult = new GeneResult(resultCancerRisk, resultCausalRefGeneCount);

        StringBuilder msg = new StringBuilder("\n\n");
        for (CancerRisk cr : resultCancerRisk) {
            msg.append(cr.getCancerType()).append("--------").append(cr.getRisk()).append("\n");
            List<RiskGeneDetail> snps = cr.getRiskLociValueList();
            for (RiskGeneDetail snp : snps) {
                //snps, refAllele, riskAllele, allele12, ior
                msg.append("\t").append(snp.snps)
                   .append("\t").append(snp.referAllele)
                   .append("\t").append(snp.riskAllele)
                   .append("\t").append(snp.personResult)
                   .append("\t").append(snp.risk).append("\n");
            }
        }

        msg.append("\n\n");

        Set<String>             keyset      = resultCausalRefGeneCount.keySet();
        Map<String, Integer>    refGeneSize = null;
        Set<String>             keysetRefG  = null;
        for (String key : keyset) {
            msg.append(key).append("\n");
            refGeneSize = resultCausalRefGeneCount.get(key);
            keysetRefG  = refGeneSize.keySet();
            for (String keyRefG : keysetRefG) {
                msg.append("\t").append(keyRefG)
                   .append("\t").append(refGeneSize.get(keyRefG)).append("\n");
            }
        }

        System.out.println(msg.toString());

        try { Thread.sleep(5000); } catch (InterruptedException e) { e.printStackTrace(); }


    }
}
