package com.self.shipmenttrack;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class TrackingHistoryActivity extends Activity {
	DataBaseHelper db;
	ListView historyList;
	TextView topMessage;
	ImageButton goBackButton;
	TrackingHistoryAdapter historyAdap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tracking_history);
		if (db == null) {
			db = new DataBaseHelper(this);
		}
		List<TrackingInputVo> trackingVOs = db.getAllTrackingNumbers();
		topMessage = (TextView) findViewById(R.id.historyText);
		historyList = (ListView) findViewById(R.id.historyList);
		if (trackingVOs != null && trackingVOs.size() > 0) {
			topMessage.setText("Here is tracking nubmer search history.");
			historyList.setVisibility(View.VISIBLE);
			historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			    @Override
			    public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
			        onListItemClick(v,pos,id);
			    }
			});

			historyAdap = new TrackingHistoryAdapter(
					TrackingHistoryActivity.this, R.layout.listitems,
					trackingVOs);
			historyList.setAdapter(historyAdap);
		} else {
			topMessage.setText("There is no history!");
			historyList.setVisibility(View.INVISIBLE);
		}
	}
	protected void onListItemClick(View v, int pos, long id) {
		List<TrackingInputVo> trackNumbers = db.getAllTrackingNumbers();
		TrackingInputVo trkNumb = trackNumbers.get(pos);
		Intent intent = new Intent(getApplicationContext(),
				TrackingDetails.class);
		intent.putExtra("trackingNumber", trkNumb.getTrackingNumber()); // optional
		intent.putExtra("carrierName", trkNumb.getCarrier());

		startActivity(intent);
	}

	public void backToHome(View V) {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
	}
}