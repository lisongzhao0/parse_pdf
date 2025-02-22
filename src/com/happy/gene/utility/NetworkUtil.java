package com.happy.gene.utility;

//import com.google.common.io.CharStreams;
//
//import org.apache.axis.client.Service;
//import org.apache.axis.client.Call;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpMethod;
//import org.apache.commons.httpclient.HttpStatus;
//import org.apache.commons.httpclient.methods.GetMethod;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.commons.httpclient.methods.PutMethod;
//import org.apache.commons.httpclient.methods.StringRequestEntity;
//import org.apache.http.HttpEntity;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.methods.*;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.ssl.SSLContexts;
//import org.apache.http.util.EntityUtils;

import javax.net.ssl.*;
//import javax.servlet.ServletInputStream;
//import javax.servlet.http.HttpServletRequest;
//import javax.xml.rpc.ServiceException;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.rmi.RemoteException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhaolisong on 29/06/2017.
 */
public class NetworkUtil {

    public static final long _2M = 2 * 1024 * 1024;

    private StringUtil  stringUtil  = StringUtil.newInstance();
    private SetUtil     setUtil     = SetUtil.newInstance();
    private JsonUtil    jsonUtil    = JsonUtil.newInstance();
    private FileUtil    fileUtil    = FileUtil.newInstance();

    public static NetworkUtil newInstance() {
        return new NetworkUtil();
    }

    private NetworkUtil() {}

//    /**
//     * 读取 Http 的请求内容。
//     * @param request Http servlet 请求
//     * @return Http 的请求内容
//     */
//    public final String readHttpRequestBody(HttpServletRequest request) {
//        if (null==request) {
//            return null;
//        }
//
//        ServletInputStream inputStream;
//        try { inputStream = request.getInputStream(); } catch (IOException io) { inputStream = null; }
//        if (null==inputStream) {
//            System.err.println("get http servlet request's input stream is null");
//            return null;
//        }
//
//        String body;
//        try { body = CharStreams.toString(new InputStreamReader(inputStream)); } catch (IOException io) { body = null; }
//        if (null==body) {
//            System.err.println("get http servlet request's body is null");
//            return null;
//        }
//
//        if (null==body || body.isEmpty()) {
//            body = "";
//        }
//        return body;
//    }


    /**
     * 抓取网络文件落地到本地；如果有一个抓取失败，则全部删掉。
     * @param urls 网络文件路径
     * @param localStoragePath 本地存储路径
     * @return 网络文件路径对保存在本地的文件路径的映射关系
     */
    public final Map<String, String> fetchAllWebFile(List<String> urls, String localStoragePath) {
        if (setUtil.isListEmpty(urls)) {
            return new HashMap<>();
        }
        if (stringUtil.isEmpty(localStoragePath)) {
            throw new RuntimeException("local storage path is empty");
        }
        File directory = new File(localStoragePath);
        if (!(directory.exists() && directory.isDirectory())) {
            throw new RuntimeException("local storage path is not existed or not a directory");
        }


        Map<String, String> result = new HashMap<>();
        byte[] buffer = new byte[1024*1024];
        boolean error = false;
        for (int i=0; i<urls.size(); i++) {
            InputStream is = null;
            OutputStream os = null;
            String tmpUrl = urls.get(i);
            try {
                if (result.containsKey(tmpUrl)) {
                    continue;
                }


                URL url = null;
                try { url = new URL(tmpUrl); }
                catch (IOException ex) {}
                if (null==url) {
                    continue;
                }

                URLConnection conn = url.openConnection();
                long contentLength = conn.getContentLengthLong();

                // big than 2M
                if (contentLength > _2M) {
                    error = true;
                }

                String fileName = fileUtil.getFileName(url.getFile());
                fileName += "_"+i;
                File file = new File(directory, fileName);

                if (!error) {
                    try { is = conn.getInputStream(); }
                    catch (IOException io) { continue; }

                    os = new FileOutputStream(file);
                    int read = 0;
                    while ((read = is.read(buffer, 0, buffer.length)) > 0) {
                        os.write(buffer, 0, read);
                    }
                    os.flush();
                    is.close();
                    os.close();
                    result.put(tmpUrl, file.getAbsolutePath());
                }
            }
            catch (IOException ex) {
                System.err.println("download image failed; image=" + tmpUrl);
                System.err.println("download image failed; exception=" + ex);
                error = true;
                if (null!=is) {
                    try { is.close(); } catch (IOException ioex) {}
                }
                if (null!=os) {
                    try { os.close(); } catch (IOException ioex) {}
                }
            }

            if (error) {
                break;
            }
        }

        // delete files and clear result set
        if (error) {
            System.err.println("download image failed; clear image downloaded=" + result);
            Set<String> keys = result.keySet();
            for (String tmp : keys) {
                String val = result.get(tmp);
                fileUtil.deleteFile(val);
            }
            result.clear();
        }

        return result;
    }



//    /**
//     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
//     */
//    public final String getIpAddress(HttpServletRequest request) throws IOException {
//        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
//
//        String ip = request.getHeader("X-Forwarded-For");
//        System.out.println("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip=" + ip);
//
//        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//                ip = request.getHeader("Proxy-Client-IP");
//                System.out.println("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip=" + ip);
//            }
//            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//                ip = request.getHeader("WL-Proxy-Client-IP");
//                System.out.println("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip=" + ip);
//            }
//            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//                ip = request.getHeader("HTTP_CLIENT_IP");
//                System.out.println("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip=" + ip);
//            }
//            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
//                System.out.println("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip=" + ip);
//            }
//            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
//                ip = request.getRemoteAddr();
//                System.out.println("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip=" + ip);
//            }
//        } else if (ip.length() > 15) {
//            String[] ips = ip.split(",");
//            for (int index = 0; index < ips.length; index++) {
//                String strIp = (String) ips[index];
//                if (!("unknown".equalsIgnoreCase(strIp))) {
//                    ip = strIp;
//                    break;
//                }
//            }
//        }
//        return ip;
//    }



