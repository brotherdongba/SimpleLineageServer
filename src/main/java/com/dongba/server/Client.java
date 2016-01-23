package com.dongba.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
	
	private Socket socket;

	public Client(Socket socket) throws IOException {
		this.socket = socket;
	}

	public void forward(Object obj) {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(obj);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
