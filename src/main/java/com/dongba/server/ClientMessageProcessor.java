package com.dongba.server;

import com.dongba.dto.CharacterMotion;
import com.dongba.dto.ChatMessage;
import com.dongba.dto.Monster;

public class ClientMessageProcessor {


	public ClientMessageProcessor() {
	}

	public void process(Object interpretedMsg, ClientMessageTransportManager messageTransportManager, MonsterManager monsterManager) throws InterruptedException {
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
			System.out.println("update monster status : " + monster.getId() + ", hp : " + monster.getHp() + ", from : " + motion.getCharacterId());
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
