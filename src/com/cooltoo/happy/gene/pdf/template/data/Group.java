package com.cooltoo.happy.gene.pdf.template.data;

import com.cooltoo.happy.gene.pdf.template.IXml;
import com.cooltoo.happy.gene.pdf.template.IZOrder;
import com.cooltoo.happy.gene.pdf.template.def.element.AbstractDef;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaolisong on 11/05/2017.
 */
public class Group implements IXml<Group>, IZOrder {

    private int    zOrder;
    private String id;
    private String value;
    private Map<String, String>    properties = new HashMap<>();

    private Map<String, Parameter> params = new HashMap<>();
    private Map<String, TreeNode>  trees  = new HashMap<>();
    private Map<String, DataTable> tables = new HashMap<>();


    public int getZOrder() {
        return zOrder;
    }
    public void setZOrder(int zOrder) {
        this.zOrder = zOrder;
    }

    public String getId() {
        return id;
    }
    public String getValue() {
        return value;
    }

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

    public Map<String, Parameter> getParams() {
        return params;
    }
    public Map<String, TreeNode> getTrees() {
        return trees;
    }
    public Map<String, DataTable> getTables() {
        return tables;
    }

    @Override
    public Group parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"group".equalsIgnoreCase(element.getName())) {
            return null;
        }


        Group def = new Group();
        def.id    = element.attributeValue("id");
        def.value = element.attributeValue("value");
        List<Attribute> attributes = element.attributes();
        for (Attribute tmp : attributes) {
            if (null!=tmp) {
                def.properties.put(tmp.getName(), tmp.getValue());
            }
        }

        try { def.setZOrder(Integer.parseInt(element.attributeValue("order"))); } catch (Exception ex) {def.setZOrder(100000000);}

        DataTable table = new DataTable();
        Parameter param = new Parameter();
        TreeNode  tree  = new TreeNode();


        List<Element> params = element.elements();
        for (Element tmp : params) {
            if ("parameter".equalsIgnoreCase(tmp.getName())) {
                Parameter newOne = param.parseElement(tmp);
                if (null!=newOne.getValue() && !"".equalsIgnoreCase(newOne.getValue())) {
                    def.params.put(newOne.getId(), newOne);
                }
            }
            if ("table".equalsIgnoreCase(tmp.getName())) {
                DataTable newOne = table.parseElement(tmp);
                def.tables.put(newOne.getId(), newOne);
            }
            if ("tree_node".equalsIgnoreCase(tmp.getName())) {
                TreeNode newOne = tree.parseElement(tmp);
                def.trees.put(newOne.getId(), newOne);
            }
        }

        return def;
    }
}
