package com.cooltoo.happy.gene.pdf.template.def.element.catalog;

import com.cooltoo.happy.gene.pdf.template.IDrawable;
import com.cooltoo.happy.gene.pdf.template.IXml;
import com.cooltoo.happy.gene.pdf.template.def.PageDef;
import com.cooltoo.happy.gene.pdf.template.def.element.AbstractDef;
import com.cooltoo.happy.gene.pdf.template.data.TreeNode;
import com.cooltoo.happy.gene.pdf.template.util.XmlDataCache;
import com.itextpdf.layout.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaolisong on 20/04/2017.
 */
public class CatalogRepeatDef extends AbstractDef implements IXml<CatalogRepeatDef> {

    private float   minYUnit;
    private boolean lastNeedTail;
    private CatalogMainHeadingDef mainHeading;
    private CatalogSubHeadingDef  subHeading;
    private List<CatalogSubHeadingTailDef> subHeadingTail = new ArrayList<>();
    private TreeNode catalog;

    public TreeNode getCatalog() {
        return catalog;
    }
    public void setCatalog(TreeNode catalog) {
        this.catalog = catalog;
    }

    public CatalogMainHeadingDef getMainHeading() {
        return mainHeading;
    }
    public CatalogSubHeadingDef  getSubHeading() {
        return subHeading;
    }
    public List<CatalogSubHeadingTailDef> getSubHeadingTail() {
        return subHeadingTail;
    }
    public float   getMinYUnit() {
        return minYUnit;
    }
    public boolean isLastNeedTail() {
        return lastNeedTail;
    }

    public CatalogRepeatDef parseElement(Element element) {
        if (null == element) {
            return null;
        }
        if (!"repeat".equalsIgnoreCase(element.getName())) {
            return null;
        }

        try { this.minYUnit = Integer.parseInt(element.attributeValue("min_unit_y"));            } catch (Exception ex) { this.minYUnit = 10f; }
        try { this.lastNeedTail = Boolean.parseBoolean(element.attributeValue("last_need_tail"));} catch (Exception ex) { this.lastNeedTail = false; }

        // TODO -- parse other component
        List<Element> subEle = element.elements();
        for (Element ele : subEle) {
            CatalogMainHeadingDef newHeading = (new CatalogMainHeadingDef()).parseElement(ele);
            if (null != newHeading) {
                this.mainHeading = newHeading;
            }
            CatalogSubHeadingDef newSubHeading = (new CatalogSubHeadingDef()).parseElement(ele);
            if (null != newSubHeading) {
                this.subHeading = newSubHeading;
            }
            CatalogSubHeadingTailDef newSubHeadingTail = (new CatalogSubHeadingTailDef()).parseElement(ele);
            if (null != newSubHeadingTail) {
                this.subHeadingTail.add(newSubHeadingTail);
            }
        }
        return this;
    }

    public Object generate(Document pdf, PageDef pageDef) throws Exception {
        XmlDataCache xmlDataCache = XmlDataCache.getInstance();
        TreeNode root = getCatalog();
        if (!(root instanceof TreeNode)) {
            return null;
        }

        List<String> paramKeys = root.getPropertiesKey();
        List<TreeNode> mainHeaders = root.getChilds();
        List<TreeNode> subHeaders  = null;

        CatalogMainHeadingDef          mainHeading     = getMainHeading();
        CatalogSubHeadingDef           subHeading      = getSubHeading();
        List<CatalogSubHeadingTailDef> subHeadingTails = getSubHeadingTail();

        float mainDeltaY = 0.0f;
        for (int i = 0; (null!=mainHeaders && i < mainHeaders.size()); i ++) {
            TreeNode main = mainHeaders.get(i);
            setParameters(xmlDataCache, main, paramKeys);
            mainHeading.generate(pdf, pageDef);

            subHeaders = main.getChilds();
            float subDeltaY = 0.0f;
            for (int j = 0; (null!=subHeaders && j < subHeaders.size()); j ++) {
                TreeNode sub = subHeaders.get(j);
                setParameters(xmlDataCache, sub, paramKeys);

                subHeading.generate(pdf, pageDef);

                float tmpDeltaY = getMinYUnit()*subHeading.getUnitYSize();
                subHeading.translate(0, tmpDeltaY);
                mainDeltaY += tmpDeltaY;
                subDeltaY  += tmpDeltaY;
            }

            boolean isLastHeader = (i+1>=mainHeaders.size());
            float subTailDeltaY = 0.0f;
            for (int j = 0; (null != subHeadingTails && j < subHeadingTails.size()); j++) {
                if (!isLastNeedTail() && isLastHeader) {
                    continue;
                }
                CatalogSubHeadingTailDef tail = subHeadingTails.get(j);
                tail.translate(0, subDeltaY);
                tail.generate(pdf, pageDef);

                float tmpDeltaY = getMinYUnit() * tail.getUnitYSize();
                mainDeltaY += tmpDeltaY;
                subTailDeltaY += tmpDeltaY;
            }

            subHeading.translate(0, subTailDeltaY);
            for (int j = 0; (null!=subHeadingTails && j < subHeadingTails.size()); j ++) {
                CatalogSubHeadingTailDef tail = subHeadingTails.get(j);
                tail.translate(0, subTailDeltaY);
            }


            mainHeading.translate(0, mainDeltaY);
            mainDeltaY = 0.0f;
        }

        removeParameters(xmlDataCache, paramKeys);
        return null;
    }

    private void setParameters(XmlDataCache xmlDataCache, TreeNode node, List<String> keys) {
        if (null==keys || null==node || null==xmlDataCache) {
            return;
        }

        for (String key : keys) {
            xmlDataCache.setParameter(key, node.getProperty(key));
        }
    }

    private void removeParameters(XmlDataCache xmlDataCache, List<String> keys) {
        if (null==keys || null==xmlDataCache) {
            return;
        }

        for (String key : keys) {
            xmlDataCache.removeParameter(key);
        }
    }

    public CatalogRepeatDef clone() {
        return null;
    }
}
