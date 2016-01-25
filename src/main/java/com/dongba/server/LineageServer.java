package com.dongba.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import com.dongba.model.Monster;

public class LineageServer extends Thread {
	
	private MessageBroadcaster broadcaster;
	
	public LineageServer() {
		broadcaster = new MessageBroadcaster();
	}
	
	@Override
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(1500);
			while (true) {
				System.out.println("Waiting client connection...");
				Socket socket = serverSocket.accept();
				System.out.println("established client connection : " + socket.getInetAddress());
				broadcaster.addClient(new Client(socket));
				ClientThread client = new ClientThread(socket, broadcaster);
				client.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addNForwardMonster(Monster monster) {
		broadcaster.addNForwardMonster(monster);
	}
	
	public static void main(String[] args) {
		LineageServer server = new LineageServer();
		server.start();
		
		while (true) {
			Scanner in = new Scanner(System.in);
			String monsterSpec = in.nextLine();
			
			String[] split = monsterSpec.split(" ");
			// "oak#1 oak 1000"
			Monster monster = new Monster(split[0], split[1], Integer.parseInt(split[2]));
			server.addNForwardMonster(monster);
		}
	}

}
