package com.happy.gene.pdfreport.pdf;


import com.happy.gene.pdfreport.pdf.def.PageDef;
import com.itextpdf.layout.Document;

/**
 * Created by zhaolisong on 12/04/2017.
 */
public interface IDrawable<T> {

    T generate(Document pdf, PageDef pageDef) throws Exception;
}
