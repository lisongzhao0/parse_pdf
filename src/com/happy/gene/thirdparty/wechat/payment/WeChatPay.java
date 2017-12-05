package com.happy.gene.thirdparty.wechat.payment;

import com.happy.gene.utility.NetworkUtil;
import com.happy.gene.utility.StringUtil;
import com.happy.gene.utility.TokenUtil;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhaolisong on 29/06/2017.
 */
public class WeChatPay {

    public final static String RESULT_FORMAT_XML    = "xml";
    public final static String RESULT_FORMAT_JSON   = "json";

    public final static String PARAM_APP_ID         = "PARAM_APP_ID";
    public final static String PARAM_APP_SECRET     = "PARAM_APP_SECRET";
    public final static String PARAM_CODE           = "PARAM_CODE";
    public final static String PARAM_REFRESH_TOKEN  = "PARAM_REFRESH_TOKEN";
    public final static String PARAM_ACCESS_TOKEN   = "PARAM_ACCESS_TOKEN";

    /***************************************
     *           微信基础接口地址            *
     ***************************************/
    // 获取token接口 (GET)
    public final static String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?appid="+ PARAM_APP_ID +"&secret="+ PARAM_APP_SECRET +"&grant_type=client_credential";
    // oauth2授权接口 (GET)
    public final static String OAUTH2_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+ PARAM_APP_ID +"&secret="+ PARAM_APP_SECRET +"&code="+ PARAM_CODE +"&grant_type=authorization_code";
    // 小程序获取 openId (GET)
    public final static String XCX_OPEN_ID = "https://api.weixin.qq.com/sns/jscode2session?appid="+ PARAM_APP_ID +"&secret="+ PARAM_APP_SECRET +"&js_code="+ PARAM_CODE +"&grant_type=authorization_code";
    // 刷新access_token接口（GET）
    public final static String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid="+ PARAM_APP_ID +"&refresh_token="+ PARAM_REFRESH_TOKEN +"&grant_type=refresh_token";
    // 菜单创建接口（POST）
    public final static String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+ PARAM_ACCESS_TOKEN;
    // 菜单查询（GET）
    public final static String MENU_GET_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token="+ PARAM_ACCESS_TOKEN;
    // 菜单删除（GET）
    public final static String MENU_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token="+ PARAM_ACCESS_TOKEN;

    /***************************************
     *            微信支付接口地址           *
     ***************************************/
    //微信支付统一接口(POST)
    public final static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    //微信退款接口(POST)
    public final static String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    //订单查询接口(POST)
    public final static String CHECK_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
    //关闭订单接口(POST)
    public final static String CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
    //退款查询接口(POST)
    public final static String CHECK_REFUND_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
    //对账单接口(POST)
    public final static String DOWNLOAD_BILL_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";
    //短链接转换接口(POST)
    public final static String SHORT_URL = "https://api.mch.weixin.qq.com/tools/shorturl";
    //接口调用上报接口(POST)
    public final static String REPORT_URL = "https://api.mch.weixin.qq.com/payitil/report";

    /***************************************
     *        商户 APIKey 和回调 URL         *
     ***************************************/
    private String paymentNotifyUrl;
    private String shangHuApiKey;
    private TokenUtil   tokenUtil   = TokenUtil.newInstance();
    private StringUtil  stringUtil  = StringUtil.newInstance();


    /***************************************
     *                 实例化               *
     ***************************************/
    private WeChatPay() {}
    public static WeChatPay newInstance() {
        return new WeChatPay();
    }

    public String getPaymentNotifyUrl() {
        return paymentNotifyUrl;
    }
    public WeChatPay setPaymentNotifyUrl(String paymentNotifyUrl) {
        this.paymentNotifyUrl = paymentNotifyUrl;
        return this;
    }

    public String getShangHuApiKey() {
        return shangHuApiKey;
    }
    public WeChatPay setShangHuApiKey(String shangHuApiKey) {
        this.shangHuApiKey = shangHuApiKey;
        return this;
    }


