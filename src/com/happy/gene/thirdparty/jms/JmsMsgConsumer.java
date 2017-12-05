package com.happy.gene.thirdparty.jms;

import com.happy.gene.utility.StringUtil;
//import org.apache.activemq.ActiveMQConnectionFactory;
//
//import javax.jms.*;

/**
 * Created by zhaolisong on 08/11/2017.
 */
public class JmsMsgConsumer {
    private StringUtil stringUtil = StringUtil.newInstance();


    private String url = "tcp://127.0.0.1:61616";
    private String username = null;
    private String password = null;
    private String queueName = null;
    private String topicName = null;
    private boolean transacted = false;
    private boolean persistent = true;
    private String  ackMode    = "AUTO_ACKNOWLEDGE";
    private JmsMsgProcessor msgProcessor = null;

//    private Session         session     = null;
//    private Connection      connection  = null;
//    private Destination     destination = null;
//    private MessageConsumer consumer    = null;
    private boolean         isChanged   = false;

    public String getUrl() { return url; }
    public String getAckMode() { return ackMode; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getQueueName() { return queueName; }
    public String getTopicName() { return topicName; }
    public boolean isTransacted() { return transacted; }
    public boolean isPersistent() { return persistent; }
    public JmsMsgProcessor getMsgProcessor() { return msgProcessor; }

    public JmsMsgConsumer setUrl(String url) { this.url = url; isChanged = stringUtil.isChanged(this.url, url); return this; }
    public JmsMsgConsumer setAckMode(String ackMode) { this.ackMode = ackMode; isChanged = stringUtil.isChanged(this.ackMode, ackMode); return this; }
    public JmsMsgConsumer setUsername(String username) { this.username = username; isChanged = stringUtil.isChanged(this.username, username); return this; }
    public JmsMsgConsumer setPassword(String password) { this.password = password; isChanged = stringUtil.isChanged(this.password, password); return this; }
    public JmsMsgConsumer setQueueName(String queueName) { this.queueName = queueName; isChanged = stringUtil.isChanged(this.queueName, queueName); return this; }
    public JmsMsgConsumer setTopicName(String topicName) { this.topicName = topicName; isChanged = stringUtil.isChanged(this.topicName, topicName); return this; }
    public JmsMsgConsumer setTransacted(boolean transacted) { this.transacted = transacted; isChanged = stringUtil.isChanged(this.transacted+"", transacted+""); return this; }
    public JmsMsgConsumer setPersistent(boolean persistent) { this.persistent = persistent; isChanged = stringUtil.isChanged(this.persistent+"", persistent+""); return this; }
    public JmsMsgConsumer setMsgProcessor(JmsMsgProcessor msgProcessor) { this.msgProcessor = msgProcessor; return this; }

    public static final JmsMsgConsumer newInstance() {
        return new JmsMsgConsumer();
    }

    private JmsMsgConsumer() {}

    public boolean consumerMsgInQueue(boolean asService) {
//        if (isChanged) {
//            JmsMsgUtil.close(session, connection, null, consumer);
//            session=null; connection=null; consumer=null;
//        }
//
//        try{
//            if (null==connection) {
//                // create the connection
//                ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(username, password, url);
//                connection = connectionFactory.createConnection();
//                connection.start();
//            }
//
//            // create the session
//            int ackMode = JmsMsgUtil.parseAckMode(getAckMode());
//            if (null==session) {
//                session = connection.createSession(transacted, ackMode);
//            }
//
//            // create the destination
//            if (null==destination) {
//                destination = session.createQueue(queueName);
//            }
//
//            // create the consumer
//            if (null==consumer) {
//                consumer = session.createConsumer(destination);
//            }
//            if (asService) {
//                int maxFailed = 12;
//                for (; maxFailed>0 ;) {
//                    // blocking till receive the message
//                    Message recvMessage = consumer.receive(20000);
//                    if (null==recvMessage) { maxFailed --; }
//                    if (null!=msgProcessor) { msgProcessor.processConsumerMsg(recvMessage); }
//                    if (Session.CLIENT_ACKNOWLEDGE == ackMode) { recvMessage.acknowledge(); }
//                }
//            }
//            else {
//                // blocking till receive the message
//                Message recvMessage = consumer.receive(20000);
//                if (null!=msgProcessor) { msgProcessor.processConsumerMsg(recvMessage); }
//                if (Session.CLIENT_ACKNOWLEDGE == ackMode) { recvMessage.acknowledge(); }
//            }
//
//        }catch (Exception e){
//            System.err.println("ERROR ---- receive message error. error="+e.getMessage());
//            return false;
//        }finally{
//            // close session and connection
//            JmsMsgUtil.close(session, connection, null, consumer);
//            session=null; connection=null; consumer=null;
//        }
        return true;
    }

}
