package com.happy.gene.utility;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

/**
 * Created by zhaolisong on 18/05/2017.
 */
public class TokenUtil {
    public  static final String DEFAULT_ENCODING = "UTF-8";
    public  static final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final HashMap<Character, Object> charSet = new HashMap<>();

    public static TokenUtil newInstance() {
        TokenUtil instance  = new TokenUtil();

        // init char set
        for (int i = 0, count = chars.length(); i < count; i ++) {
            char c = chars.charAt(i);
            Character oc = c;
            instance.charSet.put(oc, c);
        }
        return instance;
    }

    private TokenUtil(){}

    public String constructToken(String token, String suffix) {
        if (null!=token && null!=suffix && token.trim().length()>0) {
            return token+"_"+suffix;
        }
        return "";
    }

    public String getToken(String token) {
        int indexOfSplitter = null==token ? -1 : token.lastIndexOf('_');
        if (0>=indexOfSplitter) {
            return null;
        }

        token = token.substring(0, indexOfSplitter);
        // check token
        for (int i = 0, count = token.length(); i < count; i ++) {
            char c = token.charAt(i);
            if (!charSet.containsKey(c)) {
                token = null;
                break;
            }
        }

        return token;
    }

    public String getSuffix(String token) {
        int indexOfSplitter = null==token ? -1 : token.lastIndexOf('_');
        if (0>=indexOfSplitter || indexOfSplitter+1>=token.length()) {
            return null;
        }
        String suffix = token.substring(indexOfSplitter+1);
        // check suffix
        for (int i = 0, count = suffix.length(); i < count; i ++) {
            char c = suffix.charAt(i);
            if (!charSet.containsKey(c)) {
                suffix = null;
                break;
            }
        }
        return suffix;
    }

    public String generateToken(String id, String saltedPassword) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.write(id.getBytes(DEFAULT_ENCODING));
            dos.write(saltedPassword.getBytes(DEFAULT_ENCODING));
            dos.write((System.currentTimeMillis()+"").getBytes());
            dos.write((System.nanoTime()+"").getBytes());
            dos.flush();
            return DigestUtils.md2Hex(baos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static String createNoncestr(int length) {

        String res = "";
        for (int i = 0; i < length; i++) {
            Random rd = new Random();
            int index = rd.nextInt(chars.length()-1);
            char ch = chars.charAt(index);
            res += ch;
        }
        return res;
    }

    private final static String[] hexDigits = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
    /**
     * 转换字节数组为16进制字串
     * @param b 字节数组
     * @return 16进制字串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte aB : b) {
            resultSb.append(byteToHexString(aB));
        }
        return resultSb.toString();
    }

    /**
     * 转换byte到16进制
     * @param b 要转换的byte
     * @return 16进制格式
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * MD5编码
     * @param origin 原始字符串
     * @return 经过MD5加密之后的结果
     */
    public static String md5Encode(String origin, String charset, String algorithm) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance(algorithm);
            if (null==charset || "".equals(charset)) {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            }
            else {
                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charset)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }
}