    /**
     * 微信公众号基础接口调用</A>
     * @param baseApiUrl 微信公众号基础接口地址.
     * @param httpMethod 微信公众号基础接口调用方式
     * @param appId     微信分配的公众账号ID(企业号corpId即为此appId).
     * @param appSecret 微信分配的公众账号密码.
     * @param code
     * @param accessToken
     * @param refreshToken
     */
    public Map wechatBaseAPI(String baseApiUrl, String resultFormat, String httpMethod, String appId, String appSecret, String code, String accessToken, String refreshToken) {
        if (null==baseApiUrl || baseApiUrl.trim().isEmpty()) {
            return null;
        }
        if (null!=appId && !appId.trim().isEmpty()) {
            baseApiUrl = baseApiUrl.replace(PARAM_APP_ID, appId);
        }
        if (null!=appSecret && !appSecret.trim().isEmpty()) {
            baseApiUrl = baseApiUrl.replace(PARAM_APP_SECRET, appSecret);
        }
        if (null!=code && !code.trim().isEmpty()) {
            baseApiUrl = baseApiUrl.replace(PARAM_CODE, code);
        }
        if (null!=accessToken && !accessToken.trim().isEmpty()) {
            baseApiUrl = baseApiUrl.replace(PARAM_ACCESS_TOKEN, accessToken);
        }
        if (null!=refreshToken && !refreshToken.trim().isEmpty()) {
            baseApiUrl = baseApiUrl.replace(PARAM_REFRESH_TOKEN, refreshToken);
        }

        String response = NetworkUtil.newInstance().httpsRequest(baseApiUrl, httpMethod, null, null);
        if (RESULT_FORMAT_JSON.equalsIgnoreCase(resultFormat)) {
            return stringUtil.parseResponseJson(response);
        }
        if (RESULT_FORMAT_XML.equals(resultFormat)) {
            return stringUtil.parseResponseXml(response);
        }
        return new HashMap<>();
    }


    /**
     * <A href="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1">微信公众号支付API详细介绍</A>
     * @param openId 用户在商户appId下的唯一标识;trade_type=JSAPI,此参数必传.
     * @param devInfo 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
     * @param spbillCreateIp APP和网页支付提交用户端ip,Native支付填调用微信支付API的机器IP.
     * @param appId 微信分配的公众账号ID(企业号corpId即为此appId).
     * @param mchId 微信支付分配的商户号.
     * @param apiKey 微信商户平台ApiKey
     * @param outTradeNo 商户系统内部的订单号(32个字符内,可包含字母),其他说明见商户订单号
     * @param tradeType 取值如下:JSAPI(公众号支付),NATIVE(原生扫码支付),APP(app支付)
     * @param body 商品简单描述,该字段须严格按照规范传递,具体请见参数规定
     * @param feeType 符合ISO_4217标准的三位字母代码,默认人民币:CNY
     * @param totalFee 订单总金额,单位为分,详见支付金额
     * @param notifyUrl 接收微信支付异步通知回调地址,通知url必须为直接可访问的url,不能携带参数.
     */
    public Map<String, String> payByWeChat(String openId, String devInfo, String spbillCreateIp,
                                           String appId, String mchId, String apiKey,
                                           String outTradeNo, String tradeType, String body,
                                           String feeType, int totalFee,
                                           String notifyUrl
    ) {
        Map<String, String> keyParam = new HashMap();

        String key = "openid";
        keyParam.put(key, openId);

        key = "device_info";
        keyParam.put(key, devInfo);

        key = "spbill_create_ip";
        keyParam.put(key, spbillCreateIp);

        key = "appid";
        keyParam.put(key, appId);

        key = "mch_id";
        keyParam.put(key, mchId);

        key = "out_trade_no";
        keyParam.put(key, outTradeNo);

        key = "body";
        keyParam.put(key, body);

        // 随机字符串,不长于32位.推荐随机数生成算法
        key = "nonce_str";
        keyParam.put(key, tokenUtil.createNoncestr(31));

        key = "trade_type";
        keyParam.put(key, tradeType);

        key = "fee_type";
        keyParam.put(key, feeType);

        key = "total_fee";
        keyParam.put(key, String.valueOf(totalFee));

        key = "notify_url";
        keyParam.put(key, notifyUrl);


        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        String timeStart = format.format(calendar.getTime());
        calendar.add(Calendar.MINUTE, 6);
        String timeExpire = format.format(calendar.getTime());

        // 订单失效时间,格式为yyyyMMddHHmmss
        key = "time_expire";
        keyParam.put(key, timeExpire);

        // 订单生成时间,格式为yyyyMMddHHmmss
        key = "time_start";
        keyParam.put(key, timeStart);

        key = "sign";
        keyParam.put(key, createSign(apiKey, "UTF-8", new TreeMap<>(keyParam)));

        System.out.println("request payment parameter map===="+keyParam);

        String xmlWeChatOrder = stringUtil.getRequestXml(new TreeMap<>(keyParam));
        System.out.println("create sign xml "+xmlWeChatOrder);
        String response = NetworkUtil.newInstance().httpsRequest(UNIFIED_ORDER_URL, "POST", xmlWeChatOrder, null);
        keyParam = stringUtil.parseResponseXml(response);
        return keyParam;
    }

