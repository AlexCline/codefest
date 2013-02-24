package com.pghrecycles.pghrecycles.data;

public class Points {
	private int points = 4900;
	
	public int getPoints(){
		return this.points;
	}
	
	public void setPoints(int points){
		this.points = points;
	}
	
	public int addPoints(int points){
		this.points += points;
		return this.points;
	}
}
