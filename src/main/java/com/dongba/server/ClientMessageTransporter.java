package com.dongba.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.dongba.model.Account;

public class ClientMessageTransporter extends Thread {
	
	private ClientMessageTransportManager messageTransportManager;
	private Socket socket;
	private ClientMessageInterpreter clientMessageInterpreter;
	private ClientMessageProcessor clientMessageProcessor;
	private Account account;

	public ClientMessageTransporter(Socket socket, ClientMessageTransportManager messageSender, MonsterManager monsterManager) throws IOException {
		this.socket = socket;
		this.messageTransportManager = messageSender;
		this.clientMessageInterpreter = new ClientMessageInterpreter();
		this.clientMessageProcessor = new ClientMessageProcessor(monsterManager);
	}

	public void run() {
		try {
			receiveMessage();
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}

	private void receiveMessage() throws ClassNotFoundException {
		try {
			while (true) {
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				Object obj = in.readObject();
				if (obj instanceof Account) {
					this.account = (Account) obj;
					System.out.println("user " + account.getId() + " is logged in.");
				} else {
					Object interpretedMsg = clientMessageInterpreter.interpret(obj);
					try {
						clientMessageProcessor.process(interpretedMsg, messageTransportManager);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
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
