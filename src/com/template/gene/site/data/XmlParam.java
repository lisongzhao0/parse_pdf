package com.template.gene.site.data;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

/**
 * Created by zhaolisong on 21/06/2017.
 */
public class XmlParam {
    public String id;
    public String value;
    public boolean isCDATA;

    public XmlParam() {}

    public XmlParam(boolean isCDATA, String id, String value) {
        this.isCDATA    = isCDATA;
        this.id         = id;
        this.value      = value;
    }

    public XmlParam setId(String id) {
        this.id         = id;
        return this;
    }

    public XmlParam setValue(String value) {
        this.value      = value;
        return this;
    }

    public XmlParam isCDATA(boolean isCDATA) {
        this.isCDATA    = isCDATA;
        return this;
    }

    public Element toXml() {
        Element eleParam    = new DefaultElement("parameter");
        eleParam.addAttribute("id", id);
        if (isCDATA) {
            eleParam.addAttribute("value", "");
            eleParam.addCDATA(value);
        }
        else {
            eleParam.addAttribute("value", value);
        }
        return eleParam;
    }
}
