package com.dongba.server;

import java.util.ArrayList;
import java.util.List;

public class MessageBroadcaster {
	
	private List<Client> clients;
	
	public MessageBroadcaster() {
		clients = new ArrayList<Client>();
	}
	
	public void addClient(Client client) {
		clients.add(client);
	}

	public void broadcast(Object obj) {
		for (Client client : clients) {
			client.forward(obj);
		}
	}

}
