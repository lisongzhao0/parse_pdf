package com.cooltoo.happy.gene.pdf.template;

import org.dom4j.Element;

import java.util.Map;

/**
 * Created by zhaolisong on 12/04/2017.
 */
public interface IXml<T> {
    T parseElement(Element element);
}
