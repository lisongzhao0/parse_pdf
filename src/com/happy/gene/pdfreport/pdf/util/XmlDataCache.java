package com.happy.gene.pdfreport.pdf.util;

import com.happy.gene.pdfreport.pdf.data.DataTable;
import com.happy.gene.pdfreport.pdf.data.Group;
import com.happy.gene.pdfreport.pdf.data.Parameter;
import com.happy.gene.pdfreport.pdf.data.Repeat;
import com.happy.gene.pdfreport.pdf.data.TreeNode;
import com.happy.gene.pdfreport.pdf.def.AbsolutePositionTemplateDef;
import com.happy.gene.pdfreport.pdf.def.FontDef;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaolisong on 08/05/2017.
 */
public class XmlDataCache {

    public  static String TOP_KEY_REPEAT            = "repeat";
    public  static String TOP_KEY_PARAMETER         = "parameter";
    public  static String TOP_KEY_TABLE             = "table";
    public  static String TOP_KEY_TREE              = "tree";
    public  static String TOP_KEY_FONT              = "font";
    public  static String TOP_KEY_IMAGE_URL         = "image_url";
    public  static String TOP_KEY_TEMPLATE          = "template";
    public  static String TOP_KEY_GROUP             = "group";
    public  static String SECOND_KEY_IMAGE_BASE_DIR = "SECOND_KEY_IMAGE_BASE_DIR";

    private static XmlDataCache instance;

    private Map<String, Object> parameters;

    public static XmlDataCache getInstance() {
        if (null==instance) {
            instance = new XmlDataCache();
        }
        return instance;
    }

    private XmlDataCache() {
        parameters = new HashMap<>();
    }

    public void clearCache() {
        parameters.clear();
    }

    public XmlDataCache setParameters(String key, Object value) {
        if (null!=key && !"".equalsIgnoreCase(key)) {
            parameters.put(key, value);
        }
        return this;
    }
    public Object getParameters(String key) {
        return parameters.get(key);
    }

    public Object getParameter(String topKey, String key) {
        if (parameters.containsKey(topKey)) {
            Object topVal = parameters.get(topKey);
            if (topVal instanceof Map) {
                Map<String, Object> maps = (Map<String, Object>)topVal;
                return maps.get(key);
            }
        }
        return null;
    }

    public String getParameter(String parameterId) {
        Object param = getParameter(TOP_KEY_PARAMETER, parameterId);
        if (param instanceof Parameter) {
            return ((Parameter) param).getValue();
        }
        return null;
    }
    public void setParameter(String parameterId, Parameter parameter) {
        Object params = getParameters(TOP_KEY_PARAMETER);
        if (params instanceof Map) {
            ((Map) params).put(parameterId, parameter);
        }
        return;
    }
    public void setParameter(String parameterId, String parameter) {
        Object params = getParameters(TOP_KEY_PARAMETER);
        if (params instanceof Map) {
            Parameter newOne = new Parameter();
            newOne.setId(parameterId);
            newOne.setValue(parameter);
            ((Map) params).put(parameterId, newOne);
        }
        return;
    }
    public String removeParameter(String parameterId) {
        Object params = getParameters(TOP_KEY_PARAMETER);
        if (params instanceof Map) {
            Object removedOne = ((Map) params).remove(parameterId);
            if (removedOne instanceof Parameter) {
                return ((Parameter) removedOne).getValue();
            }
        }
        return null;
    }
    public void clearParameter() {
        Object params = getParameters(TOP_KEY_PARAMETER);
        if (params instanceof Map) {
            ((Map) params).clear();
        }
    }

    public FontDef getFont(String fontName) {
        Object font = getParameter(TOP_KEY_FONT, fontName);
        if (font instanceof FontDef) {
            return (FontDef) font;
        }
        return null;
    }
    public void setFont(String fontName, FontDef fontDef) {
        Object fonts = getParameters(TOP_KEY_FONT);
        if (fonts instanceof Map) {
            ((Map) fonts).put(fontName, fontDef);
        }
        return;
    }
    public FontDef removeFont(String fontName) {
        Object fonts = getParameters(TOP_KEY_FONT);
        if (fonts instanceof Map) {
            return (FontDef) ((Map) fonts).remove(fontName);
        }
        return null;
    }
    public void clearFont() {
        Object fonts = getParameters(TOP_KEY_FONT);
        if (fonts instanceof Map) {
            ((Map) fonts).clear();
        }
    }

