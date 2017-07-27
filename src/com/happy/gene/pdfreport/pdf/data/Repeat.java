package com.happy.gene.pdfreport.pdf.data;

import com.happy.gene.pdfreport.pdf.IXml;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaolisong on 28/06/2017.
 */
public class Repeat implements IXml<Repeat> {

    private String id;
    private String group;
    private Map<String, Group> groupIdToData = new HashMap<>();
    private List<String> groupIds = new ArrayList<>();

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }

    public Group getGroup(String groupId) {
        return null==groupIdToData ? null : groupIdToData.get(groupId);
    }
    public List<String> getGroupIds() {
        return groupIds;
    }

    @Override
    public Repeat parseElement(Element element) {
        if (null==element) {
            return null;
        }
        if (!"repeat".equalsIgnoreCase(element.getName())) {
            return null;
        }
        Repeat def = new Repeat();
        def.id    = element.attributeValue("id");
        def.group = element.attributeValue("group");

        Group group = new Group();
        List<Element> params = element.elements();
        for (Element tmp : params) {
            if ("group".equalsIgnoreCase(tmp.getName())) {
                Group newOne = group.parseElement(tmp);
                if (null!=newOne.getId() && !"".equalsIgnoreCase(newOne.getId())) {
                    def.groupIdToData.put(newOne.getId(), newOne);
                    def.groupIds.add(newOne.getId());
                }
            }
        }
        return def;

    }
}
