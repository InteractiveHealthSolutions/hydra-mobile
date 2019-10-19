package com.ihsinformatics.dynamicformsgenerator.data.datatypes;

/**
 * GPSWidget holds the coordinates in this class
 * 
 * 
 * */
public class GPSCoordinate {

	private double longitude;
	private double latitude;

	public GPSCoordinate(double longitude, double latitude) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

}