    public Map<String, DataTable> getTables() {
        Object table = getParameters(TOP_KEY_TABLE);
        if (table instanceof Map) {
            return (Map) table;
        }
        return null;
    }
    public DataTable getTable(String tableId) {
        Object table = getParameter(TOP_KEY_TABLE, tableId);
        if (table instanceof DataTable) {
            return (DataTable) table;
        }
        return null;
    }
    public void setTable(String tableId, DataTable table) {
        Object tables = getParameters(TOP_KEY_TABLE);
        if (tables instanceof Map) {
            ((Map) tables).put(tableId, table);
        }
        return;
    }
    public DataTable removeTable(String tableId) {
        Object tables = getParameters(TOP_KEY_TABLE);
        if (tables instanceof Map) {
            return (DataTable) ((Map) tables).remove(tableId);
        }
        return null;
    }
    public void clearTable() {
        Object tables = getParameters(TOP_KEY_TABLE);
        if (tables instanceof Map) {
            ((Map) tables).clear();
        }
    }

    public TreeNode getTree(String treeKey) {
        Object tree = getParameter(TOP_KEY_TREE, treeKey);
        if (tree instanceof TreeNode) {
            return (TreeNode) tree;
        }
        return null;
    }
    public void setTree(String treeKey, TreeNode tree) {
        Object trees = getParameters(TOP_KEY_TREE);
        if (trees instanceof Map) {
            ((Map) trees).put(treeKey, tree);
        }
        return;
    }
    public TreeNode removeTree(String treeKey) {
        Object trees = getParameters(TOP_KEY_TREE);
        if (trees instanceof Map) {
            return (TreeNode) ((Map) trees).remove(treeKey);
        }
        return null;
    }
    public void clearTree() {
        Object trees = getParameters(TOP_KEY_TREE);
        if (trees instanceof Map) {
            ((Map) trees).clear();
        }
    }

    public String getImageUrl(String imageUrlId) {
        Object imageUrl = getParameter(TOP_KEY_IMAGE_URL, imageUrlId);
        if (imageUrl instanceof String) {
            return (String) imageUrl;
        }
        return null;
    }
    public void setImageUrl(String imageUrlId, String imageUrl) {
        Object imageUrls = getParameters(TOP_KEY_IMAGE_URL);
        if (imageUrls instanceof Map) {
            ((Map) imageUrls).put(imageUrlId, imageUrl);
        }
        return;
    }
    public String removeImageUrl(String imageUrlId) {
        Object imageUrls = getParameters(TOP_KEY_IMAGE_URL);
        if (imageUrls instanceof Map) {
            return (String) ((Map) imageUrls).remove(imageUrlId);
        }
        return null;
    }
    public void clearImageUrl() {
        Object imageUrls = getParameters(TOP_KEY_IMAGE_URL);
        if (imageUrls instanceof Map) {
            ((Map) imageUrls).clear();
        }
    }

    public AbsolutePositionTemplateDef getTemplate(String templateName) {
        Object template = getParameter(TOP_KEY_TEMPLATE, templateName);
        if (template instanceof AbsolutePositionTemplateDef) {
            return (AbsolutePositionTemplateDef) template;
        }
        return null;
    }
    public void setTemplate(String templateName, AbsolutePositionTemplateDef template) {
        Object templates = getParameters(TOP_KEY_TEMPLATE);
        if (templates instanceof Map) {
            ((Map) templates).put(templateName, template);
        }
        return;
    }
    public String removeTemplate(String templateName) {
        Object templates = getParameters(TOP_KEY_TEMPLATE);
        if (templates instanceof Map) {
            return (String) ((Map) templates).remove(templateName);
        }
        return null;
    }
    public void clearTemplate() {
        Object templates = getParameters(TOP_KEY_TEMPLATE);
        if (templates instanceof Map) {
            ((Map) templates).clear();
        }
    }

    public Group getGroup(String groupId) {
        Object group = getParameter(TOP_KEY_GROUP, groupId);
        if (group instanceof Group) {
            return (Group) group;
        }
        return null;
    }
    public Group removeGroup(String tableId) {
        Object groups = getParameters(TOP_KEY_GROUP);
        if (groups instanceof Map) {
            return (Group) ((Map) groups).remove(tableId);
        }
        return null;
    }
    public void clearGroup() {
        Object groups = getParameters(TOP_KEY_GROUP);
        if (groups instanceof Map) {
            ((Map) groups).clear();
        }
    }

    public Repeat getRepeat(String repeatId) {
        Object repeat = getParameter(TOP_KEY_REPEAT, repeatId);
        if (repeat instanceof Repeat) {
            return (Repeat) repeat;
        }
        return null;
    }
    public void setRepeat(String repeatId, Repeat repeat) {
        Object repeats = getParameters(TOP_KEY_REPEAT);
        if (repeats instanceof Map) {
            ((Map) repeats).put(repeatId, repeat);
        }
        return;
    }
    public Repeat removeRepeat(String repeatId) {
        Object repeats = getParameters(TOP_KEY_REPEAT);
        if (repeats instanceof Map) {
            return (Repeat) ((Map) repeats).remove(repeatId);
        }
        return null;
    }
    public void clearRepeat() {
        Object repeats = getParameters(TOP_KEY_REPEAT);
        if (repeats instanceof Map) {
            ((Map) repeats).clear();
        }
    }
}