    /**
     * <A href="https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_2">微信公众号查询订单API详细介绍</A>
     * @param appId 微信分配的公众账号ID(企业号corpId即为此appId).
     * @param mchId 微信支付分配的商户号.
     *
     *（二选一）
     * @param transactionId 微信生成的订单号，在支付通知中有返回，建议优先使用
     * @param outTradeNo 商户侧传给微信的订单号
     */
    public Map<String, String> checkPaymentByWeChat(String appId, String mchId,
                                                    String transactionId,
                                                    String outTradeNo
    ) {
        Map<String, String> keyParam = new HashMap();

        String key = "appid";
        keyParam.put(key, appId);

        key = "mch_id";
        keyParam.put(key, mchId);

        if (null!=transactionId && transactionId.length()!=0) {
            key = "transaction_id";
            keyParam.put(key, transactionId);
        }
        if (null!=outTradeNo && outTradeNo.length()!=0) {
            key = "out_trade_no";
            keyParam.put(key, outTradeNo);
        }
        if ((null==transactionId || transactionId.trim().length()==0)
                && (null==outTradeNo    || outTradeNo.trim().length()==0)) {
            throw new RuntimeException("wechat transactionId and outTradeNo are both empty.");
        }

        // 随机字符串,不长于32位.推荐随机数生成算法
        key = "nonce_str";
        keyParam.put(key, tokenUtil.createNoncestr(31));

        key = "sign";
        keyParam.put(key, createSign(shangHuApiKey, "UTF-8", new TreeMap<>(keyParam)));

        String xmlWeChatOrder = stringUtil.getRequestXml(new TreeMap<>(keyParam));
        String response = NetworkUtil.newInstance().httpsRequest(CHECK_ORDER_URL, "POST", xmlWeChatOrder, null);
        keyParam = stringUtil.parseResponseXml(response);
        return keyParam;
    }

