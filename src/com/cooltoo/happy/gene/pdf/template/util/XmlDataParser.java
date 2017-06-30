package com.cooltoo.happy.gene.pdf.template.util;

import com.cooltoo.happy.gene.pdf.template.IPageDefRenderer;
import com.cooltoo.happy.gene.pdf.template.IZOrder;
import com.cooltoo.happy.gene.pdf.template.data.*;
import com.cooltoo.happy.gene.pdf.template.def.AbsolutePositionTemplateDef;
import com.cooltoo.happy.gene.pdf.template.def.PageGroupDef;
import com.cooltoo.happy.gene.pdf.template.def.element.AbstractDef;
import com.cooltoo.happy.gene.pdf.template.util.log.Logger;
import com.cooltoo.happy.gene.pdf.template.def.FontDef;
import com.cooltoo.happy.gene.pdf.template.def.PageDef;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultDocument;

import java.io.*;
import java.util.*;

public class XmlDataParser {

    public static XmlDataParser getInstance() {
        return new XmlDataParser();
    }

    private XmlDataParser() {}

    public Map<String, FontDef> getFonts(String templateFilePath) {
        Element root  = getRootElement(templateFilePath);
        if (null == root) {
            return null;
        }
        Element fonts = root.element("fonts");
        if (null == fonts) {
            return null;
        }
        Iterator<Element> allFont = fonts.elementIterator();

        Map<String, FontDef> res = new HashMap<>();
        while (allFont.hasNext()) {
            Element font = allFont.next();
            FontDef newFont = (new FontDef()).parseElement(font);
            if (null!=newFont) {
                res.put(newFont.getId(), newFont);
            }
        }

        return res;
    }

    public Map<String, Parameter> getParameters(String templateFilePath) {
        Element root  = getRootElement(templateFilePath);
        if (null == root) {
            return null;
        }
        Element data = root.element("data");
        if (null == data) {
            return null;
        }
        Iterator<Element> allParams = data.elementIterator();

        Parameter node = new Parameter();
        Map<String, Parameter> res = new HashMap<>();
        while (allParams.hasNext()) {
            Element param = allParams.next();
            if (!"parameter".equalsIgnoreCase(param.getName())) {
                continue;
            }

            Parameter newParam = node.parseElement(param);
            if (null!=newParam.getValue() && !"".equalsIgnoreCase(newParam.getValue())) {
                res.put(newParam.getId(), newParam);
            }
        }

        return res;
    }

    public Map<String, TreeNode> getTrees(String templateFilePath) {
        Element root  = getRootElement(templateFilePath);
        if (null == root) {
            return null;
        }
        Element data = root.element("data");
        if (null == data) {
            return null;
        }
        Iterator<Element> allParams = data.elementIterator();

        TreeNode node = new TreeNode();
        Map<String, TreeNode> res = new HashMap<>();
        while (allParams.hasNext()) {
            Element tree = allParams.next();
            if ("tree_node".equalsIgnoreCase(tree.getName())) {
                TreeNode treeRoot = node.parseElement(tree);
                res.put(treeRoot.getId(), treeRoot);
            }
        }

        return res;
    }

    public Map<String, DataTable> getTables(String templateFilePath) {
        Element root  = getRootElement(templateFilePath);
        if (null == root) {
            return null;
        }
        Element data = root.element("data");
        if (null == data) {
            return null;
        }
        Iterator<Element> allParams = data.elementIterator();

        DataTable node = new DataTable();
        Map<String, DataTable> res = new HashMap<>();
        while (allParams.hasNext()) {
            Element tree = allParams.next();
            if ("table".equalsIgnoreCase(tree.getName())) {
                DataTable table = node.parseElement(tree);
                res.put(table.getId(), table);
            }
        }

        return res;
    }

