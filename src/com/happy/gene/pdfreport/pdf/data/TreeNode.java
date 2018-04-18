package com.happy.gene.pdfreport.pdf.data;

import com.happy.gene.pdfreport.pdf.IXml;
import org.dom4j.Element;
import org.dom4j.tree.DefaultAttribute;

import java.util.*;

/**
 * Created by zhaolisong on 20/04/2017.
 */
public class TreeNode implements IXml<TreeNode> {

    private String   id    = "tree_" + System.currentTimeMillis() +"_"+ System.nanoTime();
    private String   group = null;
    private int      level = 0;
    private TreeNode parent= null;
    private List<TreeNode> childs = new ArrayList<>();
    private Map<String, String> properties = new HashMap<>();

    public boolean isRoot() {
        return null!=parent;
    }
    public String getId() {
        return id;
    }
    public String getGroup() {
        return group;
    }

    public TreeNode getParent() {
        return parent;
    }
    public TreeNode setParent(TreeNode parent) {
        this.parent = parent;
        return this;
    }

    public List<TreeNode> getChilds() {
        return childs;
    }
    public TreeNode setChilds(List<TreeNode> childs) {
        this.childs = childs;
        return this;
    }
    public TreeNode addChild(TreeNode child) {
        this.childs.add(child);
        return this;
    }

    public Map<String, String> getProperties() {
        return properties;
    }
    public String getProperty(String key) {
        return properties.get(key);
    }
    public TreeNode setProperties(String key, String value) {
        if (null!=key || !"".equalsIgnoreCase(key.trim())) {
            properties.put(key, value);
        }
        return this;
    }
    public List<String> getPropertiesKey() {
        List<String> res = new ArrayList<>();
        Set<String> keys = properties.keySet();
        for (String key : keys) {
            res.add(key);
        }
        return res;
    }

    public int getLevel() {
        return level;
    }
    public TreeNode setLevel(int level) {
        this.level = level;
        return this;
    }

    @Override
    public TreeNode parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"tree_node".equalsIgnoreCase(element.getName())) {
            return null;
        }
        TreeNode def = new TreeNode();

        Iterator<DefaultAttribute> attributes = element.attributeIterator();
        while(attributes.hasNext()) {
            DefaultAttribute attri = attributes.next();
            String name = attri.getName();
            String value= element.attributeValue(name);
            if ("id".equalsIgnoreCase(name)) {
                def.id = value;
                continue;
            }
            if ("group".equalsIgnoreCase(name)) {
                def.group = value;
                continue;
            }
            if ("value".equalsIgnoreCase(name) && (null==value || value.trim().isEmpty())) {
                value = element.getTextTrim();
            }
            def.setProperties(name, value);
        }

        Iterator<Element> children = element.elementIterator();
        while (children.hasNext()) {
            Element eleChild = children.next();

            TreeNode child = def.parseElement(eleChild);
            child.setParent(def);
            child.setLevel(def.getLevel() + 1);

            def.addChild(child);
        }
        return def;
    }
}
