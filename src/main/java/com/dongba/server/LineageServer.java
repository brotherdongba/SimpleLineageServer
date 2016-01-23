package com.dongba.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
	
	public static void main(String[] args) {
		LineageServer server = new LineageServer();
		server.start();
	}

}
