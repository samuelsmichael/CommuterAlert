package com.usefulsoftwaresolutions.android.commuterhelper;

import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingsManager {
	private SharedPreferences mSharedPreferences;
	private Context mContext;
	
	public SettingsManager(Context context) {
		mSharedPreferences=context.getSharedPreferences(context.getPackageName() + "_preferences", Activity.MODE_PRIVATE);
		mContext=context;
	}
	private String getValue(String key, String defValue) {
		return mSharedPreferences.getString(key, defValue);
	}
	private void setValue(String key, String value) {
		Editor editor=mSharedPreferences.edit();
		editor.putString(key,value);
		editor.commit();				
	}
	public Integer getMTank() {
		int value=mSharedPreferences.getInt(GlobalStaticValues.KEY_MTANK, -1);
		if(value==-1) {
			return null;
		} else {
			return Integer.valueOf(value);
		}
	}
	public void setMTank(Integer value) {
		Editor editor=mSharedPreferences.edit();
		if(value==null) {
			editor.putInt(GlobalStaticValues.KEY_MTANK, -1);
		} else {
			editor.putInt(GlobalStaticValues.KEY_MTANK, value.intValue());
		}
		editor.commit();				
	}
	public boolean getBoughtASubscription() {
		return mSharedPreferences.getBoolean(GlobalStaticValues.KEY_BOUGHT_A_SUBSCRIPTION, false);
	}
	public void setBoughtASubscription(boolean value) {
		Editor editor=mSharedPreferences.edit();
		editor.putBoolean(GlobalStaticValues.KEY_BOUGHT_A_SUBSCRIPTION, value);
		editor.commit();				
	}
	public boolean getBoughtPermanentLicense() {
		return mSharedPreferences.getBoolean(GlobalStaticValues.KEY_BOUGHT_A_PERMANENT_LICENSE, false);
	}
	public void setBoughtPermanentLicence(boolean value) {
		Editor editor=mSharedPreferences.edit();
		editor.putBoolean(GlobalStaticValues.KEY_BOUGHT_A_PERMANENT_LICENSE, value);
		editor.commit();				
	}
	public Date getSubscriptionEnds () {
		String value=getValue(GlobalStaticValues.KEY_SUBSCRIPTION_ENDS,"");
		if(value.equals("")) {
			return new GregorianCalendar().getTime();
		}
		try {
			return DbAdapter.mDateFormat.parse(value);
		} catch (Exception e) {
			return new Date();
		}
	}
	public void setSubscriptionEnds(Date date) {
		setValue(GlobalStaticValues.KEY_SUBSCRIPTION_ENDS,DbAdapter.mDateFormat.format(date));
	}
	
	public void setHelpOverlayStateOn(boolean value) {
		Editor editor=mSharedPreferences.edit();
		editor.putString(GlobalStaticValues.KEY_HELP_OVERLAY_STATE, value?"true":"false");
		editor.commit();
	}
	public boolean getHelpOverlayStateOn() {
		String value= getValue(GlobalStaticValues.KEY_HELP_OVERLAY_STATE,"false");
		return value.equals("true")?true:false;
	}
	public void setContinousAlarmOn(boolean value) {
		Editor editor=mSharedPreferences.edit();
		editor.putBoolean(GlobalStaticValues.KEY_CONTINUOUS_ALARM_STATE, value);
		editor.commit();
	}
	public boolean getContinuousAlarmOn() {
		return mSharedPreferences.getBoolean(GlobalStaticValues.KEY_CONTINUOUS_ALARM_STATE, false);
	}
	public Date getEffectiveDatetime () {
		String value=getValue(GlobalStaticValues.KEY_EFFECTIVE_DATETIME,"");
		if(value.equals("")) {
			return new GregorianCalendar().getTime();
		}
		try {
			return DbAdapter.mDateFormat.parse(value);
		} catch (Exception e) {
			return new Date();
		}
	}
	public void setEffectiveDatetiem(Date date) {
		setValue(GlobalStaticValues.KEY_EFFECTIVE_DATETIME,DbAdapter.mDateFormat.format(date));
	}
	public LatLng getEffectiveLocation() {
		String value=getValue(GlobalStaticValues.KEY_EFFECTIVE_LOCATION,"");
		if(value.equals("")) {
			return null;
		} else {
			String[] sa=value.split("\\|");
			LatLng latlng=new LatLng(Double.parseDouble(sa[0]), Double.parseDouble(sa[1]));
			return latlng;
		}
	}
	public void setJustPreviousLocation(double latitude, double longitude) {
		setValue(GlobalStaticValues.KEY_JUSTPREVIOUS_LOCATION, String.valueOf(latitude)+"|"+String.valueOf(longitude));
	}
	public LatLng getJustPreviousLocation() {
		String value=getValue(GlobalStaticValues.KEY_JUSTPREVIOUS_LOCATION,"");
		if(value.equals("")) {
			return null;
		} else {
			String[] sa=value.split("\\|");
			LatLng latlng=new LatLng(Double.parseDouble(sa[0]), Double.parseDouble(sa[1]));
			return latlng;
		}
	}
	public void setEffectiveLocation(double latitude, double longitude) {
		if(latitude==0) {
			setValue(GlobalStaticValues.KEY_EFFECTIVE_LOCATION,null);
		} else {
			setValue(GlobalStaticValues.KEY_EFFECTIVE_LOCATION, String.valueOf(latitude)+"|"+String.valueOf(longitude));
		}
	}
}
