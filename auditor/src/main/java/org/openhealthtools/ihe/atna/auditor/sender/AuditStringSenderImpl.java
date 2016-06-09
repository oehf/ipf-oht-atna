package org.openhealthtools.ihe.atna.auditor.sender;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage;

/**
 * For testing only: an audit message sender that just keeps a list of audit message
 * strings in memory instead of sending them to some repository.
 *
 * @author arnouten
 */
public class AuditStringSenderImpl implements AuditMessageSender {

    private List<String> messages = new ArrayList<>();

    public void sendAuditEvent(AuditEventMessage[] msg) throws Exception {
        add(msg);
    }

    public void sendAuditEvent(AuditEventMessage[] msg, InetAddress destination, int port) throws Exception {
        add(msg);
    }

    /**
     * Clears the message received so far
     */
    public void clear() {
        messages.clear();
    }

    private void add(AuditEventMessage[] msg) {
        for (AuditEventMessage message : msg) {
            messages.add(new String(message.getSerializedMessage(true)));
        }
    }

}
