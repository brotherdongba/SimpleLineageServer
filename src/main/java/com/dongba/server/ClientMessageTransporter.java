package com.dongba.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.dongba.model.ClientMessage;

public class ClientMessageTransporter extends Thread {
	
	private MessageSender messageSender;
	private Socket socket;
	private ClientMessageInterpreter clientMessageInterpreter;
	private ClientMessageProcessor clientMessageProcessor;

	public ClientMessageTransporter(Socket socket, MessageSender messageSender, MonsterManager monsterMgmtService) throws IOException {
		this.socket = socket;
		this.messageSender = messageSender;
		this.clientMessageInterpreter = new ClientMessageInterpreter();
		this.clientMessageProcessor = new ClientMessageProcessor();
	}

	public void run() {
		receiveMessage();
	}

	private void receiveMessage() {
		try {
			while (true) {
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				Object obj = in.readObject();
				System.out.println(obj.toString());
				ClientMessage interpretedMsg = clientMessageInterpreter.interpret(obj);
				try {
					clientMessageProcessor.process(interpretedMsg, messageSender);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			try {
				this.join();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			messageSender.removeClientMessageTransporter(this);
		} catch (ClassNotFoundException e) {
			try {
				this.join();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			messageSender.removeClientMessageTransporter(this);
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
	
	

}
