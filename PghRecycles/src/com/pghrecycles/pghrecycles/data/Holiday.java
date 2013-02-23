package com.pghrecycles.pghrecycles.data;

import android.text.format.Time;

public class Holiday {
	private String name;
	private String displayName;
	private Time date;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Time getDate() {
		return date;
	}
	public void setDate(Time date) {
		this.date = date;
	}	
}