package com.arnavsoft.quickdial;

import java.util.List;
import com.arnavsoft.quickdial.data.CallDetail;
import com.arnavsoft.quickdial.data.DBHelper;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class CallHistory extends ListActivity {
	
	TextView empty;
	CallDetailAdapter callDetailAdapter;
	
	private List<CallDetail>  callHistory;
	private DBHelper dbHelper;
    private Settings settings;
    
    private PhoneStateMonitor phoneStateMonitor;
	private TelephonyManager telephonyManager;
	
	private ProgressDialog progressDialog;
    private final Handler handler = new Handler() {
        public void handleMessage (final Message message) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            if (callHistory == null || callHistory.isEmpty()) {
                empty.setText("No Call History");
            } else {
            	callDetailAdapter = new CallDetailAdapter(CallHistory.this, callHistory);
                setListAdapter(callDetailAdapter);
            }
        }
    };
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_history);
        
        settings = new Settings(this);

        empty = (TextView) findViewById(R.id.empty);

        ListView listView = getListView();
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setEmptyView(empty);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                PopupMenu popup = new PopupMenu(CallHistory.this, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.call_history_item_menu, popup.getMenu());
                
                popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
					@Override
					public boolean onMenuItemClick(MenuItem item) {
					    switch (item.getItemId()) {
					        case R.id.menu_delete_call:
				                CallDetail callDetailToDelete = callHistory.remove(position);
				                dbHelper.deleteCallDetail(callDetailToDelete);
				                callDetailAdapter.notifyDataSetChanged();
					            return true;
					        case R.id.menu_call_again:
					    		QuickDialerApplication quickDialerApplication = (QuickDialerApplication) getApplication();
					    		CallDetail callDetail = callHistory.get(position);
					    		callDetail.setStartTime(System.currentTimeMillis());
					    		quickDialerApplication.setCallDetail(callDetail);
					    		
					        	String numberToDial = callDetail.getNumber();
								Intent makeCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Settings.accessNumber + ",,," + numberToDial));
								startActivity(makeCall);
					            return true;
					        default:
					            return false;
					    }
					}
                });
                
                popup.show();
            }
        });
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	
    	monitorPhoneState();
    }
    
	@Override
	protected void onResume() {
		super.onResume();
		
		loadCallHistory();
	}
    
	@Override
	protected void onPause() {
		super.onPause();
		
        dbHelper.closeDB();
        dbHelper = null;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		telephonyManager.listen(phoneStateMonitor, PhoneStateListener.LISTEN_NONE);
	}
	
    private void loadCallHistory() {
        dbHelper = new DBHelper(this);

        progressDialog = ProgressDialog.show(this, "Working...", "Retrieving call history", true, false);

        new Thread() {
            public void run() {
                callHistory = dbHelper.getCallDetails();
                handler.sendEmptyMessage(0);
            }
        }.start();
    }
    
	public void monitorPhoneState() {
		if (phoneStateMonitor == null) {
			phoneStateMonitor = new PhoneStateMonitor(this);
		}
		if (telephonyManager == null) {
			telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		}
        telephonyManager.listen(phoneStateMonitor, PhoneStateListener.LISTEN_CALL_STATE);
	}    
}
