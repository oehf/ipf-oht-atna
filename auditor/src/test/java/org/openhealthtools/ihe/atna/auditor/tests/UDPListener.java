package org.openhealthtools.ihe.atna.auditor.tests;

import java.io.File;
import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPListener {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		DatagramSocket socket = new DatagramSocket(1234); 
		System.out.println("Beginning UDP listener daemon...");
		
		int count = 1;
		while (true) {
			DatagramPacket packet = new DatagramPacket(new byte[65507],65507);
			socket.receive(packet);

	        try {
	        	String inputData = new String(packet.getData());
	        	
	        	int startIdx = inputData.indexOf("<?xml");
	        	if (startIdx != -1) {
	        		inputData = inputData.substring(startIdx,inputData.length());
	        	}
	        	
				System.out.println("Received UDP broadcast "+ count);
	        	String outputData = inputData.trim();
	        	System.out.println(outputData);
	        	
	        	FileOutputStream outStream = new FileOutputStream(new File("C:/auditmsg/"+ count +".xml"));
	        	outStream.write(outputData.getBytes());
	        	outStream.flush();
	        	outStream.close();
	        	count++;
	        } catch (Throwable t) {
	        	System.out.println("error deserialize");
	        	t.printStackTrace();
	        }
			

		}

	}

}
