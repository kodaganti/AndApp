package com.self.shipmenttrack;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cts.processor.datamodel.ShipmentTrackingResponse;
import com.cts.processor.datamodel.TrackingEvent;
import com.google.android.gcm.GCMRegistrar;

public class TrackingDetails extends Activity {

	TextView carrierText;
	TextView numberText;
	ImageButton imageButton;
	DataBaseHelper db;;
	private String URL = "http://192.168.119.4:8080/TrackIt/cts/trackIt/";
	
	//ListView that will hold our items references back to main.xml
    ListView lstTest;
    //Array Adapter that will hold our ArrayList and display the items on the ListView
    TrackEventAdapter arrayAdapter;
     
    //List that will  host our items and allow us to modify that array adapter
    ArrayList<TrackingEvent> alrts=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(db == null){
			db = new DataBaseHelper(this);
		}
		setContentView(R.layout.activity_tracking_details);
		imageButton = (ImageButton)findViewById(R.id.imageButton1);
		imageButton.setVisibility(View.INVISIBLE);
		lstTest= (ListView)findViewById(R.id.lstText);
		alrts = new ArrayList<TrackingEvent>();
//		textViewMessage = (TextView) findViewById(R.id.textView2);
		numberText = (TextView) findViewById(R.id.trackingNumber);
		carrierText  = (TextView) findViewById(R.id.carrierName);
		arrayAdapter = new TrackEventAdapter(TrackingDetails.this, R.layout.listitems,alrts);
		lstTest.setAdapter(arrayAdapter);
		String trackingNumber = getIntent().getStringExtra("trackingNumber");
		String carrierName = getIntent().getStringExtra("carrierName");
		boolean trackingNumberProvided = (trackingNumber != null && trackingNumber
				.length() > 0);
		boolean carrierNameProvided = (carrierName != null && carrierName
				.length() > 0);
		String regId = GCMRegistrar.getRegistrationId(this);
		if(regId != null && !"".equals(regId)){
			if (trackingNumberProvided && carrierNameProvided) {
				new RequestTask().execute(URL+regId+"/" + trackingNumber);
			} else {
				new RequestTask().execute(URL+regId+"/" + trackingNumber);
			}
		}else{
			if (trackingNumberProvided && carrierNameProvided) {
				new RequestTask().execute(URL + trackingNumber);
			} else {
				new RequestTask().execute(URL + trackingNumber);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_tracking_details, menu);
		return true;
	}

	public void backToHome(View V) {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
	}
	
	public void viewMap(View v){
//		GKMapSamp gk =new GKMapSamp();
		Intent intent = new Intent(getApplicationContext(), GKMapSamp.class);
		intent.putExtra("MyClass", alrts);  
		startActivity(intent);
	}

	class RequestTask extends AsyncTask<String, String, String> {
		private ProgressDialog dialog = new ProgressDialog(TrackingDetails.this);

		@Override
		protected void onPreExecute() {
			// TODO i18n

			dialog.setMessage("Please wait..");
			dialog.show();
		}

		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				response = httpclient.execute(new HttpGet(uri[0]));
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			ShipmentTrackingResponse response = ShipmentTrackingHelper.getResponse(result);
			if(response != null && response.getTrackingEvents() != null){
				TrackingInputVo trkVo = new TrackingInputVo();
				trkVo.setCarrier(response.getCarrier());
				trkVo.setTrackingNumber(response.getTrackingNumber());
				db.addTrackingNumber(trkVo);
			}
			alrts.addAll(response.getTrackingEvents());
			arrayAdapter.notifyDataSetChanged();
//			arrayAdapter.set
			// Do anything with response..
			/*textViewMessage.setText("Tracking Number : " + result
					+ "  -- Carrier Name : " + result);*/
			carrierText.setText(response.getCarrier());
			numberText.setText(response.getTrackingNumber());
			
			imageButton.setVisibility(View.VISIBLE);
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}
}