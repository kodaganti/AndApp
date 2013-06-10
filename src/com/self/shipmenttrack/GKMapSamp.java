package com.self.shipmenttrack;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;

import com.cts.processor.datamodel.Address;
import com.cts.processor.datamodel.TrackingEvent;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class GKMapSamp extends Activity {

	private final LatLng LOCATION_BURNABY = new LatLng(49.27645, -122.917587);
	private final LatLng LOCATION_SURRREY = new LatLng(49.187500, -122.849000);

	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapview);
		// map = (MapView)findViewById(R.id.map);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		// map.addMarker(new
		// MarkerOptions().position(LOCATION_SURRREY).title("Find me here!"));

		List<TrackingEvent> events = (ArrayList<TrackingEvent>) getIntent()
				.getSerializableExtra("MyClass");
		PolylineOptions polyLines = new PolylineOptions();
		List<Double> latContain = new ArrayList<Double>();
		map.clear();
		if (events != null) {

			for (TrackingEvent event : events) {
				if (event == null) {
					continue;
				}
				Address address = event.getAddress();
				if (address == null) {
					continue;
				}
				double lat = address.getLatitude();
				double lon = address.getLongitude();
				if (lat == 0.0 || lon == 0.0) {
					continue;
				}
				if (latContain.contains(Double.valueOf(lat))) {
					continue;
				}
				latContain.add(Double.valueOf(lat));
				polyLines.add(new LatLng(lat, lon));
				map.addMarker(new MarkerOptions()
						.position(new LatLng(lat, lon)).title(
								event.getEventDescription() + ""));
			}

			CameraUpdate center = CameraUpdateFactory.newLatLng(findMedian(events));
			CameraUpdate zoom = CameraUpdateFactory.zoomTo(3);

			map.moveCamera(center);
			map.animateCamera(zoom);
			polyLines.width(10);
			polyLines.color(0x880000ff);
			// polyLines.g
			map.addPolyline(polyLines);
		}
	}

	private LatLng findMedian(List<TrackingEvent> events){
		
		LatLng defaultLat = new LatLng(
				39.50, -98.5795);
		/*Double [] array_x = new Double [50];
		Double [] array_y = new Double [50];
		Double [] array_z = new Double [50];*/
		double x_cord = 0.0;
		double y_cord = 0.0;
		double z_cord = 0.0;
		int index = 0;
		if(events!= null){
			for(TrackingEvent event : events){
				if(event == null){
					continue;
				}
				Address add = event.getAddress();
				if(add == null){
					continue;
				}
				double lat = add.getLatitude();
				double lon = add.getLongitude();
				if(lat == 0.0 || lon == 0.0){
					continue;
				}
				/*x_n = cos(lat_n)*cos(lon_n)
				y_n = cos(lat_n)*sin(lon_n)
				z_n = sin(lat_n)*/
				
				x_cord  = x_cord + (Math.cos(lat)) *(Math.cos(lon));
				y_cord = y_cord + (Math.cos(lat)) *(Math.sin(lon));
				z_cord = z_cord + (Math.sin(lat));
				index++;
			}
			
			x_cord = x_cord/index;
			y_cord = y_cord/index;
			z_cord = z_cord/index;
			
			double radius = Math.sqrt(x_cord * x_cord + y_cord * y_cord + z_cord * z_cord);
			
			/*r = sqrt(x^2 + y^2 + z^2)
			lat = arcsin(z/r)
			lon = arctan(y/x)*/
			
			defaultLat = new LatLng(Math.asin(z_cord/radius), Math.atan(y_cord)/x_cord);
			
		}
		return defaultLat;
	}
}