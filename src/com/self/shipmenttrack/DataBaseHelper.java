package com.self.shipmenttrack;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "gk";

	// Contacts table name
	private static final String TABLE_TRACK_IT = "trackit";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String TRACKING_NUMBER = "tracking_number";
	private static final String CARRIER = "carrier";
	private static final int MAX_NUMBERS = 5;

	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TRACK_IT + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + TRACKING_NUMBER + " TEXT,"
				+ CARRIER + " TEXT" + ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACK_IT);

		// Create tables again
		onCreate(db);
	}

	public void addTrackingNumber(TrackingInputVo trackingInput) {
		if (trackingInput == null) {
			return;
		}

		if (trackingInput.getTrackingNumber() != null
				&& trackingInput.getTrackingNumber().trim() != null) {

			List<TrackingInputVo> trackingList = getAllTrackingNumbers();

			boolean isTrackingNumberExist = isTrackignNumberExist(
					trackingInput, trackingList);

			if (!isTrackingNumberExist) {
				SQLiteDatabase db = this.getWritableDatabase();
				ContentValues values = new ContentValues();

				values.put(TRACKING_NUMBER, trackingInput.getTrackingNumber());
				values.put(CARRIER, trackingInput.getCarrier());

				// Inserting Row
				db.insert(TABLE_TRACK_IT, null, values);
				db.close(); // Closing database connection
			}
		}
	}

	private boolean isTrackignNumberExist(TrackingInputVo trackingInput,
			List<TrackingInputVo> trackingList) {
		if (trackingList == null || trackingInput == null) {
			return false;
		}

		if (trackingList.size() > 0) {
			for (TrackingInputVo trackingInputVo : trackingList) {
				if (trackingInputVo == null) {
					continue;
				}
				if (trackingInputVo.getTrackingNumber().equalsIgnoreCase(
						trackingInput.getTrackingNumber())) {
					return true;
				}

			}
		}
		return false;
	}

	// Getting All Contacts
	public List<TrackingInputVo> getAllTrackingNumbers() {
		List<TrackingInputVo> trackingVoList = new ArrayList<TrackingInputVo>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_TRACK_IT;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				TrackingInputVo trackingVo = new TrackingInputVo();
				trackingVo.setId(Integer.parseInt(cursor.getString(0)));
				trackingVo.setTrackingNumber(cursor.getString(1));
				trackingVo.setCarrier(cursor.getString(2));
				// Adding trackingVo to list
				trackingVoList.add(trackingVo);
			} while (cursor.moveToNext());
		}

		// We will maintain only few tracking numbers in the DB. So delete last
		// record.
		if (trackingVoList.size() > MAX_NUMBERS) {
			deleteContact(trackingVoList.get(MAX_NUMBERS - 1));
		}
		// return list
		return trackingVoList;
	}

	// Deleting single contact
	public void deleteContact(TrackingInputVo trackingInputVo) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TRACK_IT, KEY_ID + " = ?",
				new String[] { String.valueOf(trackingInputVo.getId()) });
		db.close();
	}
}