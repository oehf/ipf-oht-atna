/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.openhealthtools.ihe.atna.auditor.queue;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openhealthtools.ihe.atna.auditor.context.AuditorModuleContext;
import org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage;
import org.openhealthtools.ihe.atna.auditor.sender.AuditMessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of an audit queue that delivers messages to the audit
 * sender asynchronously and does not block.  A separate thread daemon is used
 * to manage the message queue and delivers messages on a configurable interval.
 * 
 * Upon shutdown, the queue is flushed and the thread ends.
 * 
 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
 */
public class ThreadedAuditQueue implements AuditMessageQueue
{
	/**
	 * Logger instance
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ThreadedAuditQueue.class);
	
	/**
	 * Auditor Module Context to use
	 */
	private final AuditorModuleContext context;
	
	/**
	 * Thread that asynchronously handles delivery of messages 
	 * to the sender mechanism
	 */
	private ThreadedAuditQueueRuntime thread;
	
	/**
	 * Create an audit queue with the global auditor module context
	 */
	public ThreadedAuditQueue()
	{
		this(AuditorModuleContext.getContext());
	}
	
	/**
	 * Create an audit queue with a given auditor module context
	 * @param contextToUse The auditor module context to use
	 */	
	public ThreadedAuditQueue(AuditorModuleContext contextToUse)
	{
		context = contextToUse;
		thread = new ThreadedAuditQueueRuntime(this.context);
		thread.start();
		Runtime.getRuntime().addShutdownHook(new ThreadedAuditQueueShutdownRuntime(this));
	}
	
	/* (non-Javadoc)
	 * @see org.openhealthtools.ihe.atna.auditor.queue.AuditMessageQueue#flush()
	 */
	public void flush()
	{
		LOGGER.warn("Flushing auditor queue");
		thread.interrupt();
	}
	
	/**
	 * Gets the runtime managed thread for this queue
	 * @return The send handler thread
	 */
	protected ThreadedAuditQueueRuntime getThread()
	{
		return thread;
	}

	/* (non-Javadoc)
	 * @see org.openhealthtools.ihe.atna.auditor.queue.AuditMessageQueue#sendAuditEvent(org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage)
	 */
	public void sendAuditEvent(AuditEventMessage msg) 
	{
		thread.getMessagesToSend().add(msg);
	}
	
	/* (non-Javadoc)
	 * @see org.openhealthtools.ihe.atna.auditor.queue.AuditMessageQueue#sendAuditEvent(org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage, java.net.InetAddress, int)
	 */
	public void sendAuditEvent(AuditEventMessage msg, InetAddress destination, int port)
	{
		thread.getMessagesToSend().add(msg);
	}

	/* (non-Javadoc)
	 * @see org.openhealthtools.ihe.atna.auditor.queue.AuditMessageQueue#shutdown()
	 */
	public void shutdown()
	{
		try {
			thread.signalShutdown();
			thread.join();
			thread = null;
		} catch (InterruptedException e) {
			LOGGER.warn("Interrupted while waiting for queue shutdown, may not have completed cleanly", e);
		}
	}
	
	
	/**
	 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
	 *
	 */
	private class ThreadedAuditQueueRuntime extends Thread
	{
		private final AuditorModuleContext context;

		private final List<AuditEventMessage> msgs = Collections.synchronizedList(new ArrayList<AuditEventMessage>());
		
		private boolean shutdown = false;
		
		public ThreadedAuditQueueRuntime(AuditorModuleContext context)
		{
			this.context = context;
		}
		
		public synchronized List<AuditEventMessage> getMessagesToSend()
		{
			return msgs;
		}
		
		public void run() 
		{
			boolean endNow = false;
			while (!endNow)  {
				if (shutdown) {
					endNow = true;
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {

				}
				if (!msgs.isEmpty()) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Preparing to send " + msgs.size() + " audit messages");
					}
					AuditMessageSender sender = context.getSender();
					try {
						AuditEventMessage[] msgsToSend;
						synchronized (msgs) {
							msgsToSend = msgs.toArray(new AuditEventMessage[msgs.size()]);
							msgs.clear();
						}
						sender.sendAuditEvent(msgsToSend);
					} catch (Exception e) {
						LOGGER.debug("Error sending", e);
						//e.printStackTrace();
					}
				}
			} 
			LOGGER.info("Clean shutdown of audit queue finished");
		}
		
		/**
		 * 
		 */
		public synchronized void signalShutdown()
		{
			LOGGER.info("Signaling threaded queue shutdown");
			shutdown = true;
		}
	}
	
	
	/**
	 * @author <a href="mailto:mattadav@us.ibm.com">Matthew Davis</a>
	 *
	 */
	private class ThreadedAuditQueueShutdownRuntime extends Thread
	{
		private AuditMessageQueue queue;
		public ThreadedAuditQueueShutdownRuntime(AuditMessageQueue queue)
		{
			this.queue = queue;
		}
		public void run()
		{
			queue.shutdown();
		}
	}
	
}