    /**
     * @Description：sign签名
     * @param weChatApiKey WeChat Api Key
     * @param characterEncoding 编码格式
     * @param parameters 请求参数
     * @return
     */
    public String createSign(String weChatApiKey, String characterEncoding, SortedMap<String,Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while(it.hasNext()) {
            Map.Entry entry = (Map.Entry)it.next();
            String k = (String)entry.getKey();
            Object v = entry.getValue();
            if(null!=v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + weChatApiKey);
        String sign = tokenUtil.md5Encode(sb.toString(), characterEncoding, "MD5").toUpperCase();
        return sign;
    }

    /**
     * @Description：检查 sign 签名
     * @param weChatApiKey WeChat Api Key
     * @param characterEncoding 编码格式
     * @param parameters 返回参数
     * @return
     */
    public boolean checkSign(String weChatApiKey, String characterEncoding, SortedMap<String,Object> parameters) {
        if (null==parameters && parameters.isEmpty()) {
            return false;
        }

        Object signObj = parameters.get("sign");
        if (!(signObj instanceof String)) {
            return false;
        }

        String sign = (String)signObj;
        parameters.remove("sign");
        String newSign = createSign(weChatApiKey, characterEncoding, parameters);
        return sign.equals(newSign);
    }

    /**
     * <A href="https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_4&index=6">微信公众号申请退款API详细介绍</A>
     * @param appId 微信分配的公众账号ID(企业号corpId即为此appId).
     * @param mchId 微信支付分配的商户号.
     * @param devInfo 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
     *
     *（二选一）
     * @param transactionId 微信生成的订单号，在支付通知中有返回
     * @param outTradeNo 商户侧传给微信的订单号
     *
     * @param outRefundNo 商户退款单号, 商户系统内部的退款单号，商户系统内部唯一，同一退款单号多次请求只退一笔
     * @param totalFee 总金额，订单总金额，单位为分，只能为整数，详见支付金额
     * @param refundFee 退款金额，退款总金额，订单总金额，单位为分，只能为整数，详见支付金额
     * @param refundFeeType 货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     * @param opUserId 操作员帐号, 默认为商户号
     * @param certP12FileInputStream 商户p12证书
     */
    public Map<String, String> refundByWeChat(String appId, String mchId, String devInfo,
                                              String transactionId, String outTradeNo,
                                              String outRefundNo,
                                              int totalFee, int refundFee, String refundFeeType,
                                              String opUserId,
                                              InputStream certP12FileInputStream
    ) {
        Map<String, String> keyParam = new HashMap();
        String key = "appid";
        keyParam.put(key, appId);

        key = "mch_id";
        keyParam.put(key, mchId);

        key = "device_info";
        keyParam.put(key, devInfo);

        // 随机字符串,不长于32位.推荐随机数生成算法
        key = "nonce_str";
        keyParam.put(key, tokenUtil.createNoncestr(31));

        if (null!=outTradeNo && outTradeNo.trim().length()>0) {
            key = "out_trade_no";
            keyParam.put(key, outTradeNo);
        }
        if (null!=transactionId && transactionId.trim().length()>0) {
            key = "transaction_id";
            keyParam.put(key, transactionId);
        }
        if ((null==transactionId || transactionId.trim().length()==0)
                && (null==outTradeNo    || outTradeNo.trim().length()==0)) {
            throw new RuntimeException("wechat transactionId and outTradeNo are both empty.");
        }

        key = "out_refund_no";
        keyParam.put(key, outRefundNo);

        key = "total_fee";
        keyParam.put(key, String.valueOf(totalFee));

        key = "refund_fee";
        keyParam.put(key, String.valueOf(refundFee));

        key = "refund_fee_type";
        keyParam.put(key, refundFeeType);

        if (null!=opUserId && opUserId.trim().length()>0) {
            key = "op_user_id";
            keyParam.put(key, opUserId);
        }
        else {
            key = "op_user_id";
            keyParam.put(key, mchId);
        }

        key = "refund_account";
        keyParam.put(key, "REFUND_SOURCE_UNSETTLED_FUNDS");

        key = "sign_type";
        keyParam.put(key, "MD5");

        key = "sign";
        keyParam.put(key, createSign(shangHuApiKey, "UTF-8", new TreeMap<>(keyParam)));

        String xmlWeChatOrder = stringUtil.getRequestXml(new TreeMap<>(keyParam));
//        String response = NetworkUtil.newInstance().httpsRequest(REFUND_URL, "POST", keyParam, xmlWeChatOrder, mchId, certP12FileInputStream);
//        keyParam = stringUtil.parseResponseXml(response);
        return keyParam;
    }

    /**
     * <A href="https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_5&index=7">微信公众号查询退款API详细介绍</A>
     * @param appId   微信分配的公众账号ID(企业号corpId即为此appId).
     * @param mchId   微信支付分配的商户号.
     * @param devInfo 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"

     * 四选一
     * @param transactionId 微信订单号 String(32)  微信订单号
     * @param outTradeNo    商户订单号 String(32) 商户系统内部的订单号
     * @param outRefundNo   商户退款单号 String(32) 商户侧传给微信的退款单号
     * @param refundId      微信退款单号 String(28) 微信生成的退款单号，在申请退款接口有返回
     */

    public Map<String, String> checkRefundByWeChat(String appId, String mchId, String devInfo,
                                                   String outRefundNo,
                                                   String refundId,
                                                   String transactionId,
                                                   String outTradeNo
    ) {
        Map<String, String> keyParam = new HashMap();
        String key = "appid";
        keyParam.put(key, appId);

        key = "mch_id";
        keyParam.put(key, mchId);

        key = "device_info";
        keyParam.put(key, devInfo);

        // 随机字符串,不长于32位.推荐随机数生成算法
        key = "nonce_str";
        keyParam.put(key, tokenUtil.createNoncestr(31));

        if (null!=transactionId && transactionId.length()!=0) {
            key = "transaction_id";
            keyParam.put(key, transactionId);
        }
        if (null!=outTradeNo && outTradeNo.length()!=0) {
            key = "out_trade_no";
            keyParam.put(key, outTradeNo);
        }
        if (null!=outRefundNo && outRefundNo.length()!=0) {
            key = "out_refund_no";
            keyParam.put(key, outRefundNo);
        }
        if (null!=refundId && refundId.length()!=0) {
            key = "refund_id";
            keyParam.put(key, refundId);
        }
        if ((null==transactionId || 0==transactionId.trim().length())
                && (null==outTradeNo    || 0==outTradeNo.trim().length())
                && (null==outRefundNo   || 0==outRefundNo.trim().length())
                && (null==refundId      || 0==refundId.trim().length())) {
            throw new RuntimeException("wechat transactionId, refundId, outTradeNo and outRefundNo are empty.");
        }

        key = "sign_type";
        keyParam.put(key, "MD5");

        key = "sign";
        keyParam.put(key, createSign(shangHuApiKey, "UTF-8", new TreeMap<>(keyParam)));

        String xmlWeChatOrder = stringUtil.getRequestXml(new TreeMap<>(keyParam));
//        String response = NetworkUtil.newInstance().httpsRequest(CHECK_REFUND_URL, "POST", xmlWeChatOrder);
//        keyParam = stringUtil.parseResponseXml(response);
        return keyParam;
    }

    /**
     * <A href="https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_3&index=5">微信公众号关闭退款API详细介绍</A>
     * @param appId      微信分配的公众账号ID(企业号corpId即为此appId).
     * @param mchId      微信支付分配的商户号.
     * @param outTradeNo 商户订单号，商户系统内部的订单号
     */
    public Map<String, String> closeOrderByWeChat(String appId, String mchId, String outTradeNo) {
        Map<String, String> keyParam = new HashMap();
        String key = "appid";
        keyParam.put(key, appId);

        key = "mch_id";
        keyParam.put(key, mchId);

        // 随机字符串,不长于32位.推荐随机数生成算法
        key = "nonce_str";
        keyParam.put(key, tokenUtil.createNoncestr(31));

        if (null!=outTradeNo && outTradeNo.length()!=0) {
            key = "out_trade_no";
            keyParam.put(key, outTradeNo);
        }
        if ((null==outTradeNo || 0==outTradeNo.trim().length())) {
            throw new RuntimeException("wechat outTradeNo is empty.");
        }

        key = "sign_type";
        keyParam.put(key, "MD5");

        key = "sign";
        keyParam.put(key, createSign(shangHuApiKey, "UTF-8", new TreeMap<>(keyParam)));

        String xmlWeChatOrder = stringUtil.getRequestXml(new TreeMap<>(keyParam));
//        String response = NetworkUtil.newInstance().httpsRequest(CLOSE_ORDER_URL, "POST", xmlWeChatOrder);
//        keyParam = stringUtil.parseResponseXml(response);
        return keyParam;
    }
}
