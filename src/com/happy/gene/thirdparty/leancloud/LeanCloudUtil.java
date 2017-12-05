package com.happy.gene.thirdparty.leancloud;

import com.happy.gene.utility.JsonUtil;
import com.happy.gene.utility.SetUtil;
import com.happy.gene.utility.StringUtil;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhaolisong on 29/06/2017.
 */
public class LeanCloudUtil {
    private StringUtil  stringUtil  = StringUtil.newInstance();
    private SetUtil     setUtil     = SetUtil.newInstance();
    private JsonUtil    jsonUtil    = JsonUtil.newInstance();

    private String leanCloudId;
    private String leanCloudKey;
    private String leanCloudVersion;

    private String leanCloudUrl;
    private String leanCloudVerifySmsPath;
    private String leanCloudRequestSmsPath;
    private String leanCloudMsgPushUrl;

    private LeanCloudUtil() {}
    public static LeanCloudUtil newInstance() {
        return new LeanCloudUtil();
    }

    public LeanCloudUtil setCloudId(String cloudId, String cloudKey, String cloudVersion) {
        this.leanCloudId = cloudId;
        this.leanCloudKey = cloudKey;
        this.leanCloudVersion = cloudVersion;
        return this;
    }
    public LeanCloudUtil setCloudUrl(String cloudUrl) {
        this.leanCloudUrl = cloudUrl;
        return this;
    }
    public LeanCloudUtil setCloudVerifySmsPath(String cloudVerifySmsPath) {
        this.leanCloudVerifySmsPath = cloudVerifySmsPath;
        return this;
    }
    public LeanCloudUtil setCloudRequestSmsPath(String cloudRequestSmsPath) {
        this.leanCloudRequestSmsPath = cloudRequestSmsPath;
        return this;
    }
    public LeanCloudUtil setCloudMsgPushUrl(String cloudMsgPushUrl) {
        this.leanCloudMsgPushUrl = cloudMsgPushUrl;
        return this;
    }


    private String getVerifySmsUrl(){
        return leanCloudUrl+"/"+leanCloudVersion+"/"+leanCloudVerifySmsPath;
    }
    public void verifySmsCode(String code, String mobile){
//        HttpClient httpClient = HttpClients.createDefault();
//        HttpPost post = new HttpPost(getVerifySmsUrl()+"/"+code+"?"+mobile);
//        post.setHeader("X-LC-Id", leanCloudId);
//        post.setHeader("X-LC-Key", leanCloudKey);
//        String body = null;
//        try {
//            HttpResponse response = httpClient.execute(post);
//            int status = response.getStatusLine().getStatusCode();
//            if (status == 200) {
//                System.out.println("verify success.");
//                return;
//            }
//            body = EntityUtils.toString(response.getEntity(), "UTF-8");
//        } catch (IOException e) {
//        }
//        throw new RuntimeException("verify sms failed! " + body);
    }


    public void publishToDevice(String token, String[] channel, MessageBean customJson) {
        System.out.println("token="+token+" receive channel="+channel+" customJson="+customJson);
        pushToToken(DeviceType.Android.getDeviceType(), token, channel, customJson);
    }

    public void publishToDevices(List<String> tokens, String[] channel, MessageBean customJson) {
        System.out.println("tokens="+tokens+" receive channel="+channel+" customJson="+customJson);
        for (int i=0; i<tokens.size(); i++) {
            String tmp = tokens.get(i);
            pushToToken(DeviceType.Android.getDeviceType(), tmp, channel, customJson);
        }
    }

    private void pushToToken(String deviceType, String token, String[] channel, MessageBean customJson) {
        try {
            URL targetUrl = new URL(leanCloudMsgPushUrl);
            HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("X-LC-Id", leanCloudId);
            httpConnection.setRequestProperty("X-LC-Key", leanCloudKey);
            httpConnection.setRequestProperty("Content-Type", "application/json");

            LeanCloudMessageBean msg = new LeanCloudMessageBean();
            msg.setDeviceTypeAndToken(deviceType, token);
            msg.setChannels(channel);
            msg.setData(jsonUtil.toJsonString(customJson));

            OutputStream outputStream = httpConnection.getOutputStream();
            outputStream.write(msg.getJson().getBytes());
            outputStream.flush();

            System.out.println("http code: " + httpConnection.getResponseCode());
            String output;
            StringBuilder outputMessage = new StringBuilder();
            BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));
            while ((output = responseBuffer.readLine()) != null) {
                outputMessage.append(output);
            }
            System.out.println("http message from server: " + outputMessage.toString());

