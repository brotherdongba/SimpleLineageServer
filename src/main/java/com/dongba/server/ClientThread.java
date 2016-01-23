package com.dongba.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

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
				if (socket.isConnected() && socket.isBound()) {
					ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
					Object obj = in.readObject();
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
