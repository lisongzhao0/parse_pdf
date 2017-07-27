package com.happy.gene.pdfreport.pdf.def;

import com.happy.gene.pdfreport.pdf.IPageDefRenderer;
import com.happy.gene.pdfreport.pdf.IXml;
import com.happy.gene.pdfreport.pdf.IZOrder;
import com.happy.gene.pdfreport.pdf.def.element.AbstractDef;
import com.happy.gene.pdfreport.pdf.util.DefFactory;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.*;

/**
 * Created by zhaolisong on 11/05/2017.
 */
public class PageGroupDef extends AbstractDef implements IXml<PageGroupDef> {

    private List<AbstractDef> components = new ArrayList<>();
    private Map<String, String> properties = new HashMap<>();

    public String  getProperty(String key) {
        String value = properties.get(key);
        return value;
    }
    public boolean getBooleanProperty(String key) {
        String value = getProperty(key);
        return Boolean.parseBoolean(value);
    }
    public Integer getIntProperty(String key) {
        String value = getProperty(key);
        try { return Integer.parseInt(value); }
        catch (Exception ex) { return null; }
    }
    public List<AbstractDef> getComponents() {
        return this.components;
    }

    public void setPageDefRenderer(IPageDefRenderer pageDefRenderer) {
        for (AbstractDef obj : this.components) {
            if (obj instanceof PageDef) {
                ((PageDef) obj).setPageDefRenderer(pageDefRenderer);
            }
        }
    }

    @Override
    public PageGroupDef parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"page_group".equalsIgnoreCase(element.getName())) {
            return null;
        }


        List<Attribute> attributes = element.attributes();
        for (Attribute tmp : attributes) {
            if (null!=tmp) {
                properties.put(tmp.getName(), tmp.getValue());
            }
        }

        this.setCatalog(element.attributeValue("catalog"));
        this.setVisible(getStringUtil().isEmpty(element.attributeValue("visible")) ? "true" : element.attributeValue("visible"));
        try { this.setZOrder(Integer.parseInt(element.attributeValue("z_order"))); } catch (Exception ex) {this.setZOrder(100000000);}

        DefFactory defFactory = DefFactory.getIntance();
        List<Element> subEle = element.elements();
        for (Element ele : subEle) {
            AbstractDef subComp = defFactory.getDef(ele);
            if (null!=subComp) {
                this.components.add(subComp);
            }
        }

        Collections.sort(this.components, IZOrder.comparator);

        return this;
    }

    @Override
    public PageGroupDef clone() {
        PageGroupDef newOne = new PageGroupDef();
        newOne.setZOrder(this.getZOrder());
        Set<String> keys = this.properties.keySet();
        for (String key : keys) {
            newOne.properties.put(key+"", this.properties.get(key)+"");
        }

        for (AbstractDef obj : this.components) {
            if (obj instanceof AbstractDef) {
                AbstractDef clone = obj.clone();
                if (null!=clone) {
                    newOne.components.add(clone);
                }
            }
        }
        return newOne;
    }
}
