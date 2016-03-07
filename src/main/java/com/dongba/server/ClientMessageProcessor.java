package com.dongba.server;

import com.dongba.model.CharacterMotion;
import com.dongba.model.ChatMessage;
import com.dongba.model.Monster;

public class ClientMessageProcessor {

	private MonsterManager monsterManager;

	public ClientMessageProcessor(MonsterManager monsterManager) {
		this.monsterManager = monsterManager;
	}

	public void process(Object interpretedMsg, ClientMessageTransportManager messageTransportManager) throws InterruptedException {
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
			//send updated monster status
			send(messageTransportManager, monster);
		} else if (interpretedMsg instanceof ChatMessage) {
			//send updated monster status
			ChatMessage chatMessage = (ChatMessage) interpretedMsg;
			send(messageTransportManager, chatMessage);
		}
	}

	private void send(ClientMessageTransportManager messageTransportManager, Object obj) {
		ClientMessageBroadcastSender cmbs = new ClientMessageBroadcastSender();
		cmbs.send(messageTransportManager, obj);
	}
}
