package com.dongba.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map.Entry;
import java.util.Set;

import com.dongba.dto.Account;
import com.dongba.dto.CharacterDto;
import com.dongba.dto.Monster;
import com.dongba.dto.Position;

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
	private CharacterManager csm;
	private CharacterDto currCharacter;

	public ClientMessageTransporter(Socket socket, ClientMessageTransportManager messageSender, MonsterManager monsterManager) throws IOException {
		this.socket = socket;
		this.messageTransportManager = messageSender;
		this.clientMessageInterpreter = new ClientMessageInterpreter();
		this.clientMessageProcessor = new ClientMessageProcessor();
		this.monsterManager = monsterManager;
	}

	public void run() {
		try {
			receiveMessage();
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	private void initCharacterSelection(String currCharacterName) throws IOException {
		csm = new CharacterManager(currCharacterName);
		currCharacter = csm.loadCurrCharacter();
		Set<Entry<String, Monster>> monsterList = monsterManager.getMonsterList();
		cpc = new CharacterPositionChecker(VL, VH);
		cpc.computeCharPosition(currCharacter);
		for (Entry<String, Monster> entry : monsterList) {
			Monster monster = entry.getValue();
			Position posMon = monster.getPosition();
			if (cpc.isView(posMon)) {
				System.out.println("send monster : " + monster.getId() + ", hp : " + monster.getHp() + " (" + currCharacterName + ")");
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
					String currCharacterName = account.getCurrCharacterName();
					System.out.println("user " + account.getId() + " is logged in selecting charater with " + currCharacterName);
					initCharacterSelection(currCharacterName);
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
			try {
				disconnect();
			} catch (IOException e1) {
			}
		}
	}

	public void disconnect() throws IOException {
		System.out.println("user " + account.getId() + " is logged out.");
		csm.saveCurrCharacter(currCharacter);
		clientMessageInterpreter = null;
		clientMessageProcessor = null;
		cpc = null;
		csm = null;
		currCharacter = null;
		try {
			messageTransportManager.removeNRelease(this.getName());
		} catch (InterruptedException e1) {
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
