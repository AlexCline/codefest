package com.pghrecycles.pghrecycles.data.providers;

import com.pghrecycles.pghrecycles.data.Points;


public class PointsProvider {
	Points points = new Points();

	public int getPoints(){
		return points.getPoints();
	}
	
	public int addPoints(int pts){
		return points.addPoints(pts);
	}
	
	public void setPoints(int pts){
		points.setPoints(pts);
	}
}
