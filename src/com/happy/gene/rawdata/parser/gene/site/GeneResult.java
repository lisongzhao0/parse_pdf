package com.happy.gene.rawdata.parser.gene.site;

import java.util.*;

public class GeneResult {

    public final List<CancerRisk>                 cancerRiskList;
    public final Map<String,Map<String, Integer>> causalGeneMap;

	public GeneResult(List<CancerRisk> cancerRiskList, Map<String,Map<String, Integer>> causalGeneMap) {
	    this.cancerRiskList = cancerRiskList;
		this.causalGeneMap  = causalGeneMap;
	}
  
}

