package com.happy.gene.thirdparty.jms;

//import javax.jms.Message;

/**
 * Created by zhaolisong on 08/11/2017.
 */
public interface JmsMsgProcessor {

    boolean processProducer(Object ... args) throws Exception;

//    boolean processConsumerMsg(Message msg, Object ... args) throws Exception;
}
