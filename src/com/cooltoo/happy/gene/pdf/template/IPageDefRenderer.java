package com.cooltoo.happy.gene.pdf.template;

import com.cooltoo.happy.gene.pdf.template.def.PageDef;
import com.itextpdf.layout.Document;

/**
 * Created by zhaolisong on 10/05/2017.
 */
public interface IPageDefRenderer {
    void beforeRenderer(Document pdf, PageDef page) throws Exception;
    void rendererHeader(Document pdf, PageDef page) throws Exception;
    void rendererTail(Document pdf, PageDef page) throws Exception;
    void rendererCatalog(Document pdf, PageDef page) throws Exception;
    void afterRenderer(Document pdf, PageDef page) throws Exception;
}
