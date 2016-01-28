package com.dongba.server;

import com.dongba.model.CharacterMotion;
import com.dongba.model.ChatMessage;
import com.dongba.model.Monster;

public class ClientMessageProcessor {

	private MonsterManager monsterManager;

	public ClientMessageProcessor(MonsterManager monsterManager) {
		this.monsterManager = monsterManager;
	}

	public void process(Object interpretedMsg, MessageSender messageSender) throws InterruptedException {
		if (interpretedMsg instanceof CharacterMotion) {
			CharacterMotion motion = (CharacterMotion) interpretedMsg;
			String targetMonsterId = motion.getTargetId();
			int damage = motion.getDamage();
			//get monster from the monsterMgmtService
			Monster monster = monsterManager.getMonster(targetMonsterId);
			//process client attck
			monster.setFromCharacter(motion.getCharacterId());
			int orgHp = monster.getHp();
			int chdHp = orgHp - damage;
			monster.setHp(chdHp);
			//update monster status at the broadcaster
			monsterManager.updateMonsterStatus(monster);
			//broadcast updated monster status
			messageSender.broadcast(monster);
		} else if (interpretedMsg instanceof ChatMessage) {
			ChatMessage chatMessage = (ChatMessage) interpretedMsg;
			messageSender.broadcast(chatMessage);
		}
	}
}
