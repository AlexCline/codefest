package com.pghrecycles.pghrecycles.data.providers;

import com.pghrecycles.pghrecycles.model.ApplicationState;

public class PointsProvider {
	ApplicationState mApplicationState = ApplicationState.getInstance();

	public int getPoints(){
		return mApplicationState.getPoints();
	}
	
	public int addPoints(int pts){
		return mApplicationState.addPoints(pts);
	}
	
	public void setPoints(int pts){
		mApplicationState.setPoints(pts);
	}
}
