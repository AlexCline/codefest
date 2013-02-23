package com.pghrecycles.pghrecycles.data;


public class PickupInfo {
	private int leftLow;
	private int leftHigh;
	private int rightLow;
	private int rightHigh;
	private int zip;
	private String hood;
	private String division;
	private String streetBase;
	private String street;
	private int year;
	private int day; // using Time.DAY consts
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
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
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
