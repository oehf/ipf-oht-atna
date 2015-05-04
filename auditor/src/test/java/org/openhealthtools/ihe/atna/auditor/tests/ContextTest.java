package org.openhealthtools.ihe.atna.auditor.tests;

import org.junit.Assert;
import org.junit.Ignore;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ContextTest extends Assert {

    @Ignore
    public void testDatagramSender() throws IOException {

        DatagramSocket socket = new DatagramSocket();

        byte[] buf = new byte[652];
        for (int i = 0; i < buf.length; i++) {
            buf[i] = 1;
        }

        DatagramPacket packet = new DatagramPacket(buf, buf.length,
                InetAddress.getByName("129.6.24.109"), 8087);
        packet.setData(buf);
        socket.send(packet);

        socket.close();
    }

}
