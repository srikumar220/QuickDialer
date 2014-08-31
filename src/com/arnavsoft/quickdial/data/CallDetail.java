package com.arnavsoft.quickdial.data;

import android.os.Parcel;
import android.os.Parcelable;
import java.text.DateFormat;

public class CallDetail implements Parcelable {
	int id;
	String contactName;
	String phoneType;
	String number;
	long startTime; // unix time in millis
	long duration; // in millis

	public CallDetail() {
	}

	public CallDetail(int id, String contactName, String phoneType,
			String number, long startTime, int duration) {
		this.id = id;
		this.contactName = contactName;
		this.phoneType = phoneType;
		this.number = number;
		this.startTime = startTime;
		this.duration = duration;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public String toString() {
		DateFormat df = DateFormat.getDateTimeInstance();

		return contactName + "(" + phoneType + "):" + number + "\n"
				+ df.format(startTime) + "(" + duration / 1000 + "s)";
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(contactName);
		dest.writeString(phoneType);
		dest.writeString(number);
		dest.writeLong(startTime);
		dest.writeLong(duration);
	}

	public static final Parcelable.Creator<CallDetail> CREATOR = new Parcelable.Creator<CallDetail>() {
		public CallDetail createFromParcel(Parcel in) {
			return new CallDetail(in);
		}

		public CallDetail[] newArray(int size) {
			return new CallDetail[size];
		}
	};

	private CallDetail(Parcel in) {
		id = in.readInt();
		contactName = in.readString();
		phoneType = in.readString();
		number = in.readString();
		startTime = in.readLong();
		duration = in.readLong();
	}
}
