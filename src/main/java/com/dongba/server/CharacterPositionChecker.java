package com.dongba.server;

import com.dongba.dto.CharacterDto;
import com.dongba.dto.Position;

public class CharacterPositionChecker {
	
	private final int vl;
	
	private final int vh;
	
	private Position ll;
	
	private Position ru;

	public CharacterPositionChecker(int vl, int vh) {
		this.vl = vl;
		this.vh = vh;
	}
	
	public void computeCharPosition(CharacterDto currCharacter) {
		Position pos = currCharacter.getPosition();
		computeLL(pos);
		computeRU(pos);
	}
	
	private void computeLL(Position pos) {
		int llx = pos.getX() - vl;
		int lly = pos.getY() - vh;
		ll = new Position();
		ll.setX(llx);
		ll.setY(lly);
	}
	
	private void computeRU(Position pos) {
		int llx = pos.getX() + vl;
		int lly = pos.getY() + vh;
		ru = new Position();
		ru.setX(llx);
		ru.setY(lly);
	}
	
	public boolean isView(Position pos) {
		int posX = pos.getX();
		int posY = pos.getY();
		int llX = ll.getX();
		int ruX = ru.getX();
		if (posX < llX || posX > ruX) {
			return false;
		}
		
		int llY = ll.getY();
		int ruY = ru.getY();
		if (posY < llY || posY > ruY) {
			return false;
		}
		
		return true;
	}

}
