package com.happy.gene.rawdata.parser.gene.site;

import java.util.List;

/**
 * Created by zhaolisong on 21/09/2017.
 */
public class CancerRisk {

    private String                  cancerType  = "None";
    private double                  risk        = 1.0;
    private List<RiskGeneDetail>    riskLociValueList; //[[snps, refAllele, riskAllele, allele12, ior], ..., [snps, refAllele, riskAllele, allele12, ior]]

    public CancerRisk() { this("None", 1.0); }
    public CancerRisk(String cancerType) { this(cancerType, 1.0); }
    public CancerRisk(String cancerType, double risk) {
        this.cancerType = cancerType;
        this.risk = risk;
    }

    public String getCancerType() { return cancerType; }
    public void setCancerType(String cancerType) { this.cancerType = cancerType; }

    public double getRisk() { return risk; }
    public void setRisk(double risk) { this.risk = risk; }

    public List<RiskGeneDetail> getRiskLociValueList() { return riskLociValueList; }
    public void setRiskLociValueList(List<RiskGeneDetail> riskLociValueList) { this.riskLociValueList = riskLociValueList; }
}

