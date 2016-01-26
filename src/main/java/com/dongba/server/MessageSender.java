package com.dongba.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MessageSender {
	
	private Map<String, ClientMessageTransporter> clientMessageTransporters;
	
	public MessageSender() {
		clientMessageTransporters = new HashMap<String, ClientMessageTransporter>();
	}
	
	public void broadcast(Object obj) throws InterruptedException {
		Map<String, ClientMessageTransporter> tmpTransporters = new HashMap<String, ClientMessageTransporter>();
		tmpTransporters.putAll(clientMessageTransporters);
		for (Entry<String, ClientMessageTransporter> entry : tmpTransporters.entrySet()) {
			ClientMessageTransporter clientMessageTransporter = entry.getValue();
			if (clientMessageTransporter.sendMessage(obj) == false) {
				String transporterKey = entry.getKey();
				clientMessageTransporters.get(transporterKey).join();
				clientMessageTransporters.remove(transporterKey);
			}
		}
	}

	public void addClientMessageTransporter(ClientMessageTransporter clientMessageReceiver) {
		clientMessageTransporters.put(clientMessageReceiver.getName(), clientMessageReceiver);
	}

	public void removeClientMessageTransporter(ClientMessageTransporter clientMessageTransporter) {
		clientMessageTransporters.remove(clientMessageTransporter.getName());
	}

}
