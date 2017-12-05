package com.happy.gene.thirdparty.jms;

//import javax.jms.Connection;
//import javax.jms.MessageConsumer;
//import javax.jms.MessageProducer;
//import javax.jms.Session;

/**
 * Created by zhaolisong on 08/11/2017.
 */
public class JmsMsgUtil {

//    public static final int parseAckMode(String ackMode) {
//        if ("AUTO_ACKNOWLEDGE".equalsIgnoreCase(ackMode))   { return Session.AUTO_ACKNOWLEDGE;   }
//        if ("CLIENT_ACKNOWLEDGE".equalsIgnoreCase(ackMode)) { return Session.CLIENT_ACKNOWLEDGE; }
//        if ("DUPS_OK_ACKNOWLEDGE".equalsIgnoreCase(ackMode)){ return Session.DUPS_OK_ACKNOWLEDGE;}
//        if ("SESSION_TRANSACTED".equalsIgnoreCase(ackMode)) { return Session.SESSION_TRANSACTED; }
//        return Session.AUTO_ACKNOWLEDGE;
//    }
//
//    public static final void close(Session session, Connection connection, MessageProducer producer, MessageConsumer consumer) {
//
//        try{ if (producer != null)  { producer.close();   } } catch (Exception e) { e.printStackTrace(); } finally { producer = null; }
//        try{ if (consumer != null)  { consumer.close();   } } catch (Exception e) { e.printStackTrace(); } finally { consumer = null; }
//        try{ if (session != null)   { session.close();    } } catch (Exception e) { e.printStackTrace(); } finally { session = null; }
//        try{ if (connection != null){ connection.close(); } } catch (Exception e) { e.printStackTrace(); } finally { connection = null; }
//    }
}
