package com.example.locationserviceex;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.BassBoost.Settings;
import android.os.Bundle;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.widget.Toast;

public class GPS_Service  extends Service{

	LocationListener listener;
	LocationManager locm;
	
	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Toast.makeText(getApplicationContext(),"Location Service Started.",Toast.LENGTH_SHORT).show();
		listener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
			
			}
			
			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				Date d = new Date(location.getTime());
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				String DateMazi = format.format(d);
				Toast.makeText(getBaseContext(),DateMazi,Toast.LENGTH_LONG).show();
				Intent i =new Intent("Location_update");
				i.putExtra("date",DateMazi);
				i.putExtra("longitude",location.getLongitude());
				i.putExtra("latitude",location.getLatitude());
				sendBroadcast(i);
			}
		};
		locm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
		
		locm.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,0,listener);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Toast.makeText(getApplicationContext(),"Location Service Destroyed.",Toast.LENGTH_SHORT).show();
		if(locm!=null)
		{
			locm.removeUpdates(listener);
		}
	}
}
