package com.happy.gene.utility;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by zhaolisong on 18/05/2017.
 */
public class StringUtil {

    public static StringUtil newInstance() {
        return new StringUtil();
    }

    private static final String seed = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final char[] charSeed = seed.toCharArray();

    public boolean isEmpty(String str) {
        return (null==str || "".equals(str));
    }

    public boolean isChanged(String oldV, String newV) {
        if (null==newV && null==oldV) { return false; }
        if (null==newV && null!=oldV) { return true;  }
        if (null!=newV && null==oldV) { return true;  }
        return !oldV.equals(newV);
    }

    public boolean like(String src, String like) {
        if (null==src && null==like) { return true;  }
        if (null==src || null==like) { return false; }
        int srcI  = 0;
        int likeI = 0;

        for (; likeI < like.length() && srcI < src.length(); likeI ++) {
            char charLike = like.charAt(likeI);
            for (; srcI < src.length(); srcI ++) {
                char charSrc = src.charAt(srcI);
                if (charLike==charSrc) {
                    break;
                }
            }

            if (likeI + 1 ==like.length()) {break;}
        }

        return (likeI+1 >= like.length()) && (srcI+1 <= src.length());
    }

    public String randomIdentity(int size) {
        StringBuilder uniqueId = new StringBuilder();
        int temp = -1;

        //采用一个简单的算法以保证生成随机数的不同
        Random rand = new Random(System.currentTimeMillis()+System.nanoTime());
        for (int i = 1; i < size + 1; i++) {
            int t = rand.nextInt(charSeed.length);
            uniqueId.append(charSeed[t]);
        }
        return uniqueId.toString();
    }

    public String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest sha1Digest = MessageDigest.getInstance("SHA1");
        byte[] result = sha1Digest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    /**
     * @Description：将请求参数转换为xml格式的string
     * @param parameters  请求参数
     * @return
     */
    public String getRequestXml(Map<String,String> parameters){
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            String v = null==entry.getValue() ? null : entry.getValue().toString();
            if (null!=v) {
                if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
                    sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
                } else {
                    sb.append("<" + k + ">" + v + "</" + k + ">");
                }
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * @Description：将响应的返回的xml格式转换为Map
     * @param xml  响应的返回的xml
     * @return
     */
    public static Map<String, String> parseResponseXml(String xml){
        final Map<String, String> map = new HashMap<>();
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        try {
            SAXParser saxParser = saxFactory.newSAXParser();
            saxParser.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")), new DefaultHandler(){
                private String tag = null;
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    tag = qName;
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    if (null!=tag && !map.containsKey(tag)) {
                        map.put(tag, new String(ch, start, length));
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    tag = null;
                }
            });
        }
        catch (Exception ex) {
            map.put("data", xml);
            map.put("exception", ex.toString());
        }
        return map;
    }

    /**
     * @Description：将响应的返回的json格式转换为Map
     * @param json  响应的返回的json
     * @return
     */
    public static Map<String, Object> parseResponseJson(String json){
        JsonUtil jsonUtil = JsonUtil.newInstance();
        final Map<String, Object> map = jsonUtil.parseJsonMap(json, String.class, Object.class);
        return map;
    }
}
