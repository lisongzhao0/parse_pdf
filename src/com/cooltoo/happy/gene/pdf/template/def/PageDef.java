package com.cooltoo.happy.gene.pdf.template.def;

import com.cooltoo.happy.gene.pdf.template.*;
import com.cooltoo.happy.gene.pdf.template.def.element.*;
import com.cooltoo.happy.gene.pdf.template.util.DefFactory;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.layout.Document;
import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.*;

/**
 * Created by zhaolisong on 12/04/2017.
 */
public class PageDef extends AbstractDef implements IXml<PageDef> {

    private Map<String, String> properties = new HashMap<>();

    private boolean stopRenderer    = false;
    private boolean stopGenerate    = false;
    private int pageStartNumber     = 0;
    private int pageEndNumber       = 0;
    private int pageStartNumberInDoc= 0;
    private int pageEndNumberInDoc  = 0;
    private List<Object>     components      = new ArrayList<>();
    private IPageDefRenderer pageDefRenderer = null;
    private List<Integer>    pageHasRendered = new ArrayList<>();

    public String setProperty(String key, String val) {
        if (null==key) {
            return null;
        }
        properties.put(key, val);
        return val;
    }
    public String getProperty(String key) {
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

    public void setStopRenderer(boolean stopRenderer) {
        this.stopRenderer = stopRenderer;
    }
    public void setStopGenerate(boolean stopGenerate) {
        this.stopGenerate = stopGenerate;
    }
    public void setPageDefRenderer(IPageDefRenderer pageDefRenderer) {
        this.pageDefRenderer = pageDefRenderer;
    }

    public boolean isMultiplePage() {
        return (pageEndNumber-pageStartNumber)>0;
    }
    public int  getPageStartNumberInDoc() {
        return pageStartNumberInDoc;
    }
    public int  getPageStartNumber() {
        return pageStartNumber;
    }
    public int  getPageEndNumber() {
        return pageEndNumber;
    }
    public void setPageStartNumber(int pageStartNumber) {
        this.pageStartNumber = pageStartNumber;
    }
    public void setPageEndNumber(int pageEndNumber) {
        this.pageEndNumber = pageEndNumber;
    }

    public List<Object> getComponents() {
        return components;
    }

    public PageDef parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"page".equalsIgnoreCase(element.getName())) {
            return null;
        }

        List<Attribute> attributes = element.attributes();
        for (Attribute tmp : attributes) {
            if (null!=tmp) {
                properties.put(tmp.getName(), tmp.getValue());
            }
        }

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

    public PdfPage generate(Document pdf, PageDef pageDef) throws Exception {
        pageDef = this;

        PdfPage page = pdf.getPdfDocument().addNewPage();
        pageStartNumberInDoc = pdf.getPdfDocument().getPageNumber(page);
        if (null!=pageDefRenderer) {
            pageDefRenderer.beforeRenderer(pdf, pageDef);
        }
        if (null!=pageDefRenderer && !stopRenderer) {
            rendererPage(pdf, pageDef);
        }

        List<Object> components = getComponents();

        Collections.sort(components, IZOrder.comparator);

        Object preCom = null;
        for (Object com : components) {
            if (stopGenerate) {
                continue;
            }
            if (com instanceof IDrawable) {
                if (preCom instanceof IVariable && com instanceof IVariable) {
                    float lastY = ((IVariable) preCom).lastY();
                    ((IVariable) com).lastY(lastY);
                }
                ((IDrawable) com).generate(pdf, pageDef);
                preCom = com;
            }
        }
        page = pdf.getPdfDocument().getLastPage();
        pageEndNumberInDoc = pdf.getPdfDocument().getPageNumber(page);
        pageEndNumber = (pageEndNumberInDoc - pageStartNumberInDoc) + pageStartNumber;

        if (null!=pageDefRenderer) {
            pageDefRenderer.beforeRenderer(pdf, pageDef);
        }
        return page;
    }

    public void rendererPage(Document pdf, PageDef pageDef) throws Exception {
        PdfPage page = pdf.getPdfDocument().getLastPage();
        int pageNumber = pdf.getPdfDocument().getPageNumber(page);
        if (null!=pageDef.pageDefRenderer && !this.pageHasRendered.contains(pageNumber)) {
            pageDef.pageDefRenderer.rendererHeader(pdf, pageDef);
            pageDef.pageDefRenderer.rendererTail(pdf, pageDef);
            pageDef.pageDefRenderer.rendererCatalog(pdf, pageDef);
            pageDef.pageHasRendered.add(pageNumber);
        }
    }

    public PageDef clone() {
        PageDef newOne = new PageDef();

        newOne.setZOrder(this.getZOrder());
        newOne.stopGenerate        = false;
        newOne.pageStartNumber     = 0;
        newOne.pageEndNumber       = 0;
        newOne.pageStartNumberInDoc= 0;
        newOne.pageEndNumberInDoc  = 0;
        newOne.pageDefRenderer     = this.pageDefRenderer;

        Set<String> keys = this.properties.keySet();
        for (String key : keys) {
            newOne.properties.put(key+"", this.properties.get(key)+"");
        }

        for (Object obj : this.components) {
            if (obj instanceof AbstractDef) {
                AbstractDef clone = ((AbstractDef) obj).clone();
                if (null!=clone) {
                    newOne.components.add(clone);
                }
            }
        }
        return newOne;
    }
}