    /**
     * 发送https请求
     * @param requestUrl 请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr 提交的数据
     * @return 返回微信服务器响应的信息
     */
    public final String httpsRequest(String requestUrl, String requestMethod, String outputStr, TrustManager[] tm) {
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            if (null==tm || tm.length==0) {
                tm = new TrustManager[]{ new DefaultX509TrustManager() };
            }
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST/PUT）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            System.err.println("连接超时：" + ce);
        } catch (Exception e) {
            System.err.println("https请求异常：" + e);
        }
        return null;
    }



    // X.509是一种非常通用的证书格式。所有的证书都符合ITU-T X.509国际标准，
    // 因此(理论上)为一种应用创建的证书可以用于任何其他符合X.509标准的应用。
    private static class DefaultX509TrustManager implements X509TrustManager {
        @Override public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException { }
        @Override public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException { }
        @Override public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
    }




//    /**
//     * 发送https请求
//     * @param requestUrl 请求地址
//     * @param requestMethod 请求方式（GET、POST）
//     * @param outputStr 提交的数据
//     * @return 返回微信服务器响应的信息
//     */
//    public final String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
//        String resStr = null;
//        HttpClient htpClient = new HttpClient();
//        HttpMethod htpMethod = null;
//        try{
//            if ("PUT".equalsIgnoreCase(requestMethod)) {
//                htpMethod = new PutMethod(requestUrl);
//                ((PutMethod)htpMethod).setRequestEntity(new StringRequestEntity(outputStr, "application/x-www-form-urlencoded", "UTF-8"));
//            }
//            else if ("GET".equalsIgnoreCase(requestMethod)) {
//                htpMethod = new GetMethod(requestUrl);
//                ((GetMethod)htpMethod).setQueryString(outputStr);
//            }
//            else if ("POST".equalsIgnoreCase(requestMethod)) {
//                htpMethod = new PostMethod(requestUrl);
//                ((PostMethod)htpMethod).setRequestEntity(new StringRequestEntity(outputStr, "application/x-www-form-urlencoded", "UTF-8"));
//            }
//            htpMethod.addRequestHeader("content-type", "application/x-www-form-urlencoded");
//
//            int statusCode = htpClient.executeMethod(htpMethod);
//            System.out.println("request status="+statusCode);
//            if(statusCode != HttpStatus.SC_OK){
//                System.err.println("Method failed: "+htpMethod.getStatusLine());
//                return null;
//            }
//            byte[] responseBody = htpMethod.getResponseBody();
//            resStr = new String(responseBody, Charset.forName("UTF-8"));
//        } catch (UnsupportedEncodingException uee) {
//            System.err.println("参数编码异常："+ uee);
//        } catch(Exception e) {
//            System.err.println("https请求异常："+ e);
//        } finally {
//            htpMethod.releaseConnection();
//        }
//        return resStr;
//    }
//
//
//
//
//    /**
//     * 发送https请求(https 双向认证)
//     * @param requestUrl 请求地址
//     * @param requestMethod 请求方式（GET、POST）
//     * @param outputStr 提交的数据
//     * @return 返回微信服务器响应的信息
//     */
//    public final String httpsRequest(String requestUrl, String requestMethod, Map<String, String> parameters, String outputStr, String password, InputStream certificationFileInputStream) {
//        StringBuilder resStr = new StringBuilder();
//        try{
//            //================================================================
//            KeyStore keyStore  = KeyStore.getInstance("PKCS12");
//            try {
//                keyStore.load(certificationFileInputStream, password.toCharArray());
//            } finally {
//                certificationFileInputStream.close();
//            }
//
//            // Trust own CA and all self-signed certs
//            SSLContext sslcontext = SSLContexts.custom()
//                    .loadKeyMaterial(keyStore, password.toCharArray())
//                    .build();
//            // Allow TLSv1 protocol only
//            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
//                    sslcontext,
//                    new String[] { "TLSv1" },
//                    null,
//                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
//            CloseableHttpClient httpclient = HttpClients.custom()
//                    .setSSLContext(sslcontext)
//                    .setSSLSocketFactory(sslsf)
//                    .build();
//            try {
//
//                HttpUriRequest uriRequest = null;
//                if ("PUT".equalsIgnoreCase(requestMethod)) {
//                    uriRequest = new HttpPut(requestUrl);
//                }
//                else if ("GET".equalsIgnoreCase(requestMethod)) {
//                    uriRequest = new HttpGet(requestUrl);
//                }
//                else if ("POST".equalsIgnoreCase(requestMethod)) {
//                    uriRequest = new HttpPost(requestUrl);
//                    HttpEntity httpEntity = new StringEntity(outputStr, "UTF-8");
//                    RequestConfig requestConfig = RequestConfig.custom()
//                            .setSocketTimeout(1000*60)
//                            .setConnectTimeout(1000*60)
//                            .build();
//                    ((HttpPost)uriRequest).setConfig(requestConfig);
//                    ((HttpPost)uriRequest).setEntity(httpEntity);
//                    if (null!=parameters) {
//                        Set<String> keys = parameters.keySet();
//                        for (String key : keys) {
//                            uriRequest.addHeader(key, parameters.get(key).toString());
//                        }
//                    }
//                }
//
//                System.out.println("executing request" + uriRequest.getRequestLine());
//
//                CloseableHttpResponse response = httpclient.execute(uriRequest);
//                try {
//                    HttpEntity entity = response.getEntity();
//
//                    System.out.println("----------------------------------------");
//                    System.out.println(response.getStatusLine());
//                    if (entity != null) {
//                        System.out.println("Response content length: " + entity.getContentLength());
//                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity.getContent()));
//                        String text;
//                        resStr = new StringBuilder();
//                        while ((text = bufferedReader.readLine()) != null) {
//                            System.out.println(text);
//                            resStr.append(text).append("\n");
//                        }
//
//                    }
//                    EntityUtils.consume(entity);
//                } finally {
//                    response.close();
//                }
//            } finally {
//                httpclient.close();
//            }
//        } catch (UnsupportedEncodingException uee) {
//            System.err.println("参数编码异常：" + uee.getMessage());
//            uee.printStackTrace();
//        } catch(Exception e) {
//            System.err.println("https请求异常：" + e.getMessage());
//            e.printStackTrace();
//        } finally {
//        }
//        return resStr.toString();
//    }
//
//    public Object webServiceInvoke(String url, String method, Object[] param) throws RemoteException, ServiceException {
//        Service s = new  Service();
//        Call call = (Call)s.createCall();
//        call.setTimeout(new Integer(5000));
//        call.setTargetEndpointAddress(url);
//        call.setOperation(method);
//
//        Object val = call.invoke((Object[])param);
//
//        System.out .println(method + " : "  + val);
//
//        return val;
//    }
}
