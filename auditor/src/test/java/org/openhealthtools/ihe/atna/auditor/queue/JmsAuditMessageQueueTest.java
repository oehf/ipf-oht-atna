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
import org.junit.BeforeClass;
import org.junit.Test;
import org.openhealthtools.ihe.atna.auditor.IHEAuditor;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;
import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;

import java.util.Locale;

/**
 * @author Dmytro Rud
 */
public class JmsAuditMessageQueueTest {

    private static final String JMS_BROKER_URL = "tcp://localhost:61616";
    private static final String JMS_QUEUE_NAME = "atna";

    private static BrokerService jmsBroker;

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

    @Test
    public void testActiveMQ() throws Exception {
        PooledConnectionFactory jmsConnectionFactory = new PooledConnectionFactory(JMS_BROKER_URL);
        ActiveMQQueue jmsQueue = new ActiveMQQueue(JMS_QUEUE_NAME);
        JmsAuditMessageQueue atnaQueue = new JmsAuditMessageQueue(jmsConnectionFactory, jmsQueue, false);

        AuditorModuleContext.getContext().setQueue(atnaQueue);

        IHEAuditor.getAuditor().auditActorStartEvent(RFC3881EventCodes.RFC3881EventOutcomeCodes.SUCCESS, "actorName", "actorStarter");
    }
}
