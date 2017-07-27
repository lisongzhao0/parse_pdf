package com.happy.gene.pdfreport.pdf.data;

import com.happy.gene.pdfreport.pdf.IXml;
import org.dom4j.Element;

/**
 * Created by zhaolisong on 08/05/2017.
 */
public class Parameter implements IXml<Parameter> {

    private String id    = "param_" + System.currentTimeMillis() +"_"+ System.nanoTime();
    private String group = null;
    private String value = null;

    public Parameter() {}
    public Parameter(String id, String group, String value) {
        this.id     = id;
        this.group  = group;
        this.value  = value;
    }

    public String getId() {
        return id;
    }
    public String getGroup() {
        return group;
    }
    public String getValue() {
        return value;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setGroup(String group) {
        this.group = group;
    }
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public Parameter parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"parameter".equalsIgnoreCase(element.getName())) {
            return null;
        }
        Parameter def = new Parameter();
        def.id    = element.attributeValue("id");
        def.group = element.attributeValue("group");
        def.value = element.attributeValue("value");
        if (null==def.value || "".equalsIgnoreCase(def.value)) {
            def.value = element.getTextTrim();
        }
        return def;
    }

    public String toString() {
        StringBuilder msg = new StringBuilder();
        msg.append("Param[")
            .append("id=").append(id)
            .append(", group=").append(group)
            .append(", value=").append(value)
            .append("]\n");
        return msg.toString();
    }
}
