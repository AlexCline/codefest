package com.pghrecycles.pghrecycles.data;

public class LocationInfo {
	private int addressNum;
	private String street;
	private String streetBase;
	private int zip;
	
	
	public LocationInfo() {
		this.addressNum = -1;
		this.street = "";
		this.streetBase = "";
		this.zip = -1;
	}
	
	public LocationInfo(int addressNum, String street, String streetBase,
			int zip) {
		this.addressNum = addressNum;
		this.street = street;
		this.streetBase = streetBase;
		this.zip = zip;
	}
	public int getAddressNum() {
		return addressNum;
	}
	public void setAddressNum(int addressNum) {
		this.addressNum = addressNum;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getStreetBase() {
		return streetBase;
	}
	public void setStreetBase(String streetBase) {
		this.streetBase = streetBase;
	}
	public int getZip() {
		return zip;
	}
	public void setZip(int zip) {
		this.zip = zip;
	}
	
}
