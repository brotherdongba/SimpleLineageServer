package com.dongba.server;

import java.io.IOException;

import com.dongba.model.Character;

public class CharacterManager {
	
	private String currCharacterName;
	
	private CharacterDAO charDAO;

	public CharacterManager(String currCharacterName) {
		this.currCharacterName = currCharacterName;
		charDAO = new CharacterDAO();
	}

	public Character loadCurrCharacter() throws IOException {
		return charDAO.load(currCharacterName);
	}

	public void saveCurrCharacter(Character currCharacter) throws IOException {
		charDAO.save(currCharacter);
	}

}
