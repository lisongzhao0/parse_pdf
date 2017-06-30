package com.cooltoo.happy.gene.pdf.template;

import java.util.Comparator;

/**
 * Created by zhaolisong on 12/04/2017.
 */
public interface IZOrder {

    int getZOrder();
    void setZOrder(int zOrder);

    public static final Comparator<Object> comparator = new Comparator<Object>() {
        public int compare(Object o1, Object o2) {
            int delta = 0;
            if (o1 instanceof IZOrder && o2 instanceof IZOrder) {
                delta = ((IZOrder) o1).getZOrder() - ((IZOrder) o2).getZOrder();
            }
            return delta;
        }
    };
}
