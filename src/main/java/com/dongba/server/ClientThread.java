package com.dongba.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.dongba.model.CharacterMotion;
import com.dongba.model.Monster;

public class ClientThread extends Thread {
	
	private Socket socket;
	private MessageBroadcaster broadcaster;

	public ClientThread(Socket socket, MessageBroadcaster broadcaster) throws IOException {
		this.socket = socket;
		this.broadcaster = broadcaster;
	}

	public void run() {
		while (true) {
			try {
				if (socket.isClosed()) {
					continue;
				}
				if (socket.isConnected() && socket.isBound()) {
					ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
					Object obj = in.readObject();

					if (obj instanceof CharacterMotion) {
						CharacterMotion motion = (CharacterMotion)obj;
						String targetMonsterId = motion.getTargetId();
						int damage = motion.getDamage();
						//get monster from broadcaster
						Monster monster = broadcaster.getMonster(targetMonsterId);
						//process client attck
						int orgHp = monster.getHp();
						int chdHp = orgHp - damage;
						monster.setHp(chdHp);
						//update monster status at the broadcaster
						broadcaster.updateMonsterStatus(monster);
						//broadcast updated monster status
						broadcaster.broadcast(monster);
						continue;
					}
					broadcaster.broadcast(obj);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	
	

}
