package com.dongba.server;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;

import com.dongba.dto.CharacterDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CharacterDAO {
	
	private static final String CHARACTER_MANAGER = "characterManager";

	private final String projectRoot = System.getProperty("user.dir");
	
	private Gson gson;
	
	public CharacterDAO() {
		GsonBuilder builder = new GsonBuilder();
		gson = builder.create();
		
	}

	public CharacterDto load(String currCharacterName) throws IOException {
		Path currCharacterIdPath = Paths.get(projectRoot, CHARACTER_MANAGER, currCharacterName);
		String currCharacterString = FileUtils.readFileToString(currCharacterIdPath.toFile());
		return gson.fromJson(currCharacterString, CharacterDto.class);
	}

	public void save(CharacterDto currCharacter) throws IOException {
		Path currCharacterNamePath = Paths.get(projectRoot, CHARACTER_MANAGER, currCharacter.getName());
		String currCharacterString = gson.toJson(currCharacter);
		FileUtils.writeStringToFile(currCharacterNamePath.toFile(), currCharacterString, false);
	}

}
