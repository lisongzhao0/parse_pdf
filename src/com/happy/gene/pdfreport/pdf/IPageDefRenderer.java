package com.happy.gene.pdfreport.pdf;

import com.happy.gene.pdfreport.pdf.def.PageDef;
import com.itextpdf.layout.Document;

import java.util.Map;

/**
 * Created by zhaolisong on 10/05/2017.
 */
public interface IPageDefRenderer {
    Map<String, Integer> getCatalogs();
    void beforeRenderer(Document pdf, PageDef page) throws Exception;
    void rendererHeader(Document pdf, PageDef page) throws Exception;
    void rendererTail(Document pdf, PageDef page) throws Exception;
    void rendererCatalog(Document pdf, PageDef page) throws Exception;
    void afterRenderer(Document pdf, PageDef page) throws Exception;
}
