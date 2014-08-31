package com.arnavsoft.quickdial;

import com.arnavsoft.quickdial.data.CallDetail;
import com.arnavsoft.quickdial.data.DBHelper;
import android.app.Activity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class PhoneStateMonitor extends PhoneStateListener {
	private Activity activity;
	private QuickDialerApplication quickDialerApplication;

	public PhoneStateMonitor(Activity activity) {
		super();

		this.activity = activity;
		quickDialerApplication = (QuickDialerApplication) activity
				.getApplication();
	}

	public void onCallStateChanged(int state, String incomingNumber) {
		super.onCallStateChanged(state, incomingNumber);

		switch (state) {
		case TelephonyManager.CALL_STATE_IDLE:
			if (quickDialerApplication.isCallInitiated()) {
				long currentTime = System.currentTimeMillis();
				CallDetail callDetail = quickDialerApplication.getCallDetail();
				callDetail.setDuration(currentTime - callDetail.getStartTime());

				quickDialerApplication.setCallInitiated(false);

				DBHelper dbHelper = new DBHelper(activity);
				dbHelper.insertCallDetail(callDetail);
				dbHelper.closeDB();
			}
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			quickDialerApplication.setCallInitiated(true);
			break;
		}
	}
}