package com.cts.processor.datamodel;

import java.util.Date;

public class TrackingEvent implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6681581131539931572L;
	
	private ShipmentStatus status;
	private Address address;
	private String eventDescription;
	private String statusExceptionDescription;
	private Date eventDate;

	public String getStatusExceptionDescription() {
		return statusExceptionDescription;
	}

	public void setStatusExceptionDescription(String statusExceptionDescription) {
		this.statusExceptionDescription = statusExceptionDescription;
	}

	public ShipmentStatus getStatus() {
		return status;
	}

	public void setStatus(ShipmentStatus status) {
		this.status = status;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
}