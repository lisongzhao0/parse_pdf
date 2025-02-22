package com.happy.gene.thirdparty.leancloud;

import com.happy.gene.utility.JsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaolisong on 29/06/2017.
 */
public class MessageBean {

    public static final String LEANCLOUD_MSG_TEMPLATE_QSHL_NEW_ORDER  = "qshl_new_order";
    public static final String LEANCLOUD_MSG_TEMPLATE_QSHL_REDISPATCH = "qshl_redispatch";
    public static final String LEANCLOUD_MSG_TEMPLATE_QSHL_WITHDRAW_SUCCESS = "qshl_withdraw_success";
    public static final String LEANCLOUD_MSG_TEMPLATE_QSHL_WITHDRAW_REFUSED = "qshl_withdraw_refused";


    private JsonUtil jsonUtil = JsonUtil.newInstance();
    private String alertBody;
    private String type;
    private String status;
    private long relativeId;
    private String description;
    private Map<String, Object> properties = new HashMap<>();

    public String getAlertBody() {
        return alertBody;
    }
    public String getType() {
        return type;
    }
    public String getStatus() {
        return status;
    }
    public long getRelativeId() {
        return relativeId;
    }
    public String getDescription() {
        return description;
    }
    public void setAlertBody(String alertBody) {
        this.alertBody = alertBody;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setRelativeId(long relativeId) {
        this.relativeId = relativeId;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }
    public void setProperties(String key, Object value) {
        if (null==key || key.trim().length()==0 || null==value) {
            return;
        }
        properties.put(key, value);
    }
    public void setProperties(String json) {
        Map<String, Object> map = (HashMap<String, Object>)jsonUtil.parseJsonBean(json, HashMap.class);
        if (null!=map) {
            properties = map;
        }
    }

    public StringBuilder toHtmlParam() {
        StringBuilder msg = new StringBuilder();
        msg.append("alert=").append(alertBody);
        msg.append("&description=").append(description);
        msg.append("&message_type=").append(type);
        msg.append("&relative_id=").append(relativeId);
        msg.append("&status=").append(status);
        msg.append("&properties=").append(jsonUtil.toJsonString(properties));
        return msg;
    }
    public String toString() {
        StringBuilder msg = new StringBuilder();
        msg.append(getClass().getName()).append("@").append(hashCode()).append("[");
        msg.append("alertBody=").append(alertBody);
        msg.append(", type=").append(type);
        msg.append(", status=").append(status);
        msg.append(", relativeId=").append(relativeId);
        msg.append(", description=").append(description);
        msg.append(", properties=").append(properties);
        msg.append("]");
        return msg.toString();
    }
}
