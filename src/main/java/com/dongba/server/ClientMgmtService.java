package com.dongba.server;

import java.util.Map.Entry;
import java.util.Set;

public class ClientMgmtService implements Runnable {

	private MessageSender messageSender;

	public ClientMgmtService(MessageSender messageSender) {
		this.messageSender = messageSender;
	}

	public void run() {
		System.out.println("start clientMgmtService.");
		while (true) {
			Set<Entry<String,ClientMessageTransporter>> clientEntrySet = messageSender.getClientEntrySet();
			for (Entry<String, ClientMessageTransporter> e : clientEntrySet) {
				ClientMessageTransporter cmt = e.getValue();
				if (cmt.sendMessage(1) == false) {
					System.out.println("user " + cmt.getAccount().getId() + " is logged out.");
					try {
						cmt.join();
					} catch (InterruptedException e1) {
					}
					messageSender.removeClientMessageTransporter(cmt);
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
			}
		}
	}

}
