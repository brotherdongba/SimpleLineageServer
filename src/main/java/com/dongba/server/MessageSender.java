package com.dongba.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.dongba.model.Account;
import com.dongba.model.ChatMessage;
import com.dongba.model.Monster;

public class MessageSender {
	
	private Map<String, ClientMessageTransporter> clientMessageTransporters;
	
	public MessageSender() {
		clientMessageTransporters = new HashMap<String, ClientMessageTransporter>();
	}
	
	public void broadcast(ChatMessage message) throws InterruptedException {
		forward(message);
	}
	
	public void broadcast(Monster monster) throws InterruptedException {
		forward(monster);
	}

	private void forward(Object obj) throws InterruptedException {
		Map<String, ClientMessageTransporter> tmpTransporters = new HashMap<String, ClientMessageTransporter>();
		tmpTransporters.putAll(clientMessageTransporters);
		for (Entry<String, ClientMessageTransporter> entry : tmpTransporters.entrySet()) {
			ClientMessageTransporter clientMessageTransporter = entry.getValue();
			if (isExcluded(clientMessageTransporter.getAccount(), obj)) {
				continue;
			}
			if (clientMessageTransporter.sendMessage(obj) == false) {
				String transporterKey = entry.getKey();
				clientMessageTransporters.get(transporterKey).join();
				clientMessageTransporters.remove(transporterKey);
			}
		}
	}
	
	private boolean isExcluded(Account account, Object obj) {
		if (obj instanceof ChatMessage) {
			ChatMessage message = (ChatMessage) obj;
			return account.getId().equals(message.getCharacterId());
		}
		return false;
	}

	public void addClientMessageTransporter(ClientMessageTransporter clientMessageReceiver) {
		clientMessageTransporters.put(clientMessageReceiver.getName(), clientMessageReceiver);
	}

	public void removeClientMessageTransporter(ClientMessageTransporter clientMessageTransporter) {
		clientMessageTransporters.remove(clientMessageTransporter.getName());
	}

}
