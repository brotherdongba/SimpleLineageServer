package com.dongba.server;

import java.util.HashMap;
import java.util.Map;

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

}
