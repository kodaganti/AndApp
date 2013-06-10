package com.self.shipmenttrack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cts.processor.datamodel.Address;
import com.cts.processor.datamodel.ShipmentStatus;
import com.cts.processor.datamodel.ShipmentTrackingResponse;
import com.cts.processor.datamodel.TrackingEvent;

public class ShipmentTrackingHelper {

	public static ShipmentTrackingResponse getResponse(String responseString) {
		ShipmentTrackingResponse response = new ShipmentTrackingResponse();
		if (responseString == null) {
			return response;
		}
		try {
			JSONObject jsonObject = new JSONObject(responseString);
			response.setCarrier(jsonObject.getString("carrier"));
			response.setTrackingNumber(jsonObject.getString("trackingNumber"));
			String deliveryDate = jsonObject.getString("deliveryDate");
			if (deliveryDate != null) {
				response.setDeliveryDate(formateDate(deliveryDate));
			}
			JSONArray jArray = jsonObject.getJSONArray("trackingEvents");
			response.setTrackingEvents(getTrackingEvents(jArray));
		} catch (JSONException e) {
		}
		return response;
	}

	private static List<TrackingEvent> getTrackingEvents(JSONArray jArray)
			throws JSONException {
		List<TrackingEvent> events = new ArrayList<TrackingEvent>();
		for (int i = 0; i < jArray.length(); i++) {
			TrackingEvent event = new TrackingEvent();
			JSONObject oneObject = jArray.getJSONObject(i);
			try {
				event.setAddress(getAddress(oneObject.getString("address")));
				String eventDate = oneObject.getString("eventDate");
				if (eventDate != null) {
					event.setEventDate(formateDate(eventDate));
				}
				event.setEventDescription(oneObject.getString("eventDescription"));
				event.setStatus(ShipmentStatus.valueOf(ShipmentStatus.class,
						oneObject.getString("status")));
				event.setStatusExceptionDescription(oneObject
						.getString("statusExceptionDescription"));
			} catch (Exception e) {

			}
			events.add(event);

		}
		return events;
	}

	private static Address getAddress(String addressString) {
		Address address = new Address();

		if (addressString == null) {
			return address;
		}
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(addressString);

			try {
				address.setCity(jsonObject.getString("city"));
			} catch (Exception exception) {
			}
			try {
				address.setCountryCode(jsonObject.getString("countryCode"));
			} catch (Exception exception) {
			}

			try {
				address.setLatitude(jsonObject.getDouble("latitude"));
			} catch (Exception exception) {
			}

			try {
				address.setLongitude(jsonObject.getDouble("longitude"));
			} catch (Exception exception) {
			}

			try {
				address.setPostalCode(jsonObject.getString("countryCode"));
			} catch (Exception exception) {
			}

			try {
				address.setResidential(jsonObject.getBoolean("residential"));
				address.setStateOrProvinceCode(jsonObject
						.getString("stateOrProvinceCode"));
				address.setUrbanizationCode(jsonObject
						.getString("urbanizationCode"));
			} catch (Exception exception) {
			}

		} catch (JSONException e) {
		}
		return address;
	}

	private static Date formateDate(String dtStart) {
		SimpleDateFormat format = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss'Z'");
		Date date = new Date();
		try {
			date = format.parse(dtStart);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return date;
	}
}