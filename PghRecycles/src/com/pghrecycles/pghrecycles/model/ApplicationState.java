package com.pghrecycles.pghrecycles.model;

import com.pghrecycles.pghrecycles.data.DivisionInfo;
import com.pghrecycles.pghrecycles.data.HolidayList;
import com.pghrecycles.pghrecycles.data.PickupInfo;

public class ApplicationState {
	private static ApplicationState sInstance = new ApplicationState();
	public static ApplicationState getInstance() { return sInstance; }
	
	// private constructor
	private ApplicationState() {}
	
	private int points;
	private PickupInfo pickupInfo;
	private DivisionInfo divisionInfo;
	private HolidayList holidayList;
	private PickupDateModel pickupDateModel;

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public PickupInfo getPickupInfo() {
		return pickupInfo;
	}

	public void setPickupInfo(PickupInfo pickupInfo) {
		this.pickupInfo = pickupInfo;
	}

	public DivisionInfo getDivisionInfo() {
		return divisionInfo;
	}

	public void setDivisionInfo(DivisionInfo divisionInfo) {
		this.divisionInfo = divisionInfo;
	}

	public HolidayList getHolidayList() {
		return holidayList;
	}

	public void setHolidayList(HolidayList holidayList) {
		this.holidayList = holidayList;
	}

	public void setPickupDateModel(PickupDateModel pickupDateModel) {
		this.pickupDateModel = pickupDateModel;
	}

	public PickupDateModel getPickupDateModel() {
		return pickupDateModel;
	}
	
}
