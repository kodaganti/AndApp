package com.self.shipmenttrack;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends Activity {

	private EditText trackingNUmberMessage;
	private EditText carrierNameMessage;
	private TextView errorViewMessage;
	private static final String TAG = "MainActivity"; 
//	private MixpanelAPI mixAPi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		// Verify that the device supports GCM
		
		com.google.android.gcm.GCMRegistrar.checkDevice(this);
		 
		// Verify the application manifest contents - remove when ready to publish
		GCMRegistrar.checkManifest(this);
		 
		// Check whether the device is already registered
		String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
		 
			final String googleProjectNumber = "827171612957";
		 
		    // Not registered yet, so register the device with GCM
		    GCMRegistrar.register(this, googleProjectNumber); 
		} else {
		    Log.v(TAG, "Already registered");
		}
		
		super.onCreate(savedInstanceState);
//		mixAPi.getInstance(this, "AIzaSyB_WGzEPoMqQFtdfMXU_ATna9odChYCfu8");
		setContentView(R.layout.activity_main);
		errorViewMessage = (TextView) findViewById(R.id.error_message_home);
		trackingNUmberMessage = (EditText) findViewById(R.id.trackingText);
		trackingNUmberMessage.setText(R.string.empty_string);
		carrierNameMessage = (EditText) findViewById(R.id.carrierName);
		carrierNameMessage.setText(R.string.empty_string);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void getTrackingDetails(View V) {
		Intent intent = new Intent(getApplicationContext(),
				TrackingDetails.class);
		errorViewMessage.setText(R.string.empty_string);
		String trackingNumber = trackingNUmberMessage.getText().toString();
		String carrierName = carrierNameMessage.getText().toString();

		if (trackingNumber == null
				|| (trackingNumber != null && trackingNumber.length() == 0)) {
			errorViewMessage.setText(R.string.tracking_number_required_error);
			errorViewMessage.setTextColor(Color.RED);
			return;
		}

		intent.putExtra("trackingNumber", trackingNumber); // optional
		intent.putExtra("carrierName", carrierName);

		startActivity(intent);
	}
	
	@Override
	protected void onDestroy(){
//		mixAPi.flush();
		super.onDestroy();
	}
	
	public void historyView(View V) {
		Intent intent = new Intent(getApplicationContext(), TrackingHistoryActivity.class);
		startActivity(intent);
	}
}