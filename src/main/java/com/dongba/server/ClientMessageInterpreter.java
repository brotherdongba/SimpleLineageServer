package com.dongba.server;

import com.dongba.dto.CharacterMotion;
import com.dongba.dto.ChatMessage;
import com.dongba.dto.Monster;

public class ClientMessageInterpreter {


	public ClientMessageInterpreter() {
	}

	public Object interpret(Object obj) {
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
