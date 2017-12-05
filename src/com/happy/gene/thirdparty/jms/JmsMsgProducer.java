package com.happy.gene.thirdparty.jms;

import com.happy.gene.utility.StringUtil;
//import org.apache.activemq.ActiveMQConnectionFactory;
//
//import javax.jms.*;

/**
 * Created by zhaolisong on 08/11/2017.
 */
public class JmsMsgProducer {
    private StringUtil stringUtil = StringUtil.newInstance();

    private String url = "tcp://127.0.0.1:61616";
    private String username = null;
    private String password = null;
    private String queueName = null;
    private String topicName = null;
    private boolean transacted = false;
    private boolean persistent = true;
    private String  ackMode    = "AUTO_ACKNOWLEDGE";

//    private Session         session     = null;
//    private Connection      connection  = null;
//    private Destination     destination = null;
//    private MessageProducer producer    = null;
    private boolean         isChanged   = false;

    public String getUrl() { return url; }
    public String getAckMode() { return ackMode; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getQueueName() { return queueName; }
    public String getTopicName() { return topicName; }
    public boolean isTransacted() { return transacted; }
    public boolean isPersistent() { return persistent; }
    public JmsMsgProducer setUrl(String url) { this.url = url; isChanged = stringUtil.isChanged(this.url, url); return this; }
    public JmsMsgProducer setAckMode(String ackMode) { this.ackMode = ackMode; isChanged = stringUtil.isChanged(this.ackMode, ackMode); return this; }
    public JmsMsgProducer setUsername(String username) { this.username = username; isChanged = stringUtil.isChanged(this.username, username); return this; }
    public JmsMsgProducer setPassword(String password) { this.password = password; isChanged = stringUtil.isChanged(this.password, password); return this; }
    public JmsMsgProducer setQueueName(String queueName) { this.queueName = queueName; isChanged = stringUtil.isChanged(this.queueName, queueName); return this; }
    public JmsMsgProducer setTopicName(String topicName) { this.topicName = topicName; isChanged = stringUtil.isChanged(this.topicName, topicName); return this; }
    public JmsMsgProducer setTransacted(boolean transacted) { this.transacted = transacted; isChanged = stringUtil.isChanged(this.transacted+"", transacted+""); return this; }
    public JmsMsgProducer setPersistent(boolean persistent) { this.persistent = persistent; isChanged = stringUtil.isChanged(this.persistent+"", persistent+""); return this; }

    public static final JmsMsgProducer newInstance() {
        return new JmsMsgProducer();
    }

    private JmsMsgProducer() {}

    public boolean sendMsgToQueue(String messageBody) {
//        if (isChanged) {
//            JmsMsgUtil.close(session, connection, producer, null);
//            session=null; connection=null; producer=null;
//        }
//
//        try{
//            // create the connection
//            if (null==connection) {
//                ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(username, password, url);
//                connection = connectionFactory.createConnection();
//                connection.start();
//            }
//
//            // create the session
//            if (null==session) {
//                session = connection.createSession(transacted, JmsMsgUtil.parseAckMode(ackMode));
//            }
//
//            // create destination
//            if (null==destination) {
//                destination = session.createQueue(queueName);
//            }
//
//            // create the producer
//            if (null==producer) {
//                producer = session.createProducer(destination);
//            }
//
//            if (persistent){
//                producer.setDeliveryMode(DeliveryMode.PERSISTENT);
//            }else{
//                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
//            }
//
//            // create text message
//            Message message = session.createTextMessage(messageBody);
//
//            // send the message
//            producer.send(message);
//        }catch (Exception e){
//            System.err.println("ERROR ---- send message error. message="+messageBody+"  error="+e.getMessage());
//            return false;
//        }finally{
//            // close session and connection
//            JmsMsgUtil.close(session, connection, producer, null);
//            session=null; connection=null; producer=null;
//        }
        return true;
    }
}
