package com.self.shipmenttrack;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TrackingHistoryAdapter extends ArrayAdapter<TrackingInputVo> {

	int resource;
	String response;
	Context context;

	public TrackingHistoryAdapter(Context context, int resource,
			List<TrackingInputVo> items) {
		super(context, resource, items);
		this.resource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout alertView;
		// Get the current alert object
		TrackingInputVo al = getItem(position);

		// Inflate the view
		if (convertView == null) {
			alertView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi;
			vi = (LayoutInflater) getContext().getSystemService(inflater);
			vi.inflate(resource, alertView, true);
		} else {
			alertView = (LinearLayout) convertView;
		}
		// Get the text boxes from the listitem.xml file
		TextView alertText = (TextView) alertView
				.findViewById(R.id.txtAlertText);
		TextView alertDate = (TextView) alertView
				.findViewById(R.id.txtAlertDate);

		// Assign the appropriate data from our alert object above
		alertText.setText("Tracking # 	: " + al.getTrackingNumber());
		alertDate.setText("Carrier 		: " + al.getCarrier() + "");
		return alertView;
	}
}