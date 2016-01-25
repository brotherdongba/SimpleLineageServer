package com.dongba.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dongba.model.Monster;

public class MessageBroadcaster {
	
	private Map<String, Monster> monsters;
	private List<Client> clients;
	
	public MessageBroadcaster() {
		clients = new ArrayList<Client>();
		monsters = new HashMap<String, Monster>();
	}
	
	public void addClient(Client client) {
		clients.add(client);
	}

	public void broadcast(Object obj) {
		for (Client client : clients) {
			client.forward(obj);
		}
	}

	public void addNForwardMonster(Monster monster) {
		monsters.put(monster.getId(), monster);
		broadcast(monster);
	}

	public Monster getMonster(String targetMonsterId) {
		return monsters.get(targetMonsterId);
	}

	public void updateMonsterStatus(Monster monster) {
		monsters.put(monster.getId(), monster);
	}

}
