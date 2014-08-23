package com.arnavsoft.quickdial;

import com.arnavsoft.quickdial.data.CallDetail;
import android.app.Application;

public class QuickDialerApplication extends Application {
	
	private CallDetail callDetail;
	boolean callInitiated;

	public QuickDialerApplication() {
    	super();
	}

	@Override
	public void onCreate() {
	    super.onCreate();
	}
	
	@Override
	public void onTerminate() {
	    super.onTerminate();
	}
	
	public CallDetail getCallDetail() {
		return callDetail;
	}

	public void setCallDetail(CallDetail callDetail) {
		this.callDetail = callDetail;
	}

	public boolean isCallInitiated() {
		return callInitiated;
	}

	public void setCallInitiated(boolean callInitiated) {
		this.callInitiated = callInitiated;
	}
}
