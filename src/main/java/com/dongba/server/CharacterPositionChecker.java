package com.dongba.server;

import com.dongba.model.Position;

public class CharacterPositionChecker {
	
	private final int vl;
	
	private final int vh;
	
	private Position ll;
	
	private Position ru;
	
	public CharacterPositionChecker(int vl, int vh) {
		this.vl = vl;
		this.vh = vh;
	}
	
	public void computeLL(Position pos) {
		int llx = pos.getX() - vl;
		int lly = pos.getY() - vh;
		ll.setX(llx);
		ll.setY(lly);
	}
	
	public void computeRU(Position pos) {
		int llx = pos.getX() + vl;
		int lly = pos.getY() + vh;
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
