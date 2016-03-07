package com.dongba.server;

import java.util.HashMap;
import java.util.Map;

public class ClientMessageTransportManager {
	
	private Map<String, ClientMessageTransporter> clientMessageTransporters;
	
	public ClientMessageTransportManager() {
		clientMessageTransporters = new HashMap<String, ClientMessageTransporter>();
	}

	public void addClientMessageTransporter(ClientMessageTransporter clientMessageReceiver) {
		clientMessageTransporters.put(clientMessageReceiver.getName(), clientMessageReceiver);
	}

	public void removeClientMessageTransporter(ClientMessageTransporter clientMessageTransporter) {
		clientMessageTransporters.remove(clientMessageTransporter.getName());
	}

	public Map<String, ClientMessageTransporter> getTargetClientList() {
		return clientMessageTransporters;
	}

	public void removeNRelease(String messageTransporterName) throws InterruptedException {
		ClientMessageTransporter cmt = clientMessageTransporters.get(messageTransporterName);
		cmt.join();
		clientMessageTransporters.remove(messageTransporterName);
	}

}
