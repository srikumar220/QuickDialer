package com.arnavsoft.quickdial.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper {

	private static final String DB_NAME = "quickdialer";
	private static final String DB_TABLE = "call_history";
	private static final int DB_VERSION = 1;

	private static final String[] columns = new String[] { "id",
			"contact_name", "phone_type", "number", "start_time", "duration" };

	private DBOpenHelper dbOpenHelper;
	private SQLiteDatabase db;

	public DBHelper(Context context) {
		this.dbOpenHelper = new DBOpenHelper(context);
		if (db == null) {
			db = this.dbOpenHelper.getWritableDatabase();
		}
	}

	public void closeDB() {
		if (db != null) {
			db.close();
			db = null;
		}
	}

	public void insertCallDetail(CallDetail callDetail) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("contact_name", callDetail.getContactName());
		contentValues.put("phone_type", callDetail.getPhoneType());
		contentValues.put("number", callDetail.getNumber());
		contentValues.put("start_time", callDetail.getStartTime());
		contentValues.put("duration", callDetail.getDuration());
		db.insert(DB_TABLE, null, contentValues);
	}

	public void deleteCallDetail(CallDetail callDetail) {
		db.delete(DBHelper.DB_TABLE, "id=" + callDetail.getId(), null);
	}

	public ArrayList<CallDetail> getCallDetails() {
		ArrayList<CallDetail> callDetails = new ArrayList<CallDetail>();

		Cursor cursor = null;

		try {
			cursor = db.query(DB_TABLE, columns, null, null, null, null,
					"id DESC");
			while (cursor.moveToNext()) {
				CallDetail callDetail = new CallDetail();
				callDetail.setId(cursor.getInt(0));
				callDetail.setContactName(cursor.getString(1));
				callDetail.setPhoneType(cursor.getString(2));
				callDetail.setNumber(cursor.getString(3));
				callDetail.setStartTime(cursor.getLong(4));
				callDetail.setDuration(cursor.getInt(5));
				callDetails.add(callDetail);
			}

		} finally {
			if (cursor != null) {
				cursor.close();
				cursor = null;
			}
		}

		return callDetails;
	}

	static class DBOpenHelper extends SQLiteOpenHelper {

		DBOpenHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE "
					+ DB_TABLE
					+ " (id INTEGER PRIMARY KEY, contact_name TEXT, phone_type TEXT, number TEXT, start_time INTEGER, duration INTEGER);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// nothing to do for now.
		}

		@Override
		public void onOpen(SQLiteDatabase db) {
			super.onOpen(db);
		}
	}
}
