package com.pghrecycles.pghrecycles.data;

public class Points {
	private long points;
	
	public long getPoints(){
		return this.points;
	}
	
	public void setPoints(long points){
		this.points = points;
	}
	
	public long addPoints(long points){
		this.points += points;
		return this.points;
	}
}
