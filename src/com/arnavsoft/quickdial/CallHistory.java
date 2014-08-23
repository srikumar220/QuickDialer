package com.arnavsoft.quickdial;

import java.util.List;

import com.arnavsoft.quickdial.data.CallDetail;
import com.arnavsoft.quickdial.data.DBHelper;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

public class CallHistory extends ListActivity {
	
	List<CallDetail>  callHistory;
	DBHelper dbHelper;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_history);

        TextView empty = (TextView) findViewById(R.id.empty);

        ListView listView = getListView();
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setEmptyView(empty);
        
        dbHelper = new DBHelper(this);
        callHistory = dbHelper.getCallDetails();
        
        final ArrayAdapter<CallDetail> callHistoryAdapter = new ArrayAdapter<CallDetail>(this, android.R.layout.simple_list_item_1, callHistory);
        listView.setAdapter(callHistoryAdapter);
        
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                CallDetail callDetail = callHistory.remove(position);
                dbHelper.deleteCallDetail(callDetail);
                callHistoryAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }
    
    @Override
    protected void onPause() {
        super.onPause();

        dbHelper.closeDB();
        dbHelper = null;
    }
}