    public List<Group> getGroups(String templateFilePath) {
        Element root  = getRootElement(templateFilePath);
        if (null == root) {
            return null;
        }
        Element data = root.element("data");
        if (null == data) {
            return null;
        }
        Iterator<Element> allParams = data.elementIterator();

        Group node = new Group();
        List<Group> res = new ArrayList<>();
        while (allParams.hasNext()) {
            Element group = allParams.next();
            if ("group".equalsIgnoreCase(group.getName())) {
                Group newOne = node.parseElement(group);
                res.add(newOne);
            }
        }

        Collections.sort(res, IZOrder.comparator);

        return res;
    }

    public List<AbstractDef> getPages(String templateFilePath, IPageDefRenderer defRenderer) {
        Element root  = getRootElement(templateFilePath);
        if (null == root) {
            return null;
        }
        List<AbstractDef> res        = new ArrayList<>();
        DefFactory        defFactory = DefFactory.getIntance();

        List<Element> pageOrPageGroup = root.elements();
        for (Element tmp : pageOrPageGroup) {
            String eleName = tmp.getName();
            if (!"page".equalsIgnoreCase(eleName) && !"page_group".equalsIgnoreCase(eleName)) {
                continue;
            }
            AbstractDef newPage = defFactory.getDef(tmp);
            if (newPage instanceof PageDef) {
                ((PageDef) newPage).setPageDefRenderer(defRenderer);
                res.add(newPage);
            }
            if (newPage instanceof PageGroupDef) {
                ((PageGroupDef) newPage).setPageDefRenderer(defRenderer);
                res.add(newPage);
            }
        }

        return res;
    }

    public Map<String, AbsolutePositionTemplateDef> getTemplates(String templateFilePath) {
        Element root  = getRootElement(templateFilePath);
        if (null == root) {
            return null;
        }

        Element rootTemplates = root.element("templates");
        if (null==rootTemplates) {
            return null;
        }

        Iterator<Element> templates = rootTemplates.elementIterator();

        Map<String, AbsolutePositionTemplateDef> res = new HashMap<>();
        while (templates.hasNext()) {
            Element template = templates.next();
            AbsolutePositionTemplateDef newTemplate = (new AbsolutePositionTemplateDef()).parseElement(template);
            if (null!=newTemplate) {
                res.put(newTemplate.getName(), newTemplate);
            }
        }

        return res;
    }

    public Map<String, Repeat> getRepeats(String templateFilePath) {
        Element root  = getRootElement(templateFilePath);
        if (null == root) {
            return null;
        }
        Element data = root.element("data");
        if (null == data) {
            return null;
        }
        Iterator<Element> allParams = data.elementIterator();

        Repeat node = new Repeat();
        Map<String, Repeat> res = new HashMap<>();
        while (allParams.hasNext()) {
            Element elem = allParams.next();
            if ("repeat".equalsIgnoreCase(elem.getName())) {
                Repeat repeat = node.parseElement(elem);
                res.put(repeat.getId(), repeat);
            }
        }

        return res;
    }

    public Element getRootElement(byte[] content) {
        try {
            SAXReader reader  = new SAXReader();
            Document doc = reader.read(new ByteArrayInputStream(content));
            if (!(doc instanceof DefaultDocument)) {
                Logger.error("get DefaultDocument instance failed! content byte array size={}", null==content ? "null" : ""+content.length);
                return null;
            }
            DefaultDocument defDoc = (DefaultDocument) doc;
            Element root  = defDoc.getRootElement();
            return root;
        }
        catch (Exception ex) {
            Logger.error("parse xml file has exception={}", ex.getMessage());
            return null;
        }
    }

    private Element getRootElement(String templateFilePath) {
        File file = new File(templateFilePath);
        if (!file.exists() || !file.isFile()) {
            Logger.error("file not exist! file={}", templateFilePath);
            return null;
        }
        try {
            SAXReader reader  = new SAXReader();
            Document doc = reader.read(file);
            if (!(doc instanceof DefaultDocument)) {
                Logger.error("get DefaultDocument instance failed! file={}", templateFilePath);
                return null;
            }
            DefaultDocument defDoc = (DefaultDocument) doc;
            Element root  = defDoc.getRootElement();
            return root;
        }
        catch (Exception ex) {
            Logger.error("parse xml file has exception={}", ex.getMessage());
            return null;
        }
    }
}

