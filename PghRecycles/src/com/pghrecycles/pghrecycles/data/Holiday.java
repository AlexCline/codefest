package com.pghrecycles.pghrecycles.data;

import android.text.format.Time;

public class Holiday {
	private String name;
	private Time date;
	
	public Holiday(String name, Time date) {
		this.name = name;
		this.date = date;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Time getDate() {
		return date;
	}
	public void setDate(Time date) {
		this.date = date;
	}	
}