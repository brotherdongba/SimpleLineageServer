package com.dongba.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import com.dongba.dto.Monster;
import com.dongba.dto.Position;

public class LineageServer extends Thread {
	
	private ClientMessageTransportManager messageTransportManager;
	
	private MonsterManager monsterManager;
	
	public LineageServer() {
		messageTransportManager = new ClientMessageTransportManager();
		monsterManager = new MonsterManager();
	}
	
	public ClientMessageTransportManager getMessageSender() {
		return messageTransportManager;
	}
	
	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(1500);
			while (true) {
				System.out.println("Waiting client connection...");
				Socket socket = serverSocket.accept();
				System.out.println("established client connection : " + socket.getInetAddress());
				ClientMessageTransporter clientMessageTransporter = new ClientMessageTransporter(socket, messageTransportManager, monsterManager);
				messageTransportManager.addClientMessageTransporter(clientMessageTransporter);
				clientMessageTransporter.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addNForwardMonster(Monster monster) throws InterruptedException {
		monsterManager.addMonster(monster);
		ClientMessageBroadcastSender cmbs = new ClientMessageBroadcastSender();
		cmbs.send(messageTransportManager, monster);
	}
	
	public static void main(String[] args) throws InterruptedException {
		LineageServer server = new LineageServer();
		server.start();
		while (true) {
			Scanner in = new Scanner(System.in);
			String monsterSpec = in.nextLine();
			
			String[] split = monsterSpec.split(" ");
			// oak#1 oak 1000 100 200(id, name, hp, x, y)
			Monster monster = new Monster(null, split[0], split[1], Integer.parseInt(split[2]));
			int x = Integer.parseInt(split[3]);
			int y = Integer.parseInt(split[4]);
			Position pos = new Position(x, y);
			monster.setPos(pos);
			server.addNForwardMonster(monster);
		}
	}

}
