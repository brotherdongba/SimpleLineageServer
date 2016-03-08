package com.dongba.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.dongba.model.Monster;

public class MonsterManager {
	
	Map<String, Monster> monsters;
	
	public MonsterManager() {
		monsters = new HashMap<String, Monster>();
	}

	public void addMonster(Monster monster) {
		monsters.put(monster.getId(), monster);
	}

	public Monster getMonster(String targetMonsterId) {
		return monsters.get(targetMonsterId);
	}

	public void updateMonsterStatus(Monster monster) {
		monsters.put(monster.getId(), monster);
	}

	public Set<Entry<String, Monster>> getMonsterList() {
		return monsters.entrySet();
	}

}
