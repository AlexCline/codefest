package com.pghrecycles.pghrecycles.data;

import com.pghrecycles.pghrecycles.data.DivisionInfo.Division;


public class PickupInfo {
	private int leftLow;
	private int leftHigh;
	private int rightLow;
	private int rightHigh;
	private int zip;
	private String hood;
	private Division division;
	private String streetBase;
	private String street;
	private int year;
	private int day; // using Time.DAY consts
	
	public PickupInfo() {
	}
	
	public PickupInfo(int leftLow, int leftHigh, int rightLow, int rightHigh,
			int zip, String hood, Division division, String streetBase,
			String street, int year, int day) {
		this.leftLow = leftLow;
		this.leftHigh = leftHigh;
		this.rightLow = rightLow;
		this.rightHigh = rightHigh;
		this.zip = zip;
		this.hood = hood;
		this.division = division;
		this.streetBase = streetBase;
		this.street = street;
		this.year = year;
		this.day = day;
	}
	public int getLeftLow() {
		return leftLow;
	}
	public void setLeftLow(int leftLow) {
		this.leftLow = leftLow;
	}
	public int getLeftHigh() {
		return leftHigh;
	}
	public void setLeftHigh(int leftHigh) {
		this.leftHigh = leftHigh;
	}
	public int getRightLow() {
		return rightLow;
	}
	public void setRightLow(int rightLow) {
		this.rightLow = rightLow;
	}
	public int getRightHigh() {
		return rightHigh;
	}
	public void setRightHigh(int rightHigh) {
		this.rightHigh = rightHigh;
	}
	public int getZip() {
		return zip;
	}
	public void setZip(int zip) {
		this.zip = zip;
	}
	public String getHood() {
		return hood;
	}
	public void setHood(String hood) {
		this.hood = hood;
	}
	public Division getDivision() {
		return division;
	}
	public void setDivision(Division division) {
		this.division = division;
	}
	public String getStreetBase() {
		return streetBase;
	}
	public void setStreetBase(String streetBase) {
		this.streetBase = streetBase;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
}
