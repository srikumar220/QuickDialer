package com.arnavsoft.quickdial;

import java.text.DateFormat;
import java.util.List;

import com.arnavsoft.quickdial.data.CallDetail;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CallDetailAdapter extends BaseAdapter {
	Context context;
	List<CallDetail> callDetails;

	CallDetailAdapter(Context context, List<CallDetail> callDetails) {
		this.context = context;
		this.callDetails = callDetails;
	}

	@Override
	public int getCount() {
		return callDetails.size();
	}

	@Override
	public Object getItem(int position) {
		return callDetails.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CallDetail callDetail = callDetails.get(position);

		if ((convertView == null) || !(convertView instanceof CallDetailView)) {
			CallDetailView callDetailView = new CallDetailView(context, callDetail);

			if (position % 2 == 1) {
				callDetailView.setBackgroundColor(Color.parseColor("#FFF8C6"));
			} else {
				callDetailView.setBackgroundColor(Color.parseColor("#FFF8DC"));
			}
			callDetailView.invalidate();
			return callDetailView;
		}

		CallDetailView view = (CallDetailView) convertView;
		view.setDetails(callDetail);

		return view;
	}

	private final class CallDetailView extends LinearLayout {
		// Split the CallDetail into two broad category for easy display.
		// TODO: Make them into its own category in the future.
		TextView contactInfo;
		TextView timeInfo;

		public CallDetailView(Context context, CallDetail callDetail) {
			super(context);

			setOrientation(LinearLayout.VERTICAL);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			params.setMargins(5, 3, 5, 0);

			contactInfo = new TextView(context);
			contactInfo.setTextSize(16);
			contactInfo.setText(callDetail.getContactName() + "("
					+ callDetail.getPhoneType() + "): "
					+ callDetail.getNumber());
			addView(contactInfo, params);

			DateFormat df = DateFormat.getDateTimeInstance();

			timeInfo = new TextView(context);
			timeInfo.setTextSize(14);
			timeInfo.setTextColor(Color.MAGENTA);
			timeInfo.setText(df.format(callDetail.getStartTime()) + "("
					+ callDetail.getDuration() / 1000 + "s)");
			addView(timeInfo, params);
		}

		public void setDetails(CallDetail callDetail) {
			contactInfo
					.setText(callDetail.getContactName() + "("
							+ callDetail.getPhoneType() + "):"
							+ callDetail.getNumber());

			DateFormat df = DateFormat.getDateTimeInstance();
			timeInfo.setText(df.format(callDetail.getStartTime()) + "("
					+ callDetail.getDuration() / 1000 + "s)");
		}
	}
}
