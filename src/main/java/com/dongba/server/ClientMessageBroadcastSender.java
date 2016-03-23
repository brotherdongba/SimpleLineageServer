package com.dongba.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.dongba.dto.Account;
import com.dongba.dto.ChatMessage;

public class ClientMessageBroadcastSender {

	public void send(ClientMessageTransportManager messageTransportManager, Object obj) {
		Map<String, ClientMessageTransporter> tmpTransporters = new HashMap<String, ClientMessageTransporter>();
		tmpTransporters.putAll(messageTransportManager.getTargetClientList());
		for (Entry<String, ClientMessageTransporter> entry : tmpTransporters.entrySet()) {
			ClientMessageTransporter messageTransporter = entry.getValue();
			Account account = messageTransporter.getAccount();
			if (obj instanceof ChatMessage) {
				ChatMessage message = (ChatMessage) obj;
				if (account.getId().equals(message.getCharacterId())) {
					continue;
				}
			}
			if (messageTransporter.sendMessage(obj) == false) {
				try {
					messageTransporter.disconnect();
				} catch (IOException e) {
				}
			}
		}
	}
	
}
