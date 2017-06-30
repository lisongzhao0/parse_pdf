package com.cooltoo.happy.gene.pdf.template.def;

import com.cooltoo.happy.gene.pdf.template.*;
import com.cooltoo.happy.gene.pdf.template.def.element.AbstractDef;
import com.cooltoo.happy.gene.pdf.template.def.element.ParagraphDef;
import com.cooltoo.happy.gene.pdf.template.def.element.TextDef;
import com.cooltoo.happy.gene.pdf.template.def.element.TextGroupDef;
import com.cooltoo.happy.gene.pdf.template.util.DefFactory;
import com.cooltoo.happy.gene.pdf.template.util.ParametersUtil;
import org.dom4j.Element;

import java.util.*;

/**
 * Created by zhaolisong on 08/05/2017.
 */
public class AbsolutePositionTemplateDef extends AbstractDef implements IXml<AbsolutePositionTemplateDef>{

    public static final String DEFAULT_CONDITION = "default_condition";

    private String name;
    private Map<String, ConditionDef> conditionToComponents = new HashMap<>();

    public String getName() {
        return name;
    }
    public List<AbstractDef> getComponents(String condition) {
        ConditionDef conditionDef = conditionToComponents.get(condition);
        if (null==conditionDef) {
            return new ArrayList<>();
        }
        return conditionDef.getComponents();
    }

    public AbsolutePositionTemplateDef parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"template".equalsIgnoreCase(element.getName())) {
            return null;
        }

        this.name  = element.attributeValue("name");
        try { this.setZOrder(Integer.parseInt(element.attributeValue("z_order"))); } catch (Exception ex) {this.setZOrder(0);};

        ConditionDef defaultCondition = new ConditionDef();
        DefFactory defFactory = DefFactory.getIntance();

        List<Element> subEle = element.elements();
        for (Element ele : subEle) {
            AbstractDef subComp = defFactory.getDef(ele);
            if (null!=subComp) {
                defaultCondition.addComponent(subComp);
            }

            if ("condition".equalsIgnoreCase(ele.getName())) {
                ConditionDef condition = (new ConditionDef()).parseElement(ele);
                if (null!=condition) {
                    conditionToComponents.put(condition.getIndex(), condition);
                }
            }
        }
        if (!defaultCondition.getComponents().isEmpty()) {
            conditionToComponents.put(DEFAULT_CONDITION, defaultCondition);
        }


        return this;
    }

    public AbsolutePositionTemplateDef clone() {
        AbsolutePositionTemplateDef newOne = new AbsolutePositionTemplateDef();
        newOne.setZOrder(this.getZOrder());
        newOne.name = this.name;
        Set<String> keys = this.conditionToComponents.keySet();
        for (String key : keys) {
            ConditionDef value  = this.conditionToComponents.get(key);
            newOne.conditionToComponents.put(key, value.clone());
        }
        return newOne;
    }

    public void replaceValue(Map<String, String> replaceValues) {
        Collection<ConditionDef> conditions = conditionToComponents.values();
        if (null!=conditions && conditions.size()>0) {
            for (ConditionDef tmp : conditions) {
                tmp.replaceValue(replaceValues);
            }
        }
    }

    private static class ConditionDef extends AbstractDef implements IXml<ConditionDef>{

        private String index;
        private List<AbstractDef> components = new ArrayList<>();

        public String getIndex() {
            return index;
        }
        public List<AbstractDef> getComponents() {
            return components;
        }
        protected void addComponent(AbstractDef component) {
            if (null!=component) {
                components.add(component);
                Collections.sort(components, IZOrder.comparator);
            }
        }

        public ConditionDef parseElement(Element element) {
            if (null==element) {
                return null;
            }
            if (!"condition".equalsIgnoreCase(element.getName())) {
                return null;
            }

            try { this.index  = element.attributeValue("index");                       } catch (Exception ex) {this.index = Integer.MIN_VALUE+""; }
            try { this.setZOrder(Integer.parseInt(element.attributeValue("z_order"))); } catch (Exception ex) {this.setZOrder(Integer.MIN_VALUE);}


            DefFactory defFactory = DefFactory.getIntance();
            List<Element> subEle = element.elements();
            for (Element ele : subEle) {
                AbstractDef subComp = defFactory.getDef(ele);
                if (null!=subComp) {
                    this.components.add(subComp);
                }
            }
            Collections.sort(components, IZOrder.comparator);

            return this;
        }

        public ConditionDef clone() {
            ConditionDef newOne = new ConditionDef();
            newOne.setZOrder(this.getZOrder());
            newOne.index = this.index;
            for (AbstractDef key : this.components) {
                newOne.components.add(key.clone());
            }
            return newOne;
        }

        private void replaceValue(Map<String, String> replaceValues) {
            List<AbstractDef> components = getComponents();
            if (null==components || components.isEmpty()) {
                return;
            }

            ParametersUtil parametersUtil = getParametersUtil();
            for (AbstractDef tmp : components) {
                if (tmp instanceof TextDef) {
                    String oldVal = ((TextDef) tmp).getValue();
                    String newVal = parametersUtil.replaceParameter(oldVal, replaceValues);
                    if (null!=oldVal && !oldVal.equals(newVal)) {
                        ((TextDef) tmp).setValue(newVal);
                    }
                }
                if (tmp instanceof ParagraphDef) {
                    String oldVal = ((ParagraphDef) tmp).getValue();
                    String newVal = parametersUtil.replaceParameter(oldVal, replaceValues);
                    if (null!=oldVal && !oldVal.equals(newVal)) {
                        ((ParagraphDef) tmp).setValue(newVal);
                    }
                }
                if (tmp instanceof TextGroupDef) {
                    List<TextDef> textComponents = ((TextGroupDef) tmp).getComponents();
                    if (null==textComponents || textComponents.isEmpty()) {
                        continue;
                    }
                    for (TextDef groupCom : textComponents) {
                        String oldVal = groupCom.getValue();
                        String newVal = parametersUtil.replaceParameter(oldVal, replaceValues);
                        if (null!=oldVal && !oldVal.equals(newVal)) {
                            groupCom.setValue(newVal);
                        }
                    }
                }
            }
        }
    }

}
