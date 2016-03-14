package com.dongba.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map.Entry;
import java.util.Set;

import com.dongba.model.Account;
import com.dongba.model.Character;
import com.dongba.model.Monster;
import com.dongba.model.Position;

public class ClientMessageTransporter extends Thread {
	
	static private final int VL = 150;
	
	static private final int VH = 75;
	
	private ClientMessageTransportManager messageTransportManager;
	private Socket socket;
	private ClientMessageInterpreter clientMessageInterpreter;
	private ClientMessageProcessor clientMessageProcessor;
	private Account account;
	private MonsterManager monsterManager;
	private CharacterPositionChecker cpc;

	public ClientMessageTransporter(Socket socket, ClientMessageTransportManager messageSender, MonsterManager monsterManager) throws IOException {
		this.socket = socket;
		this.messageTransportManager = messageSender;
		this.clientMessageInterpreter = new ClientMessageInterpreter();
		this.clientMessageProcessor = new ClientMessageProcessor();
		this.monsterManager = monsterManager;
		this.cpc = new CharacterPositionChecker(VL, VH);
	}

	public void run() {
		try {
			receiveMessage();
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	private void initClientConnection() {
		Set<Entry<String, Monster>> monsterList = monsterManager.getMonsterList();
		Character character = account.getCharacter();
		cpc.setCharacter(character);
		cpc.computeCharPosition();
		for (Entry<String, Monster> entry : monsterList) {
			Monster monster = entry.getValue();
			Position posMon = monster.getPos();
			if (cpc.isView(posMon)) {
				System.out.println("send monster : " + monster.getId() + ", hp : " + monster.getHp() + " (" + character.getName() + ")");
				sendMessage(monster);
			}
		}
	}

	private void receiveMessage() throws ClassNotFoundException {
		try {
			while (true) {
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				Object obj = in.readObject();
				if (obj instanceof Account) {
					account = (Account) obj;
					//TODO: getting character info with the id
					account.getCharacter().setPos(new Position(200, 150));
					System.out.println("user " + account.getId() + " is logged in.");
					initClientConnection();
				} else {
					Object interpretedMsg = clientMessageInterpreter.interpret(obj);
					try {
						clientMessageProcessor.process(interpretedMsg, messageTransportManager, monsterManager);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			//TODO: setting character info with the id
			System.out.println("user " + account.getId() + " is logged out.");
			try {
				messageTransportManager.removeNRelease(this.getName());
			} catch (InterruptedException e1) {
			}
		}
	}

	public boolean sendMessage(Object obj) {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(obj);
			out.flush();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}
