package com.arnavsoft.quickdial;

import java.util.ArrayList;
import java.util.List;

import com.arnavsoft.quickdial.R;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.TrackedActivity;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuickDialer extends TrackedActivity implements OnSharedPreferenceChangeListener{
	
	private static final int PICK_CONTACT = 0;
	private static final String SEPERATOR = ":: ";	
	private static String accessNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reliance_india_call);
        
        // Make the default preferences to take effect.
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        
        Button pickContact = (Button) findViewById(R.id.pickContact); 
        pickContact.setOnClickListener(new View.OnClickListener()
        {
        	public void onClick(View v) {
        		EasyTracker.getTracker().trackEvent("ui_action", "button_click", "pickContact", 0L);
                if (null == accessNumber) {
        			Toast noAccessNumber = Toast.makeText(QuickDialer.this, R.string.no_access_number_detail, Toast.LENGTH_LONG);
        			noAccessNumber.show();
                } else {       		        		
	        		Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
	        		startActivityForResult(pickContact, PICK_CONTACT);
                }
        	}
        });
        
        loadPreferences();
        if (null == accessNumber) {
			Toast noAccessNumber = Toast.makeText(this, R.string.no_access_number_mini, Toast.LENGTH_LONG);
			noAccessNumber.show();
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (resultCode == RESULT_OK) {
            switch (requestCode) {  
            case PICK_CONTACT:
            	Cursor cursor = null;
            	try {
					final List<String> phoneNumbers = new ArrayList<String>();
					Uri contactUri  = data.getData();
					String contactId = contactUri.getLastPathSegment();  
					cursor = getContentResolver().query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + "=?", new String[] {contactId}, null);
					for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
						String label = null;
						switch (cursor.getInt(cursor.getColumnIndex(Phone.TYPE))) {
						case Phone.TYPE_HOME:
							label = "Home";
							break;							
						case Phone.TYPE_MOBILE:
							label = "Mobile";
							break;							
						case Phone.TYPE_WORK:
							label = "Work";
							break;							
						case Phone.TYPE_FAX_WORK:
							label = "Fax Work";
							break;							
						case Phone.TYPE_FAX_HOME:
							label = "Fax Home";
							break;							
						case Phone.TYPE_PAGER:
							label = "Pager";
							break;							
						case Phone.TYPE_OTHER:
							label = "Other";
							break;							
						case Phone.TYPE_CALLBACK:
							label = "Callback";
							break;							
						case Phone.TYPE_CAR:
							label = "Car";
							break;							
						case Phone.TYPE_COMPANY_MAIN:
							label = "Company Main";
							break;							
						case Phone.TYPE_ISDN:
							label = "Isdn";
							break;							
						case Phone.TYPE_MAIN:
							label = "Main";
							break;							
						case Phone.TYPE_OTHER_FAX:
							label = "Other Fax";
							break;							
						case Phone.TYPE_RADIO:
							label = "Radio";
							break;							
						case Phone.TYPE_TELEX:
							label = "Telex";
							break;							
						case Phone.TYPE_TTY_TDD:
							label = "Tty Tdd";
							break;							
						case Phone.TYPE_WORK_MOBILE:
							label = "Work Mobile";
							break;							
						case Phone.TYPE_WORK_PAGER:
							label = "Work Pager";
							break;							
						case Phone.TYPE_ASSISTANT:
							label = "Assistant";
							break;							
						case Phone.TYPE_MMS:
							label = "MMS:";
							break;							
						case Phone.TYPE_CUSTOM:
							label = cursor.getString(cursor.getColumnIndex(Phone.LABEL));
							break;
						}
						
						phoneNumbers.add(label + SEPERATOR + cursor.getString(cursor.getColumnIndex(Phone.DATA)));
					} 
					int phoneNumberCount = phoneNumbers.size();
					if (phoneNumberCount == 0) {
						Toast noNumbers = Toast.makeText(this, R.string.no_phone_numbers, Toast.LENGTH_LONG);
						noNumbers.show();						
					}
					else if (phoneNumberCount == 1) {						
						String numberToDial = getPhoneNumberPart(phoneNumbers.get(0));
						Intent makeCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + accessNumber + ",,," + numberToDial));
						startActivity(makeCall);
					}
					else if (phoneNumberCount > 1) {
						CharSequence[] allNumbers = phoneNumbers.toArray(new String[phoneNumbers.size()]);
						AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
						dialogBuilder.setTitle(R.string.pick_number);
						dialogBuilder.setItems(allNumbers, new DialogInterface.OnClickListener() {							
							@Override
							public void onClick(DialogInterface dialog, int selectedNumberIdx) {
								String numberToDial = getPhoneNumberPart(phoneNumbers.get(selectedNumberIdx));
								Intent makeCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + accessNumber + ",,," + numberToDial));
								startActivity(makeCall);
							}
						});
						
						AlertDialog pickNumberDialog = dialogBuilder.create();
						pickNumberDialog.show();
					}
				} catch (NotFoundException e) {
					e.printStackTrace();
				}
            	finally {
            		if (cursor != null) {
            			cursor.close();
            		}
            	}
            }
        }  	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reliance_india_call, menu);
        return true;
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
    	switch (item.getItemId()) 
    	{
    	case R.id.menu_settings:
    		EasyTracker.getTracker().trackEvent("ui_action", "menu_click", "menu_settings", 0L);
    		Intent preferences = new Intent(this, PreferencesActivity.class);
    		startActivity(preferences);
    		return true;
    	case R.id.menu_help:
    		EasyTracker.getTracker().trackEvent("ui_action", "menu_click", "menu_help", 0L);
    		TextView textView = new TextView(this);
		    SpannableString helpMsg = new SpannableString(getText(R.string.menu_help_message));
		    Linkify.addLinks(helpMsg, Linkify.ALL);		
		    textView.setText(helpMsg);
		    // Clicking links inside the textView inside AlertDialog does not
		    // work without this.
		    textView.setMovementMethod(LinkMovementMethod.getInstance());

			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
			dialogBuilder.setTitle(R.string.menu_help_title);
			dialogBuilder.setPositiveButton(R.string.ok, null);
			dialogBuilder.setView(textView);
			
			AlertDialog menuHelpDialog = dialogBuilder.create();
			menuHelpDialog.show();
    		return true;    		
    	}
		return super.onOptionsItemSelected(item);
	}
    
    private void loadPreferences() {
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	
    	// Register this class to listen to any preferences changes.
    	// Otherwise the old values will continue to be used.
    	preferences.registerOnSharedPreferenceChangeListener(this);
		accessNumber = preferences.getString("access_number", null);   	
    }

	private String getPhoneNumberPart(String labelAndPhoneNumber) {
    	return labelAndPhoneNumber.substring(labelAndPhoneNumber.indexOf(SEPERATOR) + 3);
    }

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		loadPreferences();
	}
}
