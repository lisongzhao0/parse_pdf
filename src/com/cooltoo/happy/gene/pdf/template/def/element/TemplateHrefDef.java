package com.cooltoo.happy.gene.pdf.template.def.element;

import com.cooltoo.happy.gene.pdf.template.IDrawable;
import com.cooltoo.happy.gene.pdf.template.IXml;
import com.cooltoo.happy.gene.pdf.template.def.AbsolutePositionTemplateDef;
import com.cooltoo.happy.gene.pdf.template.def.PageDef;
import com.cooltoo.happy.gene.pdf.template.util.XmlDataCache;
import com.itextpdf.layout.Document;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by zhaolisong on 10/05/2017.
 */
public class TemplateHrefDef extends AbstractDef implements IXml<TemplateHrefDef> {

    private String name;
    private String condition = AbsolutePositionTemplateDef.DEFAULT_CONDITION;
    private AbsolutePositionTemplateDef template;
    private Map<String, String> values = new HashMap<>();

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public TemplateHrefDef parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"template".equalsIgnoreCase(element.getName())) {
            return null;
        }

        this.name      = element.attributeValue("name");
        this.condition = element.attributeValue("condition");
        try { this.setZOrder(Integer.parseInt(element.attributeValue("z_order"))); } catch (Exception ex) {this.setZOrder(0);};
        if (null==this.condition || "".equalsIgnoreCase(this.condition)) {
            this.condition = AbsolutePositionTemplateDef.DEFAULT_CONDITION;
        }

        List<Element> replaceValues = element.elements();
        for (Element tmp : replaceValues) {
            if (!"value".equalsIgnoreCase(tmp.getName())) {
                continue;
            }
            String valueId = tmp.attributeValue("id");
            String value = tmp.attributeValue("value");
            values.put(valueId, value);
        }

        XmlDataCache dataCache = XmlDataCache.getInstance();
        AbsolutePositionTemplateDef template = dataCache.getTemplate(name);

        this.template = template.clone();
        this.template.replaceValue(values);

        return this;
    }

    @Override
    public Object generate(Document pdf, PageDef pageDef) throws Exception {
        AbsolutePositionTemplateDef template = this.template;
        if (null!=template) {
            List<AbstractDef> components = null;
            components = template.getComponents(condition);
            if (null==components || components.isEmpty()) {
                components = template.getComponents(AbsolutePositionTemplateDef.DEFAULT_CONDITION);
            }
            if (null==components || components.isEmpty()) {
                return null;
            }
            for (AbstractDef tmp : components) {
                if (tmp instanceof IDrawable) {
                    ((IDrawable) tmp).generate(pdf, pageDef);
                }
            }
        }
        return null;
    }

    @Override
    public AbstractDef clone() {
        TemplateHrefDef newOne = new TemplateHrefDef();
        newOne.setZOrder(this.getZOrder());
        newOne.name      = this.name;
        newOne.condition = this.condition;

        Set<String> keys = this.values.keySet();
        for (String key : keys) {
            newOne.values.put(key+"", this.values.get(key)+"");
        }

        newOne.template = this.template.clone();

        return newOne;
    }
}
