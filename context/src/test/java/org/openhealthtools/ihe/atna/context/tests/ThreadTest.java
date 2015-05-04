package org.openhealthtools.ihe.atna.context.tests;

import org.junit.Assert;
import org.junit.Test;
import org.openhealthtools.ihe.atna.context.GenericModuleContext;
import org.openhealthtools.ihe.atna.context.SecurityContext;
import org.openhealthtools.ihe.atna.context.SecurityContextFactory;
import org.openhealthtools.ihe.utils.thread.ConfigurableInheritableThreadLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ThreadTest extends Assert {

    private static final Logger LOG = LoggerFactory.getLogger(ThreadTest.class);

    private class EnumeratedThread extends Thread {
        int threadNum;

        public EnumeratedThread(int num) {
            threadNum = num;
        }

        public void run() {

            SecurityContext context = SecurityContextFactory.getSecurityContext();

            int count = threadNum * 10000;
            while (count < threadNum * 10000 + 10000) {
                context.registerModuleContext(Integer.toString(count), new GenericModuleContext());
                count++;
            }
            LOG.debug("Got SecurityContext in Thread {}: {}", threadNum, context.toString());

        }
    }

    @Test
    public void testSecurityContextThreadSafety() throws Exception {
        ConfigurableInheritableThreadLocal.enableThreading(true);

        SecurityContext context = SecurityContextFactory.getSecurityContext();

        int threadCount = 20;

        Map<String, EnumeratedThread> testThreads = new HashMap<>();

        EnumeratedThread t;
        for (int i = 0; i < threadCount; i++) {
            t = new EnumeratedThread(i);
            t.start();
            testThreads.put(Integer.toString(i), t);
        }

        for (int i = 0; i < threadCount; i++) {
            while (testThreads.get(Integer.toString(i)).isAlive()) {
                LOG.debug("Got SecurityContext in Main Thread: {}", context.toString());
                Thread.sleep(100);
            }
        }

        LOG.debug("{}", Thread.activeCount());
    }

}

