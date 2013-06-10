package com.self.shipmenttrack;

public class TrackingInputVo {

	private long id;
	private String trackingNumber;
	private String carrier;

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}