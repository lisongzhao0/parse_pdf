package com.happy.gene.rawdata.parser.gene.site;

import java.util.*;

public class GeneResult {

    public final List<CancerRisk>                 cancerRiskList;
    public final Map<String,Map<String, Integer>> causalGeneMap;

	public GeneResult(List<CancerRisk> cancerRiskList, Map<String,Map<String, Integer>> causalGeneMap) {
	    this.cancerRiskList = cancerRiskList;
		this.causalGeneMap  = causalGeneMap;
	}

	public void clear() {
		if (null!=cancerRiskList) {
			List tmpList = null;
			for (CancerRisk tmp : cancerRiskList) {
				tmpList = tmp.getRiskLociValueList();
				if (null!=tmpList) { tmpList.clear(); }
				tmp.setRiskLociValueList(null);
			}
			cancerRiskList.clear();
		}
		if (null!=causalGeneMap) {
			Map<String, Integer> values = null;
			Set<String> keys = causalGeneMap.keySet();
			for (String key : keys) {
				values = causalGeneMap.get(key);
				if (null!=values) { values.clear(); }
				causalGeneMap.put(key, null);
			}
			causalGeneMap.clear();
		}
		System.gc();
	}
}

