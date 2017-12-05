package com.happy.gene.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhaolisong on 18/05/2017.
 */
public class SetUtil {

    private static final SetUtil setUtil = new SetUtil();

    public static SetUtil newInstance() {
        return new SetUtil();
    }

    public boolean isListEmpty(List list) {
        return (null==list || list.isEmpty());
    }

    public boolean isMapEmpty(Map map) {
        return (null==map || map.isEmpty());
    }

    public boolean isSetEmpty(Set set) {
        return (null==set || set.isEmpty());
    }

    public boolean isIterableEmpty(Iterable iter) {
        if (null==iter) {
            return true;
        }
        int i = 0;
        for (Object tmp : iter) {
            i++;
            break;
        }
        return i<=0;
    }

    public int listSize(List list) {
        if (isListEmpty(list)) {
            return 0;
        }
        return list.size();
    }

    public int mapSize(Map map) {
        if (isMapEmpty(map)) {
            return 0;
        }
        return map.size();
    }

    public int setSize(Set set) {
        if (isSetEmpty(set)) {
            return 0;
        }
        return set.size();
    }

    public int iterableSize(Iterable iter) {
        if (null==iter) {
            return 0;
        }
        int i = 0;
        for (Object tmp : iter) {
            i++;
            break;
        }
        return i;
    }

    public List copy(List src) {
        if (null==src) {
            return src;
        }

        List dest = new ArrayList();
        for (int i = 0; i < src.size(); i ++) {
            dest.add(src.get(i));
        }

        return dest;
    }

    public List getSetByPage(List srcSet, int pageIndex, int sizePerPage, List resultSet) {
        if (null==resultSet) {
            resultSet = new ArrayList();
        }
        if (isListEmpty(srcSet)) {
            return resultSet;
        }

        int endIndex   = (pageIndex*sizePerPage + sizePerPage);
        int startIndex = (pageIndex*sizePerPage)>=0
                ? (pageIndex*sizePerPage)
                : 0;

        for (int i = startIndex; i < srcSet.size(); i++) {
            if (i < endIndex) {
                resultSet.add(srcSet.get(i));
                continue;
            }
            break;
        }

        return resultSet;
    }

    public void removeListNullVal(List list) {
        if (isListEmpty(list)) { return; }

        for (int i=0, count=list.size(); i < count; i ++) {
            Object obj = list.get(i);
            if (null==obj) {
                list.remove(i);
                i --;
                count --;
                continue;
            }
            if ((obj instanceof String) && "".equals(obj)) {
                list.remove(i);
                i --;
                count --;
                continue;
            }
        }
    }
}
