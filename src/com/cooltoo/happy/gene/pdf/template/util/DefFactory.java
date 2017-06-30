package com.cooltoo.happy.gene.pdf.template.util;

import com.cooltoo.happy.gene.pdf.template.def.PageDef;
import com.cooltoo.happy.gene.pdf.template.def.PageGroupDef;
import com.cooltoo.happy.gene.pdf.template.def.element.*;
import com.cooltoo.happy.gene.pdf.template.def.element.catalog.CatalogDef;
import com.cooltoo.happy.gene.pdf.template.def.element.table.HeaderAreaDef;
import com.cooltoo.happy.gene.pdf.template.def.element.table.HeaderDef;
import com.cooltoo.happy.gene.pdf.template.def.element.table.RowDef;
import com.cooltoo.happy.gene.pdf.template.def.element.table.VerticalTableDef;
import org.dom4j.Element;

import java.util.Map;

/**
 * Created by zhaolisong on 25/04/2017.
 */
public class DefFactory {

    public static DefFactory getIntance() {
        return new DefFactory();
    }

    public AbstractDef getDef(Element ele) {
        AbstractDef newDef = (new PageGroupDef()).parseElement(ele);
        if (null!=newDef) {
            return newDef;
        }
        newDef = (new PageDef()).parseElement(ele);
        if (null!=newDef) {
            return newDef;
        }
        newDef = (new PathDef()).parseElement(ele);
        if (null!=newDef) {
            return newDef;
        }
        newDef = (new RectDef()).parseElement(ele);
        if (null!=newDef) {
            return newDef;
        }
        newDef = (new TextDef()).parseElement(ele);
        if (null!=newDef) {
            return newDef;
        }
        newDef = (new TextGroupDef()).parseElement(ele);
        if (null!=newDef) {
            return newDef;
        }
        newDef = (new ImageDef()).parseElement(ele);
        if (null!=newDef) {
            return newDef;
        }
        newDef = (new CatalogDef()).parseElement(ele);
        if (null!=newDef) {
            return newDef;
        }
        newDef = (new VerticalTableDef()).parseElement(ele);
        if (null!=newDef) {
            return newDef;
        }
        newDef = (new HeaderDef(null)).parseElement(ele);
        if (null!=newDef) {
            return newDef;
        }
        newDef = (new RowDef(null)).parseElement(ele);
        if (null!=newDef) {
            return newDef;
        }
        newDef = (new PercentDef()).parseElement(ele);
        if (null!=newDef) {
            return newDef;
        }
        newDef = (new CircleDef()).parseElement(ele);
        if (null!=newDef) {
            return newDef;
        }
        newDef = (new ParagraphDef()).parseElement(ele);
        if (null!=newDef) {
            return newDef;
        }
        newDef = (new TemplateHrefDef()).parseElement(ele);
        if (null!=newDef) {
            return newDef;
        }
        newDef = (new RepeatDef()).parseElement(ele);
        if (null!=newDef) {
            return newDef;
        }
        newDef = (new HeaderAreaDef()).parseElement(ele);
        if (null!=newDef) {
            return newDef;
        }
        return null;
    }
}
