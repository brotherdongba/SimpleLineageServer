package com.dongba.server;

import java.io.IOException;

import com.dongba.dto.CharacterDto;

public class CharacterManager {
	
	private String currCharacterName;
	
	private CharacterDAO charDAO;

	public CharacterManager(String currCharacterName) {
		this.currCharacterName = currCharacterName;
		charDAO = new CharacterDAO();
	}

	public CharacterDto loadCurrCharacter() throws IOException {
		return charDAO.load(currCharacterName);
	}

	public void saveCurrCharacter(CharacterDto currCharacter) throws IOException {
		charDAO.save(currCharacter);
	}

}
