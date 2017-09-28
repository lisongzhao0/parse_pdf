package com.happy.gene.rawdata.parser.gene.site;

public class RiskGeneDetail {
    public final String snps;
    public final String referAllele;
    public final String riskAllele;
    public final String personResult;
    public final double risk;

    public RiskGeneDetail(String snps, String referAllele, String riskAllele, String personResult, double risk) {
        this.snps           = snps;
        this.referAllele    = referAllele;
        this.riskAllele     = riskAllele;
        this.personResult   = personResult;
        this.risk           = risk;
    }
}

