package com.pghrecycles.pghrecycles.data;

public class DivisionInfo {
	private String name;
	private YardDebrisSchedule yardDebrisSchedule;
	private RecyclingSchedule recyclingSchedule;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public YardDebrisSchedule getYardDebrisSchedule() {
		return yardDebrisSchedule;
	}
	public void setYardDebrisSchedule(YardDebrisSchedule yardDebrisSchedule) {
		this.yardDebrisSchedule = yardDebrisSchedule;
	}
	public RecyclingSchedule getRecyclingSchedule() {
		return recyclingSchedule;
	}
	public void setRecyclingSchedule(RecyclingSchedule recyclingSchedule) {
		this.recyclingSchedule = recyclingSchedule;
	}
}
