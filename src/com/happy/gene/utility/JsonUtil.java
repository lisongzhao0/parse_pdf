package com.happy.gene.utility;

//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhaolisong on 2016/11/7.
 */
public class JsonUtil {

    public static JsonUtil newInstance() {
        return new JsonUtil();
    }

    private JsonUtil(){}

    public <T> T parseJsonMap(String content, Class keyType, Class valueType) {
        try {
            T returnValue = null;
//            ObjectMapper mapper = new ObjectMapper();
//            TypeFactory typeFactory = mapper.getTypeFactory();
//            returnValue =  mapper.readValue(content, typeFactory.constructMapType(HashMap.class, keyType, valueType));
//            if (null==returnValue) {
//                return (T) new HashMap<T, T>();
//            }
            return returnValue;
        }
        catch (Exception ex) {
            System.err.println("parse the json map error, json="+content+
                    ", keyClass="+keyType+
                    " valueClass="+valueType+
                    " throwable=" + ex.getMessage());
            return (T) new HashMap<T, T>();
        }
    }

    public <T> T parseJsonList(String content, Class clazz) {
        try {
            T returnValue = null;
//            ObjectMapper mapper = new ObjectMapper();
//            TypeFactory typeFactory = mapper.getTypeFactory();
//            returnValue =  mapper.readValue(content, typeFactory.constructCollectionType(List.class, clazz));
//            if (null==returnValue) {
//                return (T) new ArrayList<T>();
//            }
            return returnValue;
        }
        catch (Exception ex) {
            System.err.println("parse the json list error, json="+content+", class="+clazz+" throwable="+ex.getMessage());
            return (T) new ArrayList<T>();
        }
    }

    public <T> T parseJsonBean(String content, Class<T> clazz) {
        try {
            T value = null;
//            ObjectMapper mapper = new ObjectMapper();
//            value =  mapper.readValue(content, clazz);
            return value;
        }
        catch (Exception ex) {
            System.err.println("parse the json bean error, json="+content+", class="+clazz+" throwable="+ex.getMessage());
            return null;
        }
    }

    public String toJsonString(Object obj) {
        try {
            String value = null;
//            ObjectMapper mapper = new ObjectMapper();
//            value = mapper.writeValueAsString(obj);
            return value;
        }
        catch (Exception ex) {
            return null;
        }
    }
}
