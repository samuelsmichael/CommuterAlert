package com.usefulsoftwaresolutions.android.commuterhelper;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class ActivitySplash extends Activity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {
    private LocationClient mLocationClient;
	private LocationManager mLocationManager = null;
	private boolean iveAlreadyStartedMainActivity=false;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int densityDpi = (int)(metrics.density * 160f);
		Point size = new Point();
		display.getSize(size);
		int width = (int)(float)((float)size.x * .9f);
		int height = (int)(float)((float)width * .2859f);
		
		ImageView imageView=(ImageView)findViewById(R.id.ivImageSplash);
		RelativeLayout.LayoutParams parms = new RelativeLayout.LayoutParams(width,height);
		
		imageView.setLayoutParams(parms);
		imageView.requestLayout();
		/*
		imageView.getLayoutParams().width = 420;// width;
		imageView.setScaleType(ScaleType.CENTER_CROP);
		imageView.requestLayout();
		*/
	}
	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		new WaitASecThenCheckIfGPSIsOn()
		.execute(new Void[] {null});
	}
	private class WaitASecThenCheckIfGPSIsOn extends
	AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}
		protected void onPostExecute(Void result) {
			if(!getLocationManager().isProviderEnabled(LocationManager.GPS_PROVIDER)) {

			        final AlertDialog.Builder builder = new AlertDialog.Builder(					
			        		new ContextThemeWrapper(ActivitySplash.this,
							R.style.AlertDialogCustomLight));
			        builder.setTitle("GPS is disabled");//
			        builder.setMessage("For best results, your GPS should be enabled. Do you want to enable it?")
			               .setCancelable(false)
			               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			                   public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
			                       startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			                   }
			               })
			               .setNegativeButton("No", new DialogInterface.OnClickListener() {
			                   public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
			                        dialog.cancel();
			                        ActivitySplash.this.finish();
			                   }
			               });
			        final AlertDialog alert = builder.create();
			        final Handler handler = new Handler();

			        alert.show();	    	
			    } else {
			    	// We've got GPS, so now fetch latest location
			    	// But first ... kick off a timer.  If we're not done by 6 seconds, then finish anyway.
			    	new WaitABitAndThenLaunchMainActivityAnyway().execute(new Void[] {null});
			        mLocationClient = new LocationClient(ActivitySplash.this, ActivitySplash.this, ActivitySplash.this);		
			        mLocationClient.connect();
			    }
			
			
		}
	
	}
	private LocationManager getLocationManager() {
		if (mLocationManager == null) {
			mLocationManager = (android.location.LocationManager) getSystemService(Context.LOCATION_SERVICE);
		}
		return mLocationManager;
	}	

	private void getCurrentLocation() {
		String provider=getProvider();
        if(provider==null) {
        	provider=LocationManager.GPS_PROVIDER;
        }
        if(getLocationManager().getLastKnownLocation(provider)!=null) {
        	waitThisManyMillisecondsThenFinish(3000);
        } else {
	        if(getLocationManager().isProviderEnabled(provider)) {
				getLocationManager().requestLocationUpdates(getProvider(), 0, 0, new LocationListener() {
					@Override
					public void onLocationChanged(Location location) {
						getLocationManager().removeUpdates(this);
						ActivitySplash.this.waitThisManyMillisecondsThenFinish(0);
					}
					@Override
					public void onProviderDisabled(String provider) {
					}
					@Override
					public void onProviderEnabled(String provider) {
					}
					@Override
					public void onStatusChanged(String provider, int status, Bundle extras) {
					}
				},Looper.getMainLooper());					
	        } else {
	        }
        }
		
	}
	private String getProvider() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		return getLocationManager().getBestProvider(criteria, false);
	}

	@Override
	public void onConnected(Bundle arg0) {
		getCurrentLocation();
        mLocationClient.disconnect();
	}


	@Override
	public void onDisconnected() {
	}	

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		waitThisManyMillisecondsThenFinish(3000);
	}	

	private void waitThisManyMillisecondsThenFinish(int millis) {
		if(!iveAlreadyStartedMainActivity) {
			iveAlreadyStartedMainActivity=true;		
			try {
				Thread.sleep(millis);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			startActivity(new Intent(this,Home2.class));
			finish();
		}
	}
	private class WaitABitAndThenLaunchMainActivityAnyway extends
	AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}
		protected void onPostExecute(Void result) {
			waitThisManyMillisecondsThenFinish(1);
		}
	}
	
}
