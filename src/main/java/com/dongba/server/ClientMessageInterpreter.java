package com.dongba.server;

import com.dongba.model.CharacterMotion;
import com.dongba.model.ChatMessage;
import com.dongba.model.ClientMessage;
import com.dongba.model.Monster;

public class ClientMessageInterpreter {


	public ClientMessageInterpreter() {
	}

	public ClientMessage interpret(Object obj) {
		if (obj instanceof CharacterMotion) {
			return (CharacterMotion)obj;
		} else if (obj instanceof ChatMessage) {
			return (ChatMessage) obj;
		} else if (obj instanceof Monster) {
			return (Monster) obj;
		}
		return null;
	}

}
