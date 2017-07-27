package com.happy.gene.pdfreport.pdf;

import org.dom4j.Element;

/**
 * Created by zhaolisong on 12/04/2017.
 */
public interface IXml<T> {
    T parseElement(Element element);
}
