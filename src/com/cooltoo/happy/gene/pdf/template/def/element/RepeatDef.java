package com.cooltoo.happy.gene.pdf.template.def.element;

import com.cooltoo.happy.gene.pdf.template.IDrawable;
import com.cooltoo.happy.gene.pdf.template.IVariable;
import com.cooltoo.happy.gene.pdf.template.IXml;
import com.cooltoo.happy.gene.pdf.template.IZOrder;
import com.cooltoo.happy.gene.pdf.template.data.*;
import com.cooltoo.happy.gene.pdf.template.def.PageDef;
import com.cooltoo.happy.gene.pdf.template.util.DefFactory;
import com.cooltoo.happy.gene.pdf.template.util.ParametersUtil;
import com.cooltoo.happy.gene.pdf.template.util.XmlDataCache;
import com.itextpdf.layout.Document;
import org.dom4j.Element;

import java.util.*;

/**
 * Created by zhaolisong on 28/06/2017.
 */
public class RepeatDef extends AbstractDef implements IXml<RepeatDef>, IVariable {

    private List<AbstractDef> components = new ArrayList<>();
    private String            data       = null;
    private float             lastY      = 0f;

    public List<AbstractDef> getComponents() {
        return components;
    }
    public void setComponents(List<AbstractDef> components) {
        this.components = components;
    }

    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    public float lastY() {
        return lastY;
    }
    public void lastY(float lastY) {
        this.lastY = lastY;
    }

    @Override
    public RepeatDef parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"repeat".equalsIgnoreCase(element.getName())) {
            return null;
        }

        this.data = element.attributeValue("data");
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


    public Object generate(Document pdf, PageDef pageDef) throws Exception {
        if (getComponents().isEmpty()) {
            return null;
        }

        // order component
        List<AbstractDef> components = getComponents();
        Collections.sort(components, IZOrder.comparator);

        // get data
        XmlDataCache    cache           = XmlDataCache.getInstance();
        ParametersUtil  parametersUtil  = getParametersUtil();
        List<String>    paramName       = parametersUtil.getParameterName(data);

        Object preCom = null;
        float startY = lastY();
        for (String pn : paramName) {
            // prepare data
            Repeat repeat = cache.getRepeat(pn);
            if (null==repeat) { continue; }

            List<String> groupIds = repeat.getGroupIds();
            if (null==groupIds || groupIds.isEmpty()) { continue; }

            for (String gi : groupIds) {
                Group g = repeat.getGroup(gi);
                if (null==g) {continue;}

                Map<String, Parameter> gParams = g.getParams();
                Map<String, DataTable> gTables = g.getTables();
                Map<String, TreeNode>  gTrees  = g.getTrees();
                Set<String> keys = null;
                keys = gParams.keySet();
                for (String key : keys) {
                    cache.setParameter(key, gParams.get(key));
                }
                keys = gTables.keySet();
                for (String key : keys) {
                    cache.setTable(key, gTables.get(key));
                }
                keys = gTrees.keySet();
                for (String key : keys) {
                    cache.setTree(key, gTrees.get(key));
                }

                // draw components
                for (int i = 0; i < components.size(); i ++) {
                    AbstractDef com = components.get(i);
                    if (i == 0 && startY != 0 && com instanceof IVariable) {
                        ((IVariable) com).lastY(startY);
                    }
                    if (preCom instanceof IVariable && com instanceof IVariable) {
                        startY = ((IVariable) preCom).lastY();
                        ((IVariable) com).lastY(startY);
                    }
                    ((IDrawable) com).generate(pdf, pageDef);
                    preCom = com;
                }
            }
        }

        return this;
    }

    @Override
    public AbstractDef clone() {
        RepeatDef newOne = new RepeatDef();
        newOne.setZOrder(this.getZOrder());

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
