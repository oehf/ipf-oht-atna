/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openhealthtools.ihe.atna.auditor.queue;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openhealthtools.ihe.atna.auditor.IHEAuditor;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.test.JmsAtnaMessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.ConnectionFactory;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes.RFC3881EventOutcomeCodes.SUCCESS;
import static org.openhealthtools.ihe.atna.test.SyslogServerFactory.createJMSConsumer;

/**
 * @author Dmytro Rud
 */
public class JmsAuditMessageQueueTest {

    private static final String JMS_BROKER_URL = "tcp://localhost:61616";
    private static final String JMS_QUEUE_NAME = "atna";

    private static BrokerService jmsBroker;

    private JmsAuditMessageQueue atnaQueue;

    private Logger LOG = LoggerFactory.getLogger(JmsAuditMessageQueueTest.class);

    @BeforeClass
    public static void beforeClass() throws Exception {
        Locale.setDefault(Locale.ENGLISH);

        // some dummy values
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryHost("localhost");
        AuditorModuleContext.getContext().getConfig().setAuditRepositoryPort(514);

        jmsBroker = new BrokerService();
        jmsBroker.addConnector(JMS_BROKER_URL);
        jmsBroker.setUseJmx(false);
        jmsBroker.setPersistent(false);
        jmsBroker.deleteAllMessages();
        jmsBroker.start();
    }

    @After
    public void tearDown(){
        if (atnaQueue != null) {
            atnaQueue.shutdown();
        }
    }

    @Test
    public void testActiveMQ() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        createJMSConsumer(JMS_BROKER_URL, JMS_QUEUE_NAME, latch, false);

        PooledConnectionFactory jmsConnectionFactory = new PooledConnectionFactory(JMS_BROKER_URL);
        ActiveMQQueue jmsQueue = new ActiveMQQueue(JMS_QUEUE_NAME);
        atnaQueue = new JmsAuditMessageQueue(jmsConnectionFactory, jmsQueue, false);
        AuditorModuleContext.getContext().setQueue(atnaQueue);

        IHEAuditor.getAuditor().auditActorStartEvent(SUCCESS, "actorName", "actorStarter");

        latch.await();
        assertEquals(0, latch.getCount());
    }

    @Test
    public void testMultiActiveMQ() throws Exception {
        CountDownLatch latch = new CountDownLatch(latchCount());
        LOG.info("Latch count: " + latch.getCount());
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        IntStream.range(0, (int)latch.getCount())
            .forEach(i ->
                executorService.execute(new JmsAtnaMessageConsumer(latch, JMS_BROKER_URL, JMS_QUEUE_NAME))
            );

        ConnectionFactory jmsConnectionFactory = new PooledConnectionFactory(JMS_BROKER_URL);
        ActiveMQQueue jmsQueue = new ActiveMQQueue(JMS_QUEUE_NAME);
        atnaQueue = new JmsAuditMessageQueue(jmsConnectionFactory, jmsQueue, false);
        AuditorModuleContext.getContext().setQueue(atnaQueue);

        IntStream.range(0, (int)latch.getCount())
            .forEach(i ->
                IHEAuditor.getAuditor().auditActorStartEvent(SUCCESS, "actorName" + i, "actorStarter" + i)
            );

        latch.await();
        assertEquals(0, latch.getCount());
        LOG.info("Latch count: " + latch.getCount());
        LOG.info("Shutting down Executor service");
        executorService.shutdown();
    }

    private static int latchCount(){
        Random r = new Random();
        int low = 10;
        int high = 100;
        return r.nextInt(high-low) + low;
    }

}
