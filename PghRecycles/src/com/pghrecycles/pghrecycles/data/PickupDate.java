package com.pghrecycles.pghrecycles.data;

import android.text.format.Time;

/**
 * represents a date for a pickup, ie, garbage, recycling, yard debris
 * @author Adam
 *
 */
public class PickupDate {
	private Time date;

	public Time getDate() {
		return date;
	}

	public void setDate(Time date) {
		this.date = date;
	}	
}
