package org.openhealthtools.ihe.atna.test;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.util.concurrent.CountDownLatch;

/**
 *
 */
public class JmsAtnaMessageConsumer implements Runnable, ExceptionListener {

    private final CountDownLatch latch;
    private final String jmsBrokerUrl;
    private final String jmsQueueName;

    private Logger LOG = LoggerFactory.getLogger(JmsAtnaMessageConsumer.class);

    public JmsAtnaMessageConsumer(CountDownLatch latch, String jmsBrokerUrl, String jmsQueueName) {
        this.latch = latch;
        this.jmsQueueName = jmsQueueName;
        this.jmsBrokerUrl = jmsBrokerUrl;
    }

    public void run() {
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(jmsBrokerUrl);
            Connection connection = connectionFactory.createConnection();
            connection.start();

            connection.setExceptionListener(this);
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(jmsQueueName);
            MessageConsumer consumer = session.createConsumer(destination);
            Message message = consumer.receive(1000);

            if (message instanceof BytesMessage) {
                BytesMessage bytesMessage = (BytesMessage) message;
                byte[] bytes = new byte[(int)bytesMessage.getBodyLength()];
                bytesMessage.readBytes(bytes);
                String text = new String(bytes);
                LOG.info("JMS Consumer Received: " + text);
            } else if (message instanceof TextMessage){
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                LOG.info("JMS Consumer Received: " + text);
            } else {
                LOG.info("JMS Consumer Received: " + message);
            }
            latch.countDown();
            consumer.close();
            session.close();
            connection.close();
        } catch (Exception e) {
            System.out.println("Caught: " + e);
        }
    }

    public synchronized void onException(JMSException ex) {
        System.out.println("JMS Exception occured.  Shutting down client.");
    }
}
