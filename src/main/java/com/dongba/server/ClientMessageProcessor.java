package com.dongba.server;

import com.dongba.model.CharacterMotion;
import com.dongba.model.ChatMessage;
import com.dongba.model.ClientMessage;
import com.dongba.model.MessageType;
import com.dongba.model.Monster;

public class ClientMessageProcessor {

	private MonsterManager monsterManager;

	public ClientMessageProcessor() {
		this.monsterManager = new MonsterManager();
	}

	public void process(ClientMessage interpretedMsg, MessageSender messageSender) throws InterruptedException {
		MessageType type = interpretedMsg.getType();
		switch (type) {
		case CHARACTERMOTION :
			CharacterMotion motion = (CharacterMotion) interpretedMsg;
			String targetMonsterId = motion.getTargetId();
			int damage = motion.getDamage();
			//get monster from the monsterMgmtService
			Monster monster = monsterManager.getMonster(targetMonsterId);
			//process client attck
			int orgHp = monster.getHp();
			int chdHp = orgHp - damage;
			monster.setHp(chdHp);
			//update monster status at the broadcaster
			monsterManager.updateMonsterStatus(monster);
			//broadcast updated monster status
			messageSender.broadcast(monster);
			break;
		case CHATMESSAGE :
			ChatMessage chatMessage = (ChatMessage) interpretedMsg;
			messageSender.broadcast(chatMessage);
		default:
			break;
		}
	}
}
