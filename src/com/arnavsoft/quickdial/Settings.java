package com.arnavsoft.quickdial;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

public class Settings implements OnSharedPreferenceChangeListener {

	Context context;
	public static String accessNumber;

	public Settings(Context context) {
		this.context = context;

		// Make the default preferences to take effect.
		PreferenceManager.setDefaultValues(context, R.xml.settings, false);

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		accessNumber = preferences.getString("access_number", null);

		preferences.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// we have just one preference, accessNumber
		accessNumber = sharedPreferences.getString(key, null); 
	}
}
