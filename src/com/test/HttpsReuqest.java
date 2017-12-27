package com.test;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpsReuqest {
    public static void main(String[] args) throws Exception {
        String s=httpsRequest("https://www.happy-gene.com:12500/gene/thirdparty/happygene/healthlink/message/dispatch","PUT","{\"chekmentList\": [{\"age\": \"30\",\"code\": \"JZ001\",\"createTime\": 1513755059207,\"customerId\": \"402881b257b381a60157b7de033e0941\",\"dateCollected\": \"20161015\",\"id\": \"2c90545e6072a8c3016072d524070005\",\"sampleType\": \"1010601\",\"setmealName\": \"TM3\",\"sex\": \"男\",\"sourceId\": \"2c90545e6072a8c3016072d524060004\",\"status\": \"0\"}],\"company\": \"远盟康健科技有限公司\",\"consignor\": \"管理员\",\"createTime\": 1513755059205,\"dateSent\": \"20171220\",\"eventsNo\": \"HL161012154000016\",\"messageUUID\": \"2c90545e6072a8c3016072d524060004\",\"issueNo\": \"肿瘤基础0新增\",\"numOfsamples\": \"1\"}");
        System.out.println(s);

    }

    public static class MyX509TrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            // TODO Auto-generated method stub

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            // TODO Auto-generated method stub

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }

    }
    /**
     * 处理https GET/POST请求
     * 请求地址、请求方法、参数
     * */
    public static String httpsRequest(String requestUrl,String requestMethod,String outputStr){
        StringBuffer buffer=null;
        try{
            //创建SSLContext
            SSLContext sslContext=SSLContext.getInstance("SSL");
            TrustManager[] tm={new MyX509TrustManager()};
            //初始化
            sslContext.init(null, tm, new java.security.SecureRandom());;
            //获取SSLSocketFactory对象
            SSLSocketFactory ssf=sslContext.getSocketFactory();
            URL url=new URL(requestUrl);
            HttpsURLConnection conn=(HttpsURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(requestMethod);
            //设置当前实例使用的SSLSoctetFactory
            conn.setSSLSocketFactory(ssf);
            conn.connect();
            //往服务器端写内容
            if(null!=outputStr){
                OutputStream os=conn.getOutputStream();
                os.write(outputStr.getBytes("utf-8"));
                os.close();
            }

            //读取服务器端返回的内容
            InputStream is=conn.getInputStream();
            InputStreamReader isr=new InputStreamReader(is,"utf-8");
            BufferedReader br=new BufferedReader(isr);
            buffer=new StringBuffer();
            String line=null;
            while((line=br.readLine())!=null){
                buffer.append(line);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
