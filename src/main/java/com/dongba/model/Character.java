package com.dongba.model;

import java.io.IOException;
import java.io.Serializable;

import com.dongba.server.CharacterDAO;

public class Character implements Serializable {
	
	private static final long serialVersionUID = -2834776107888338692L;

	private final String id;
	
	private final String name;
	
	private int hp;
	
	private int str;
	
	private Position pos;
	
	public Character(String id, String name, int hp, int str) {
		this.id = id;
		this.name = name;
		this.hp = hp;
		this.str = str;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getStr() {
		return str;
	}

	public void setStr(int str) {
		this.str = str;
	}

	public Position getPosition() {
		return pos;
	}

	public void setPos(Position pos) {
		this.pos = pos;
	}
	
	public static void main(String[] args) throws IOException {
		Character character = new Character("dongba", "dongba", 100, 20);
		character.setPos(new Position(200, 150));
		CharacterDAO characterDAO = new CharacterDAO();
		characterDAO.save(character);
	}

}