            httpConnection.disconnect();
        } catch (MalformedURLException e) {
            System.out.println("leancloud push message - url - error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("leancloud push message - io - error: " + e.getMessage());
        }

    }

    public void sendOrderMessage(List<String> mobiles, String template, Map<String, String> parameters) {
        if (setUtil.isListEmpty(mobiles)) {
            return;
        }
        Map<String, Object> customJson = new HashMap<>();
        customJson.put("template", template);
        Set<String> keys = parameters.keySet();
        for (String k : keys) {
            customJson.put(k, parameters.get(k));
        }
        for (String mobile : mobiles) {
            if (stringUtil.isEmpty(mobile)) {
                continue;
            }
            customJson.put("mobilePhoneNumber", mobile);
            String messageSendingUrl = leanCloudUrl+"/"+leanCloudVersion+"/"+leanCloudRequestSmsPath;
            sendOrderMessage(messageSendingUrl, jsonUtil.toJsonString(customJson));
        }
    }

    public void sendOrderMessage(String mobile, String template, Map<String, String> parameters) {
        if (stringUtil.isEmpty(mobile)) {
            return;
        }
        Map<String, Object> customJson = new HashMap<>();
        customJson.put("template", template);
        Set<String> keys = parameters.keySet();
        for (String k : keys) {
            customJson.put(k, parameters.get(k));
        }

        customJson.put("mobilePhoneNumber", mobile);
        String messageSendingUrl = leanCloudUrl+"/"+leanCloudVersion+"/"+leanCloudRequestSmsPath;
        sendOrderMessage(messageSendingUrl, jsonUtil.toJsonString(customJson));

    }

    private void sendOrderMessage(String leanCloudAPIUrl, String custumJson) {
        if (stringUtil.isEmpty(custumJson)) {
            return;
        }
        try {
            URL targetUrl = new URL(leanCloudAPIUrl);
            HttpURLConnection httpConnection = (HttpURLConnection) targetUrl.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("X-LC-Id", leanCloudId);
            httpConnection.setRequestProperty("X-LC-Key", leanCloudKey);
            httpConnection.setRequestProperty("Content-Type", "application/json");

            OutputStream outputStream = httpConnection.getOutputStream();
            outputStream.write(custumJson.getBytes());
            outputStream.flush();

            System.out.println("http code: " + httpConnection.getResponseCode());
            String output;
            StringBuilder outputMessage = new StringBuilder();
            BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));
            while ((output = responseBuffer.readLine()) != null) {
                outputMessage.append(output);
            }
            System.out.println("http message from server: " + outputMessage.toString());

            httpConnection.disconnect();
        } catch (MalformedURLException e) {
            System.out.println("leancloud send short message - url - error: "+ e.getMessage());
        } catch (IOException e) {
            System.out.println("leancloud send short message - io - error: "+ e.getMessage());
        }
    }

    public static class LeanCloudMessageBean {
        String doubleQuote = "\"";
        private String channels;
        private String where;
        private String jsonData;

        public void setChannels(String[] channels) {
            StringBuilder msg = new StringBuilder();
            msg.append("[");
            int count = null==channels ? 0 : channels.length;
            for (int i=0; i<count; i++) {
                msg.append(doubleQuote).append(channels[i]).append(doubleQuote);
                if ((i+1)!=count) {
                    msg.append(",");
                }
            }
            msg.append("]");
            this.channels = msg.toString();
            if (null==channels || channels.length==0) {
                this.channels=null;
            }
        }

        public void setDeviceTypeAndToken(String deviceType, String deviceToken) {
            StringBuilder msg = new StringBuilder();
            msg.append("{");
            msg.append(doubleQuote).append("deviceType").append(doubleQuote).append(":").append(doubleQuote).append(deviceType).append(doubleQuote);
            msg.append(",");
            msg.append(doubleQuote).append("installationId").append(doubleQuote).append(":").append(doubleQuote).append(deviceToken).append(doubleQuote);
            msg.append("}");
            this.where = msg.toString();
            if (null==deviceToken || deviceToken.trim().length()==0) {
                this.where = null;
            }
            if (null==deviceType || deviceType.trim().length()==0) {
                this.where = null;
            }
        }

        public void setData(String jsonData) {
            this.jsonData = jsonData;
        }

        public String getJson() {
            StringBuilder msg = new StringBuilder();
            msg.append("{");
            if (null!=channels) {
                msg.append(doubleQuote).append("channels").append(doubleQuote).append(":").append(channels).append(",");
            }
            if (null!=where) {
                msg.append(doubleQuote).append("where").append(doubleQuote).append(":").append(where).append(",");
            }
            msg.append(doubleQuote).append("data").append(doubleQuote).append(":").append(jsonData);
            msg.append("}");
            return msg.toString();
        }
    }
}
