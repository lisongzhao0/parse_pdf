package com.happy.gene.pdfreport.pdf.util;

import com.happy.gene.pdfreport.pdf.IPageDefRenderer;
import com.happy.gene.pdfreport.pdf.IZOrder;
import com.happy.gene.pdfreport.pdf.data.DataTable;
import com.happy.gene.pdfreport.pdf.data.Group;
import com.happy.gene.pdfreport.pdf.data.Parameter;
import com.happy.gene.pdfreport.pdf.data.Repeat;
import com.happy.gene.pdfreport.pdf.data.TreeNode;
import com.happy.gene.pdfreport.pdf.def.AbsolutePositionTemplateDef;
import com.happy.gene.pdfreport.pdf.def.FontDef;
import com.happy.gene.pdfreport.pdf.def.PageDef;
import com.happy.gene.pdfreport.pdf.def.PageGroupDef;
import com.happy.gene.pdfreport.pdf.def.element.AbstractDef;
import com.happy.gene.pdfreport.pdf.util.log.Logger;
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

    public Map<String, FontDef> getFonts(String templateDirPath, String fontFilePath) {
        if (null==templateDirPath || templateDirPath.isEmpty()) {
            return null;
        }
        templateDirPath = templateDirPath.replace('\\','/');
        templateDirPath = templateDirPath.endsWith("/") ? templateDirPath : templateDirPath + "/";
        Element root  = getRootElement(templateDirPath+fontFilePath);

        return getFonts(root);
    }

    public Map<String, FontDef> getFonts(Document document) {
        Element root  = null==document ? null : document.getRootElement();
        return getFonts(root);
    }

    public Map<String, FontDef> getFonts(Element root) {
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
        return getParameters(root);
    }

    public Map<String, Parameter> getParameters(Document document) {
        Element root  = null==document ? null : document.getRootElement();
        return getParameters(root);
    }

    public Map<String, Parameter> getParameters(Element root) {
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
        return getTrees(root);
    }

    public Map<String, TreeNode> getTrees(Document document) {
        Element root  = null==document ? null : document.getRootElement();
        return getTrees(root);
    }

    public Map<String, TreeNode> getTrees(Element root) {
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
        return getTables(root);
    }

    public Map<String, DataTable> getTables(Document document) {
        Element root  = null==document ? null : document.getRootElement();
        return getTables(root);
    }

    public Map<String, DataTable> getTables(Element root) {
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
        return getGroups(root);
    }

    public List<Group> getGroups(Document document) {
        Element root  = null==document ? null : document.getRootElement();
        return getGroups(root);
    }

    public List<Group> getGroups(Element root) {
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
        return getPages(root, defRenderer);
    }

    public List<AbstractDef> getPages(Document document, IPageDefRenderer defRenderer) {
        Element root  = null==document ? null : document.getRootElement();
        return getPages(root, defRenderer);
    }

    public List<AbstractDef> getPages(Element root, IPageDefRenderer defRenderer) {
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
        return getTemplates(root);
    }

    public Map<String, AbsolutePositionTemplateDef> getTemplates(Document document) {
        Element root  = null==document ? null : document.getRootElement();
        return getTemplates(root);
    }

    public Map<String, AbsolutePositionTemplateDef> getTemplates(Element root) {
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
        return getRepeats(root);
    }

    public Map<String, Repeat> getRepeats(Document document) {
        Element root  = null==document ? null : document.getRootElement();
        return getRepeats(root);
    }

    public Map<String, Repeat> getRepeats(Element root) {
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

